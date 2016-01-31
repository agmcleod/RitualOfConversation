package com.agmcleod.ritual_of_conversation.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class BoundingBoxComponent implements Component {
    public Rectangle rectangle;

    public void makeRectangle(float x, float y, float w, float h) {
        rectangle = new Rectangle(x, y, w, h);
    }

    public BoundingBoxComponent(float w, float h) {
        makeRectangle(-w/2, -h/2, w, h);
    }

    public BoundingBoxComponent(float x, float y, float w, float h) {
        makeRectangle(x, y, w, h);
    }
}
