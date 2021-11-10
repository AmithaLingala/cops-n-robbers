package com.copsrobbers.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameManager {

    public static int getCoins() {

        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        return prefs.getInteger("CurrScore", 0);
    }

    public static void setCoins(int score) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        prefs.putInteger("CurrScore", score);
    }

    public static void updateCoins(int score) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        int curScore = GameManager.getCoins();
        curScore = curScore + score;
        GameManager.setCoins(curScore);
        int maxScore = prefs.getInteger("MaxScore", 0);
        if (maxScore < curScore) {
            prefs.putInteger("MaxScore", curScore);
        }
    }

    public static int getWeapons() {

        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        return prefs.getInteger("CurrWeapons", 0);
    }

    public static void setWeapons(int weaponCount) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        prefs.putInteger("CurrWeapons", weaponCount);
    }

    public static void updateWeapons(int weaponCount) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        int curWeapons = GameManager.getWeapons();
        curWeapons = curWeapons + weaponCount;
        GameManager.setWeapons(curWeapons);
    }

    public static int getLevel() {

        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        return prefs.getInteger("Currlevel", 1);
    }

    public static void setLevel(int level) {
        Preferences prefs = Gdx.app.getPreferences(CopsAndRobbersV1.class.getName());
        prefs.putInteger("Currlevel", level);
    }

}
