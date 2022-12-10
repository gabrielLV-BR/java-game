package com.application.javagame.Objects.Weapons;

import com.application.javagame.GameState;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneModel;

public abstract class Weapon extends GameObject {
    public final String NAME;
    public final float DAMAGE;
    public final float MAX_FIRE_TIME;
    public final int BULLETS_IN_MAG;
    public final Vector3 WEAPON_LOCATION_OFFSET;
    
    public Weapon(String name, SceneModel sceneModel, float damage, float fireTime, int bulletsInMag, Vector3 weaponLocationOffset) {
        super(sceneModel, Vector3.Zero);

        this.NAME = name;
        this.DAMAGE = damage;
        this.MAX_FIRE_TIME = fireTime;
        this.BULLETS_IN_MAG = bulletsInMag;
        this.WEAPON_LOCATION_OFFSET = weaponLocationOffset;

        this.scene = new Scene(sceneModel);
    }

    public abstract void fire(Ray ray, GameState state);
    public void update(GameState state) {}

    public void register(GameState state) {
        state.addGameObject(this);
    }
}
