package com.mygdx.game.nodes;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.helpers.Globals;
import com.mygdx.game.helpers.ObjectPool;

import java.util.ArrayList;

public class MovementNode extends ColliderObject {


    public MovementNode(){

        this(0,0, getMaskLayers(0),getMaskLayers(0));

    }

    public MovementNode(float x, float y, ArrayList<Integer> mask, ArrayList<Integer> layers){

        super(x,y,mask,layers);

    }

    private Vector2 tempPos = new Vector2(0,0);

    public Vector2 moveAndSlide(float x, float y, float delta){
        return moveAndSlide(((Vector2) ObjectPool.getGarbage(Vector2.class)).set(x,y),delta);
    }

    public Vector2 moveAndSlide(Vector2 distance, double delta){
        return moveAndSlide(distance,(float)delta);
    }

    public Vector2 moveAndSlide(Vector2 distance,float delta){

        Vector2 prevPos =  ((Vector2) ObjectPool.getGarbage(Vector2.class)).set(position);

        Vector2 scaledDistance =  ((Vector2) ObjectPool.getGarbage(Vector2.class)).set(distance).scl(delta);

       // Vector2 tempPos = position.cpy();

        position.set(getFirstCollision(scaledDistance));

        Vector2 difference =  ((Vector2) ObjectPool.getGarbage(Vector2.class));

        difference.set(position.x - prevPos.x, position.y - prevPos.y);




        if ( !difference.epsilonEquals(scaledDistance,0.1f) ){
            Node tempCollider = lastCollider;

            updateParentPos();

            position.set(getFirstCollision( ((Vector2) ObjectPool.getGarbage(Vector2.class)).set(scaledDistance.x - difference.x, 0)));
            updateParentPos();

            position.set(getFirstCollision( ((Vector2) ObjectPool.getGarbage(Vector2.class)).set(0, scaledDistance.y - difference.y)));

            lastCollided = true;
            lastCollider = tempCollider;
        }

        difference.set(position.x - prevPos.x, position.y - prevPos.y);

        Vector2 output = difference.scl(Globals.inverse(delta));

        //if (output.len() > 100) System.out.println("Big difference! " + difference + " temp pos: " + tempPos + " pos: " + position);

        return output;
    }


    public Vector2 moveAndSlideBruteForce(Vector2 distance){

        if ((int)distance.x == 0 && (int)distance.y == 0) return distance.cpy();

        Vector2 returnVal = new Vector2(0,0);




        if (!testMove(distance)) {
            returnVal.set(distance.x,distance.y);
            position.add(returnVal);
            return returnVal;
        }



        if ( !isZeroApprox(distance.x) && !testMove((int) Math.signum(distance.x),0)) {

            int high = (int) distance.x;
            int low = 0;
            int n = (int)((high + low)/2);

            returnVal.x = distance.x;

            if (!testMove(distance.x,0)) high = low;

            while (Math.abs(low) + 1 < Math.abs(high)){

                if (testMove(n,0)){

                    high = n;
                    returnVal.x = moveTowardsZero(n,1);

                }
                else{

                    low = n;
                    returnVal.x = n;
                }

                n = (int)((high + low)/2);


            }
        }
        else{
            returnVal.x = 0;
        }

        position.add(returnVal);

        if ( !isZeroApprox(distance.y) && !testMove(0,(int) Math.signum(distance.y))) {

            int high = (int) distance.y;
            int low = 0;
            int n = (int)((high + low)/2);

            returnVal.y = distance.y;

            if (!testMove(0, distance.y)) high = low;

            while (Math.abs(low) + 1 < Math.abs(high)){

                if (testMove(0,n)){

                    high = n;
                    returnVal.y = moveTowardsZero(n,1);

                }
                else{

                    low = n;
                    returnVal.y = n;
                }

                n = (int)((high + low)/2);


            }
        }
        else{
            returnVal.y = 0;
        }



        position.add(0,returnVal.y);

        return returnVal;

    }

    private int moveTowardsZero(int num, int amount){
        if (num > 0) return Math.max(num-amount,0);
        if (num < 0) return Math.min(num+amount,0);
        return 0;
    }

    public boolean testMove(Vector2 distance){
        return testMove(distance.x, distance.y);
    }


    public boolean testMove(float x, float y){
        updateGlobalPosition();
        updateParentPos();

        return sweepTestArray(((Vector2) ObjectPool.getGarbage(Vector2.class)).set(x,y),myRoot.getCollidersInLayers(mask)).collides;
    }

    public Vector2 move(Vector2 distance){

        Vector2 returnVal = Vector2.Zero;

        if (!testMove(distance)) returnVal.set(distance);

        else if (!testMove(distance.x,0)) returnVal.set(distance.x,0);

        else if (!testMove(0,distance.y)) returnVal.set(0,distance.y);

        position.add(returnVal);

        return returnVal;

    }

    public boolean isZeroApprox(float num){

        return (num < 0.01 && num > -0.01);

    }




}
