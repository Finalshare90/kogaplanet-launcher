package com.gmail.kogaplanetdev.kogaplanetlauncher.entities;

import java.util.HashMap;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionHandler implements ContactListener {

	HashMap<String, Object> fixtureDataA;
	HashMap<String, Object> fixtureDataB;
	
	@SuppressWarnings("unchecked")
	@Override
	public void beginContact(Contact arg0) {
	
		// Get the attributes of both bodies.
		fixtureDataA = (HashMap<String, Object>)arg0.getFixtureA().getUserData();
		fixtureDataB = (HashMap<String, Object>)arg0.getFixtureB().getUserData();
		
		try {
			if((Boolean)fixtureDataA.get("canKill") && (Boolean)fixtureDataB.get("canBeKilled")){
				fixtureDataB.replace("isAlive", false);
		}
		}catch(NullPointerException e){
			if((Boolean)fixtureDataB.get("canKill") && (Boolean)fixtureDataA.get("canBeKilled")){
				fixtureDataA.replace("isAlive", false);
				
		}
	}
		
	}
	@Override
	public void endContact(Contact arg0) {}
	@Override
	public void postSolve(Contact arg0, ContactImpulse arg1) {}
	@Override
	public void preSolve(Contact arg0, Manifold arg1) {}
	
}
