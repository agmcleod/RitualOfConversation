package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.entities.NpcEntity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class NpcCharacterActor extends Actor {
    private NpcEntity npcEntity;
    private TextureRegion contentRegion;
    private TextureRegion neutralRegion;
    private TextureRegion uncomfortableRegion;
    public NpcCharacterActor(TextureAtlas atlas, NpcEntity npcEntity) {
        this.npcEntity = npcEntity;
        setBounds(0, RitualOfConversation.GAME_HEIGHT - 50, 50, 50);
        contentRegion = atlas.findRegion("content");
        neutralRegion = atlas.findRegion("neutral");
        uncomfortableRegion = atlas.findRegion("uncomfortable");
    }

    @Override
    public void draw(Batch batch, float alpha) {
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
