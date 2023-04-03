package com.mygdx.game.nodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.entities.Player;
import com.mygdx.game.helpers.GroupHandler;
import com.mygdx.game.helpers.ObjectPool;
import java.util.ArrayList;

/**
 * The Node class represents a single node in a tree hierarchy. Each node has a position, a name, a list of child nodes,
 * and a list of groups it belongs to. Nodes are used to represent entities in a game world and their relationships to
 * each other.
 */
public class Node {

    /**
     * The root of the tree hierarchy that this node belongs to.
     */
    public Root myRoot;

    /**
     * The child nodes of this node.
     */
    public ArrayList<Node> children;

    /**
     * The groups that this node belongs to.
     */
    public ArrayList<String> groups;

    /**
     * The position of this node relative to its parent.
     */
    public Vector2 position;

    /**
     * The global position of this node, taking into account the positions of all parent nodes.
     */
    public Vector2 globalPosition;

    /**
     * The position of this node's parent.
     */
    public Vector2 parentPosition;

    /**
     * The name of this node.
     */
    public String name = "";

    /**
     * The parent node of this node.
     */
    Node parent = this;

    /**
     * whether the ready function has been called
     */
    protected boolean ready;

    /**
     * Creates a new node with position (0,0).
     */
    public Node(){

        this(0,0);

    }

    /**
     * Creates a new node with the given position.
     *
     * @param x The x-coordinate of the node's position.
     * @param y The y-coordinate of the node's position.
     */
    public Node(float x, float y){
        position = new Vector2(x,y);
        parentPosition = new Vector2(0,0);
        globalPosition = new Vector2(0,0);
        children = new ArrayList<>();
        groups = new ArrayList<>();
        ready = false;
        updateGlobalPosition();
    }

    /**
     * Initializes this node with the given position and clears its child nodes and group memberships.
     *
     * @param x The x-coordinate of the node's position.
     * @param y The y-coordinate of the node's position.
     * @return This node.
     */
    public Node init(float x, float y){
        position.set(x,y);
        children.clear();
        groups.clear();
        updateGlobalPosition();
        name = "";
        ready = false;
        return this;
    }

    /**
     * Sets the name of this node.
     *
     * @param name The name to set.
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Gets the parent node of this node.
     *
     * @return The parent node.
     */
    public Node getParent(){
        return parent;
    }

    /**
     * Updates the node hierarchy by calling the update() method on this node and its child nodes.
     */
    public void updateCascade(){

        update(Math.min(1/6f, Gdx.graphics.getDeltaTime()));

        updateParentPos();

        for (Node child: children){
            child.updateCascade();
        }

    }

    /**
     * Updates this node with the given delta time. Override this function to give behaviors to your node.
     *
     * @param delta The amount of time that has passed since the last update.
     */
    public void update(double delta){

    }

    /**
     * Called before the first time update is called. Override this function.
     * Called after the root is set.
     */
    public void ready(){

    }

    /**
     * Updates the position of this node's parent and all child nodes.
     */
    public void updateParentPos(){
        updateGlobalPosition();

        for (Node child : children) {
            child.parentPosition.set(globalPosition);

            if (!child.ready){
                child.parent = this;
                child.setMyRoot(myRoot);
                child.updateGlobalPosition();
                child.ready();
                child.ready = true;

            }

        }

    }

    public void setMyRoot(Root newRoot){
        myRoot = newRoot;
    }

    /**
     * Calls the render function for this node and all of its children.
     *
     * @param batch The SpriteBatch object used for rendering.
     */
    public void renderCascade(SpriteBatch batch){
        render(batch);
        for (Node child: children){
            child.renderCascade(batch);
        }
    }

    /**
     * Does nothing. Override this function to add custom rendering behavior.
     *
     * @param batch The SpriteBatch object used for rendering.
     */
    public void render(SpriteBatch batch){

    }

    /**
     * Calls the debug function for this node and all of its children.
     */
    public void debugCascade(){
        debug();
        for (Node child: children){
            child.debugCascade();
        }
    }

    /**
     * Does nothing. Override this function to add custom debug behavior.
     */
    public void debug(){

    }

    /**
     * Adds a child node to this node's list of children.
     *
     * @param child The node to add as a child.
     */
    public void addChild(Node child){
        children.add(child);
        child.parentPosition.set(globalPosition);
        child.parent = this;
        child.setMyRoot(myRoot);
        if (!child.ready) child.ready();
    }



    /**
     * Removes a child node from this node's list of children.
     *
     * @param child The node to remove as a child.
     *
     * @return True if the child node was successfully removed, false otherwise.
     */
    public boolean removeChild(Node child){

        if (children.contains(child)){
            children.remove(child);
            return true;
        }

        return false;

    }

    /**
     * Adds this node to the specified group. Adds the group to it's personal groups list
     *
     * @param group The name of the group to add this node to.
     *
     * @return True if the node was successfully added to the group, false if the node was already in the group.
     */
    public boolean addToGroup(String group){
        if (getGroupHander().isInGroup(group,this)) return false;
        getGroupHander().addToGroup(group,this);
        groups.add(group);
        return true;
    }


    /**
     * Adds this node to the "queue_free" group, which will free the node after this cycle has ended.
     */
    public void queueFree(){
        addToGroup(GroupHandler.QUEUEFREE);
    }

    /**
     * Removes this node from its parent, removes it from all groups, and returns it to the object pool.
     * Also calls the free function on all of its children.
     */
    public void free(){

        for (String group : groups){

           if (! getGroupHander().removeFromGroup(group,this) ) System.out.println("wasn't in group?");
        }

        ObjectPool.remove(this);

        getParent().removeChild(this);

        for (int i = children.size()-1;i>=0;i--){
            children.get(i).free();
            //children.remove(i);
        }
        children.clear();
        groups.clear();


    }

    /**
     * Updates the global position of this node based on its parent's global position and its own position.
     */
    public void updateGlobalPosition() {


        this.globalPosition.set( parentPosition.x + position.x,parentPosition.y+position.y  );

    }

    /**
     * Returns the most recently added child node of this node.
     *
     * @return The most recently added child node.
     */
    public Node getNewestChild(){
        return children.get(children.size() - 1);
    }

    /**
     * Returns the child node with the specified name.
     *
     * @param name The name of the child node to return.
     *
     * @return The child node with the specified name, or null if no child node with that name exists.
     */
    public Node getChild(String name){
        for (Node child : children){
            if (child.name.equals( name)) return child;
        }
        return null;
    }

    /**
     * Returns true if the node is a member of the specified group.
     *
     * @param group the name of the group
     * @return true if the node is in the group, false otherwise
     */
    public boolean isInGroup(String group) {
        return getGroupHander().isInGroup(group,this);
    }

    /**
     * Removes the node from the specified group.
     *
     * @param group the name of the group
     * @return true if the node was removed from the group, false if the node was not a member of the group
     */
    public boolean removeFromGroup(String group){

       boolean returnVal = getGroupHander().removeFromGroup(group,this);

       groups.remove(group);

       return returnVal;
    }

    /**
     * Returns the root node of the node's parent tree.
     * You cannot guarantee this will work until an update cascade begins, so you can't call this in init.
     * Use ready or update instead.
     *
     * @return the root node of the tree
     */
    public Node getRootNode(){
        return myRoot.rootNode;
    }

    /**
     * Returns the group handler associated with the node's parent tree.
     *
     * @return the group handler
     */
    public GroupHandler getGroupHander(){
        return myRoot.groups;
    }
}
