package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.actors.DialogueOptionBubbleActor;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
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
    private Array<DialogueOptionBubbleActor> currentDialogueActors;
    private TextureAtlas atlas;
    private int awkwardness;
    private Map<String, DialogueContent> dialogues;
    private Engine engine;
    private ImmutableArray<Entity> entities;
    private BitmapFont font;
    private NpcText npcText;
    private Player player;
    private Stage stage;
    private String currentId = "1";
    private final int[] DIALOGUE_SPAWN_POINTS = new int[] {
            0, 150, 300, 450, 600
    };

    public DialogueSystem(Engine engine, Stage stage, TextureAtlas atlas, BitmapFont font, NpcText npcText, Player player) {
        this.player = player;
        this.npcText = npcText;
        this.engine = engine;
        this.stage = stage;
        this.atlas = atlas;
        this.font = font;

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
        DialogueOptionBubble collidedBubble = null;
        for (int i = 0; i < entities.size(); i++) {
            DialogueOptionBubble optionBubble = (DialogueOptionBubble) entities.get(i);
            TransformComponent transformComponent = optionBubble.getTransform();
            transformComponent.position.y -= 100 * dt;

            if (transformComponent.position.y + transformComponent.height < 0) {
                int nextIndex = i - 1;
                if (nextIndex < 0) {
                    nextIndex = entities.size() - 1;
                }
                if (nextIndex == i) {
                    transformComponent.position.y = Gdx.graphics.getHeight() + transformComponent.height / 2;
                } else {
                    transformComponent.position.y = ((GameEntity) entities.get(nextIndex)).getTransform().position.y + STAGGER;
                }
            }

            if (optionBubble.getBoundingBox().overlaps(player.getBoundingBox())) {
                collidedBubble = optionBubble;
            }
        }

        if (collidedBubble != null) {
            selectAnswer(collidedBubble);
        }
    }

    private void selectAnswer(DialogueOptionBubble dialogueOptionBubble) {
        DialogueOptionComponent dialogueOptionComponent = dialogueOptionBubble.getDialogueOptionComponent();
        DialogueOptionContent content = dialogues.get(currentId).options[dialogueOptionComponent.optionIndex];
        if (content.nextId == null) {
            Gdx.app.exit();
        } else {
            currentId = content.nextId;
            awkwardness += content.score;

            for (int i = 0; i < entities.size(); i++) {
                engine.removeEntity(entities.get(i));
            }

            for (int i = 0; i < currentDialogueActors.size; i++) {
                currentDialogueActors.get(i).remove();
            }
        }
    }

    private void startDialogue() {
        if (dialogues.containsKey(currentId)) {
            DialogueContent content = dialogues.get(currentId);
            npcText.getTextContentComponent().text = content.text;

            int startY = Gdx.graphics.getHeight();

            currentDialogueActors = new Array<DialogueOptionBubbleActor>(content.options.length);

            for (int i = 0; i < content.options.length; i++) {
                DialogueOptionContent optionContent = content.options[i];
                int startPoint = DIALOGUE_SPAWN_POINTS[MathUtils.random(0, DIALOGUE_SPAWN_POINTS.length - 1)];
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
                stage.addActor(actor);
                currentDialogueActors.insert(i, actor);
            }

            setEntities(engine);
        }
    }

    private void setEntities(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(DialogueOptionComponent.class).get());
    }
}
