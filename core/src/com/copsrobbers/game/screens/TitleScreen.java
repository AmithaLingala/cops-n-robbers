package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.ui.Components;

/**
 * Class to implement Title Screen
 */

public class TitleScreen implements Screen {

    private final Stage stage;
    private final Game game;
    private final Robber robber;

    public TitleScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new ScreenViewport());
        MapManager mapManager = MapManager.obtain();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        Table table = new Table();
        table.setWidth(width * 0.5f);
        table.setHeight(height);
        table.setPosition(table.getWidth() * 0.5f, 0);

        Label title = Components.createLabel("Cops And Robbers!", 24);
        title.setAlignment(Align.center);

        Label highScore = Components.createLabel("High Score " + GameManager.getMaxScore(), 20);
        highScore.setAlignment(Align.center);

        TextButton playButton = Components.createTextButton("Play!", new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });


        TextButton helpButton = Components.createTextButton("Help", new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new HelpScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        TextButton aboutButton = Components.createTextButton("About", new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new GameScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        table.add(title).padTop(mapManager.getTileHeight()).padBottom(mapManager.getTileHeight()).expandX().fillX().center();
        table.row().expandX().fillX();

        table.add(highScore).padBottom(mapManager.getTileHeight()).expandX().fillX().center();
        table.row().expandX().fillX();

        table.add(playButton).padBottom(mapManager.getTileHeight()).expandX().fillX();
        table.row().expandX().fillX();

        table.add(helpButton).padBottom(mapManager.getTileHeight()).expandX().fillX();
        table.row().expandX().fillX();

        // TODO add about screen and uncomment the following
        // table.add(aboutButton).padBottom(mapManager.getTileHeight()).expandX();

        robber = new Robber(new Rectangle(0, 0, mapManager.getTileWidth(), mapManager.getTileHeight()), () -> {
        });
        stage.addActor(table);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);
        stage.act();
        stage.draw();
        Batch batch = stage.getBatch();
        batch.begin();
        batch.draw(robber.getRegion(delta), robber.getX(), robber.getY());
        batch.end();
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
