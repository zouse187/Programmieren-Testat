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
        // Coins: spawn, bewegen, entfernen
        coins.update(delta);
        // Prüfe, ob der Spieler Coins eingesammelt hat
        coins.collectCollisions(player);

        // Enemys: eigene Spawn-/Update-Logik und Kollisionen
        enemy.update(delta);
        // Prüfe, ob der Spieler mit einem Enemy kollidiert (Punktabzug)
        enemy.badCollisions(player);

        // Hintergrundfarbe setzen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
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
