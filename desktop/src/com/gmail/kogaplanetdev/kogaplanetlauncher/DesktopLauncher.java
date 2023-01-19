package com.gmail.kogaplanetdev.kogaplanetlauncher;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		
		//objeto da janela, N�O TOCAR
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();	
		
		//Outros objetos...
		LLTJokes jokes = new LLTJokes();
		
		
		//config de gr�ficos e perfomance
		config.setForegroundFPS(60);
		config.setDecorated(true);
		config.setResizable(true);
		
		
		//config relacionadas ao programa em si
		config.setMaximized(true);
		config.setWindowIcon("logos/256x_kgp.png");
		config.setTitle("KogaPlanet Launcher 2.2.3 alpha(Build BRA shortHair) " + jokes.chooseAjoke());	
		
		//inicia uma nova instancia do programa, N�O TOCAR
		new Lwjgl3Application(new KogaPlanetLauncher(), config);
	}
}
