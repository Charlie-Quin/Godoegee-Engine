package com.mygdx.game.nodes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.helpers.GroupHandler;
import com.mygdx.game.helpers.ObjectPool;

import java.util.ArrayList;

/**
 * The Root class represents the root of a scene graph hierarchy.
 * It holds references to the colliders, the root node of the scene graph and a group handler.
 */
public class Root {

    /** The colliders in the scene. */
    public Array<ColliderObject> colliders = new Array<ColliderObject>();

    /** The root node of the scene graph. */
    public Node rootNode;

    /** The group handler. */
    public GroupHandler groups = new GroupHandler();

    /**
     * Creates a new Root object.
     */
    public Root(){

    }

    /**
     * Returns an array of colliders that belong to the specified layers.
     *
     * @param mask The list of layer IDs to filter colliders by.
     * @return An array of ColliderObjects that belong to the specified layers.
     */
    public Array<ColliderObject> getCollidersInLayers(ArrayList<Integer> mask){
        Array<ColliderObject> returnArray = new Array<>();
        for (ColliderObject obj : colliders){
            for (int n : obj.layers){
                boolean added = false;
                for (int o : mask){
                    if (n == o){
                        returnArray.add(obj);
                        added = true;
                        break;
                    }
                }
                if (added) break;
            }
        }
        return returnArray;
    }

    /**
     * Calls the updateCascade method of the root node, which updates the entire scene tree.
     */
    public void update(){


        rootNode.updateCascade();


    }

    /**
     * Calls the renderCascade method of the root node, which renders the entire scene tree.
     *
     * @param batch The SpriteBatch to render the scene with.
     */
    public void render(SpriteBatch batch){
        rootNode.renderCascade(batch);
    }

    /**
     * Calls the debugCascade method of the root node, which draws debug information for the entire scene tree.
     */
    public void debug(){rootNode.debugCascade();}


    /**
     * shorthand for rootNode.addChild()
     * @param child
     */
    public void add(Object child){
        rootNode.addChild( (Node) child );
    }

    /**
     * shorthand for ObjectPool.get()
     * @param type
     * @return
     * @param <T>
     */
    public <T> T poolGet(Class<T> type){
        return ObjectPool.get(type);
    }

    public Node last(){
        return rootNode.getNewestChild();
    }


    /**
     * shuts down the scene
     */
    public void close(){

        rootNode.free();
        rootNode = null;

    }

    public boolean isOpen(){
        return rootNode != null;
    }

    /**
     * overide to open the scene
     */
    public void open(){

    }

}