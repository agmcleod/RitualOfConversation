package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.SoundManager;
import com.agmcleod.ritual_of_conversation.actors.DialogueOptionBubbleActor;
import com.agmcleod.ritual_of_conversation.actors.NpcTextActor;
import com.agmcleod.ritual_of_conversation.components.DialogueOptionComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.DialogueOptionBubble;
import com.agmcleod.ritual_of_conversation.entities.GameEntity;
import com.agmcleod.ritual_of_conversation.entities.NpcText;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
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
    private final float STAGGER = 200;
    private final float SELECTED_TIMEOUT = 1f;
    private Array<DialogueOptionBubbleActor> currentDialogueActors;
    private TextureAtlas atlas;
    private int awkwardness;
    private Map<String, DialogueContent> dialogues;
    private Engine engine;
    private ImmutableArray<Entity> entities;
    private BitmapFont font;
    private NpcText npcText;
    private NpcTextActor npcTextActor;
    private Rectangle rectA;
    private Rectangle rectB;
    private Player player;
    private DialogueOptionBubble selectedOption;
    private float selectedTimer;
    private Stage stage;
    private String currentId = "1";
    private final int[] DIALOGUE_SPAWN_POINTS = new int[] {
            0, 150, 300, 450, 600
    };

    public DialogueSystem(Engine engine, Stage stage, TextureAtlas atlas, BitmapFont font, NpcText npcText, NpcTextActor npcTextActor, Player player) {
        this.player = player;
        this.npcText = npcText;
        this.engine = engine;
        this.stage = stage;
        this.atlas = atlas;
        this.font = font;
        awkwardness = 0;
        this.npcTextActor = npcTextActor;

        rectA = new Rectangle();
        rectB = new Rectangle();

        try {
            String jsonText = new String(Files.readAllBytes(Gdx.files.internal("dialogue.json").file().toPath()));
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, DialogueContent>>() {}.getType();
            dialogues = gson.fromJson(jsonText, type);

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
                    engine.removeEntity(entities.get(i));
                }

                for (int i = 0; i < currentDialogueActors.size; i++) {
                    currentDialogueActors.get(i).remove();
                }
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
        for (int i = 0; i < entities.size(); i++) {
            DialogueOptionBubble optionBubble = (DialogueOptionBubble) entities.get(i);
            TransformComponent transformComponent = optionBubble.getTransform();
            transformComponent.position.y -= 100 * dt;

            if (transformComponent.position.y + transformComponent.height / 2 < 0) {
                int nextIndex = i - 1;
                if (nextIndex < 0) {
                    nextIndex = entities.size() - 1;
                }
                if (nextIndex == i) {
                    transformComponent.position.y = Gdx.graphics.getHeight() + transformComponent.height / 2;
                } else {
                    TransformComponent nextTransform = ((GameEntity) entities.get(nextIndex)).getTransform();
                    // if next one is off screen
                    if (nextTransform.position.y - nextTransform.width / 2 >= Gdx.graphics.getHeight()) {
                        transformComponent.position.y = nextTransform.position.y + STAGGER;
                    } else {
                        transformComponent.position.y = Gdx.graphics.getHeight() + transformComponent.height / 2;
                    }
                }
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
            Gdx.app.exit();
        } else {
            currentId = content.nextId;
            awkwardness += content.score;

            selectedTimer = SELECTED_TIMEOUT;
        }
    }

    private void startDialogue() {
        if (dialogues.containsKey(currentId)) {
            DialogueContent content = dialogues.get(currentId);
            npcText.getTextContentComponent().text = content.text;

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

                engine.addEntity(bubble);
                DialogueOptionBubbleActor actor = new DialogueOptionBubbleActor(atlas, font, bubble);
                actor.setZIndex(stage.getActors().size - 2);
                stage.addActor(actor);
                currentDialogueActors.insert(i, actor);
            }

            npcTextActor.setZIndex(999);

            setEntities(engine);
            SoundManager.sounds.get("blip").play();
        }
    }

    private void setEntities(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(DialogueOptionComponent.class).get());
    }
}
