package com.alexngai.lighttest.network;

import com.badlogic.gdx.math.Vector2;

public class Player {

	int ID;
	Vector2 position;
	Vector2 velocity;
	Vector2 acceleration;
	
	public Player(int ID, Vector2 position, Vector2 velocity, Vector2 acceleration){
		this.ID = ID;
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
		
		//don't forget ANGULAR VELOCITY
	}
}
