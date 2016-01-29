package com.alexngai.lighttest.gameobjects;

import box2dLight.Light;

import com.alexngai.lighttest.helpers.Assets;
import com.alexngai.lighttest.helpers.LightHelper;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class GameCharacter {
	public static float ballRadius = 3.2f;
	private static final int teamA = 1;
	private static final int teamB = 2;
	
	private World world;
	private Body circleBody;
	private TextureRegion texture;
	
	private Vector2 position;
	private Vector2 velocity;
	private Vector2 acceleration;
	private float radius;
	private int team;
	private boolean hasFlag;
	private FlagObject flag;
	
	private float linearDamping = 0.8f;
	private float angularDamping = 0.4f;
	private float density = 0.1f;
	private float friction = 0.2f;
	private float restitution = 0.8f; //bounciness upon collisions
	
	private Light light; 
	
	private boolean inUse;
	
	public GameCharacter(){
		inUse = false;
	}
	
	public GameCharacter(World w, Vector2 position, int team){
		world = w;
		this.position = position;
		radius = ballRadius;
		
		BodyDef circleDef = new BodyDef();
		circleDef.type = BodyType.DynamicBody;
		circleDef.position.set(position.x, position.y);
		circleDef.linearDamping = (float) linearDamping;
		circleDef.angularDamping = (float) angularDamping;
		circleBody = world.createBody(circleDef);

		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(radius);

		FixtureDef circleFixture = new FixtureDef();
		circleFixture.shape = circleShape;
		circleFixture.density = density;
		circleFixture.friction = friction;
		circleFixture.restitution = restitution;
	
		circleBody.createFixture(circleFixture);
		
		if (team == teamA) ObjectTable.addObject(circleBody.getFixtureList().first().hashCode(), ObjectTable.playerTypeA, this);
		else ObjectTable.addObject(circleBody.getFixtureList().first().hashCode(), ObjectTable.playerTypeB, this);
		
		
		this.team = team;
		
		inUse = false;
	}
	
    public void update(float delta) {
    	position = circleBody.getPosition();
    	if (light != null) light.setPosition(position);
    }

    public void render(SpriteBatch batch){
		Vector2 position = circleBody.getPosition();
		float angle = MathUtils.radiansToDegrees * circleBody.getAngle();
    	batch.draw(texture, position.x - ballRadius, position.y - ballRadius,
				ballRadius, ballRadius, ballRadius * 2, ballRadius * 2, 1, 1, angle);
    	if (hasFlag){
    		float flagRadius = FlagObject.flagRadius;
    		if (team == teamA) batch.draw(Assets.orange_flag, position.x - flagRadius, position.y - flagRadius,
        			flagRadius, flagRadius, flagRadius * 2, flagRadius * 2, 1, 1, angle);
    		else batch.draw(Assets.blue_flag, position.x - flagRadius, position.y - flagRadius,
        			flagRadius, flagRadius, flagRadius * 2, flagRadius * 2, 1, 1, angle);
    	}
    }
    
    public Body getBody(){
    	return circleBody;
    }
    
    public Vector2 getPosition(){
    	return circleBody.getPosition();
    }
    
    public Vector2 getVelocity(){
    	return circleBody.getLinearVelocity();
    }
    
    public Vector2 getAcceleration(){
    	return acceleration;
    }
    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    public float getRadius() {
        return radius;
    }

	public TextureRegion getTexture() {
		return texture;
	}

	public void setTexture(TextureRegion texture) {
		this.texture = texture;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getLinearDamping() {
		return linearDamping;
	}

	public float getAngularDamping() {
		return angularDamping;
	}

	public float getDensity() {
		return density;
	}

	public float getFriction() {
		return friction;
	}

	public float getRestitution() {
		return restitution;
	}

	public void setLinearDamping(float linearDamping) {
		this.linearDamping = linearDamping;
		circleBody.setLinearDamping(linearDamping);
	}

	public void setAngularDamping(float angularDamping) {
		this.angularDamping = angularDamping;
		circleBody.setAngularDamping(angularDamping);
	}
	
	public void setInUse(boolean b){
		inUse = b;
	}
	
	public boolean isInUse(){
		return inUse;
	}

	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}
	
	public int getTeam(){
		return team;
	}

	public boolean hasFlag() {
		return hasFlag;
	}

	public void setHasFlag(boolean hasFlag) {
		this.hasFlag = hasFlag;
	}
	
	public FlagObject getFlag() {
		return flag;
	}

	public void setFlag(FlagObject flag) {
		this.flag = flag;
	}
	
	public void releaseFlag(){
		hasFlag = false;
		flag.returnHome();
		flag = null;
		light.setActive(true);
		}

	@Override
	public String toString(){
		return "game character";
	}
	

}
