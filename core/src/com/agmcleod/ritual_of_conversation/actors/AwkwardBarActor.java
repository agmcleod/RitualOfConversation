package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.AwkwardBar;
import com.agmcleod.ritual_of_conversation.helpers.EntityToScreenConversion;
import com.agmcleod.ritual_of_conversation.helpers.TextureRegionDrawer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class AwkwardBarActor extends Actor {
    private AwkwardBar awkwardBar;
    private TextureAtlas.AtlasRegion barBorder;
    private TextureAtlas.AtlasRegion bar;
    public AwkwardBarActor(TextureAtlas atlas, AwkwardBar awkwardBar) {
        this.awkwardBar = awkwardBar;
        this.bar = atlas.findRegion("awkwardbar");
        barBorder = atlas.findRegion("barborder");
    }

    @Override
    public void draw(Batch batch, float alpha) {
        TransformComponent transformComponent = awkwardBar.getTransform();
        Vector2 position = EntityToScreenConversion.getPosition(transformComponent);
        setBounds(position.x, position.y, transformComponent.width, transformComponent.height);
        TextureRegionDrawer.drawRegionForBatch(batch, bar, getX(), getY(), getWidth(), getHeight());
        TextureRegionDrawer.drawRegionForBatch(batch, barBorder, Gdx.graphics.getWidth() - 50, 0, 50, Gdx.graphics.getHeight());
    }
}
