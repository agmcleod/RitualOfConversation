package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.ComponentMappers;
import com.agmcleod.ritual_of_conversation.components.InstructionStateComponent;
import com.badlogic.ashley.core.Entity;

/**
 * Created by aaronmcleod on 2016-01-31.
 */
public class Instructions extends Entity {
    public Instructions() {
        add(new InstructionStateComponent());
    }

    public InstructionStateComponent getInstructionStateComponent() {
        return ComponentMappers.instructionstate.get(this);
    }
}
