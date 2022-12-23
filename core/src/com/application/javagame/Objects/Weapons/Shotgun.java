package com.application.javagame.Objects.Weapons;

import com.application.javagame.GameState;
import com.application.javagame.Managers.Assets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;


public class Shotgun extends Weapon {
    int bullets;
    boolean fired = false;

    Sound fireSound;
    Vector3 weaponOffset;
    static Texture txt = Assets.Get("explosion.png");;
    public Shotgun() {
        super(
            "Shotgun", 
            5, 0.8f, 8,
            true
        );

        bullets = 3;
        fireSound = Assets.Get("sounds/shotgun.mp3");

        // texture = Assets.Get("guns/shotgun.png");
        texture = new Texture(Gdx.files.internal("guns/shotgun.png"));
        regions = TextureRegion.split(texture, texture.getWidth() / 7, 132);

        System.out.println(texture);
        System.out.println("Cols: " + regions[0].length);
        System.out.println("Rows: " + regions.length);

        weaponOffset = new Vector3(0, 0, 0);

        {
            TextureRegion[] idleFrames = new TextureRegion[] { regions[0][0] };
            idleAnimation = new Animation<>(1, idleFrames);
        }
        {
            int[] indices = new int[]{ 1, 2, 3, 4, 4, 3, 3, 5 };
            TextureRegion[] fireFrames = new TextureRegion[indices.length];
            System.out.println("Indices len: " + indices.length);
            System.out.println("region size: " + fireFrames.length);
            for(int i = 0; i < indices.length; i++) fireFrames[i] = regions[0][indices[i]]; 
            fireAnimation = new Animation<>(MAX_FIRE_TIME / indices.length, fireFrames);
        }
        {
            int[] indicesRow = new int[] {};
            int[] indicesCol = new int[] {};
            TextureRegion[] reloadFrames = new TextureRegion[indicesRow.length];
            for(int i = 0; i < indicesRow.length; i++) 
                reloadFrames[i] = regions[indicesRow[i]][indicesCol[i]]; 
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

        sprite.setBounds(0, 0, 180, 250);
        sprite.setCenterX(Gdx.graphics.getWidth() / 2);
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

        super.fire(ray, state);

        fireSound.play(0.2f);
        for(int i = 0; i < 5; i++) {
            Ray r = ray.cpy();
            r.direction.add(tmpVector.setToRandomDirection().scl(0.2f));

            currentAnimation = fireAnimation;
            fired = true;
            time = 0;

            super.fire(r, state);
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
