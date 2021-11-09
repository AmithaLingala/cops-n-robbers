package com.copsrobbers.game.characters;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.MapManager;
import com.copsrobbers.game.screens.GameScreen;

public class Character {
    float x;
    float y;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    float width;
    float height;
    MapManager mapManager;
    public Character(Rectangle bounds)
    {
        this.x = bounds.x;
        this.y = bounds.y;
        this.width = bounds.width;
        this.height = bounds.height;
         mapManager = MapManager.obtain();
    }
    public void update(GameScreen.MOVES move){

        int i = (int) Math.floor(this.x / this.width);
        int j = (int) Math.floor(this.y / this.height);
        float curX = this.x;
        float curY = this.y;
        boolean canMove = false;

        switch (move) {
            case LEFT:
                canMove = mapManager.canMove(i - 1, j);
                this.x -= this.width;
                break;
            case RIGHT:
                canMove = mapManager.canMove(i + 1, j);
                this.x += this.width;
                break;
            case UP:
                canMove = mapManager.canMove(i, j + 1);
                this.y += this.height;
                break;
            case DOWN:
                canMove = mapManager.canMove(i, j - 1);
                this.y -= this.height;
                break;
        }

        if (!canMove) {
            this.x = curX;
            this.y = curY;
        }
        else{
            this.x = (float) (Math.floor(this.x / this.width) * this.width);
            this.y = (float) (Math.floor(this.y / this.width) * this.width);
        }

    }
}
