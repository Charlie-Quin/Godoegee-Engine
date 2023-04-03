package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.helpers.Globals;
import com.mygdx.game.helpers.GroupHandler;
import com.mygdx.game.helpers.ObjectPool;
import com.mygdx.game.helpers.SceneHandler;
import com.mygdx.game.nodes.*;
import com.mygdx.game.scenes.TestScene;

public class MyGdxGame extends ApplicationAdapter {

	private OrthographicCamera camera;


	Root scene1;// = new testScene();
	
	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1024, 600);

		Globals.camera = camera;

		Globals.globalShape.setColor(new Color(0, 0, 1, 0.5f));

		SceneHandler.ready();

		SceneHandler.setCurrentScene("TestScene");

	}

	@Override
	public void render () {

//		System.out.println("before");
//		ObjectPool.printTotal();

		ScreenUtils.clear(0.921f, 0.55f, 0.96f, 1);

		SceneHandler.update();

	}
	
	@Override
	public void dispose () {


	}
}
