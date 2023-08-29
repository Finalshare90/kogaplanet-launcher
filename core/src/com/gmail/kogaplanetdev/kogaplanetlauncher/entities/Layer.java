package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import java.util.ArrayList;

public class Layer {
	
	int layerIndex;
	ArrayList<String> tileMap;

	public Layer(ArrayList<String> tileMap, int layerIndex){
		this.tileMap = tileMap;
		this.layerIndex = layerIndex;
	}
}