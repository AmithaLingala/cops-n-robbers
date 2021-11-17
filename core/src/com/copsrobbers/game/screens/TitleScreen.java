package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.copsrobbers.game.CopsAndRobbers;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.ui.Components;

public class TitleScreen implements Screen {

    private final Stage stage;
    private final Game game;

    public TitleScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());

        MapManager mapManager = MapManager.obtain();
        Label title = Components.createLabel("Cops n Robbers!", 24);
        title.setSize(Gdx.graphics.getWidth(), mapManager.getTileHeight());
        title.setAlignment(Align.center);
        title.setY(mapManager.getScreenHeight() - 3 * mapManager.getTileHeight());

        stage.addActor(title);
        TextButton playButton = new TextButton("Play!", CopsAndRobbers.gameSkin);
        playButton.setWidth(Gdx.graphics.getWidth() * 0.5f);
        playButton.setHeight(Gdx.graphics.getHeight() / 10f);
        playButton.setPosition(Gdx.graphics.getWidth() * 0.5f - playButton.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.5f - playButton.getHeight() / 10f);
        playButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });
        stage.addActor(playButton);


    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
