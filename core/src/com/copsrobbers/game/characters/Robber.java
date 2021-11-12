package com.copsrobbers.game.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.copsrobbers.game.GameManager;
import com.copsrobbers.game.MapManager;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.items.Item;
import com.copsrobbers.game.listeners.LevelListener;
import com.copsrobbers.game.overlay.TiledMapActor;
import com.copsrobbers.game.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class Robber extends Character {
    ArrayList<CellModel> neighbours;
    ArrayList<TiledMapActor> actors;
    private final LevelListener ll;

    public Robber(Rectangle bounds, LevelListener ll) {
        super(bounds);
        this.ll = ll;
        this.neighbours = new ArrayList<>();
        this.actors = new ArrayList<>();
        Texture robberImg = new Texture(Gdx.files.internal("Spritesheet.PNG"));
        TextureRegion[] regions = TextureRegion.split(robberImg, 88, 88)[0];
        setWalk(regions);
        //setCharImg(robberImg);
    }

    @Override
    public void update(GameScreen.MOVES move) {
        super.update(move);
        clearTargets();
        ArrayList<Item> tempItems = new ArrayList<>();
        for (Item item : getMapManager().getItems()) {
            if (item.isCollided(this)) {
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

        if (x + 1 < mapWidth) {
            neighbours.add(new CellModel((x + 1), y));
        }
        if (x - 1 >= 0) {
            neighbours.add(new CellModel((x - 1), y));
        }
        if (y + 1 < mapHeight) {
            neighbours.add(new CellModel(x, y + 1));
        }
        if (y - 1 >= 0) {
            neighbours.add(new CellModel(x, y - 1));
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
