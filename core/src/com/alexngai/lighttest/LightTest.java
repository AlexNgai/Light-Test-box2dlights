package com.alexngai.lighttest;

import com.badlogic.gdx.ApplicationListener;
import com.alexngai.lighttest.helpers.Assets;
import com.alexngai.lighttest.helpers.InputHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;

public class LightTest extends Game {

	GameInstanceScreen currentScreen;
	
	@Override
	public void create() {
		Assets.load();
		
		loadGameScreen();
	}

	public void dispose(){
		Assets.dispose();
	}
	
	public void loadGameScreen(){
		currentScreen = new GameInstanceScreen();
		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(currentScreen.getStage());
		multiplexer.addProcessor(currentScreen.getInputProcessor());
		Gdx.input.setInputProcessor(multiplexer);
		setScreen(currentScreen);
	}
}
