package de.hsbi.programmieren;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Coins verwaltet fallende Münzen, die der Spieler einsammeln kann.
public class Coins {
	private final ArrayList<Coin> coins;
	private final Random random; 
	private final float worldWidth; 
	private final float worldHeight;

	private float spawnInterval = 1.8f;
	private float spawnTimer = 0f;

	private final float coinWidth = 20f;
	private final float coinHeight = 20f;
	private final float fallSpeed = 150f;

	// Konstruktor
	public Coins(float worldWidth, float worldHeight) {
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;
		this.coins = new ArrayList<>();
		this.random = new Random();
	}

	// Update-Methode: spawnt neue Coins, bewegt existierende nach unten
	// und entfernt sie, wenn sie aus dem Bildschirm gefallen sind
	public void update(float delta) {
		spawnTimer += delta;
		if (spawnTimer >= spawnInterval) {
			spawnTimer -= spawnInterval;
			spawnCoin();
		}

		// update coins and entfernt die, die das Fenster verlassen haben
		Iterator<Coin> it = coins.iterator();
		while (it.hasNext()) {
			Coin c = it.next();
			c.update(delta);
			if (c.y + c.height < 0) {
				it.remove();
			}
		}
	}

	// rendert alle Coins
	public void render(ShapeRenderer renderer) {
		for (Coin c : coins) {
			c.render(renderer);
		}
	}

	// Prüft Kollisionen zwischen Spieler und Coins; entferne getroffene Coins und erhöhe Punkte
	public void collectCollisions(Player player) {
		Iterator<Coin> it = coins.iterator();
		while (it.hasNext()) {
			Coin c = it.next();
			if (overlaps(c.x, c.y, c.width, c.height, player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
				it.remove();
				player.addPoints(1);
			}
		}
	}

	// einfache AABB-Kollisionsprüfung
	private boolean overlaps(float ax, float ay, float aw, float ah, float bx, float by, float bw, float bh) {
		return ax < bx + bw && ax + aw > bx && ay < by + bh && ay + ah > by;
	}

	// erzeugt einen neuen Coin an einer zufälligen X-Position oben im Bildschirm
	private void spawnCoin() {
		float x = random.nextFloat() * (worldWidth - coinWidth);
		float y = worldHeight; // start at top
		coins.add(new Coin(x, y, coinWidth, coinHeight, fallSpeed));
	}

	// innere Klasse für einzelne Coin-Instanzen
	private static class Coin {
		float x, y, width, height, speed;

		// Konstruktor
		Coin(float x, float y, float width, float height, float speed) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.speed = speed;
		}

		// aktualisiert die Position des Coins
		void update(float delta) {
			y -= speed * delta;
		}

		// rendert den Coin als gelbes Rechteck
		void render(ShapeRenderer renderer) {
			renderer.setColor(1f, 0.9f, 0f, 1f);
			renderer.rect(x, y, width, height);
		}
	}
}
