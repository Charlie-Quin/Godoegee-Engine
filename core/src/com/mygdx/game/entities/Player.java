package com.mygdx.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helpers.*;
import com.mygdx.game.nodes.*;

import java.util.HashMap;

public class Player extends MovementNode {

    private float MAXSPEED = 180;

    private float speed = 5;
    private Vector2 vel;

    private float JUMPFORCE = 200;

    private float GRAVITY = 2000;

    private float ACCEL = 6;

    double rotation = 0;

    private Node bulletHolder;

    //private double


    public Player() {
        this(0f,0f);
    }

    public Player( float x, float y) {

        super(x, y, getMaskLayers(0),getMaskLayers(0));

        vel = new Vector2(0,0);

        init(x,y);

    }

    public Player init(float x, float y){


        super.init(x,y,getMaskLayers(0),getMaskLayers(0));

        addChild(ObjectPool.get(TextureEntity.class).init(TextureHolder.penguinTexture,0f,1f,0,0));
        getNewestChild().setName("sprite");
        addChild(ObjectPool.get(CollisionShape.class).init(8,12,0,0));

        rotation = 0;

        vel.set(0,0);

        return this;
    }

    public void ready(){
        bulletHolder = getRootNode().getChild("bulletHolder");
    }

    private final Vector2 targetSpeed = new Vector2();

    public void update(double delta){

        targetSpeed.set(0.0f,0.0f);

        boolean onFloor = testMove(0,-1);

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) targetSpeed.x -= MAXSPEED;
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) targetSpeed.x += MAXSPEED;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) vel.y -= 100;

        if(Gdx.input.isKeyPressed(Input.Keys.UP) && onFloor) vel.y = JUMPFORCE;

        vel.x  = lerp(vel.x,targetSpeed.x, ACCEL * (float)delta);

        if (vel.x != 0){
            ((TextureEntity) getChild("sprite")).setFlip(vel.x < 0,false);
        }

        if (!onFloor){
            //rotation = rotation % 360;
            rotation += 4 + Math.min(10,rotation * 0.01);

        }
        else{
            rotation = rotation % 360;
            rotation += MathHelpers.differenceBetweenAngles(Math.toRadians(rotation),0) * 30;
            rotation = MathHelpers.moveTowardsZero(rotation,0.1);

        }


        if (((TextureEntity) getChild("sprite")).getFlipX()) {
            ((TextureEntity) getChild("sprite")).setRotation(rotation);
        } else {
            ((TextureEntity) getChild("sprite")).setRotation(-rotation);
        }




        vel.y -= GRAVITY / 5 * delta;

        //System.out.println("vel before: " + vel);

        Vector2 tempVector = moveAndSlide( vel,(float) delta) ;


        vel.set(tempVector);


        if (Gdx.input.isKeyJustPressed(Input.Keys.X)){

            bulletHolder.addChild( ( (Bullet) ObjectPool.get( Bullet.class) ).init(globalPosition.x,globalPosition.y,vel.x*2,vel.y*2) );

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)){
            if (SceneHandler.getCurrentScene().equals("TestScene")){
                SceneHandler.setCurrentScene("TestScene2");
            }
            else{
                SceneHandler.setCurrentScene("TestScene");
            }
        }


        Globals.cameraOffset.set(position);

        //System.out.println(vel + " delta " + delta);

    }

    private float lerp(float a, float b, float f){
        return a + f * (b - a);
    }


    public HashMap<String,Object> save(){
        HashMap<String,Object> returnHash = (HashMap<String, Object>) ObjectPool.get(HashMap.class);

        returnHash.put("position",ObjectPool.get(Vector2.class).set(position));


        return returnHash;
    }




}
