package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.components.BoundingBoxComponent;
import com.agmcleod.ritual_of_conversation.components.ComponentMappers;
import com.agmcleod.ritual_of_conversation.components.PlayerComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class Player extends GameEntity {
    private final float HEIGHT = 92;
    private final float WIDTH = 70;

    public Player() {
        TransformComponent transform = new TransformComponent(RitualOfConversation.GAME_WIDTH / 2, HEIGHT  / 2, WIDTH, HEIGHT);
        add(transform);
        add(new BoundingBoxComponent(-WIDTH/2, 0, WIDTH, HEIGHT /2));
        add(new PlayerComponent());
    }

    public PlayerComponent getPlayerComponent() {
        return ComponentMappers.player.get(this);
    }

    public Rectangle getBoundingBox() {
        return ComponentMappers.collidable.get(this).rectangle;
    }

    public boolean isMovingLeft() {
        return getPlayerComponent().movingLeft;
    }

    public boolean isMovingRight() {
        return getPlayerComponent().movingRight;
    }

    public boolean setInputKeyState(int keyCode, boolean state) {
        return getPlayerComponent().setInputKeyState(keyCode, state);
    }
}
