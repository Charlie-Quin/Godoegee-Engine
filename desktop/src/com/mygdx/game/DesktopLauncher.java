package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
import org.lwjgl.system.Configuration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("PenguinMassacureGaim");
		config.setWindowedMode(1024, 600);



		//config.GLFW_CHECK_THREAD0 = false;

		new Lwjgl3Application(new MyGdxGame(), config);
	}
}
