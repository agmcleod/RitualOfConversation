package com.agmcleod.ritual_of_conversation.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class TransformComponent implements Component {
    public Vector2 position;
    public Vector2 scale;
    public float width;
    public float height;

    public TransformComponent(float x, float y, float w, float h) {
        position = new Vector2(x, y);
        scale = new Vector2(1, 1);
        width = w;
        height = h;
    }
}
