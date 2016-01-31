package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.badlogic.gdx.Gdx;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class AwkwardBar extends GameEntity {
    private final float WIDTH = 16;
    private final float HEIGHT = 507;
    private float startY = 20-HEIGHT / 2;
    public AwkwardBar() {
        add(new TransformComponent(Gdx.graphics.getWidth() - 25, startY, WIDTH, HEIGHT));
    }

    public void setAwkwardOffset(float awkwardBarPercent) {
        getTransform().position.y = startY + HEIGHT * awkwardBarPercent;
        System.out.println();
    }
}
