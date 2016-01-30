package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.NpcText;
import com.agmcleod.ritual_of_conversation.helpers.EntityToScreenConversion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class NpcTextActor extends Actor {
    private BitmapFont font;
    private NpcText npcText;
    private TextureRegion region;
    public NpcTextActor(TextureAtlas atlas, BitmapFont font, NpcText npcText) {
        this.npcText = npcText;
        this.font = font;
        this.region = atlas.findRegion("npctextbackground");
    }

    @Override
    public void draw(Batch batch, float alpha) {
        TransformComponent transformComponent = npcText.getTransform();
        Vector2 position = EntityToScreenConversion.getPosition(transformComponent);
        batch.draw(this.region, 0, Gdx.graphics.getHeight() - region.getRegionHeight());
        font.draw(batch, npcText.getTextContentComponent().text, position.x, position.y);
    }
}
