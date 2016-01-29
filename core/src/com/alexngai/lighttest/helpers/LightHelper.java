package com.alexngai.lighttest.helpers;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

//contains all light objects and settings
public class LightHelper {
	
	RayHandler handler;
	
	public LightHelper(World world, Camera camera){
		handler = new RayHandler(world);
		handler.setCombinedMatrix(camera.combined);
		
		//handler.setAmbientLight(.1f, .1f, .1f, .1f);
	}

	public void addPointLight(int numRays, Color c, float radius, Vector2 position){
		new PointLight(handler, numRays, c, radius, position.x, position.y);
	}
	
	public RayHandler getRayHandler(){
		return handler;
	}
}
