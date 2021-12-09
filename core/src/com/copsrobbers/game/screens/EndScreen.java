package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.copsrobbers.game.managers.AssetManager;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.ui.Components;

public class EndScreen implements Screen {
    private final Stage stage;
    private final Game game;

    public EndScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        MapManager mapManager = MapManager.obtain();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        Table table = new Table();
        table.setWidth(width * 0.5f);
        table.setHeight(height);
        table.setPosition(table.getWidth() * 0.5f, 0);

        Label title = Components.createLabel("Game Over!", 24);
        title.setAlignment(Align.center);

        Label lvl = Components.createLabel("Level " + GameManager.getLevel(), 24);
        lvl.setAlignment(Align.center);

        Label highScore = Components.createLabel("High Score " + GameManager.getMaxScore(), 20);
        highScore.setAlignment(Align.center);

        Label coins = Components.createLabel("Current Score: " + GameManager.getCoins(), 22);
        coins.setAlignment(Align.center);

        Label weapons = Components.createLabel("Weapons Collected: " + GameManager.getWeapons(), 22);
        weapons.setAlignment(Align.center);
        weapons.setY(mapManager.getScreenHeight() - 4 * mapManager.getTileHeight());

        TextButton retryButton = Components.createTextButton("Retry", new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameManager.reset();
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        TextButton homeButton = Components.createTextButton("Main Menu", new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                GameManager.reset();
                game.setScreen(new TitleScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        table.add(title)
                .padTop(mapManager.getTileHeight())
                .padBottom(mapManager.getTileHeight())
                .expandX().fillX().center();
        table.row().expandX().fillX();

        table.add(lvl).padBottom(mapManager.getTileHeight()).fillX().expandX();
        table.row().expandX().fillX();

        table.add(highScore).padBottom(mapManager.getTileHeight()).fillX().expandX();
        table.row().expandX().fillX();

        table.add(coins).fillX().expandX();
        table.row().expandX().fillX();

        table.add(weapons).fillX().expandX();
        table.row().expandX().fillX();

        table.add(retryButton).padTop(mapManager.getTileHeight()).padBottom(mapManager.getTileHeight()).fillX().expandX();
        table.row().expandX().fillX();

        table.add(homeButton).fillX().expandX();
        table.row().padBottom(mapManager.getTileHeight()).expandX().fillX();

        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        AssetManager.obtain().playGameOver();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
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
