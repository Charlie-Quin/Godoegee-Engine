package com.mygdx.game.nodes.collisionShapeHelpers;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.nodes.Node;

public class SweepInfo {

        public Node collider;
        public float time;

        public float length; //total length it *tried* to move
        public Vector2 firstImpact;

        public Vector2 offset;

        public boolean collides;

        public SweepInfo() {
            this.time = 1;
        }

        public SweepInfo init(){
                time = 0;
                length = 0;
                collides = false;
                return this;
        }

    
}
