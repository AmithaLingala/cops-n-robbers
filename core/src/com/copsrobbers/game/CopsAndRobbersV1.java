package com.copsrobbers.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.copsrobbers.game.managers.AssetManager;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.screens.TitleScreen;

public class CopsAndRobbersV1 extends Game {
    static public Skin gameSkin;


    @Override
    public void create() {
        gameSkin = new Skin(Gdx.files.internal("skin/holo/Holo-dark-mdpi.json"));
        GameManager.reset();
        MapManager.initialize();
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
