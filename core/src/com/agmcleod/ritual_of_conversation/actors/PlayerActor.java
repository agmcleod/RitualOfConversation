package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.agmcleod.ritual_of_conversation.helpers.EntityToScreenConversion;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class PlayerActor extends Actor {
    private Player player;
    private TextureRegion region;

    public PlayerActor(Player player, TextureAtlas textureAtlas) {
        this.player = player;
        region = textureAtlas.findRegion("player");
    }

    @Override
    public void draw(Batch batch, float alpha) {
        TransformComponent transformComponent = player.getTransform();
        Vector2 position = EntityToScreenConversion.getPosition(transformComponent);
        batch.draw(region, position.x, position.y, transformComponent.width, transformComponent.height);
    }
}
