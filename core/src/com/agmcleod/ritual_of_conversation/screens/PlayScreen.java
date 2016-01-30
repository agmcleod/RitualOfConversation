package com.agmcleod.ritual_of_conversation.screens;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.SoundManager;
import com.agmcleod.ritual_of_conversation.actors.BackgroundActor;
import com.agmcleod.ritual_of_conversation.actors.NpcTextActor;
import com.agmcleod.ritual_of_conversation.actors.PlayerActor;
import com.agmcleod.ritual_of_conversation.entities.NpcText;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.agmcleod.ritual_of_conversation.systems.DialogueSystem;
import com.agmcleod.ritual_of_conversation.systems.MovementSystem;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class PlayScreen implements Screen {
    private TextureAtlas atlas;
    private BitmapFont dialogueFont;
    private Engine engine;
    private float fadeTimer;
    private TransitionCallback fadeOutCallback;
    private RitualOfConversation game;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private boolean transitioningOut;

    public PlayScreen(RitualOfConversation game) {
        this.game = game;
    }

    @Override
    public void show() {
        engine = new Engine();
        transitioningOut = false;
        stage = new Stage(new ScalingViewport(Scaling.stretch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        atlas = new TextureAtlas(Gdx.files.internal("atlas.txt"));
        dialogueFont = new BitmapFont(Gdx.files.internal("munro24.fnt"));
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(stage);

        ObjectMap<String, Sound> sounds = SoundManager.sounds;
        sounds.put("blip", Gdx.audio.newSound(Gdx.files.internal("blip.ogg")));
        sounds.put("chime", Gdx.audio.newSound(Gdx.files.internal("chime.ogg")));

        Player player = new Player();
        stage.addActor(new BackgroundActor(atlas));
        createPlayer(player);
        createDialogueSystem(player);

        engine.addSystem(new MovementSystem());

        fadeOutCallback = new TransitionCallback() {
            @Override
            public void callback() {
                game.gotoEndScreen();
            }
        };
    }

    public void createDialogueSystem(Player player) {
        NpcText npcText = new NpcText();
        NpcTextActor npcTextActor = new NpcTextActor(atlas, dialogueFont, npcText);
        DialogueSystem dialogueSystem = new DialogueSystem(this, npcText, npcTextActor, player);
        engine.addSystem(dialogueSystem);

        stage.addActor(npcTextActor);
    }

    public void createPlayer(Player player) {
        PlayerActor playerActor = new PlayerActor(atlas, player);
        stage.setKeyboardFocus(playerActor);

        engine.addEntity(player);
        stage.addActor(playerActor);
    }

    public Stage getStage() {
        return stage;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public Engine getEngine() {
        return engine;
    }

    public BitmapFont getDialogueFont() {
        return dialogueFont;
    }

    public void gotoEndScreen() {
        transitioningOut = true;
        fadeTimer = 0;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (transitioningOut) {
            fadeTimer += delta;
            game.drawBlackTransparentSquare(shapeRenderer, fadeTimer / RitualOfConversation.FADE_TIMEOUT, fadeOutCallback);
        } else {
            update(delta);
            stage.act(delta);
            stage.draw();
        }
    }

    public void update(float dt) {
        engine.update(dt);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        atlas.dispose();
        dialogueFont.dispose();
        ObjectMap.Entries<String, Sound> it = SoundManager.sounds.iterator();
        while (it.hasNext()) {
            ObjectMap.Entry<String, Sound> entry = it.next();
            entry.value.dispose();
        }

        SoundManager.sounds.clear();
        shapeRenderer.dispose();
    }
}
