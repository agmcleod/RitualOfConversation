package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.components.DialogueOptionComponent;
import com.agmcleod.ritual_of_conversation.entities.NpcText;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
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
    private Map<String, DialogueContent> dialogues;
    private Engine engine;
    private ImmutableArray<Entity> entities;
    private NpcText npcText;
    private Player player;
    private String currentId = "1";

    public DialogueSystem(Engine engine, NpcText npcText, Player player) {
        this.player = player;
        this.npcText = npcText;
        this.engine = engine;

        try {
            String jsonText = new String(Files.readAllBytes(Gdx.files.internal("dialogue.json").file().toPath()));
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, DialogueContent>>() {}.getType();
            dialogues = gson.fromJson(jsonText, type);

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(dialogues.get(currentId).options.length);
    }

    @Override
    public void addedToEngine(Engine engine) {
        setEntities(engine);
    }

    @Override
    public void update(float dt) {

    }

    private void startDialogue() {

    }

    private void setEntities(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(DialogueOptionComponent.class).get());
    }
}
