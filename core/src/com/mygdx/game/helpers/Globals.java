package com.mygdx.game.helpers;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Globals {

    public static int gamespeed = 1;
    public static OrthographicCamera camera;

    public static boolean showCollision = false;

    public static boolean sceneJustChanged = true;

    public static ShapeRenderer globalShape= new ShapeRenderer();

    public static Vector2 cameraOffset = Vector2.Zero;

    public Globals(){

    }

    public static float inverse(float num){
        if (num == 0) return 0;

        return 1f/num;


    }




}
