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

    // getter für Punktestand
    public int getPoints() {
        return points;
    }

    // Bewegung der Spielfigur nach links
    public void moveLeft() {
        x -= 3;
        if(x == 0) {
            x += 3;
        }
    }

    // Bewegung der Spielfigur nach rechts
    public void moveRight() {
        x += 3;
        if(x == 939) {
            x -= 3;
        }
    }

    // Spielfigur zeichnen
    public void render(ShapeRenderer renderer) {
        renderer.setColor(0, 1, 0, 1);
        renderer.rect(x, y, 60, 70);
    }
}