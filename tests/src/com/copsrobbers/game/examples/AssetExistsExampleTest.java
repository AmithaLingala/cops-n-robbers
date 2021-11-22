/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.copsrobbers.game.examples;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.copsrobbers.game.CopsAndRobbers;
import com.copsrobbers.game.GdxTestRunner;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.algorithm.LevelGenerator;
import com.copsrobbers.game.characters.Cop;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.listeners.GameListener;
import com.copsrobbers.game.listeners.LevelListener;
import com.copsrobbers.game.managers.GameManager;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.screens.GameScreen;

import java.util.LinkedList;

@RunWith(GdxTestRunner.class)
public class AssetExistsExampleTest {
	@Before
	public void setup(){

		CopsAndRobbers app = new CopsAndRobbers(640,480);
		MapManager.initialize(640,480);
	}
	@After
    public void teardown(){
	    GameManager.reset();
    }
    @Test
    public void robberCanMove(){
        CellModel[][] cells = new CellModel[10][10];
        LevelGenerator lg = new LevelGenerator(cells);
        lg.generate(0);
        MapManager.obtain().generate(cells);
        lg.generateRobber(new LevelListener() {
            @Override
            public void nextLevel() {

            }
        });
        Robber robber = MapManager.obtain().getRobber();
        float x = robber.getX();
        float y = robber.getY();
        robber.update(GameScreen.MOVES.LEFT);
        float nx = robber.getX();
        float ny = robber.getY();
        Assert.assertEquals("Robber mush not move up",y,ny,0.1f);
        float expected = x-MapManager.obtain().getTileWidth();
        Assert.assertEquals("Robber should move left",expected,nx,0.1f);
    }

    @Test
    public void copCanMove(){
        CellModel[][] cells = new CellModel[10][10];
        LevelGenerator lg = new LevelGenerator(cells);
        lg.generate(0);
        MapManager.obtain().generate(cells);
        lg.generateRobber(()->{});
        lg.generateCops(()->{});
        Robber robber = MapManager.obtain().getRobber();
        Cop cop = MapManager.obtain().getCops().get(0);
        float x = cop.getX();
        float y = cop.getY();
        cop.update(robber,new LinkedList<LinkedList<Integer>>());
        float nx = cop.getX();
        float ny = cop.getY();
        Assert.assertFalse("Cop should move towards robber",(x==nx && y==ny));
    }

    @Test
    public void robberCollisionWithObstacles(){
        CellModel robPos = new CellModel(0,0);
        CellModel boxPos = new CellModel(0,1);
        CellModel boxPos2 = new CellModel(1,0);
        CellModel boxPos3 = new CellModel(1,1);
        boxPos.setBox(true);
        boxPos2.setBox(true);
        boxPos3.setBox(true);

        CellModel[][] cells = new CellModel[][]{{robPos,boxPos},{boxPos2,boxPos3}};
        MapManager.obtain().generate(cells);
        Robber robber = new Robber(new Rectangle(robPos.getRow(), robPos.getColumn(), MapManager.obtain().getMapWidth(), MapManager.obtain().getMapHeight()),()->{});
        float x = robber.getX();
        float y = robber.getY();
        for(GameScreen.MOVES move: GameScreen.MOVES.values()){
            robber.update(move);
            float nx = robber.getX();
            float ny = robber.getY();
            Assert.assertTrue("Robber should not move towards obstacle",(x==nx && y==ny));
        }
    }
}
