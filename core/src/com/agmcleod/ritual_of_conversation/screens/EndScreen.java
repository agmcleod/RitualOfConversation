package com.agmcleod.ritual_of_conversation.screens;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class EndScreen implements Screen {
    private RitualOfConversation game;
    private TextureAtlas atlas;
    private TextureRegion background;
    private SpriteBatch batch;
    public EndScreen(RitualOfConversation game) {
        this.game = game;
    }
    @Override
    public void show() {
        atlas = new TextureAtlas(Gdx.files.internal("atlas.txt"));
        background = atlas.findRegion("endscreen");
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0, RitualOfConversation.GAME_WIDTH, RitualOfConversation.GAME_HEIGHT);
        batch.end();
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
    }
}
