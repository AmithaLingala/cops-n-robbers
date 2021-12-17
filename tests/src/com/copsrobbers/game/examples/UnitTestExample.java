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

import static org.junit.Assert.assertEquals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.copsrobbers.game.CopsAndRobbers;
import com.copsrobbers.game.algorithm.CellModel;
import com.copsrobbers.game.algorithm.LevelGenerator;
import com.copsrobbers.game.characters.Robber;
import com.copsrobbers.game.listeners.LevelListener;
import com.copsrobbers.game.managers.MapManager;
import com.copsrobbers.game.screens.GameScreen;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnitTestExample {

	@Test
	public void gameMustNotCrashOnLaunch() {
		new CopsAndRobbers(640,480);
	}

}
