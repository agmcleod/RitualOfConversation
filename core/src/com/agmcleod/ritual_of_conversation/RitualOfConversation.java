package com.agmcleod.ritual_of_conversation;

import com.agmcleod.ritual_of_conversation.screens.EndScreen;
import com.agmcleod.ritual_of_conversation.screens.PlayScreen;
import com.agmcleod.ritual_of_conversation.screens.StartScreen;
import com.agmcleod.ritual_of_conversation.screens.TransitionCallback;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class RitualOfConversation extends Game {
	public static final float FADE_TIMEOUT = 0.5f;
	private Color whiteColor;
	private EndScreen endScreen;
	private PlayScreen playScreen;
	private StartScreen startScreen;

	@Override
	public void create () {
		whiteColor = new Color(1, 1, 1, 1);
		playScreen = new PlayScreen(this);
		endScreen = new EndScreen(this);
		startScreen = new StartScreen(this);
		setScreen(startScreen);
	}

	public void gotoEndScreen() {
		setScreen(endScreen);
	}

	public void gotoPlayScreen() {
		setScreen(playScreen);
	}

	public void drawWhiteTransparentSquare(ShapeRenderer shapeRenderer, float percent, TransitionCallback callback) {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		whiteColor.set(1, 1, 1, percent);
		shapeRenderer.setColor(whiteColor);
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		shapeRenderer.rect(0, 0, w, h);
		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		if (percent > 1.0f || percent < 0f) {
			callback.callback();
		}
	}
}
