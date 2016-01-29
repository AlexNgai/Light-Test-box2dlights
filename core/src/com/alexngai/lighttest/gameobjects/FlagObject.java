package com.alexngai.lighttest.gameobjects;

import box2dLight.Light;

import com.alexngai.lighttest.helpers.LightHelper;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class FlagObject {
	public static float flagRadius = 2f;
	
	private Vector2 home;
	private Vector2 position;
	private Light light;
	private TextureRegion texture;
	
	private GameCharacter player;
	
	public FlagObject(Vector2 position, TextureRegion texture, Light light){
		this.position = position;
		home = position;
		this.texture = texture;
		this.light = light;
	}
	
	public void update(){
		if (player != null){
			position = player.getBody().getPosition();
		} else position = home;
		if (light != null) light.setPosition(position);
	}
	
	public void render(SpriteBatch batch){
    	batch.draw(texture, position.x - flagRadius, position.y - flagRadius,
    			flagRadius, flagRadius, flagRadius * 2, flagRadius * 2, 1, 1, 0);
    }
	
	public Vector2 getPosition(){
		return position;
	}
	
	public void setPosition(Vector2 position){
		this.position = position;
	}
	
	public GameCharacter getHoldingPlayer(){
		return player;
	}
	
	public void setHoldingPlayer(GameCharacter player){
		if (this.player == null){
			this.player = player;
		}
	}
	
	public void returnHome(){
		position = home;
		player = null;
	}
}
