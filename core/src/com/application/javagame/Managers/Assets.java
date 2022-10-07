package com.application.javagame.Managers;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    private static AssetManager manager = null;

    private static void initialize() {
        manager = new AssetManager();
    }

    public static AssetManager getManager() {
        if(Assets.manager == null) initialize();
        return Assets.manager;
    }

}
