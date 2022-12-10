package com.application.javagame.Objects.Weapons;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.Particle;
import com.application.javagame.Objects.Entities.Enemies.Enemy;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;

import net.mgsx.gltf.scene3d.scene.SceneAsset;

public class Handgun extends Weapon {
    public int bullets;
    boolean fired = false;
    Sound fireSound;
    float fireTime;

    private final static float scale = 0.2f;

    public Handgun(float damage, float fireTime, int bulletsInMag) {
        super(
            "Handgun", 
            Assets.<SceneAsset>Get("handgun.glb").scene,
            damage, fireTime, bulletsInMag, new Vector3(5, 5, -5).scl(scale)
        );

        
        scene.modelInstance.transform.scl(scale);
        bullets = bulletsInMag;

        fireSound = Assets.Get("sounds/shotgun.mp3");
        fireTime = 0f;
    }

    @Override
    public void update(GameState state) {
        scene.modelInstance.transform.setTranslation(
            state.getPlayer().getPosition().add(WEAPON_LOCATION_OFFSET)
        );
    }

    @Override
    public void fire(Ray ray, GameState state) {
        if(bullets <= 0) reload();
        bullets--;

        ClosestRayResultCallback resultCallback = state.physicsWorld.rayCast(ray);
        
        fireSound.play(0.2f);

        if(resultCallback.hasHit()) {
            if(resultCallback.getCollisionObject().userData instanceof Enemy) {
                System.out.println("Acertou: " + resultCallback.getCollisionObject());
                Enemy enemy = (Enemy)resultCallback.getCollisionObject().userData;
                System.out.println("Acertou o inimigo!");

                state.removeGameObject(enemy);
                enemy.damage(DAMAGE);
            }
            resultCallback.getHitPointWorld(tmpVector);

            Texture txt = Assets.Get("explosion.png");
            Particle p = new Particle(txt, tmpVector, 0.1f, new Vector2(50.0f, 50.0f));
            state.addGameObject(p);
        }
    }

    public void reload() {
        bullets = BULLETS_IN_MAG;
    }
    
}
