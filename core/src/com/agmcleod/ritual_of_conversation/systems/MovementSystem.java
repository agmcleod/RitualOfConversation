package com.agmcleod.ritual_of_conversation.systems;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.actors.AwkwardBarActor;
import com.agmcleod.ritual_of_conversation.components.PlayerComponent;
import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class MovementSystem extends EntitySystem {
    private ImmutableArray<Entity> entities;
    private final float PLAYER_VELOCITY = 500;
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float dt) {
        for (int i = 0; i < entities.size(); ++i) {
            Player player = (Player) entities.get(i);

            TransformComponent transformComponent = player.getTransform();
            Vector2 position = transformComponent.position;
            if (player.isMovingLeft()) {
                position.x -= PLAYER_VELOCITY * dt;
            } else if (player.isMovingRight()) {
                position.x += PLAYER_VELOCITY * dt;
            }

            float hWidth = transformComponent.width / 2;

            if (position.x - hWidth < 0) {
                position.x = hWidth;
            } else if (position.x + hWidth > RitualOfConversation.GAME_WIDTH - AwkwardBarActor.BORDER_WIDTH) {
                position.x = RitualOfConversation.GAME_WIDTH - AwkwardBarActor.BORDER_WIDTH - hWidth;
            }
        }
    }
}
