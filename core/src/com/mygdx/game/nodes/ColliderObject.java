package com.mygdx.game.nodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.helpers.Globals;
import com.mygdx.game.helpers.ObjectPool;
import com.mygdx.game.nodes.collisionShapeHelpers.SweepInfo;

import java.util.ArrayList;

import static com.mygdx.game.helpers.MathHelpers.isEqualApprox;

public class ColliderObject extends Node {

    public Node lastCollider;
    public boolean lastCollided;
    Array<CollisionShape> shapes;
    protected ArrayList<Integer> mask;
    protected ArrayList<Integer> layers;

    public boolean isColliding(){
        return overlaps(myRoot.colliders);
    }

    public Vector2 getFirstCollision(Vector2 distance){

        SweepInfo currentInfo = sweepTestArray(distance,myRoot.getCollidersInLayers(mask));


        Vector2 output =  ((Vector2) ObjectPool.getGarbage(Vector2.class) ).set(position.x + distance.x,position.y + distance.y);

        if (currentInfo != null){
            output.set(position.x + currentInfo.offset.x,position.y + currentInfo.offset.y);
            lastCollided = currentInfo.collides;
            if (lastCollided){
                lastCollider = currentInfo.collider;
            }
        }

        //if (currentInfo.offset.len() > 100 * (1/60)) System.out.println("len " + currentInfo.offset.len() + " " + currentInfo.firstImpact);


//        if (isEqualApprox(currentInfo.offset.y,1673/60,0.5) ){
//            System.out.println("hey, 1673 after colliding");
//        }

        return output;

    }

    public SweepInfo sweepTest(Vector2 distance, ColliderObject other){

        if (other == this) return null;

        SweepInfo returnSweepInfo = null;
        SweepInfo tempSweepInfo;
        for (CollisionShape myShape : shapes){
            tempSweepInfo = myShape.sweepTestArray(distance,other.getShapes());
            if (returnSweepInfo == null || tempSweepInfo.time < returnSweepInfo.time )
                returnSweepInfo = tempSweepInfo;

        }
        return returnSweepInfo;  
    }

    public SweepInfo sweepTestArray(Vector2 distance, Array<ColliderObject> others){
        SweepInfo returnSweepInfo = null;
        SweepInfo tempSweepInfo;
        for (ColliderObject other : others){
            tempSweepInfo = sweepTest(distance,other);

            if (tempSweepInfo != null && (returnSweepInfo == null || tempSweepInfo.time < returnSweepInfo.time) )
                returnSweepInfo = tempSweepInfo;

        }

        //System.out.println("colliderobjecct array " + returnSweepInfo.firstImpact);

        return returnSweepInfo;
    }

    public boolean overlaps(ColliderObject other){

        if (other == this){
            //System.out.println("collider equals this!");
            return false;
        }

        for (CollisionShape shape : shapes){
            if (shape instanceof CollisionShape) {
                if (shape.overlaps(other.getShapes())) return true;
            }
        }
        return false;
    }

    public boolean overlaps(Array<ColliderObject> others){
        for (ColliderObject other : others){
            if (overlaps(other)) return true;
        }

        return false;
    }

    public Array<CollisionShape> getShapes(){
        return shapes;
    }

    public ColliderObject init(float x, float y, ArrayList<Integer> mask, ArrayList<Integer> layers){
        super.init(x,y);

        shapes.clear();
        lastCollider = null;
        lastCollided = false;


        setMaskLayers(mask,layers);

        return this;
    }


    public ColliderObject(){
        this(0,0, getMaskLayers(0) , getMaskLayers(0));

    }

    public static ArrayList<Integer> getMaskLayers(int...newArr){

        ArrayList<Integer> arr = (ArrayList<Integer>) ObjectPool.getGarbage(ArrayList.class);

        arr.clear();

        for (int i = 0; i < newArr.length; i++){
            arr.add(newArr[i]);
        }

        return arr;
    }

    public void setMaskLayers(ArrayList<Integer> mask, ArrayList<Integer> layers){

        this.mask.clear();
        for (int i = 0; i < mask.size(); i++){
            this.mask.add(i,mask.get(i));
        }

        this.layers.clear();
        for (int i = 0; i < layers.size(); i++){
            this.layers.add(i,layers.get(i));
        }
    }

    public ColliderObject(float x, float y, ArrayList<Integer> mask, ArrayList<Integer> layers ){

        super(x,y);

        lastCollider = null;
        lastCollided = false;

        if (myRoot != null){
            this.myRoot.colliders.add(this);
            System.out.println("added self to colliders");
        }

        shapes = new Array<>();

        this.mask = new ArrayList<>();
        this.mask.clear();
        for (int i = 0; i < mask.size(); i++){
            this.mask.add(i,mask.get(i));
        }

        this.layers = new ArrayList<>();
        this.layers.clear();
        for (int i = 0; i < layers.size(); i++){
            this.layers.add(i,layers.get(i));
        }

    }

    public void setMyRoot(Root newRoot){
        boolean rootChanged = newRoot != myRoot;

        if (rootChanged && myRoot != null){
            myRoot.colliders.removeValue(this,true);
        }

        this.myRoot = newRoot;

        if (myRoot != null && rootChanged){
            this.myRoot.colliders.add(this);
        }

    }

    public void addChild(Node child){
        super.addChild(child);

        if (child instanceof CollisionShape){
            shapes.add( (CollisionShape) child);
        }

    }

    public boolean removeChild(Node child){

        if (children.contains(child)){

            children.remove(children.indexOf(child));

        }

        if (child instanceof CollisionShape){
            if (shapes.contains((CollisionShape) child,true)){
                shapes.removeIndex(shapes.indexOf( (CollisionShape) child,true));
                return true;
            }
        }

        return false;

    }

    public void free(){
        super.free();

        //int num = myRoot.colliders.size;

        myRoot.colliders.removeValue(this,true);  // System.out.println("removed a collider");

    }

}
