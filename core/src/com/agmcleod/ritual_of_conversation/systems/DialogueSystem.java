package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.entities.NpcText;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class DialogueSystem extends EntitySystem {
    private NpcText npcText;
    private Player player;
    public DialogueSystem(NpcText npcText, Player player) {
        this.player = player;
        this.npcText = npcText;
    }

    @Override
    public void addedToEngine(Engine engine) {

    }

    @Override
    public void update(float dt) {

    }

    private void setEntities() {

    }
}
