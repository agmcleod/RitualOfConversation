package com.agmcleod.ritual_of_conversation.entities;

import com.agmcleod.ritual_of_conversation.components.*;
import com.badlogic.gdx.Gdx;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class NpcEntity extends GameEntity {
    public NpcEntity() {
        add(new TextContentComponent());
        NpcStateComponent stateComponent = new NpcStateComponent();
        stateComponent.state = NpcState.CONTENT;
        add(stateComponent);
    }

    public NpcStateComponent getNpcStateComponent() {
        return ComponentMappers.npcState.get(this);
    }

    public TextContentComponent getTextContentComponent() {
        return ComponentMappers.textContent.get(this);
    }

    public void setEmotionState(float awkwardnessPercent) {
        if (awkwardnessPercent >= 0.66f) {
            getNpcStateComponent().state = NpcState.UNCOMFORTABLE;
        } else if (awkwardnessPercent >= 0.33f) {
            getNpcStateComponent().state = NpcState.NEUTRAL;
        } else {
            getNpcStateComponent().state = NpcState.CONTENT;
        }
    }
}
