package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.SoundManager;
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
    private final float NEXT_TOKEN_TIME = 0.15f;
    private BitmapFont font;
    private NpcEntity npcEntity;
    private TextureRegion region;
    private String text;
    private int currentToken = 0;
    private float tokenTimer = 0;
    private String[] tokens;
    public NpcTextActor(TextureAtlas atlas, BitmapFont font, NpcEntity npcEntity) {
        text = "";
        this.npcEntity = npcEntity;
        this.font = font;
        this.region = atlas.findRegion("top_banner");
        this.setBounds(50, Gdx.graphics.getHeight() - 50, 860, 50);
        tokens = new String[0];
    }

    @Override
    public void draw(Batch batch, float alpha) {
        String componentText = npcEntity.getTextContentComponent().text;
        if (text != componentText) {
            text = componentText;
            tokens = componentText.split(" ");
            if (tokens.length == 1) {
                SoundManager.sounds.get("blip").play();
            }
            currentToken = 0;
            tokenTimer = NEXT_TOKEN_TIME;
        }
        batch.draw(this.region, getX(), getY(), getWidth(), getHeight());
        font.draw(batch, textToRender(), getX() + 10, getY() + getHeight());
    }

    public String textToRender() {
        if (tokens.length <= 1 || currentToken == tokens.length) {
            return text;
        } else {
            tokenTimer -= Gdx.graphics.getDeltaTime();
            String newText = "";
            for (int i = 0; i < (currentToken + 1); i++) {
                newText += tokens[i] + " ";
            }
            if (tokenTimer <= 0) {
                SoundManager.sounds.get("blip").play();
                currentToken++;
                tokenTimer = NEXT_TOKEN_TIME;
            }

            return newText;
        }
    }
}
