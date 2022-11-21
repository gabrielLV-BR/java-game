package com.application.javagame.Objects;

import com.application.javagame.GameState;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Particle extends GameObject {

    float duration;
    final float initialDuration;
    Decal decal;

    public Particle(Texture texture, Vector3 position, float duration, Vector2 dimension) {
        decal = Decal.newDecal(new TextureRegion(texture), true);
        decal.setPosition(position);
        this.duration = duration;
        this.initialDuration = duration;
        decal.setDimensions(dimension.x, dimension.y);
    }

    public Particle(Texture texture, Vector3 position, float duration) {
        this(texture, position, duration, new Vector2(texture.getWidth(), texture.getHeight()));
    }

    @Override
    public void update(GameState state) {
        Camera camera = state.getPlayer().getCamera();
        decal.lookAt(camera.position, camera.up);

        duration -= state.delta;

        float normalizedDuration = duration / initialDuration;

        decal.setScale(easingFunction(normalizedDuration));

        if(duration <= 0) state.removeGameObject(this);
        else state.decalBatch.add(decal);   
    }

    private float easingFunction(float num) {
        return num;
    }

    public Decal getDecal() { return decal; }
    public float getRemainingTime() { return duration; }
    public void setDuration(float dur) { duration = dur; }    
}