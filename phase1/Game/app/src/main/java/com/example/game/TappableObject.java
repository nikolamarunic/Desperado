package com.example.game;

import android.view.MotionEvent;

public class TappableObject extends GameObject {
    /**
     * length of hitbox.
     */
    private int length;
    /**
     * height of hitbox.
     */
    private int height;

    public TappableObject(int newX, int newY, int newLength, int newHeight) {
        super(newX, newY);
        length = newLength;
        height = newHeight;
    }

    public boolean isTapped(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (super.x <= x && x <= super.x + length) {
            return super.y <= y && x <= super.y + height;
        } else {
            return false;
        }
    }

    public void setLength(int newLength) {
        length = newLength;
    }
    public int getLength() {
        return length;
    }

    public void setHeight(int newHeight) {
        length = newHeight;
    }

    public int getHeight() {
        return height;
    }
}
