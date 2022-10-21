package com.application.javagame.Managers;

import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class ModelManager {

    private static AssetManager assetManager;
    private static HashMap<String, Model> modelMap;

    private ModelManager() {
        assetManager = Assets.GetManager();
        modelMap = new HashMap<>();
    }

    public static void Initialize() {
        if(modelMap == null) new ModelManager();
    }

    public static void Store(String key, String name) {
        if(modelMap.containsKey(key)) return;
        if(!assetManager.isLoaded(name)) assetManager.finishLoadingAsset(name);
        
        Model m = assetManager.get(name, Model.class);

        if(m != null) modelMap.put(key, m);
    }

    public static ModelInstance GetInstance(String key, Vector3 pos) {
        Model m = modelMap.get(key);
        if(m == null) return null;
        return new ModelInstance(m, pos);
    }
    
    public static ModelInstance GetInstance(String key) {
        Model m = modelMap.get(key);
        if(m == null) return null;
        return new ModelInstance(m);
    }
}
