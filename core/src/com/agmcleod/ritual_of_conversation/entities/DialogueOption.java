package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.ComponentMappers;
import com.agmcleod.ritual_of_conversation.components.DialogueOptionComponent;
import com.agmcleod.ritual_of_conversation.components.TextContentComponent;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class DialogueOption extends GameEntity {
    public DialogueOption() {
        add(new TextContentComponent());
        add(new DialogueOptionComponent());
    }

    public TextContentComponent getTextContentComponent() {
        return ComponentMappers.textContent.get(this);
    }
}
