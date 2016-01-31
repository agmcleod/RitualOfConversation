package com.agmcleod.ritual_of_conversation.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by aaronmcleod on 2016-01-31.
 */
public class InstructionStateComponent implements Component {
    public InstructionState instructionState = InstructionState.NONE;
    public String instructionText = null;
}
