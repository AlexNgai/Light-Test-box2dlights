package com.alexngai.lighttest.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class UIHelper {
	
	private Stage stage;
	private Skin uiSkin;
	private Skin touchpadSkin;
	private Touchpad touchpad;
	
	private boolean placeTeam = false;
	private boolean placeEnemy = false;
	
	public UIHelper(){
		stage = new Stage();
		uiSkin = new Skin(Gdx.files.internal("data/uiskin.json"));
	
		loadTouchpad();
		
		final TextButton button = new TextButton("place team", uiSkin, "default");

		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();
		
		button.setWidth(100);
		button.setHeight(50);
		button.setPosition(width - 120, 20);

		button.addListener(new ClickListener(){
			@Override 
			public void clicked(InputEvent event, float x, float y){
				placeTeam = !placeTeam;
				if (placeTeam) button.setText("ACTIVE");
				else button.setText("place team");
			}
        });
		
		final TextButton button2 = new TextButton("place enemy", uiSkin, "default");
		
		button2.setWidth(100);
		button2.setHeight(50);
		button2.setPosition(width - 240, 20);

		button2.addListener(new ClickListener(){
			@Override 
			public void clicked(InputEvent event, float x, float y){
				placeEnemy = !placeEnemy;
				if (placeEnemy) button2.setText("ACTIVE");
				else button2.setText("place enemy");
			}
        });
		
        stage.addActor(button);
        stage.addActor(button2);
	}

	public Stage getStage(){
		return stage;
	}
	
	public void loadTouchpad(){
		touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("data/joystick_background.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("data/joystick_ball.png"));
        
        TouchpadStyle touchpadStyle = new TouchpadStyle();
        
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");

        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(5, touchpadStyle);
        //setBounds(x,y,width,height)
        
        
        float width = Gdx.graphics.getWidth();
        touchpad.setPosition((float).15*width, (float)(.15*width));
        touchpad.setBounds(15, 15, (float).2*width, (float).2*width);
        stage.addActor(touchpad);
	}
	
	public Touchpad getTouchpad(){
		return touchpad;
	}
	
	public boolean getPlaceTeam(){
		return placeTeam;
	}
	
	public boolean getPlaceEnemy(){
		return placeEnemy;
	}
}
