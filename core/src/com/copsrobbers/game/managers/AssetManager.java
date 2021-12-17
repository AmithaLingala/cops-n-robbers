package com.copsrobbers.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Class to manage audios for the game
 */
public class AssetManager {
    private static AssetManager instance = null;
    private final Sound ominous;
    private final Sound explosion;
    private final Sound itemCollect;
    private final Sound complete;

    public static void initialize() {
        if(instance== null) {
            instance = new AssetManager();
        }
    }

    public static AssetManager obtain() {
        return instance;
    }
    private AssetManager() {
        explosion = Gdx.audio.newSound(Gdx.files.internal("effects/megumin.wav"));
        itemCollect = Gdx.audio.newSound(Gdx.files.internal("effects/item-collect.wav"));
        complete = Gdx.audio.newSound(Gdx.files.internal("effects/complete.wav"));
        ominous = Gdx.audio.newSound(Gdx.files.internal("effects/ominous.wav"));
    }

    public void playGameOver() {
        ominous.play();
    }
    public void playExplosion() {
        explosion.play();
    }
    public void playComplete() {
        complete.play();
    }
    public void playItemCollect() {
        itemCollect.play();
    }
}
