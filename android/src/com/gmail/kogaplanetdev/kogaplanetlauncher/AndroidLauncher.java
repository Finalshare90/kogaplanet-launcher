package com.gmail.kogaplanetdev.kogaplanetlauncher;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gmail.kogaplanetdev.kogaplanetlauncher.KogaPlanetLauncher;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new KogaPlanetLauncher(), config);
	}
}
