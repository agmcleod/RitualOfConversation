package com.agmcleod.ritual_of_conversation.screens;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.SoundManager;
import com.agmcleod.ritual_of_conversation.actors.*;
import com.agmcleod.ritual_of_conversation.components.InstructionState;
import com.agmcleod.ritual_of_conversation.entities.AwkwardBar;
import com.agmcleod.ritual_of_conversation.entities.Instructions;
import com.agmcleod.ritual_of_conversation.entities.NpcEntity;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.agmcleod.ritual_of_conversation.systems.DialogueSystem;
import com.agmcleod.ritual_of_conversation.systems.InstructionSystem;
import com.agmcleod.ritual_of_conversation.systems.MovementSystem;
import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class PlayScreen implements Screen {
    private TextureAtlas atlas;
    private BitmapFont dialogueFont;
    private DialogueSystem dialogueSystem;
    private Engine engine;
    private float fadeTimer;
    private TransitionCallback fadeInCallback;
    private TransitionCallback fadeOutCallback;
    private RitualOfConversation game;
    private BitmapFont instructionsFont;
    private InstructionSystem instructionSystem;
    private boolean instructionAwkwardnessBarQueued;
    private boolean instructionCollisionChoiceQueued;
    private MovementSystem movementSystem;
    private Music music;
    private BitmapFont npcFont;
    private NpcCharacterActor npcCharacterActor;
    private NpcTextActor npcTextActor;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private boolean transitioningIn;
    private boolean transitioningOut;

    public PlayScreen(RitualOfConversation game) {
        this.game = game;
    }

    @Override
    public void show() {
        engine = new Engine();
        transitioningOut = false;
        transitioningIn = true;
        stage = new Stage(new ScreenViewport());
        atlas = new TextureAtlas(Gdx.files.internal("atlas.txt"));
        dialogueFont = new BitmapFont(Gdx.files.internal("munro24.fnt"));
        npcFont = new BitmapFont(Gdx.files.internal("munro24white.fnt"));
        instructionsFont = new BitmapFont(Gdx.files.internal("munro40white.fnt"));
        shapeRenderer = new ShapeRenderer();
        Gdx.input.setInputProcessor(stage);

        ObjectMap<String, Sound> sounds = SoundManager.sounds;
        sounds.put("blip", Gdx.audio.newSound(Gdx.files.internal("blip.ogg")));
        sounds.put("chime", Gdx.audio.newSound(Gdx.files.internal("chime.ogg")));
        sounds.put("chimegood", Gdx.audio.newSound(Gdx.files.internal("chimegood.ogg")));
        sounds.put("chimebad", Gdx.audio.newSound(Gdx.files.internal("chimebad.ogg")));

        Player player = new Player();
        stage.addActor(new BackgroundActor(atlas));
        createPlayer(player);
        createDialogueSystem(player);
        movementSystem = new MovementSystem();
        music = Gdx.audio.newMusic(Gdx.files.internal("routineconversation.mp3"));
        engine.addSystem(movementSystem);
        createInstructionSystem();

        fadeTimer = RitualOfConversation.FADE_TIMEOUT;
        fadeOutCallback = new TransitionCallback() {
            @Override
            public void callback() {
                game.gotoEndScreen();
            }
        };
        fadeInCallback = new TransitionCallback() {
            @Override
            public void callback() {
                transitioningIn = false;
            }
        };

        instructionCollisionChoiceQueued = false;
        instructionAwkwardnessBarQueued = false;

        music.setLooping(true);
        music.play();
    }

    public void bumpUiZIndex() {
        npcTextActor.setZIndex(999);
        npcCharacterActor.setZIndex(999);
    }

    public void createDialogueSystem(Player player) {
        NpcEntity npcEntity = new NpcEntity();
        npcTextActor = new NpcTextActor(atlas, npcFont, npcEntity);
        npcCharacterActor = new NpcCharacterActor(atlas, npcEntity);
        AwkwardBar awkwardBar = new AwkwardBar();
        dialogueSystem = new DialogueSystem(this, npcEntity, player, awkwardBar);

        AwkwardBarActor awkwardBarActor = new AwkwardBarActor(atlas, awkwardBar);
        engine.addSystem(dialogueSystem);
        stage.addActor(awkwardBarActor);
        stage.addActor(npcTextActor);
        stage.addActor(npcCharacterActor);
    }

    public void createInstructionSystem() {
        Instructions instructions = new Instructions();
        instructionSystem = new InstructionSystem(instructions);
        engine.addSystem(instructionSystem);
        stage.addActor(new InstructionsActor(instructionsFont, instructions, shapeRenderer));
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
        music.stop();
        music.dispose();
        transitioningOut = true;
        fadeTimer = 0;
    }

    public void queueCollisionChoiceInstruction() {
        instructionCollisionChoiceQueued = true;
    }

    public void queueAwkwardnessBarInstruction() {
        instructionAwkwardnessBarQueued = true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (instructionSystem.isShowingInstruction()) {
            if (movementSystem.checkProcessing()) {
                movementSystem.setProcessing(false);
            }
            if (dialogueSystem.checkProcessing()) {
                dialogueSystem.setProcessing(false);
            }
        } else {
            if (!movementSystem.checkProcessing()) {
                movementSystem.setProcessing(true);
            }
            if (!dialogueSystem.checkProcessing()) {
                dialogueSystem.setProcessing(true);
            }
        }

        if (!transitioningOut) {
            update(delta);
        }

        stage.act(delta);
        stage.draw();

        if (instructionAwkwardnessBarQueued) {
            instructionSystem.nextInstructionState(InstructionState.AWKWARDNESS_BAR);
            instructionAwkwardnessBarQueued = false;
        } else if (instructionCollisionChoiceQueued) {
            instructionSystem.nextInstructionState(InstructionState.COLLISION_CHOICE);
            instructionCollisionChoiceQueued = false;
        }

        if (transitioningOut) {
            fadeTimer += delta;
            game.drawWhiteTransparentSquare(shapeRenderer, fadeTimer / RitualOfConversation.FADE_TIMEOUT, fadeOutCallback);
        } else if (transitioningIn) {
            fadeTimer -= delta;
            game.drawWhiteTransparentSquare(shapeRenderer, fadeTimer / RitualOfConversation.FADE_TIMEOUT, fadeInCallback);
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
        npcFont.dispose();
        instructionsFont.dispose();
        ObjectMap.Entries<String, Sound> it = SoundManager.sounds.iterator();
        while (it.hasNext()) {
            ObjectMap.Entry<String, Sound> entry = it.next();
            entry.value.dispose();
        }

        SoundManager.sounds.clear();
        shapeRenderer.dispose();
    }
}
