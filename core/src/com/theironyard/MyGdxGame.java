package com.theironyard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	TextureRegion down, up, right, left, stand;


	static final int X_MAX = 800;
	static final int Y_MAX = 600;
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int MAX_VELOCITY = 200;
	static final float DECELERATION = 0.75f;
	static final float DEFAULT = 10f;


	float x, y, xv, yv;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Texture tiles = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(tiles, WIDTH, HEIGHT);
		down = grid[6][0];
		up = grid[6][1];
		stand = grid[6][2];
		right = grid[6][3];
		left = new TextureRegion(right);
		left.flip(true, false);




	}

	@Override
	public void render () {
		move();
		Gdx.gl.glClearColor(0.3f, 0.8f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		TextureRegion img;

		if (yv>DEFAULT){
			img = up;
		} else if (yv < -DEFAULT) {
			img = down;
		} else if (xv < -DEFAULT) {
			img = left;
		} else if (xv > DEFAULT) {
			img = right;
		} else {
			img = stand;
		}



		batch.begin();

		if ((x >= 0) && (x <= X_MAX) && (y >= 0) && (y <= Y_MAX)) {
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);

		} else if (x > X_MAX) {
			x = 0;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);

		} else if (x < 0) {
			x = X_MAX;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);
		} else if (y > Y_MAX) {
			y = 0;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);

		} else if (y < 0) {
			y = Y_MAX;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);
		}
		batch.end();

	}

	public void move () {
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				yv = MAX_VELOCITY*1.5f;
			} else {
				yv = MAX_VELOCITY;
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				yv = -MAX_VELOCITY * 1.5f;
			} else {
				yv = -MAX_VELOCITY;
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				xv = MAX_VELOCITY*1.5f;
			} else {
				xv = MAX_VELOCITY;
			}
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				xv = -MAX_VELOCITY * 1.5f;
			} else {
				xv = -MAX_VELOCITY;
			}
		}


		float delta = Gdx.graphics.getDeltaTime();
		y+= yv * delta;
		x+= xv * delta;

		yv = decelerate(yv);
		xv = decelerate(xv);



	}

	public float decelerate (float velocity) {
		velocity *= DECELERATION;
		if (Math.abs(velocity)<1) {
			velocity = 0;
		}
		return velocity;
	}
}
