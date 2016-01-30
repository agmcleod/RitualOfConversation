package com.agmcleod.ritual_of_conversation.helpers;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class EntityToScreenConversion {
    private static Vector2 cache = new Vector2();

    public static Vector2 getPosition(TransformComponent transformComponent) {
        cache.set(transformComponent.position.x - transformComponent.width / 2, transformComponent.position.y - transformComponent.height / 2);
        return cache;
    }
}

