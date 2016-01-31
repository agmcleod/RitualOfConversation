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
    private TextureRegion contentRegion;
    private TextureRegion neutralRegion;
    private TextureRegion uncomfortableRegion;
    public NpcTextActor(TextureAtlas atlas, BitmapFont font, NpcEntity npcEntity) {
        this.npcEntity = npcEntity;
        this.font = font;
        this.region = atlas.findRegion("top_banner");
        this.setBounds(50, Gdx.graphics.getHeight() - 50, 860, 50);

        contentRegion = atlas.findRegion("content");
        neutralRegion = atlas.findRegion("neutral");
        uncomfortableRegion = atlas.findRegion("uncomfortable");
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(this.region, getX(), getY(), getWidth(), getHeight());
        font.draw(batch, npcEntity.getTextContentComponent().text, getX() + 10, getY() + getHeight());
        TextureRegion region;
        switch (npcEntity.getNpcStateComponent().state) {
            case CONTENT:
                region = contentRegion;
                break;
            case NEUTRAL:
                region = neutralRegion;
                break;
            default:
                region = uncomfortableRegion;
                break;
        }

        batch.draw(region, 0, getY(), 50, 50);
    }
}
