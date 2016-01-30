package com.agmcleod.ritual_of_conversation.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.agmcleod.ritual_of_conversation.RitualOfConversation;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 960;
		config.height = 540;
		new LwjglApplication(new RitualOfConversation(), config);
	}
}
