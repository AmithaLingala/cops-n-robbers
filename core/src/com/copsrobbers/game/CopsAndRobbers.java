package com.copsrobbers.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.copsrobbers.game.managers.AssetManager;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.screens.TitleScreen;

public class CopsAndRobbers extends Game {
    static public Skin gameSkin;
    private int width;
    private int height;

    public CopsAndRobbers() {
        super();
        this.width = 0;
        this.height = 0;
    }

    public CopsAndRobbers(int width, int height) {
        super();
        this.width = width;
        this.height = height;
    }

    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.internal("skin/holo/Holo-dark-mdpi.json"));
        if (width == 0 && height == 0) {
            this.width = Gdx.graphics.getWidth();
            this.height = Gdx.graphics.getHeight();
        }
        MapManager.initialize(width, height);
        GameManager.reset();
        AssetManager.initialize();
        this.setScreen(new TitleScreen(this));
    }

    @Override
    public void render() {
        super.render();

    }


    @Override
    public void dispose() {


    }

}
