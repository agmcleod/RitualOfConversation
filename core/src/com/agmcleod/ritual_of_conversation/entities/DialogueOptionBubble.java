package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.*;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class DialogueOptionBubble extends GameEntity {
    public static final float WIDTH = 300;
    public static final float HEIGHT = 150;
    public DialogueOptionBubble(float x, float y) {
        add(new TransformComponent(x, y, WIDTH, HEIGHT));
        add(new TextContentComponent());
        add(new DialogueOptionComponent());
        add(new BoundingBoxComponent(WIDTH, HEIGHT));
    }

    public Rectangle getBoundingBox() {
        return ComponentMappers.collidable.get(this).rectangle;
    }

    public TextContentComponent getTextContentComponent() {
        return ComponentMappers.textContent.get(this);
    }

    public DialogueOptionComponent getDialogueOptionComponent() {
        return ComponentMappers.dialogueOptions.get(this);
    }
}
