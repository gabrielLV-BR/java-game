package com.application.javagame.Events;

import com.badlogic.gdx.math.Vector2;

public interface MouseListener {
    void onMouseMoved(Vector2 from, Vector2 to);
    void onMouseDragged(Vector2 from, Vector2 to);
    void onMouseClicked(Vector2 where, int key, int modifiers);
    void onMouseReleased(Vector2 where, int key, int modifiers);
}
