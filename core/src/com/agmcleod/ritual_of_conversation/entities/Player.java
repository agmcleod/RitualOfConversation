package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class Player extends GameEntity {
    private final float height = 100;
    private final float width = 100;

    public Player() {
        TransformComponent transform = new TransformComponent(Gdx.graphics.getWidth() / 2, 0 + height / 2, width, height);
        add(transform);
    }
}
