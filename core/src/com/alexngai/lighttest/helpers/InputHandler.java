package com.alexngai.lighttest.helpers;

import java.io.IOException;
import java.util.Random;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.alexngai.lighttest.GameRenderer2;
import com.alexngai.lighttest.GameWorld;
import com.alexngai.lighttest.gameobjects.GameCharacter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class InputHandler implements InputProcessor {

	private OrthographicCamera camera;
	private RayHandler handler; 
	private UIHelper uihandler;
	private GameWorld gameWorld;
	private World world;
	
	private Touchpad touchpad;
	
	private GameCharacter gamechar;
	private Body circleBody;
	private Body groundBody;
	
	private MouseJoint mouseJoint = null;
	private MouseJoint joystickMouseJoint = null;
	
	private Vector3 testPoint = new Vector3();
	private Vector2 target = new Vector2();
	
	private boolean downPressed = false;
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	
	public InputHandler(GameWorld gameWorld, GameRenderer2 gameRenderer, UIHelper uihandler){
		//Gdx.input.setInputProcessor(this);
		camera = gameRenderer.getCamera();
		gamechar = gameWorld.getPlayerCharacter();
		circleBody = gamechar.getBody();
		groundBody = gameRenderer.getTiledMapHelper().getGroundBody();
		this.gameWorld = gameWorld;
		world = gameWorld.getWorld();
		handler = gameRenderer.getRayHandler();
		this.uihandler = uihandler;
		touchpad = uihandler.getTouchpad();
	}
	
	public void update(float delta){
		float x = touchpad.getKnobPercentX();
		float y = touchpad.getKnobPercentY();
		float mag = (float) Math.sqrt(x*x + y*y);
		
		if (x==0 && y ==0){
			if (joystickMouseJoint != null){
				world.destroyJoint(joystickMouseJoint);
				joystickMouseJoint = null;
			}
		}
		
		else if (x!=0 || y!=0){
			if (joystickMouseJoint != null) world.destroyJoint(joystickMouseJoint);
			Vector2 pos = circleBody.getPosition();
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundBody;
			def.bodyB = circleBody;

			def.collideConnected = true;
			def.target.set(pos.x, pos.y);
			def.maxForce = mag*50.0f * circleBody.getMass();
	
			joystickMouseJoint = (MouseJoint) world.createJoint(def);
			Vector2 pos2 = new Vector2(pos.x+10*x, pos.y+10*y);
			joystickMouseJoint.setTarget(pos2);
			circleBody.setAwake(true);
		}
	}
	

	@Override
	public boolean keyDown(int keycode) {
		Gdx.app.log("Input", "code: " + keycode);
		Vector2 temp = circleBody.getPosition();
		if (keycode == Input.Keys.RIGHT){
			rightPressed = true;
		}

		if (keycode == Input.Keys.LEFT){
			leftPressed = true;
		}

		if (keycode == Input.Keys.UP){
		}

		if (keycode == Input.Keys.DOWN){
			downPressed = true;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.RIGHT){
			rightPressed = false;
		}
		if (keycode == Input.Keys.LEFT){
			leftPressed = false;
		}
		if (keycode == Input.Keys.UP){
		}
		if (keycode == Input.Keys.DOWN){
			downPressed = false;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Gdx.app.log("Input", "touch down");
		testPoint.set(screenX, screenY,0);
		camera.unproject(testPoint);

		if (downPressed){
			Random rand = new Random();
			float r = (float) (rand.nextFloat() + 0.5);
			float g = (float) (rand.nextFloat() + 0.5);
			float b = (float) (rand.nextFloat() + 0.5);
			Color randcol = new Color(r,g,b, .6f);
			new PointLight(handler, 50, randcol, 25, testPoint.x, testPoint.y);
			
		} else if (leftPressed){
			gameWorld.addNewPlayer(false, testPoint.x, testPoint.y);
		} else if (rightPressed){
			gameWorld.addNewPlayer(true, testPoint.x, testPoint.y);
		} else if (uihandler.getPlaceTeam()){
			gameWorld.addNewPlayer(true, testPoint.x, testPoint.y);
		} else if (uihandler.getPlaceEnemy()){
			gameWorld.addNewPlayer(false, testPoint.x, testPoint.y);
			//ServerConnection.sendMsg();
		}
		else {
			if (mouseJoint != null) world.destroyJoint(mouseJoint);

			Vector2 pos = circleBody.getPosition();
			MouseJointDef def = new MouseJointDef();
			def.bodyA = groundBody;
			def.bodyB = circleBody;

			def.collideConnected = true;
			def.target.set(pos.x, pos.y);
			def.maxForce = 50.0f * circleBody.getMass();

			mouseJoint = (MouseJoint) world.createJoint(def);
			Vector2 pos2 = new Vector2(testPoint.x, testPoint.y);
			mouseJoint.setTarget(pos2);
			circleBody.setAwake(true);
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Gdx.app.log("Input", "touch up");
		if (mouseJoint != null) {
			world.destroyJoint(mouseJoint);
			mouseJoint = null;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (mouseJoint != null) {
			camera.unproject(testPoint.set(screenX,screenY,0));
			mouseJoint.setTarget(target.set(testPoint.x, testPoint.y));
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
