package com.alexngai.lighttest;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.alexngai.lighttest.gameobjects.FlagObject;
import com.alexngai.lighttest.gameobjects.GameCharacter;
import com.alexngai.lighttest.gameobjects.ObjectTable;
import com.alexngai.lighttest.gameobjects.PlayerPool;
import com.alexngai.lighttest.helpers.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.utils.Box2DBuild;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.ContactImpulse;

//contains all box2d physics objects
public class GameWorld {

	private static final int maxPlayers = 8;
	private static final int maxTeamAPlayers = 4;
	private static final int maxTeamBPlayers = 4;
	private static final int teamA = 1;
	private static final int teamB = 2;
	
	private final int winScore = 3;
	private int teamAScore = 0;
	private int teamBScore = 0;

	private int teamANumPlayers = 0;
	private int teamBNumPlayers = 0;

	private FlagObject flagA;
	private FlagObject flagB;
	
	private World world;
	private GameCharacter playerCharacter;
	private Body circleBody;
	private PlayerPool playerPool;

	private RayHandler handler;
	

	public GameWorld(){
		world = new World(new Vector2(0f, 0f), false);
		playerPool = new PlayerPool();
		createCollisionListener();
		
		addPrimePlayer(50, 30);		
	}
	
	public void postCreate(){
		//create player light
		Color color2 = new Color(0f, 255f, 105f, .7f);
		playerCharacter.setLight(new PointLight(handler, 1000, color2, 50, circleBody.getPosition().x, circleBody.getPosition().y));
		addFlag(new Vector2(50,50), teamA);
		addFlag(new Vector2(100,100), teamB);
	}

	public void update(float delta){
		world.step(delta, 4, 2); 
		playerPool.update(delta);
		checkFlagCollision();
		flagA.update(); 
		flagB.update();
	}
	
	public void render(SpriteBatch batch){
		flagA.render(batch); 
		flagB.render(batch);
		playerPool.render(batch);
		
	}

	public GameCharacter getPlayerCharacter(){
		return playerCharacter;
	}

	public World getWorld(){
		return world;
	}

	public void dispose(){
		world.dispose();
	}

	public void addPrimePlayer(float x, float y){
		if ((teamANumPlayers + teamBNumPlayers) < maxPlayers){
			playerCharacter = new GameCharacter(world, new Vector2(x,y), teamA);
			playerCharacter.setTexture(Assets.blue_ball);
			circleBody = playerCharacter.getBody();
			
			playerPool.addPlayer(playerCharacter);
			teamANumPlayers++;
		}
	}

	public void addNewPlayer(boolean team, float x, float y){
		if ((teamANumPlayers + teamBNumPlayers) < maxPlayers){
			if (team && teamANumPlayers < maxTeamAPlayers){
				GameCharacter player = new GameCharacter(world, new Vector2(x,y), teamA);
				player.setTexture(Assets.blue_ball);
				teamANumPlayers++;
				Color color2 = new Color(0f, 255f, 105f, .7f);
				player.setLight(new PointLight(handler, 250, color2, 30, x, y));
				playerPool.addPlayer(player);
			}
			else if (!team && teamBNumPlayers < maxTeamBPlayers){
				GameCharacter player = new GameCharacter(world, new Vector2(x,y), teamB);
				player.setTexture(Assets.orange_ball);
				teamBNumPlayers++;
				Color color2 = new Color(255f, 130f, 0f, .7f);
				player.setLight(new PointLight(handler, 250, color2, 30, x, y));
				playerPool.addPlayer(player);
			}
		}
	}
	
	public RayHandler getHandler() {
		return handler;
	}

	public void setHandler(RayHandler handler) {
		this.handler = handler;
	}

	public void addFlag(Vector2 position, int teamType){
		if (teamType == teamA){
			Color color2 = new Color(0f, 255f, 105f, .8f);
			Light light = new PointLight(handler, 250, color2, 75, position.x, position.y);
			flagA = new FlagObject(position, Assets.blue_flag, light);
		}
		else if (teamType == teamB){
			Color color2 = new Color(255f, 130f, 0f, .8f);
			Light light = new PointLight(handler, 250, color2, 75, position.x, position.y);
			flagB = new FlagObject(position, Assets.orange_flag, light);
		}
	}
	
	private boolean checkFlagCollision(){
		
		for (GameCharacter p : playerPool.getplayerArray()){
			if (p.isInUse()){
				if (p.getTeam() == teamA && p.getPosition().dst(flagB.getPosition())< (FlagObject.flagRadius+GameCharacter.ballRadius)){
					flagB.setHoldingPlayer(p); 
					p.setHasFlag(true);
					p.setFlag(flagB);
					p.getLight().setActive(false);
					return true;
				}
				else if (p.getTeam() == teamB && p.getPosition().dst(flagA.getPosition())<(FlagObject.flagRadius+GameCharacter.ballRadius)){
					flagA.setHoldingPlayer(p); 
					p.setHasFlag(true);
					p.setFlag(flagA);
					p.getLight().setActive(false);
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean checkPlayerCollision(){
		for (GameCharacter p : playerPool.getplayerArray()){
			if (p.isInUse()){
				if (p.getTeam() == teamA && p.getPosition().dst(flagB.getPosition())< (FlagObject.flagRadius+GameCharacter.ballRadius)){
					flagB.setHoldingPlayer(p); 
					p.setHasFlag(true);
					p.getLight().setActive(false);
					return true;
				}
				else if (p.getTeam() == teamB && p.getPosition().dst(flagA.getPosition())<(FlagObject.flagRadius+GameCharacter.ballRadius)){
					flagA.setHoldingPlayer(p); 
					p.setHasFlag(true);
					p.getLight().setActive(false);
					return true;
				}
			}
		}
		return false;
	}
	
	private void createCollisionListener() {
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
                //Gdx.app.log("Contact", ""+ fixtureA.hashCode() + ":" + fixtureB.hashCode());
                //Gdx.app.log("beginContact", "between " + fixtureA.toString() + " and " + fixtureB.toString());
                //Gdx.app.log("Contact", "between:" + ObjectTable.getType(fixtureA.hashCode()) + ", " + 
                //		ObjectTable.getType(fixtureB.hashCode()) );
                handleContact(fixtureA.hashCode(), fixtureB.hashCode());
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

        });
    }
	
	private void handleContact(int hashcodeA, int hashcodeB){
		int typeA = ObjectTable.getType(hashcodeA);
		int typeB = ObjectTable.getType(hashcodeB);
		
		if ( typeA==ObjectTable.playerTypeA && typeB==ObjectTable.playerTypeB ){
			GameCharacter A = (GameCharacter) ObjectTable.getObject(hashcodeA);
			GameCharacter B = (GameCharacter) ObjectTable.getObject(hashcodeB);
			collided(A, B);
		}
		else if ( typeA==ObjectTable.playerTypeB && typeB==ObjectTable.playerTypeA ){
			GameCharacter A = (GameCharacter) ObjectTable.getObject(hashcodeB);
			GameCharacter B = (GameCharacter) ObjectTable.getObject(hashcodeA);
			collided(A, B);
		}
	}
	
	private void collided(GameCharacter A, GameCharacter B){
		if (A.hasFlag()) A.releaseFlag();
		if (B.hasFlag()) B.releaseFlag();
	}
	
}
