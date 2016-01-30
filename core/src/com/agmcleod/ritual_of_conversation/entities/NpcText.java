package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.ComponentMappers;
import com.agmcleod.ritual_of_conversation.components.TextContentComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.badlogic.gdx.Gdx;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class NpcText extends GameEntity {
    private final float HEIGHT = 50;
    public NpcText() {
        add(new TransformComponent(30, Gdx.graphics.getHeight() - HEIGHT, Gdx.graphics.getWidth(), HEIGHT));
        add(new TextContentComponent());
    }

    public TextContentComponent getTextContentComponent() {
        return ComponentMappers.textContent.get(this);
    }
}
