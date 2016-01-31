package com.agmcleod.ritual_of_conversation.actors;

import com.agmcleod.ritual_of_conversation.RitualOfConversation;
import com.agmcleod.ritual_of_conversation.components.InstructionState;
import com.agmcleod.ritual_of_conversation.components.InstructionStateComponent;
import com.agmcleod.ritual_of_conversation.entities.Instructions;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * Created by aaronmcleod on 2016-01-31.
 */
public class InstructionsActor extends Actor {
    private final float WIDTH = 500;
    private Instructions instructions;
    private BitmapFont instructionsFont;
    private ShapeRenderer shapeRenderer;
    public InstructionsActor(BitmapFont instructionsFont, Instructions instructions, ShapeRenderer shapeRenderer) {
        this.instructions = instructions;
        this.instructionsFont = instructionsFont;
        this.shapeRenderer = shapeRenderer;
        setBounds((RitualOfConversation.GAME_WIDTH - WIDTH) / 2, RitualOfConversation.GAME_HEIGHT / 2 + 100, WIDTH, 200);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        InstructionStateComponent instructionStateComponent = instructions.getInstructionStateComponent();
        InstructionState state = instructionStateComponent.instructionState;
        if (state == InstructionState.AWKWARDNESS_BAR || state == InstructionState.COLLISION_CHOICE) {
            batch.end();
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.6f);
            shapeRenderer.rect(0, 0, RitualOfConversation.GAME_WIDTH, RitualOfConversation.GAME_HEIGHT);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
            batch.begin();

            instructionsFont.draw(
                    batch, instructionStateComponent.instructionText,
                   getX(), getY(), getWidth(), Align.center, true
            );
        }
    }
}
