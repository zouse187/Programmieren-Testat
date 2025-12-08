package de.hsbi.programmieren;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Player player;
    private Coins coins;
    private Enemy enemy;

    private int currentLevel = 1; // Startlevel
    private final int LEVEL_THRESHOLD = 15; // Punkte, die für Level 2 erreicht werden müssen
    private float levelChangeTimer = 0f; // Timer für eine kurze Pause/Anzeige
    private final float LEVEL_CHANGE_DURATION = 2.0f; // 2 Sekunden Levelwechsel-Pause

    @Override
    public void create() {
        // Kamera einrichten
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 1000);

        // Renderer und Schriftart initialisieren
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // vorhandene BitmapFont erzeugen und skalieren (größere Anzeige)
        font = new BitmapFont();
        font.getData().setScale(2.0f); // Schrift 2x so groß
        // Linear-Filter setzen, damit die skalierte Schrift weniger pixelig wirkt
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

        // Spieler initialisieren
        player = new Player(480);
        // Coins manager initialisieren (world size entspricht der Kamera-Größe)
        coins = new Coins(1000, 1000);
        // Enemy manager initialisieren
        enemy = new Enemy(1000, 1000);
    }

    // Render-Schleife
    @Override
    public void render() {
        handleInput();

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        // **********************************************
    // NEU: Levelwechsel-Logik
    // **********************************************
    if (currentLevel == 1 && player.getPoints() >= LEVEL_THRESHOLD) {
        currentLevel = 2;
        // Gegner-Parameter für Level 2 setzen (mehr Spawns)
        enemy.setSpawnInterval(1.2f); // Verkürze das Spawn-Intervall (mehr Gegner)
        enemy.setFallSpeed(250f);     // Schnellere Gegner
        levelChangeTimer = LEVEL_CHANGE_DURATION;
    }

    if (levelChangeTimer > 0) {
        // Levelwechsel-Pause
        levelChangeTimer -= delta;

        // Level 2 Hintergrundfarbe
        Gdx.gl.glClearColor(0.2f, 0.1f, 0.1f, 1); // Rot-Dunkelrot für Level 2
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        // Große Anzeige des Levelwechsels
        font.getData().setScale(3.0f); // Größere Schrift
        font.draw(batch, "LEVEL 2", camera.viewportWidth / 2 - 80, camera.viewportHeight / 2);
        font.getData().setScale(2.0f); // Zurück zur normalen Größe
        batch.end();
        return; // Breche die normale Render- und Update-Logik ab
    }
    // **********************************************

    handleInput();

    // Coins: spawn, bewegen, entfernen
    coins.update(delta);
    // Prüfe, ob der Spieler Coins eingesammelt hat
    coins.collectCollisions(player);

    // Enemys: eigene Spawn-/Update-Logik und Kollisionen
    enemy.update(delta);
    // Prüfe, ob der Spieler mit einem Enemy kollidiert (Punktabzug)
    enemy.badCollisions(player);

    // **********************************************
    // GEÄNDERT: Hintergrundfarbe Level 1 oder 2
    // **********************************************
    if (currentLevel == 1) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1); // Dunkelblau (Level 1)
    } else {
        Gdx.gl.glClearColor(0.2f, 0.1f, 0.1f, 1); // Dunkelrot (Level 2)
    }
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    camera.update();

        // Punktestand anzeigen (oben links, Y = viewportHeight - margin)
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        float yPos = camera.viewportHeight - 20; // 20 Pixel Abstand zur Oberkante
        font.draw(batch, "Punkte: " + player.getPoints(), 10, yPos);
        batch.end();
    }

    // Eingaben verarbeiten
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) player.moveLeft();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) player.moveRight();
    }

    // Ressourcen freigeben
    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
