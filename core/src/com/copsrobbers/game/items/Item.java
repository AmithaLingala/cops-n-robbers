package com.copsrobbers.game.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.copsrobbers.game.characters.Robber;

public abstract class Item extends Image {
    private float x;
    private float y;
    private float width;
    private float height;

    public Item(Rectangle bounds) {
        this.x = bounds.x;
        this.y = bounds.y;
        this.width = bounds.width;
        this.height = bounds.height;


    }

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

    public boolean isCollided(Robber robber) {
        return robber.getX() == this.getX() && robber.getY() == this.getY();

    }

    public abstract void collect();

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public abstract TextureRegion getRegion(float stateTime);
}
