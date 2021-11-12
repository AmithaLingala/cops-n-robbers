package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.copsrobbers.game.CopsAndRobbersV1;
import com.copsrobbers.game.GameManager;
import com.copsrobbers.game.MapManager;

public class NextLevelScreen implements Screen {
    private final Stage stage;
    private final Game game;

    public NextLevelScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        int level = GameManager.getLevel()+1;

        TextButton playButton = new TextButton("Go to level "+level, CopsAndRobbersV1.gameSkin);
        playButton.setWidth(Gdx.graphics.getWidth()/2.0f);
        playButton.setPosition(Gdx.graphics.getWidth()/2.0f-playButton.getWidth()/2,Gdx.graphics.getHeight()/2.0f-playButton.getHeight()/2);
        playButton.addListener(new InputListener(){
            @Override
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                GameManager.setLevel(level);
                game.setScreen(new GameScreen(game));
            }
            @Override
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Amble-Light.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BLACK;
        BitmapFont font24 = generator.generateFont(parameter); // font size 24 pixels
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font24;
        MapManager mapManager = MapManager.obtain();

        Label coins = new Label("Coins: "+ GameManager.getCoins(),labelStyle);
        coins.setSize(Gdx.graphics.getWidth(), mapManager.getTileHeight());
        coins.setAlignment(Align.center);
        coins.setY(mapManager.getScreenHeight()- 2*mapManager.getTileHeight());

        Label weapons = new Label("Weapons: "+ GameManager.getWeapons(),labelStyle);
        weapons.setSize(Gdx.graphics.getWidth(), mapManager.getTileHeight());
        weapons.setAlignment(Align.center);
        weapons.setY(mapManager.getScreenHeight()- 3*mapManager.getTileHeight());

        stage.addActor(coins);
        stage.addActor(weapons);
        stage.addActor(playButton);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
