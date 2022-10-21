package com.application.javagame.Managers;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    private static AssetManager manager = null;

    private static void Initialize() {
        manager = new AssetManager();
    }

    public static AssetManager GetManager() {
        if(Assets.manager == null) Initialize();
        return Assets.manager;
    }

}
