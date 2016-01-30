package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.ComponentMappers;
import com.agmcleod.ritual_of_conversation.components.DialogueOptionComponent;
import com.agmcleod.ritual_of_conversation.components.TextContentComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;

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
    }

    public TextContentComponent getTextContentComponent() {
        return ComponentMappers.textContent.get(this);
    }

    public DialogueOptionComponent getDialogueOptionComponent() {
        return ComponentMappers.dialogueOptions.get(this);
    }
}
