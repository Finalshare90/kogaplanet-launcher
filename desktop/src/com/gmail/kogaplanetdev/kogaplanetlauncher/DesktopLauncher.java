package com.gmail.kogaplanetdev.kogaplanetlauncher;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		// Window objects, do not touch:)
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();	
		
		// it is self-explaning.
		LLTJokes jokes = new LLTJokes();
		
		
		//graphics config & window config
		config.setForegroundFPS(60);
		config.setDecorated(true);
		config.setResizable(true);
		config.setMaximized(true);
		config.setWindowIcon("logos/256x_kgp.png");
		config.setTitle("KogaPlanet Launcher 3.0.0 alpha(Build Abyssinian) " + jokes.chooseAjoke());	
		
		new Lwjgl3Application(new KogaPlanetLauncher(), config);
	}
}
