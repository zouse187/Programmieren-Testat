package de.hsbi.programmieren.testat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter {

    System.out.println("Main gestartet");
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private float playerX = 100;
    private float playerY = 100;
    private int points = 0;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
    }

    @Override
    public void render() {
        handleInput();

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // Spielfigur zeichnen
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);
        shapeRenderer.rect(playerX, playerY, 40, 40);
        shapeRenderer.end();

        // Punktestand anzeigen
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "Punkte: " + points, 10, 580);
        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            playerX -= 3;
            points++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            playerX += 3;
            points++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            playerY += 3;
            points++;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            playerY -= 3;
            points++;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}