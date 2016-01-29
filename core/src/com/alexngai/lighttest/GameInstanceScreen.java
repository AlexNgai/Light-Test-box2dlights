package com.alexngai.lighttest;

import java.util.Random;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.alexngai.lighttest.gameobjects.GameCharacter;
import com.alexngai.lighttest.gameobjects.ObjectTable;
import com.alexngai.lighttest.helpers.InputHandler;
import com.alexngai.lighttest.helpers.TiledMapHelper;
import com.alexngai.lighttest.helpers.UIHelper;
import com.alexngai.lighttest.network.ClientConnection;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameInstanceScreen implements Screen{

	//controls game world, input handler, and renderer
	//also includes tilemap helper and 
	InputHandler inputHandler;
	GameRenderer2 gameRenderer;
	GameWorld gameWorld;
	UIHelper uiHandler;
	ClientConnection connection;
	
	public GameInstanceScreen() {
		uiHandler = new UIHelper();
		gameWorld = new GameWorld();
		gameRenderer = new GameRenderer2(gameWorld, uiHandler);
		inputHandler = new InputHandler(gameWorld, gameRenderer, uiHandler);
		
		gameWorld.setHandler(gameRenderer.getRayHandler());
		gameWorld.postCreate();
		
		//connection = new ClientConnection(gameWorld);
	}

	public void dispose(){
		gameWorld.dispose();
	}

	@Override
	public void render(float delta){
		inputHandler.update(delta);
		//connection.update(delta);
		gameWorld.update(delta);
		gameRenderer.render(delta);
	}
	
	public void resize(int width, int height){
		gameRenderer.getViewport().update(width, height);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	public InputProcessor getInputProcessor(){
		return inputHandler;
	}

	public Stage getStage(){
		return gameRenderer.getStage();
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

}
