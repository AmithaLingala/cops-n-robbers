package com.copsrobbers.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.copsrobbers.game.CopsAndRobbers;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.ui.Components;

public class HelpScreen implements Screen {
    private final Stage stage;
    private final Game game;

    public HelpScreen(Game aGame) {
        game = aGame;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        MapManager mapManager = MapManager.obtain();
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        Table table = new Table();
        table.setWidth(width * 0.5f);
        table.setHeight(height);
        table.setPosition(table.getWidth() * 0.5f, 0);

        Label title = Components.createLabel("How To Play", 24);
        title.setAlignment(Align.center);

        TextButton backButton = Components.createTextButton("Back", new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new TitleScreen(game));
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        String howToPlay = "Collect as many coins and bombs as you can before exiting\n\n" +
                "Swipe on screen to move robber in that direction once\n\n" +
                "Avoid being captured by the cops\n\n" +
                "Click on bomb icon at bottom center with which you can destroy boxes or freeze cops for one turn.";
        TextArea description = Components.createTextArea(howToPlay, 22, true);
        ScrollPane scrollPane = new ScrollPane(description, CopsAndRobbers.gameSkin);


        table.add(title).padTop(mapManager.getTileHeight()).expandX().fillX().center();
        table.row().expandX().fillX();

        table.add(scrollPane).padTop(mapManager.getTileHeight()).expandY().colspan(2).fillY().expandX().fillX();
        table.row().expandX().fillX();

        table.add(backButton).padBottom(mapManager.getTileHeight()).expandX();

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
