package de.hsbi.programmieren;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Player player;

    @Override
    public void create() {
        // Kamera einrichten
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1000, 1000);

        // Renderer und Schriftart initialisieren
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();

        // Spieler initialisieren
        player = new Player(480);
    }

    // Render-Schleife
    @Override
    public void render() {
        handleInput();

        // Hintergrundfarbe setzen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Spieler zeichnen
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.render(shapeRenderer);
        shapeRenderer.end();

        // Punktestand anzeigen
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Punkte: " + player.getPoints(), 10, 980);
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