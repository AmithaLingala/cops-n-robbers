package com.copsrobbers.game.items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.Utils;

public class Coin extends Item{
    private final int score;
    private final Animation<TextureRegion> spin;
    private float stateTime =0;
    public Coin(Rectangle bounds, int score) {
        super(bounds);
        this.score = score;
        Texture coinTexture = new Texture("Coins.png");
        TextureRegion[] regions = TextureRegion.split(coinTexture, 32, 32)[0];
        spin = new Animation<>(0.10f, regions);
        spin.setPlayMode(Animation.PlayMode.LOOP);
    }
    public Coin(Rectangle bounds){
        this(bounds,1);
    }
    public TextureRegion getRegion(float stateTime){
        this.stateTime += stateTime;
        return spin.getKeyFrame(this.stateTime);
    }
    @Override
    public void use() {
        Utils.obtain().updateScore(score);
    }

}
