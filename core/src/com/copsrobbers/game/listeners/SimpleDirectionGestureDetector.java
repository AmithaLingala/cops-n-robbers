package com.copsrobbers.game.listeners;

import com.badlogic.gdx.input.GestureDetector;
import com.copsrobbers.game.screens.GameScreen;

/**
 * Class for Gesture detection
 */
public class SimpleDirectionGestureDetector extends GestureDetector {
    public interface DirectionListener {
        void onDirChange(GameScreen.MOVES move);

    }

    public SimpleDirectionGestureDetector(DirectionListener directionListener) {
        super(new DirectionGestureListener(directionListener));
    }

    private static class DirectionGestureListener extends GestureAdapter {
        DirectionListener directionListener;

        public DirectionGestureListener(DirectionListener directionListener) {
            this.directionListener = directionListener;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            if(Math.abs(velocityX)>Math.abs(velocityY)){
                if(velocityX>0){
                    directionListener.onDirChange(GameScreen.MOVES.RIGHT);
                }else{
                    directionListener.onDirChange(GameScreen.MOVES.LEFT);
                }
            }else{
                if(velocityY>0){
                    directionListener.onDirChange(GameScreen.MOVES.DOWN);
                }else{
                    directionListener.onDirChange(GameScreen.MOVES.UP);
                }
            }
            return true;
        }

    }

}
