package de.hsbi.programmieren;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Player {
    private float x;
    private float y;
    private int points;

    // Konstruktor
    public Player(float startX) {
        this.x = startX;
        this.points = 0;
    }

    // setter für Punktestand (für Restart)
    public void setPoints(int points) {
        this.points = points;
    }

    // getter für Position und Größe (für Kollisionserkennung)
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return 60f;
    }

    public float getHeight() {
        return 70f;
    }

    // Punkte erhöhen
    public void addPoints(int n) {
        this.points = Math.max(0, this.points + n);
    }

    // getter für Punktestand
    public int getPoints() {
        return points;
    }

    // Bewegung der Spielfigur nach links
    public void moveLeft() {
        x -= 5;
        if(x == 0) {
            x += 5;
        }
    }

    // Bewegung der Spielfigur nach rechts
    public void moveRight() {
        x += 5;
        if(x == 940) {
            x -= 5;
        }
    }

    // Spielfigur zeichnen
    public void render(ShapeRenderer renderer) {
        renderer.setColor(0, 1, 0, 1);
        renderer.rect(x, y, 60, 70);
    }
}