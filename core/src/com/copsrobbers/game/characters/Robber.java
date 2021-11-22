package com.copsrobbers.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.listeners.LevelListener;
import com.copsrobbers.game.managers.AssetManager;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.screens.GameScreen;
import com.copsrobbers.game.ui.TiledMapActor;

import java.util.ArrayList;
import java.util.List;

public class Robber extends Character {
    private final LevelListener ll;
    ArrayList<CellModel> neighbours;
    ArrayList<TiledMapActor> actors;

    public Robber(Rectangle bounds, LevelListener ll) {
        super(bounds);
        this.ll = ll;
        this.neighbours = new ArrayList<>();
        this.actors = new ArrayList<>();
        Texture robberImg = new Texture(Gdx.files.internal("Spritesheet.PNG"));
        TextureRegion[] regions = TextureRegion.split(robberImg, 88, 88)[0];
        setWalk(regions);
    }

    @Override
    public void update(GameScreen.MOVES move) {
        super.update(move);
        clearTargets();
        ArrayList<Item> tempItems = new ArrayList<>();
        for (Item item : getMapManager().getItems()) {
            if (item.isCollided(this)) {
                AssetManager.obtain().playItemCollect();
                item.collect();
                tempItems.add(item);
            }
        }
        for (Item item : tempItems) {
            getMapManager().getItems().remove(item);
        }
        tryEscape();
    }

    public void tryEscape() {
        Rectangle gate = getMapManager().getGate();
        if (gate.x == this.getX() && gate.y == this.getY()) {
            ll.nextLevel();
        }
    }

    public void highlightTargets(Stage stage, List<Cop> cops) {
        clearTargets();
        int mapWidth = (int) getMapManager().getMapWidth();
        int mapHeight = (int) getMapManager().getMapHeight();
        int x = (int) (this.getX() / getMapManager().getTileWidth());
        int y = (int) (this.getY() / getMapManager().getTileHeight());
        int[][] dirs = {
                {0, 1}, {1, 0},
                {0, -1}, {-1, 0},
                {1, 1}, {-1, -1},
                {1, -1}, {-1, 1}
        };
        for (int[] dir : dirs) {
            int tempX = x + dir[0];
            int tempY = y + dir[1];
            //if (tempX >= 0 && tempX < mapWidth && tempY >= 0 && tempY < mapHeight) {
                if(!getMapManager().hasWall(tempX,tempY)) {
                    neighbours.add(new CellModel(tempX, tempY));
                }

        }

        for (CellModel cell : neighbours) {
            updateCellType(cell);
            getMapManager().highlightTile(cell, true);
            TiledMapActor actor = getMapManager().setEvent(cell.getRow(), cell.getColumn(), getMapManager().getLayer(cell), stage, new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    getMapManager().updateTileType(cell, MapManager.Layers.BACKGROUND);
                    for (Cop cop : cops) {
                        if (cop.getX() / getMapManager().getTileWidth() == cell.getRow() && cop.getY() / getMapManager().getTileHeight() == cell.getColumn()) {
                            cop.freezeCop();
                        }
                    }
                    AssetManager.obtain().playExplosion();
                    clearTargets();
                    GameManager.updateWeapons(-1);
                }
            });
            actors.add(actor);
        }

    }

    public void clearTargets() {
        for (CellModel cell : neighbours) {
            updateCellType(cell);
            getMapManager().highlightTile(cell, false);
        }
        for (TiledMapActor actor : actors) {
            actor.clear();
        }
        neighbours.clear();
        actors.clear();
    }

    private void updateCellType(CellModel cell) {
        int x = cell.getRow();
        int y = cell.getColumn();
        cell.setBox(getMapManager().hasObstacle(x, y));
        cell.setWall(getMapManager().hasWall(x, y));
    }

}
