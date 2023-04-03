package com.mygdx.game.helpers;

import com.mygdx.game.nodes.Node;

import java.util.ArrayList;

public class GroupHandler {

    public GroupHandler(){

    }

    public static final String QUEUEFREE = "queueFree";

    public ArrayList<String> names = new ArrayList<>();
    public  ArrayList<ArrayList<Node>> nodes = new ArrayList<ArrayList<Node>>();

    public void addToGroup(String group, Node obj){
        if (!names.contains(group)){
            names.add(group);
            nodes.add(new ArrayList<Node>());
        }

        nodes.get(names.indexOf(group)).add(obj);
    }

    public boolean removeFromGroup(String group, Node obj) {
        int pos = names.indexOf(group);

        if (pos == -1){
            System.out.println("Group does not exist");
            System.exit(0);
        }

        return nodes.get(pos).remove(obj);

    }

    public boolean isInGroup(String group, Node obj){
        int pos = names.indexOf(group);

        if (pos == -1){
            return false;
        }

        return nodes.get(pos).contains(obj);
    }

    public ArrayList<Node> getNodesInGroup(String group){

        if (!names.contains(group)){
            names.add(group);
            nodes.add(new ArrayList<Node>());
        }

        ArrayList<Node> returnArr = (ArrayList<Node>) ObjectPool.getGarbage(ArrayList.class);
        returnArr.clear();

        for (Node n : nodes.get(names.indexOf(group))){
            returnArr.add(n);
        }

        return returnArr;
    }

    public void clearGroup(String group){
        int pos = names.indexOf(group);

        if (pos == -1){
            return;
        }
        nodes.get(names.indexOf(group)).clear();
    }




}
