package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.helpers.TextureRegionDrawer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class BackgroundActor extends Actor {
    private TextureAtlas.AtlasRegion region;
    public BackgroundActor(TextureAtlas atlas) {
        region = atlas.findRegion("background");
    }

    @Override
    public void draw(Batch batch, float alpha) {
        TextureRegionDrawer.drawRegionForBatch(batch, region, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
