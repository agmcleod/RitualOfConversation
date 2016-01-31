package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.components.InstructionState;
import com.agmcleod.ritual_of_conversation.components.InstructionStateComponent;
import com.agmcleod.ritual_of_conversation.entities.Instructions;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Created by aaronmcleod on 2016-01-31.
 */
public class InstructionSystem extends EntitySystem {
    private final float INSTRUCTION_TIMEOUT = 3.5f;
    private Instructions instructions;
    private float instructionTimer;
    private boolean showingInstruction;

    public InstructionSystem(Instructions instructions) {
        this.instructions = instructions;
        showingInstruction = false;
    }

    public String getTextForInstructionState(InstructionState instructionState) {
        switch (instructionState) {
            case COLLISION_CHOICE:
                return "There are more than one answers to give.";
            case AWKWARDNESS_BAR:
                return "As you talk, the awkwardness of the conversation goes up & down.";
            default:
                return null;

        }
    }

    public boolean isShowingInstruction() {
        return showingInstruction;
    }

    public void nextInstructionState(InstructionState requestedState) {
        InstructionStateComponent instructionStateComponent = instructions.getInstructionStateComponent();
        InstructionState instructionState = instructionStateComponent.instructionState;
        int compare = requestedState.compareTo(instructionState);
        boolean instructionChanged = true;
        if (compare == 1) {
            switch (requestedState) {
                case COLLISION_CHOICE:
                case AWKWARDNESS_BAR:
                    showingInstruction = true;
                    break;
                case COLLISION_CHOICE_VIEWED:
                case DONE:
                    showingInstruction = false;
                    break;
            }
            instructionState = requestedState;
        } else {
            instructionChanged = false;
        }

        if (instructionChanged) {
            instructionStateComponent.instructionState = instructionState;
            String instructionText = getTextForInstructionState(instructionStateComponent.instructionState);
            instructionStateComponent.instructionText = instructionText;
            if (instructionText != null) {
                instructionTimer = INSTRUCTION_TIMEOUT;
            }
        }
    }

    @Override
    public void update(float dt) {
        if (instructionTimer > 0) {
            instructionTimer -= dt;
            if (instructionTimer <= 0) {
                InstructionState instructionState = instructions.getInstructionStateComponent().instructionState;
                switch (instructionState) {
                    case COLLISION_CHOICE:
                        nextInstructionState(InstructionState.COLLISION_CHOICE_VIEWED);
                        break;
                    case AWKWARDNESS_BAR:
                        nextInstructionState(InstructionState.DONE);
                        break;
                }
            }
        }
    }
}
