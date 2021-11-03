package com.copsrobbers.game.items;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.copsrobbers.game.Utils;
import com.copsrobbers.game.characters.Robber;

public abstract class Item extends Image {
    private  float x;
    private  float y;

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

    private  float width;
    private  float height;

    public Item(Rectangle bounds)
    {
        this.x = bounds.x;
        this.y = bounds.y;
        this.width = bounds.width;
        this.height = bounds.height;


    }

    public boolean isCollided(Robber robber){
        if(robber.getX()== this.getX() && robber.getY() == this.getY())
            return true;
        else
            return false;

    }
    public abstract void use();

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
