package com.agmcleod.ritual_of_conversation;

import com.agmcleod.ritual_of_conversation.screens.PlayScreen;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RitualOfConversation extends Game {
	private PlayScreen playScreen;

	@Override
	public void create () {
		playScreen = new PlayScreen(this);
		setScreen(playScreen);
	}
}
