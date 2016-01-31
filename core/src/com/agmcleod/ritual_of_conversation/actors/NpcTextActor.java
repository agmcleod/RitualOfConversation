package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.entities.NpcEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class NpcTextActor extends Actor {
    private BitmapFont font;
    private NpcEntity npcEntity;
    private TextureRegion region;
    public NpcTextActor(TextureAtlas atlas, BitmapFont font, NpcEntity npcEntity) {
        this.npcEntity = npcEntity;
        this.font = font;
        this.region = atlas.findRegion("top_banner");
        this.setBounds(50, Gdx.graphics.getHeight() - 50, 860, 50);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(this.region, getX(), getY(), getWidth(), getHeight());
        font.draw(batch, npcEntity.getTextContentComponent().text, getX() + 10, getY() + getHeight());
    }
}
