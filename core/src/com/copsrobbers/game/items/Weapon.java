package com.copsrobbers.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;

/**
 * Class for the Weapon inherited from Item class
 */
public class Weapon extends Item {
    private final Animation<TextureRegion> spin;
    private float stateTime = 0;

    /**
     * Constructor
     * @param bounds Rectangle for weapon position and size
     * @param score Value of the weapon to add to weapon count
     */
    public Weapon(Rectangle bounds, int score) {
        super(bounds);
        Texture weaponTexture = new Texture("EMP.png");
        MapManager mapManager = MapManager.obtain();
        TextureRegion[] regions = TextureRegion.split(weaponTexture, mapManager.getTextureSize(), mapManager.getTextureSize())[0];
        spin = new Animation<>(1 / 5f, regions);
        spin.setPlayMode(Animation.PlayMode.LOOP);
        GameManager.updateWeapons(score);
    }

    public Weapon(Rectangle bounds) {
        this(bounds, 0);
    }

    @Override
    public void collect() {
        GameManager.updateWeapons(1);
    }

    public TextureRegion getRegion(float stateTime) {
        this.stateTime += stateTime;
        return spin.getKeyFrame(this.stateTime);
    }

}
