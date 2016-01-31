package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.components.TransformComponent;
import com.agmcleod.ritual_of_conversation.entities.DialogueOptionBubble;
import com.agmcleod.ritual_of_conversation.helpers.EntityToScreenConversion;
import com.agmcleod.ritual_of_conversation.helpers.TextureRegionDrawer;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * Created by aaronmcleod on 2016-01-30.
 */
public class DialogueOptionBubbleActor extends Actor {
    private DialogueOptionBubble dialogueOptionBubble;
    private BitmapFont font;
    private TextureAtlas.AtlasRegion region;
    public DialogueOptionBubbleActor(TextureAtlas atlas, BitmapFont font, DialogueOptionBubble dialogueOptionBubble) {
        this.dialogueOptionBubble = dialogueOptionBubble;
        region = atlas.findRegion("speech");
        this.font = font;
        setBoundsFromEntity();
    }

    @Override
    public void draw(Batch batch, float alpha) {
        setBoundsFromEntity();
        batch.setColor(1, 1, 1, dialogueOptionBubble.getDialogueOptionComponent().alpha);
        TextureRegionDrawer.drawRegionForBatch(batch, region, getX(), getY(), getWidth(), getHeight());
        font.draw(batch, dialogueOptionBubble.getTextContentComponent().text, getX() + 20, getY() + getHeight() - 20, getWidth() - 40, Align.left, true);
        batch.setColor(1, 1, 1, 1);
    }

    public void setBoundsFromEntity() {
        TransformComponent transformComponent = dialogueOptionBubble.getTransform();
        Vector2 position = EntityToScreenConversion.getPosition(transformComponent);
        setBounds(position.x, position.y, transformComponent.width, transformComponent.height);
    }

}
