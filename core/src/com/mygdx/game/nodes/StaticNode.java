package com.mygdx.game.nodes;

import java.util.ArrayList;

public class StaticNode extends ColliderObject {



    public StaticNode(){
        this(0,0, getMaskLayers(0), getMaskLayers(0));
    }

    public StaticNode(float x, float y, ArrayList<Integer> mask, ArrayList<Integer> layers){
        super(x,y,mask,layers);
    }


}
