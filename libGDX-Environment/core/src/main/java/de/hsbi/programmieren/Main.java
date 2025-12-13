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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Player player;
    private Coins coins;
    private Enemy enemy;

    private int currentLevel = 1; // Startlevel
    private final int pointsForLevel2 = 15; // Punkte, die für Level 2 erreicht werden müssen
    private final int pointsForWin = 40; // Punkte, die fürs gewinnen erreicht werden müssen
    private float levelChangeTimer = 0f; // Timer für eine kurze Pause/Anzeige
    private final float LEVEL_CHANGE_DURATION = 2.0f; // 2 Sekunden Levelwechsel-Pause

    private boolean gameState = true;

    private Stage stage; // Die Stage, auf der UI-Elemente liegen
    private Skin skin;   // Das Skin, das das Aussehen der Widgets definiert
    private TextButton button; // Der Button selbst

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

        
        // Stage initialisieren mit einem ScreenViewport (passt sich an Fenstergröße an)
        stage = new Stage(new ScreenViewport());

        // Stage als Input-Processor setzen, damit Maus/Touch-Events an die Stage gehen
        Gdx.input.setInputProcessor(stage);

        // Skin aus den internen Assets laden
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // TextButton erstellen
        button = new TextButton("Restart", skin);

        // Position des Buttons in Stage-Koordinaten setzen
        button.setPosition(220, 100);

        // Größe des Buttons setzen (Breite=200, Höhe=50)
        button.setSize(200, 50);

        // Listener hinzufügen: reagiert auf Zustandsänderungen wie Klicks
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                // Button wurde geklickt - Spiel zurücksetzen
                gameState = true;
                currentLevel = 1;
                player.setPoints(0);
                enemy.setSpawnInterval(2.6f); // Verkürze das Spawn-Intervall (mehr Gegner)
                enemy.setFallSpeed(180f);

                // Hintergrundfarbe zurücksetzen
                camera.update();

                // Zeichne Coins und Spieler im selben ShapeRenderer-Durchlauf
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                coins.render(shapeRenderer);
                enemy.render(shapeRenderer);
                player.render(shapeRenderer);
                shapeRenderer.end();

                // Punktestand anzeigen (oben links, Y = viewportHeight - margin)
                batch.setProjectionMatrix(camera.combined);
                batch.begin();
                float yPos = camera.viewportHeight - 20; // 20 Pixel Abstand zur Oberkante
                font.draw(batch, "Punkte: " + player.getPoints(), 10, yPos);

                // Level rechts oben
                String levelText = "Level: " + currentLevel;
                float textWidth = font.getRegion().getRegionWidth(); // grobe Breite
                font.draw(batch, levelText, camera.viewportWidth - 125, camera.viewportHeight - 20);

                batch.end();

            }
        });

        // Button der Stage hinzufügen, damit er gezeichnet und aktualisiert wird
        stage.addActor(button);

    }

    // Render-Schleife
    @Override
    public void render() {
        // Wenn das Spiel vorbei ist, keine Updates mehr durchführen
        if (gameState == false) {
            return;
        }
        handleInput();

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        // **********************************************
        // NEU: Levelwechsel-Logik
        // **********************************************
        if (currentLevel == 1 && player.getPoints() >= pointsForLevel2) {
            currentLevel = 2;
            // Gegner-Parameter für Level 2 setzen (mehr Spawns)
            enemy.setSpawnInterval(0.5f); // Verkürze das Spawn-Intervall (mehr Gegner)
            enemy.setFallSpeed(250f);     // Schnellere Gegner
            levelChangeTimer = LEVEL_CHANGE_DURATION;
        }

        // Levelwechsel-Pause behandeln
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

        // Prüfe ob das Spiel gewonnen wurde
        if (player.getPoints() >= pointsForWin) {
            gameState = false;

            // 'Gewonnen' Hintergrundfarbe
            Gdx.gl.glClearColor(0f, 1f, 0f, 1); // Grün für Gewonnen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            camera.update();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            // Große Anzeige wenn Spielgewonnen
            font.getData().setScale(3.0f); // Größere Schrift
            font.draw(batch, "Gewonnen", camera.viewportWidth / 2 - 90, camera.viewportHeight / 2);
            font.getData().setScale(2.0f); // Zurück zur normalen Größe
            batch.end();

            // Stage aktualisieren: verarbeitet Input und Animationen (deltaTime sorgt für zeitbasiertes Verhalten)
            stage.act(Gdx.graphics.getDeltaTime());
            // Stage zeichnen: alle Actors (inkl. Button) werden gerendert
            stage.draw();

            return; // Breche die normale Render- und Update-Logik ab 
        }

        // Eingaben verarbeiten
        handleInput();

        // Coins: spawn, bewegen, entfernen
        coins.update(delta);
        // Prüfe, ob der Spieler Coins eingesammelt hat
        coins.collectCollisions(player);

        // Enemys: eigene Spawn-/Update-Logik und Kollisionen
        enemy.update(delta);

        // Prüfe, ob der Spieler mit einem Enemy kollidiert (Punktabzug)
        if (enemy.badCollisions(player) == true) {
            gameState = false;

            // 'Game Over' Hintergrundfarbe
            Gdx.gl.glClearColor(0.8f, 0.3f, 0.3f, 1); // Hellrot für 'Game Over'
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            camera.update();
            batch.setProjectionMatrix(camera.combined);
            batch.begin();
            // Große Anzeige des Levelwechsels
            font.getData().setScale(3.0f); // Größere Schrift
            font.draw(batch, "Game Over", camera.viewportWidth / 2 - 110, camera.viewportHeight / 2);
            font.getData().setScale(2.0f); // Zurück zur normalen Größe
            batch.end();

            // Stage aktualisieren: verarbeitet Input und Animationen (deltaTime sorgt für zeitbasiertes Verhalten)
            stage.act(Gdx.graphics.getDeltaTime());
            // Stage zeichnen: alle Actors (inkl. Button) werden gerendert
            stage.draw();

            return; // Breche die normale Render- und Update-Logik ab
        }

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

        // Zeichne Coins und Spieler im selben ShapeRenderer-Durchlauf
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        coins.render(shapeRenderer);
        enemy.render(shapeRenderer);
        player.render(shapeRenderer);
        shapeRenderer.end();

        // Punktestand anzeigen (oben links, Y = viewportHeight - margin)
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        float yPos = camera.viewportHeight - 20; // 20 Pixel Abstand zur Oberkante
        font.draw(batch, "Punkte: " + player.getPoints(), 10, yPos);

        // Level rechts oben
        String levelText = "Level: " + currentLevel;
        font.draw(batch, levelText, camera.viewportWidth - 125, camera.viewportHeight - 20);

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
        // Dispose aller genutzten Ressourcen
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();

        // Ressourcen freigeben: wichtig, um Speicherlecks zu vermeiden
        stage.dispose(); // gibt Stage-Ressourcen frei
        skin.dispose();  // gibt Texturen/Fonts des Skins frei
    }
}
