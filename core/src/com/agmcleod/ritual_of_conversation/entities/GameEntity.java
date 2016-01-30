package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.ComponentMappers;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public abstract class GameEntity extends Entity {
    public TransformComponent getTransform() {
        return ComponentMappers.transformable.get(this);
    }
}
