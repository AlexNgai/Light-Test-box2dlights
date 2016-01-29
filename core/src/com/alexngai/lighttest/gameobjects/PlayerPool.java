package com.alexngai.lighttest.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerPool {
	private GameCharacter[] playerArray;
	private int maxNumObjects = 8;

	public PlayerPool(){

		playerArray = new GameCharacter[maxNumObjects]; 
		for (int i=0; i<maxNumObjects; i++){
			playerArray[i] = new GameCharacter();
		}	
	}

	public void render(SpriteBatch batch){
		for (int i=0; i<maxNumObjects; i++){
			if (playerArray[i].isInUse()){
				playerArray[i].render(batch);
				//Gdx.app.log("Player", "updated" + i);
			}
		}
	}

	public void update(float delta){
		for (int i=0; i<maxNumObjects; i++){
			if (playerArray[i].isInUse()){
				playerArray[i].update(delta);
				//Gdx.app.log("Player", "updated" + i);
			}
		}
	}

	public void addPlayer(GameCharacter player){
		for (int i=0; i<maxNumObjects; i++){
			if (!playerArray[i].isInUse()){
				playerArray[i] = player;
				player.setInUse(true);
				Gdx.app.log("Player", "in use " + i);
				break;
			}
		}
	}

	public GameCharacter[] getplayerArray(){
		return playerArray;
	}

	public int getMaxNumObjects() {
		return maxNumObjects;
	}

	public void setMaxNumObjects(int maxNumObjects) {
		this.maxNumObjects = maxNumObjects;
		playerArray = new GameCharacter[maxNumObjects]; 
		for (int i=0; i<maxNumObjects; i++){
			playerArray[i] = new GameCharacter();
		}	
	}

	public void clearAllObjects(){
		for (int i=0; i<maxNumObjects; i++){
			playerArray[i].setInUse(false);
		}
	}

}
