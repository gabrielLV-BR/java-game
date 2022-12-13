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
    Vector3 normal;

    public Particle(Texture texture, Vector3 position, Vector3 normal, float duration, Vector2 dimension) {
        decal = Decal.newDecal(new TextureRegion(texture), true);
        decal.setPosition(position);
        this.duration = duration;
        this.initialDuration = duration;
        decal.setDimensions(dimension.x, dimension.y);
        this.normal = normal;
    }

    public Particle(Texture texture, Vector3 position, Vector3 normal, float duration) {
        this(texture, position, normal, duration, new Vector2(texture.getWidth(), texture.getHeight()));
    }
    public Particle(Texture texture, Vector3 position, float duration) {
        this(texture, position, null, duration, new Vector2(texture.getWidth(), texture.getHeight()));
    }

    @Override
    public void register(GameState state) {
        if(normal != null) {
            Vector3 right = tmpVector.set(normal).crs(Vector3.Y).cpy();
            Vector3 up = tmpVector.set(normal).crs(right);
            decal.setRotation(normal, up);
            decal.setPosition(tmpVector.set(decal.getPosition()).add(normal.cpy().scl(0.1f)));
        }
        state.addGameObject(this);
    }

    @Override
    public void update(GameState state) {
        if(normal == null) {
            Camera camera = state.getPlayer().getCamera();
            decal.lookAt(camera.position, camera.up);
        }
        duration -= state.delta;

        float normalizedDuration = duration / initialDuration;

        decal.setColor(1, 1, 1, normalizedDuration);
        // decal.setScale(easingFunction(normalizedDuration));

        if(duration <= 0) state.removeGameObject(this);
        else state.decalBatch.add(decal);   
    }

    public Decal getDecal() { return decal; }
    public float getRemainingTime() { return duration; }
    public void setDuration(float dur) { duration = dur; }    
}