package com.application.javagame.Managers;

import com.badlogic.gdx.assets.AssetManager;

public class Assets {
    private static AssetManager manager = null;

    public static void Initialize() {
        manager = new AssetManager();
    }

    public static AssetManager GetManager() {
        return Assets.manager;
    }

    public static <T> T Get(String name) {
        manager.finishLoadingAsset(name);
        return manager.get(name);
    }

}
