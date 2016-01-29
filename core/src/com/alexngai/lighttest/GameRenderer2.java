package com.alexngai.lighttest;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.alexngai.lighttest.GameInstanceScreen;
import com.alexngai.lighttest.gameobjects.GameCharacter;
import com.alexngai.lighttest.helpers.Assets;
import com.alexngai.lighttest.helpers.InputHandler;
import com.alexngai.lighttest.helpers.LightHelper;
import com.alexngai.lighttest.helpers.TiledMapHelper;
import com.alexngai.lighttest.helpers.UIHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

//contains all render objects
public class GameRenderer2 {

	public boolean lights_enabled = true;
	public static float PIXELS_PER_METER = 10f;

	public static float height = 100;
	public static float width;
	OrthographicCamera camera;
	Viewport viewport;
	Box2DDebugRenderer renderer;
	SpriteBatch batch = new SpriteBatch();
	FPSLogger logger;
	RayHandler handler;
	TiledMapHelper tiledMapHelper;
	LightHelper lightHelper;
	UIHelper uiHelper;
	Stage stage;
	
	GameWorld gameInstance;
	World world;

	GameCharacter playerCharacter;
	Body circleBody;

	public GameRenderer2(GameWorld gameInstance, UIHelper uihandler){
		this.gameInstance = gameInstance;
		world = gameInstance.getWorld();
		playerCharacter = gameInstance.getPlayerCharacter();
		circleBody = playerCharacter.getBody();


		float ppu = Gdx.graphics.getHeight() / height;
		width = Gdx.graphics.getWidth() / ppu;

		camera = new OrthographicCamera(width,height);
		camera.position.set(width/2, height/2, 0);
		viewport = new FitViewport(width, height, camera);

		stage = uihandler.getStage();
		camera.update();
		
		tiledMapHelper = new TiledMapHelper(camera);
		tiledMapHelper.loadCollisions("data/textures_1_bounds.txt", world, PIXELS_PER_METER);

		
		renderer = new Box2DDebugRenderer(false, false, false, true, false, false);
		//renderer = new Box2DDebugRenderer();
		logger = new FPSLogger();
		batch = new SpriteBatch();

		lightHelper = new LightHelper(world, camera);
		handler = lightHelper.getRayHandler();
		
	}

	public void render(float delta){

		camera.update();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tiledMapHelper.render();

		batch.begin();

		batch.enableBlending();
		gameInstance.render(batch);

		batch.end();
		
		batch.setProjectionMatrix(camera.combined);
		if (lights_enabled){
			/** BOX2D LIGHT STUFF BEGIN */

			handler.setCombinedMatrix(camera.combined, camera.position.x,
					camera.position.y, camera.viewportWidth * camera.zoom,
					camera.viewportHeight * camera.zoom);
			handler.updateAndRender();
		}



		renderer.render(world, camera.combined);
		handler.updateAndRender();
		logger.log();
		//Gdx.app.log("FPS", logger.toString());

		Vector2 circPos = circleBody.getPosition();
		camera.position.set(circPos.x, circPos.y, 0);
		camera.update();

        stage.act(Gdx.graphics.getDeltaTime()); 
		stage.draw();

	}

	public RayHandler getRayHandler(){
		return handler;
	}

	public OrthographicCamera getCamera(){
		return camera;
	}

	public Viewport getViewport(){
		return viewport;
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	public TiledMapHelper getTiledMapHelper() {
		return tiledMapHelper;
	}
	
	public Stage getStage(){
		return stage;
	}

}
