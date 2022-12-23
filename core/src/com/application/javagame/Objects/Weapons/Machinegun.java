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


public class Machinegun extends Weapon {
    int bullets;
    boolean fired = false;

    Sound fireSound, reloadSound;
    Vector3 weaponOffset;
    static Texture txt = Assets.Get("explosion.png");;

    public Machinegun() {
        super(
            "Machinegun", 
            1, 0.1f, 20,
            false
        );

        bullets = 3;
        fireSound = Assets.Get("sounds/pistol.mp3");
        reloadSound = Assets.Get("sounds/pistol_reload.mp3");

        texture = new Texture(Gdx.files.internal("guns/machinegun.png"));
        System.out.println(texture);
        regions = TextureRegion.split(
            texture, texture.getWidth() / 3, texture.getHeight() 
        );

        weaponOffset = new Vector3(0, 0, 0);

        {
            TextureRegion[] idleFrames = new TextureRegion[] { regions[0][0] };
            idleAnimation = new Animation<>(1, idleFrames);
        }
        {
            int[] indices = new int[]{ 1, 2 };
            TextureRegion[] fireFrames = new TextureRegion[indices.length];
            for(int i = 0; i < indices.length; i++) fireFrames[i] = regions[0][indices[i]]; 
            fireAnimation = new Animation<>(MAX_FIRE_TIME / indices.length, fireFrames);
        }
        {
            TextureRegion[] reloadFrames = new TextureRegion[0];
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

        sprite.setBounds(0, 0, 180, 190);
        sprite.setCenterX(Gdx.graphics.getWidth() / 2);
        sprite.setRegionY(0);
        sprite.setRegion(currentAnimation.getKeyFrame(time, true));
    }

    @Override
    public void fire(Ray ray, GameState state) {
        if(fired) return;
        bullets--;
        
        currentAnimation = fireAnimation;
        fired = true;
        time = 0;

        fireSound.play(0.2f);
        super.fire(ray, state);
    }
}
