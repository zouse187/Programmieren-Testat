package de.hsbi.programmieren;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Enemy verwaltet feindliche Einheiten, die ähnlich wie Coins von oben fallen,
 * aber andere Parameter (Größe, Spawn-Rate, Geschwindigkeit) haben.
 *
 * Bei Kontakt mit dem Player wird dem Spieler ein Punkt abgezogen.
 */
public class Enemy {
    // Liste aktiver Gegner
    private final ArrayList<EnemyUnit> enemies;
    private final Random random;
    private final float worldWidth;
    private final float worldHeight;

    // Spawn-Parameter (längeres Intervall als bei Coins)
    private float spawnInterval = 2.6f; // Sekunden zwischen Spawns
    private float spawnTimer = 0f;

    // Eigenschaften der Gegner
    private final float enemyWidth = 30f;
    private final float enemyHeight = 25f;
    private float fallSpeed = 180f; // Fallgeschwindigkeit in Pixel/s

    public Enemy(float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.enemies = new ArrayList<>();
        this.random = new Random();
    }
/**
     * Setzt das Intervall, in dem neue Gegner erzeugt werden (Level-Design).
     */
    public void setSpawnInterval(float newInterval) {
        this.spawnInterval = newInterval;
    }
    
    /**
     * Setzt die Fallgeschwindigkeit der Gegner (Level-Design).
     */
    public void setFallSpeed(float newSpeed) {
        this.fallSpeed = newSpeed;
    }
    
    /**
     * Update: spawnt neue Gegner und bewegt existierende nach unten.
    // ... Rest der Klasse


    /**
     * Update: spawnt neue Gegner und bewegt existierende nach unten.
     * Entfernt Gegner, die aus dem Bildschirm gefallen sind.
     */
    public void update(float delta) {
        // Zeit sammeln und ggf. neuen Gegner erzeugen
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval) {
            spawnTimer -= spawnInterval;
            spawnEnemy();
        }

        // Gegner aktualisieren und entfernen, wenn sie den Bildschirm verlassen haben
        Iterator<EnemyUnit> it = enemies.iterator();
        while (it.hasNext()) {
            EnemyUnit e = it.next();
            e.update(delta);
            if (e.y + e.height < 0) {
                it.remove();
            }
        }
    }

    /**
     * Rendern aller aktiven Gegner (rotes Rechteck pro Gegner).
     */
    public void render(ShapeRenderer renderer) {
        for (EnemyUnit e : enemies) {
            e.render(renderer);
        }
    }

   //Prüft Kollisionen zwischen Player und Gegnern.
   //Entfernt getroffene Gegner und gibt true zurück, wenn eine Kollision passiert ist.
    public boolean badCollisions(Player player) {
    Iterator<EnemyUnit> it = enemies.iterator();
        while (it.hasNext()) {
            EnemyUnit e = it.next();
            if (overlaps(e.x, e.y, e.width, e.height,
                        player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
                it.remove();
                return true; // Kollision → Game Over
            }
        }
        return false; // keine Kollision
    }

    // AABB-Kollisionstest (Axis-Aligned Bounding Box)
    private boolean overlaps(float ax, float ay, float aw, float ah, float bx, float by, float bw, float bh) {
        return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
    }

    // Erzeugt einen neuen Gegner an einer zufälligen X-Position oben
    private void spawnEnemy() {
        float x = random.nextFloat() * (worldWidth - enemyWidth);
        float y = worldHeight; // oben am Bildschirmrand
        enemies.add(new EnemyUnit(x, y, enemyWidth, enemyHeight, fallSpeed));
    }

    /**
     * Innere Klasse für einzelne Gegner-Instanzen.
     */
    private static class EnemyUnit {
        float x, y, width, height, speed;

        EnemyUnit(float x, float y, float width, float height, float speed) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.speed = speed;
        }

        // Bewegung nach unten
        void update(float delta) {
            y -= speed * delta;
        }

        // Zeichnet den Gegner als rotes Rechteck
        void render(ShapeRenderer renderer) {
            renderer.setColor(1f, 0.2f, 0.2f, 1f);
            renderer.rect(x, y, width, height);
        }
    }
}