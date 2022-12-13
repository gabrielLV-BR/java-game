package com.application.javagame.Objects.Weapons;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.application.javagame.Objects.Particle;
import com.application.javagame.Objects.Entities.Player;
import com.application.javagame.Objects.Entities.Enemies.Enemy;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;


public class Handgun extends Weapon {
    int bullets;
    boolean fired = false;

    Sound fireSound;
    Vector3 weaponOffset;
    static Texture txt = Assets.Get("explosion.png");;
    public Handgun() {
        super(
            "Handgun", 
            2, 0.5f, 8,
            new Vector3(2, 0, 0)
        );

        bullets = 3;
        fireSound = Assets.Get("sounds/shotgun.mp3");

        Texture handgunSprites = Assets.Get("guns/handgun.png");
        TextureRegion[][] regions = TextureRegion.split(handgunSprites, 80, 119);

        weaponOffset = new Vector3(0, 0, 0);

        {
            TextureRegion[] idleFrames = new TextureRegion[] { regions[0][0] };
            idleAnimation = new Animation<>(1, idleFrames);
        }
        {
            int[] indices = new int[]{ 0, 2, 3, 4, 3 };
            TextureRegion[] fireFrames = new TextureRegion[indices.length];
            for(int i = 0; i < indices.length; i++) fireFrames[i] = regions[0][indices[i]]; 
            fireAnimation = new Animation<>(MAX_FIRE_TIME / indices.length, fireFrames);
        }
        {
            int[] indices = new int[]{ 5, 6, 7, 7, 7, 7, 7, 7, 6, 5 };
            TextureRegion[] reloadFrames = new TextureRegion[indices.length];
            for(int i = 0; i < indices.length; i++) reloadFrames[i] = regions[0][indices[i]]; 
            reloadAnimation = new Animation<>(0.1f, reloadFrames);
        }
        currentAnimation = idleAnimation;

        sprite = new Sprite(currentAnimation.getKeyFrame(0));
    }

    @Override
    public void update(GameState state) {
        time += state.delta;

        if(currentAnimation.isAnimationFinished(time) && currentAnimation != idleAnimation) {
            fired = false;
            currentAnimation = idleAnimation;
        }

        Player p = state.getPlayer();

        sprite.setBounds(0, 0, 180, 250);
        sprite.setCenterX(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 4);
        sprite.setRegionY(0);
        sprite.setRegion(currentAnimation.getKeyFrame(time, true));
    }

    @Override
    public void fire(Ray ray, GameState state) {
        if(fired) return;
        if(bullets <= 0) {
            reload();
            return;
        }
        bullets--;
        System.out.println("Remaining bullets:" + bullets);
        ClosestRayResultCallback resultCallback = state.physicsWorld.rayCast(ray);
        
        currentAnimation = fireAnimation;
        fired = true;
        time = 0;

        fireSound.play(0.2f);

        if(resultCallback.hasHit()) {
            if(resultCallback.getCollisionObject().userData instanceof Enemy) {
                Enemy enemy = (Enemy)resultCallback.getCollisionObject().userData;
                enemy.damage(DAMAGE);
            }
            resultCallback.getHitPointWorld(tmpVector);

            Particle p = new Particle(txt, tmpVector, 0.1f, new Vector2(50.0f, 50.0f));
            state.addGameObject(p);
        }
    }

    public void reload() {
        System.out.println("RELOADING");
        bullets = BULLETS_IN_MAG;
        currentAnimation = reloadAnimation;
        time = 0;
        fired = true;
    }
}
