package com.agmcleod.ritual_of_conversation.screens;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.helpers.TextureRegionDrawer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class StartScreen implements Screen, InputProcessor {
    private RitualOfConversation game;
    private TextureAtlas atlas;
    private TextureAtlas.AtlasRegion background;
    private SpriteBatch batch;
    private TransitionCallback fadeOutCallback;
    private float fadeTimer;
    private boolean transitioningOut;
    private ShapeRenderer shapeRenderer;
    public StartScreen(final RitualOfConversation game) {
        this.game = game;
        atlas = new TextureAtlas(Gdx.files.internal("atlas.txt"));
        background = atlas.findRegion("startscreen");
        batch = new SpriteBatch();
        fadeTimer = 0;
        transitioningOut = false;
        fadeOutCallback = new TransitionCallback() {
            @Override
            public void callback() {
                game.gotoPlayScreen();
            }
        };
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        TextureRegionDrawer.drawRegionForBatch(batch, background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if (transitioningOut) {
            fadeTimer += delta;
            game.drawWhiteTransparentSquare(shapeRenderer, fadeTimer / RitualOfConversation.FADE_TIMEOUT, fadeOutCallback);
        }
    }

    @Override
    public void resize(int width, int height) {

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
        batch.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        transitioningOut = true;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
