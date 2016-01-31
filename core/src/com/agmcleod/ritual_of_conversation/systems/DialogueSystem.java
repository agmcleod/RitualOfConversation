package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.SoundManager;
import com.agmcleod.ritual_of_conversation.actors.DialogueOptionBubbleActor;
import com.agmcleod.ritual_of_conversation.components.DialogueOptionComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.AwkwardBar;
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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
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
    private final float BUBBLE_VELOCITY = 250;
    private final float NEXT_TOKEN_TIME = 0.15f;
    private final float STAGGER = 270;
    private final float SELECTED_TIMEOUT = 1f;
    private final String AWKWARDNESS_CAP_ID = "32";
    private Array<DialogueOptionBubbleActor> currentDialogueActors;
    private AwkwardBar awkwardBar;
    private int awkwardness;
    private String currentId = "1";
    private int currentToken = 0;
    private Map<String, DialogueContent> dialogues;
    private PlayScreen playScreen;
    private ImmutableArray<Entity> entities;
    private NpcEntity npcEntity;
    private Rectangle rectA;
    private Rectangle rectB;
    private Player player;
    private DialogueOptionBubble selectedOption;
    private float selectedTimer;
    private String[] tokens;
    private float tokenTimer = 0;

    private final int[] DIALOGUE_SPAWN_POINTS = new int[] {
            0, 150, 300, 450, 600
    };

    public DialogueSystem(PlayScreen playScreen, NpcEntity npcEntity, Player player, AwkwardBar awkwardBar) {
        this.player = player;
        this.npcEntity = npcEntity;
        awkwardness = 0;
        this.playScreen = playScreen;
        this.awkwardBar = awkwardBar;

        rectA = new Rectangle();
        rectB = new Rectangle();

        tokens = new String[0];

        try {
            FileHandle fileHandle = Gdx.files.internal("dialogue.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fileHandle.read()));
            String jsonText = "";
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                jsonText += line;
            }
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
                if (awkwardness > 0) {
                    playScreen.queueAwkwardnessBarInstruction();
                }
                startDialogue();
            }
        } else {
            moveBubbles(dt);
        }

        if (currentToken < tokens.length) {
            tokenTimer -= dt;
            String newText = "";
            for (int i = 0; i < (currentToken + 1); i++) {
                newText += tokens[i] + " ";
            }
            if (tokenTimer <= 0) {
                SoundManager.sounds.get("blip").play();
                currentToken++;
                tokenTimer = NEXT_TOKEN_TIME;
            }
            npcEntity.getTextContentComponent().text = newText;
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
                transformComponent.position.y = Math.max(highX, RitualOfConversation.GAME_HEIGHT - transformComponent.height / 2);
            }

            rectB.set(optionBubble.getBoundingBox());
            rectB.x += transformComponent.position.x;
            rectB.y += transformComponent.position.y;
            if (rectB.y > 0 && rectB.overlaps(rectA)) {
                collidedBubble = optionBubble;
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

            if (content.score > 0) {
                SoundManager.sounds.get("chimebad").play();
            } else if (content.score < 0) {
                SoundManager.sounds.get("chimegood").play();
            } else {
                SoundManager.sounds.get("chime").play();
            }

            awkwardBar.setAwkwardOffset(awkwardness / AWKWARDNESS_CAP);

            npcEntity.setEmotionState(awkwardness / AWKWARDNESS_CAP);

            selectedTimer = SELECTED_TIMEOUT;
        }
    }

    private void setNpcText(String text) {
        tokens = text.split(" ");
        currentToken = 0;
        tokenTimer = NEXT_TOKEN_TIME;
    }

    private void startDialogue() {
        if (dialogues.containsKey(currentId)) {
            DialogueContent content = dialogues.get(currentId);

            setNpcText(content.text);

            // multiplied so there's a delay
            float startY = RitualOfConversation.GAME_HEIGHT * 1.5f;

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

            playScreen.bumpUiZIndex();

            if (content.options.length > 1) {
                playScreen.queueCollisionChoiceInstruction();
            }

            setEntities(playScreen.getEngine());
        }
    }

    private void setEntities(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(DialogueOptionComponent.class).get());
    }
}
