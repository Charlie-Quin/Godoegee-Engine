package com.mygdx.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.helpers.LayerNames;
import com.mygdx.game.helpers.ObjectPool;
import com.mygdx.game.helpers.TextureHolder;
import com.mygdx.game.helpers.Utils;
import com.mygdx.game.nodes.*;

public class SimpleIcePlatform extends StaticNode {

    public SimpleIcePlatform(){
        this(0,0);
    }
    public SimpleIcePlatform( float x, float y) {

        super( x, y, getMaskLayers(LayerNames.DEFAULT), getMaskLayers(LayerNames.DEFAULT));

    }

    public SimpleIcePlatform init(float x, float y){

        super.init(x,y,getMaskLayers(LayerNames.DEFAULT),getMaskLayers(LayerNames.DEFAULT));

        Texture iceTexture = TextureHolder.iceTexture;


        addChild( ObjectPool.get(TextureEntity.class).init(iceTexture,0,0,0,0));
        getNewestChild().setName("sprite");

        ( (TextureEntity) getNewestChild()).setFlip(false,false);

        addChild( ObjectPool.get(CollisionShape.class).init(96/2,16,0,0));



        return this;
    }

    public void ready(){
        addToGroup("icePlatform");
    }

    @Override
    public void update(double delta) {
       super.update(delta);

    }


}
