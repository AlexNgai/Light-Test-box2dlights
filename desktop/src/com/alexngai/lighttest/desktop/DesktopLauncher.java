package com.alexngai.lighttest.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.alexngai.lighttest.LightTest;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        //config.useGL20 = true;
		config.width = 960;//1080/2;
        config.height = 540;//1920/2;
		new LwjglApplication(new LightTest(), config);
	}
}
