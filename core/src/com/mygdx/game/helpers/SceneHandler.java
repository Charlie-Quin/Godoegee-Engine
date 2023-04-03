package com.mygdx.game.helpers;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.mygdx.game.nodes.Node;
import com.mygdx.game.nodes.Root;
import com.mygdx.game.nodes.TextureEntity;
import com.mygdx.game.scenes.TestScene;

import java.util.HashMap;

import static com.mygdx.game.helpers.Globals.camera;
import static com.mygdx.game.helpers.Globals.sceneJustChanged;

public class SceneHandler {

    public static HashMap<String, Root> scenes;

    static SpriteBatch batch;

    private static String currentSceneName;

    private static Root currentScene;

    private static boolean sceneChangeThisFrame = false;
    public static void ready(){
        scenes = new HashMap<>();

        scenes.put("TestScene", new TestScene());


        batch = new SpriteBatch();

    }


    public static void setCurrentScene(String scene){


        currentScene = scenes.get(scene);

        currentSceneName = scene;

        System.out.println(scene);
        
        sceneChangeThisFrame = true;

    }

    public static String getCurrentScene(){
        return currentSceneName;
    }

    public static void update(){
        
        boolean sceneWasChangedBeforeStart = Globals.sceneJustChanged;

        Root tempScene = currentScene;

        if (!tempScene.isOpen()){
            tempScene.open();
            sceneChangeThisFrame = false;
            sceneJustChanged = true;

        }

        tempScene.update();


        camera.position.set(Globals.cameraOffset,camera.position.z);
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        tempScene.render(batch);


        batch.end();

        Globals.globalShape.begin(ShapeRenderer.ShapeType.Filled);
        if (Globals.showCollision){
            tempScene.debug();
        }
        Globals.globalShape.end();


        ObjectPool.takeOutTrash();


        //ObjectPool.printTotal();



        for (Node n : tempScene.groups.getNodesInGroup(GroupHandler.QUEUEFREE)){
            n.free();
        }

        tempScene.groups.clearGroup(GroupHandler.QUEUEFREE);
        
        if (sceneChangeThisFrame){

            tempScene.close();
            currentScene.open();


            sceneJustChanged = true;
            sceneChangeThisFrame = false;
        }
        else{
            sceneJustChanged = false;
        }


    }





}
