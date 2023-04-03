package com.mygdx.game.scenes;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.helpers.GroupHandler;
import com.mygdx.game.helpers.ObjectPool;
import com.mygdx.game.nodes.*;

public class TestScene extends Root {



    public void open(){

        rootNode = poolGet(Node.class);
        rootNode.setMyRoot(this);


    }


}
