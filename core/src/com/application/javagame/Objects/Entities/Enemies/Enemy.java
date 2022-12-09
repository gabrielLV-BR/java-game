package com.application.javagame.Objects.Entities.Enemies;

import com.application.javagame.GameState;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.scene.SceneModel;

public abstract class Enemy extends GameObject {
    public float life;
    public float damage;
    public final String NAME;
    public Enemy(String name, SceneModel sceneModel, Vector3 p, float life, float damage) {
        super(sceneModel, p);
        this.life = life;
        this.damage = damage;
        NAME = name;
    }

    public void damage(float dmg) {
        this.life -= dmg;
    }

    public void die(GameState state) {
        state.removeGameObject(this);
    }
}
