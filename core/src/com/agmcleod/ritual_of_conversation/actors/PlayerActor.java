package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.Player;
import com.agmcleod.ritual_of_conversation.helpers.EntityToScreenConversion;
import com.agmcleod.ritual_of_conversation.helpers.TextureRegionDrawer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by aaronmcleod on 2016-01-29.
 */
public class PlayerActor extends Actor {
    private Player player;
    private TextureAtlas.AtlasRegion region;
    private ShapeRenderer shapeRenderer;

    public PlayerActor(TextureAtlas textureAtlas, ShapeRenderer shapeRenderer, final Player player) {
        this.player = player;
        region = textureAtlas.findRegion("player");
        this.shapeRenderer = shapeRenderer;

        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return player.setInputKeyState(keycode, true);
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return player.setInputKeyState(keycode, false);
            }
        });
    }

    @Override
    public void draw(Batch batch, float alpha) {
        TransformComponent transformComponent = player.getTransform();
        Vector2 position = EntityToScreenConversion.getPosition(transformComponent);
        TextureRegionDrawer.drawRegionForBatch(batch, region, position.x, position.y, transformComponent.width, transformComponent.height);

        /*batch.end();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Rectangle box = player.getBoundingBox();
        shapeRenderer.rect(transformComponent.position.x + box.x, transformComponent.position.y + box.y, box.width, box.height);
        shapeRenderer.end();
        batch.begin(); */
    }
}
