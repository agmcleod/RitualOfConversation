package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.BoundingBoxComponent;
import com.agmcleod.ritual_of_conversation.components.ComponentMappers;
import com.agmcleod.ritual_of_conversation.components.PlayerComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.badlogic.gdx.Gdx;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class Player extends GameEntity {
    private final float HEIGHT = 100;
    private final float WIDTH = 100;

    public Player() {
        TransformComponent transform = new TransformComponent(Gdx.graphics.getWidth() / 2, 0 + HEIGHT / 2, WIDTH, HEIGHT);
        add(transform);
        add(new BoundingBoxComponent(WIDTH, HEIGHT));
        add(new PlayerComponent());
    }

    public PlayerComponent getPlayerComponent() {
        return ComponentMappers.player.get(this);
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
