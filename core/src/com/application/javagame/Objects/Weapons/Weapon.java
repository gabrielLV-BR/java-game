package com.application.javagame.Objects.Weapons;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.Particle;
import com.application.javagame.Objects.Entities.Enemies.Enemy;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;

public abstract class Weapon {
    public final String NAME;
    public final float DAMAGE;
    public final float MAX_FIRE_TIME;
    public final int BULLETS_IN_MAG;
    public final boolean IS_SINGLE_ACTION;

    protected final Vector3 tmpVector;

    private static Texture holeTexture = null;

    float time = 0;

    Sprite sprite;
    Animation<TextureRegion> currentAnimation;
    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> fireAnimation;
    Animation<TextureRegion> reloadAnimation;
    
    public Weapon(String name, float damage, float fireTime, int bulletsInMag, boolean singleAction) {
        this.NAME = name;
        this.DAMAGE = damage;
        this.MAX_FIRE_TIME = fireTime;
        this.BULLETS_IN_MAG = bulletsInMag;

        IS_SINGLE_ACTION = singleAction;

        sprite = new Sprite();
        tmpVector = new Vector3(0, 0, 0);

        if(holeTexture == null)
            holeTexture = Assets.Get("hole.png");
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean IsSingleAction() {
        return IS_SINGLE_ACTION;
    }

    public void fire(Ray ray, GameState state) {
        ClosestRayResultCallback resultCallback = state.physicsWorld.rayCast(ray);

        if(resultCallback.hasHit()) {
            if(resultCallback.getCollisionObject().userData instanceof Enemy) {
                Enemy enemy = (Enemy)resultCallback.getCollisionObject().userData;
                enemy.damage(DAMAGE);
            } else {
                Vector3 normal = new Vector3();
                resultCallback.getHitNormalWorld(normal);
                resultCallback.getHitPointWorld(tmpVector);

                Particle p = new Particle(holeTexture, tmpVector, normal, 10f, new Vector2(5.0f, 5.0f));
                p.register(state);
            }
        }
    }

    public void update(GameState state) {}
}
