package com.application.javagame.Objects.Weapons;

import java.util.ArrayList;

import com.application.javagame.GameState;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

public abstract class Weapon {
    public final String NAME;
    public final float DAMAGE;
    public final float MAX_FIRE_TIME;
    public final int BULLETS_IN_MAG;
    public final Vector3 WEAPON_OFFSET;

    protected final Vector3 tmpVector;

    float time = 0;

    Sprite sprite;
    Animation<TextureRegion> currentAnimation;
    Animation<TextureRegion> idleAnimation;
    Animation<TextureRegion> fireAnimation;
    Animation<TextureRegion> reloadAnimation;
    
    public Weapon(String name, float damage, float fireTime, int bulletsInMag, Vector3 weaponOffset) {
        this.NAME = name;
        this.DAMAGE = damage;
        this.MAX_FIRE_TIME = fireTime;
        this.BULLETS_IN_MAG = bulletsInMag;
        this.WEAPON_OFFSET = weaponOffset;

        sprite = new Sprite();
        tmpVector = new Vector3(0, 0, 0);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract void fire(Ray ray, GameState state);
    public void update(GameState state) {}
}
