package com.application.javagame.Objects.Entities.Enemies;

import com.application.javagame.GameState;
import com.application.javagame.Objects.GameObject;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.application.javagame.Objects.Explosion;

import net.mgsx.gltf.scene3d.scene.SceneModel;

public abstract class Enemy extends GameObject {
    public float life;
    public float damage;

    protected btRigidBody body;
    protected Vector3 dir;

    private final String NAME;
    private final int POINTS;

    public Enemy(String name, SceneModel sceneModel, Vector3 p, float life, float damage, int points) {
        super(sceneModel, p);
        this.life = life;
        this.damage = damage;

        NAME = name;
        POINTS = points;

        dir = new Vector3(0, 0, 0);

        scene.animationController.action("ACTION",0, 10000, null, 0);
    }

    public void damage(float dmg) {
        this.life -= dmg;
    }

    public void die(GameState state) {
        state.addGameObject(
            new Explosion(body.getCenterOfMassPosition(), 0.5f)
        );
        state.addPoints(POINTS);
        state.removeGameObject(this);
        state.physicsWorld.removeBody(body);
    }
}
