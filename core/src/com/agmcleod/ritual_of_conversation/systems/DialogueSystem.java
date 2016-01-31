package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.SoundManager;
import com.agmcleod.ritual_of_conversation.actors.DialogueOptionBubbleActor;
import com.agmcleod.ritual_of_conversation.actors.NpcTextActor;
import com.agmcleod.ritual_of_conversation.components.DialogueOptionComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.DialogueOptionBubble;
import com.agmcleod.ritual_of_conversation.entities.NpcEntity;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.agmcleod.ritual_of_conversation.screens.PlayScreen;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

class DialogueOptionContent {
    public String text;
    public String nextId;
    public int score;

    public DialogueOptionContent(String text, String nextId, int score) {
        this.text = text;
        this.nextId = nextId;
        this.score = score;
    }
}

class DialogueContent {
    public DialogueOptionContent[] options;
    public String text;

    public DialogueContent(String text, DialogueOptionContent[] options) {
        this.text = text;
        this.options = options;
    }
}

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class DialogueSystem extends EntitySystem {
    private final float AWKWARDNESS_CAP = 100;
    private final float BUBBLE_VELOCITY = 150;
    private final float STAGGER = 200;
    private final float SELECTED_TIMEOUT = 1f;
    private final String AWKWARDNESS_CAP_ID = "32";
    private Array<DialogueOptionBubbleActor> currentDialogueActors;
    private int awkwardness;
    private String currentId = "1";
    private Map<String, DialogueContent> dialogues;
    private PlayScreen playScreen;
    private ImmutableArray<Entity> entities;
    private NpcEntity npcEntity;
    private NpcTextActor npcTextActor;
    private Rectangle rectA;
    private Rectangle rectB;
    private Player player;
    private DialogueOptionBubble selectedOption;
    private float selectedTimer;
    private final int[] DIALOGUE_SPAWN_POINTS = new int[] {
            0, 150, 300, 450, 600
    };

    public DialogueSystem(PlayScreen playScreen, NpcEntity npcEntity, NpcTextActor npcTextActor, Player player) {
        this.player = player;
        this.npcEntity = npcEntity;
        awkwardness = 0;
        this.npcTextActor = npcTextActor;
        this.playScreen = playScreen;

        rectA = new Rectangle();
        rectB = new Rectangle();

        try {
            String jsonText = new String(Files.readAllBytes(Gdx.files.internal("dialogue.json").file().toPath()));
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, DialogueContent>>() {}.getType();
            dialogues = gson.fromJson(jsonText, type);

            for (Map.Entry<String, DialogueContent> entry : dialogues.entrySet()) {
                Collections.shuffle(Arrays.asList(entry.getValue().options));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        startDialogue();
    }

    @Override
    public void addedToEngine(Engine engine) {
        setEntities(engine);
    }

    @Override
    public void update(float dt) {
        if (selectedTimer > 0) {
            selectedTimer -= dt;
            selectedOption.getDialogueOptionComponent().alpha = selectedTimer / SELECTED_TIMEOUT;
            if (selectedTimer <= 0) {
                for (int i = 0; i < entities.size(); i++) {
                    playScreen.getEngine().removeEntity(entities.get(i));
                }

                for (int i = 0; i < currentDialogueActors.size; i++) {
                    currentDialogueActors.get(i).remove();
                }
                currentDialogueActors.clear();
                startDialogue();
            }
        } else {
            moveBubbles(dt);
        }
    }

    private void moveBubbles(float dt) {
        DialogueOptionBubble collidedBubble = null;
        rectA.set(player.getBoundingBox());
        TransformComponent playerTransform = player.getTransform();
        rectA.x += playerTransform.position.x;
        rectA.y += playerTransform.position.y;
        Array<Float> points = new Array<Float>();
        for (int i = 0; i < entities.size(); i++) {
            DialogueOptionBubble optionBubble = (DialogueOptionBubble) entities.get(i);
            TransformComponent transformComponent = optionBubble.getTransform();
            transformComponent.position.y -= BUBBLE_VELOCITY * dt;

            points.add(transformComponent.position.y);

            if (transformComponent.position.y + transformComponent.height / 2 < 0) {
                float highX = STAGGER + (entities.size() * STAGGER);
                transformComponent.position.y = Math.max(highX, Gdx.graphics.getHeight() - transformComponent.height / 2);
            }

            rectB.set(optionBubble.getBoundingBox());
            rectB.x += transformComponent.position.x;
            rectB.y += transformComponent.position.y;
            if (rectB.overlaps(rectA)) {
                collidedBubble = optionBubble;
                SoundManager.sounds.get("chime").play();
            }
        }

        if (collidedBubble != null) {
            selectAnswer(collidedBubble);
        }
    }

    private void selectAnswer(DialogueOptionBubble dialogueOptionBubble) {
        selectedOption = dialogueOptionBubble;
        DialogueOptionComponent dialogueOptionComponent = dialogueOptionBubble.getDialogueOptionComponent();
        DialogueOptionContent content = dialogues.get(currentId).options[dialogueOptionComponent.optionIndex];
        if (content.nextId == null) {
            playScreen.gotoEndScreen();
        } else {
            currentId = content.nextId;
            awkwardness += content.score;
            if (awkwardness >= AWKWARDNESS_CAP) {
                currentId = AWKWARDNESS_CAP_ID;
            } else if (awkwardness < 0) {
                awkwardness = 0;
            }

            npcEntity.setEmotionState(awkwardness / AWKWARDNESS_CAP);

            selectedTimer = SELECTED_TIMEOUT;
        }
    }

    private void startDialogue() {
        if (dialogues.containsKey(currentId)) {
            DialogueContent content = dialogues.get(currentId);
            npcEntity.getTextContentComponent().text = content.text;

            // multiplied so there's a delay
            float startY = Gdx.graphics.getHeight() * 1.5f;

            currentDialogueActors = new Array<DialogueOptionBubbleActor>(content.options.length);

            int lastStartPoint = -100;

            for (int i = 0; i < content.options.length; i++) {
                DialogueOptionContent optionContent = content.options[i];
                int startPoint = 0;
                do {
                    startPoint = DIALOGUE_SPAWN_POINTS[MathUtils.random(0, DIALOGUE_SPAWN_POINTS.length - 1)];
                } while(startPoint == lastStartPoint);
                lastStartPoint = startPoint;
                startPoint += MathUtils.random(-10, 10);
                if (startPoint < 0) {
                    startPoint = MathUtils.random(0, 10);
                }

                startPoint += DialogueOptionBubble.WIDTH / 2;
                DialogueOptionBubble bubble = new DialogueOptionBubble(startPoint, startY + (STAGGER * i));
                bubble.getTextContentComponent().text = optionContent.text;
                DialogueOptionComponent optionComponent = bubble.getDialogueOptionComponent();
                optionComponent.optionIndex = i;

                playScreen.getEngine().addEntity(bubble);
                Stage stage = playScreen.getStage();
                DialogueOptionBubbleActor actor = new DialogueOptionBubbleActor(playScreen.getAtlas(), playScreen.getDialogueFont(), bubble);
                actor.setZIndex(stage.getActors().size - 2);
                stage.addActor(actor);
                currentDialogueActors.insert(i, actor);
            }

            npcTextActor.setZIndex(999);

            setEntities(playScreen.getEngine());
            SoundManager.sounds.get("blip").play();
        }
    }

    private void setEntities(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(DialogueOptionComponent.class).get());
    }
}
