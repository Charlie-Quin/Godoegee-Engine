package com.mygdx.game.helpers;

import com.badlogic.gdx.math.Vector2;

public class Utils {

    public static boolean is_on_screen(Vector2 pos,float toleranceX,float toleranceY){
        return is_on_screen(pos.x,pos.y,toleranceX,toleranceY);
    }

    public static boolean is_on_screen(float x, float y,float toleranceX,float toleranceY){
        if (x > Globals.cameraOffset.x + Globals.camera.viewportWidth/2 + toleranceX) return false;
        if (x < (Globals.cameraOffset.x - (Globals.camera.viewportWidth/2)) - toleranceX) return false;

        if (y > Globals.cameraOffset.y + Globals.camera.viewportHeight/2 + toleranceY) return false;
        if (y < (Globals.cameraOffset.y - (Globals.camera.viewportHeight/2)) - toleranceY) return false;

        return true;
    }


}
