package com.mygdx.game.nodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.helpers.Globals;
import com.mygdx.game.helpers.GroupHandler;
import com.mygdx.game.nodes.collisionShapeHelpers.AABB;
import com.mygdx.game.nodes.collisionShapeHelpers.SweepInfo;

import static com.mygdx.game.helpers.MathHelpers.isEqualApprox;

public class CollisionShape extends Node {

    AABB boundingBox;

    public CollisionShape(){
        this(0,0);
    }

    public CollisionShape(int sizeX, int sizeY){
        this(sizeX,sizeY,0,0);
    }

    public CollisionShape(int sizeX, int sizeY, int posX, int posY){

        super(posX,posY);

        boundingBox = new AABB();


    }

    public CollisionShape init(int sizeX, int sizeY, int posX, int posY){
        super.init(posX,posY);


        boundingBox.pos.set(globalPosition);
        boundingBox.half.set(sizeX,sizeY);
        return this;
    }


    public boolean overlaps(CollisionShape other){
        updateGlobalPosition();
        boundingBox.pos.set(globalPosition);

        return (boundingBox.intersectAABB(other.getAABB()));
    }

    public boolean overlaps(Array<CollisionShape> shapes){
        for (CollisionShape shape : shapes){
            if (overlaps(shape)) return true;
        }
        return false;
    }

    public SweepInfo sweepTest(Vector2 distance, CollisionShape other){

        updateGlobalPosition();

        boundingBox.pos.set(globalPosition);

        SweepInfo returnInfo =  boundingBox.sweepAABB(other.getAABB(),distance);

        returnInfo.collider = other.getParent();


        return returnInfo;

    }

    public SweepInfo sweepTestArray(Vector2 distance, Array<CollisionShape> others){


        SweepInfo returnSweepInfo = null;
        SweepInfo tempSweepInfo;
        for (CollisionShape other : others){
            tempSweepInfo = sweepTest(distance,other);
            if (returnSweepInfo == null || tempSweepInfo.time < returnSweepInfo.time ){
                //System.out.println(tempSweepInfo.time);
                returnSweepInfo = tempSweepInfo;
                //System.out.println("hey");
            }


        }

        return returnSweepInfo;
    }




    public AABB getAABB(){
        updateGlobalPosition();
        boundingBox.pos.set(globalPosition);
        return boundingBox;
    }


    public void debug(){



        if (Globals.showCollision){


            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
             //I'm using the Filled ShapeType, but remember you have three of them

            Globals.globalShape.rect( (globalPosition.x - boundingBox.half.x)- Globals.cameraOffset.x + 512,  (globalPosition.y - boundingBox.half.y)- Globals.cameraOffset.y + 300,boundingBox.half.x * 2,boundingBox.half.y * 2); //assuming you have created those x, y, width and height variables




            //Gdx.gl.glDisable(GL20.GL_BLEND); //this breaks transparency apparently?
        }


    }

}
