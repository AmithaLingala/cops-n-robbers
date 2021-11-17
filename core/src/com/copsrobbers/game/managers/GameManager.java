package com.copsrobbers.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.copsrobbers.game.CopsAndRobbers;

public class GameManager {
    public static int getCoins() {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        return prefs.getInteger("CurrScore", 0);
    }

    public static void setCoins(int score) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        prefs.putInteger("CurrScore", score);
        prefs.flush();
    }

    public static int getMaxScore() {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        return prefs.getInteger("MaxScore", 0);
    }

    public static void updateCoins(int score) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        int curScore = GameManager.getCoins();
        curScore = curScore + score;
        GameManager.setCoins(curScore);
        int maxScore = prefs.getInteger("MaxScore", 0);
        if (maxScore < curScore) {
            prefs.putInteger("MaxScore", curScore);
        }
        prefs.flush();
    }

    public static int getWeapons() {

        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        return prefs.getInteger("CurrWeapons", 0);
    }

    public static void setWeapons(int weaponCount) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        prefs.putInteger("CurrWeapons", weaponCount);
        prefs.flush();
    }

    public static void updateWeapons(int weaponCount) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        int curWeapons = GameManager.getWeapons();
        curWeapons = curWeapons + weaponCount;
        GameManager.setWeapons(curWeapons);
        prefs.flush();
    }

    public static int getLevel() {

        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        return prefs.getInteger("CurLevel", 1);
    }

    public static void setLevel(int level) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbers.class.getName());
        prefs.putInteger("CurLevel", level);
        prefs.flush();
    }
    public static void reset() {
        setLevel(1);
        setWeapons(0);
        setCoins(0);
    }
}
