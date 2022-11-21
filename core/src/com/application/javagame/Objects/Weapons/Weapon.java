package com.application.javagame.Objects.Weapons;

import com.application.javagame.GameState;
import com.badlogic.gdx.math.collision.Ray;

public abstract class Weapon {
    public final String NAME;
    public final float DAMAGE;
    public final float MAX_FIRE_TIME;
    public final int BULLETS_IN_MAG;

    public Weapon(String name, float damage, float fireTime, int bulletsInMag) {
        this.NAME = name;
        this.DAMAGE = damage;
        this.MAX_FIRE_TIME = fireTime;
        this.BULLETS_IN_MAG = bulletsInMag;
    }

    public abstract void fire(Ray ray, GameState state);
    public void update(GameState state) {}
}
