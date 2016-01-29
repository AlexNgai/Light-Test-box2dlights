package com.alexngai.lighttest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
    public static TextureRegion blue_ball, orange_ball, gray_ball;
    public static TextureRegion blue_flag, orange_flag;
    
    //loads all game assets
    public static void load() {

    	blue_ball = new TextureRegion(new Texture(Gdx.files.internal("data/ball_blue.png")));
    	orange_ball = new TextureRegion( new Texture(Gdx.files.internal("data/ball_orange.png")));
    	gray_ball = new TextureRegion(new Texture(Gdx.files.internal("data/ball_standard.png")));
    	
       	orange_flag = new TextureRegion(new Texture(Gdx.files.internal("data/flag_orange.png")));
    	blue_flag = new TextureRegion(new Texture(Gdx.files.internal("data/flag_blue.png")));


    }

    public static void dispose() {

    }
}
