package com.theironyard;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.org.apache.xpath.internal.operations.Neg;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	TextureRegion down, up, right, left, stand, standLeft, upFlip, downFlip;
	boolean faceRight = true;
	Animation walkRight;
	Animation walkLeft;
	Animation walkUp;
	Animation walkDown;
	float time;


	static final int X_MAX = 800;
	static final int Y_MAX = 600;
	static final int NEG_BORDER = -35;
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	static final int MAX_VELOCITY = 200;
	static final float DECELERATION = 0.75f;
	static final float V_STOP = 10f;


	float x, y, xv, yv;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		/*pulls and stores each sprite from tiles.png
		flips necessary sprite for animation purposes*/
		Texture tiles = new Texture("tiles.png");
		TextureRegion[][] grid = TextureRegion.split(tiles, WIDTH, HEIGHT);
		down = grid[6][0];
		downFlip = new TextureRegion(down);
		downFlip.flip(true, false);
		up = grid[6][1];
		upFlip = new TextureRegion(up);
		upFlip.flip(true, false);
		stand = grid[6][2];
		standLeft = new TextureRegion(stand);
		standLeft.flip(true, false);
		right = grid[6][3];
		left = new TextureRegion(right);
		left.flip(true, false);


		walkRight = new Animation(0.2f, right, stand);
		walkLeft = new Animation(0.2f, left, standLeft);
		walkUp = new Animation(0.2f, up, upFlip);
		walkDown = new Animation(0.2f, down, downFlip);




	}

	@Override
	public void render () {
		move();

		time +=Gdx.graphics.getDeltaTime();

		Gdx.gl.glClearColor(0.3f, 0.8f, 0.4f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		TextureRegion img;


		//determines animations/sprites to use for different movement scenarios
		if (yv> V_STOP){
			img = walkUp.getKeyFrame(time, true);
		} else if (yv < -V_STOP) {
			img = walkDown.getKeyFrame(time, true);;
		} else if (xv < -V_STOP) {
			img = walkLeft.getKeyFrame(time, true);;
		} else if (xv > V_STOP) {
			img = walkRight.getKeyFrame(time, true);;
		} else {
			if (faceRight) {
				img = stand;
			} else {
				img = standLeft;
			}
		}



		/* draws character sprite while checking if walking if the map.
		if so, resets character to opposite edge*/

		batch.begin();

		if ((x >= NEG_BORDER) && (x <= X_MAX) && (y >= NEG_BORDER) && (y <= Y_MAX)) {
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);

		} else if (x > X_MAX) {
			x = NEG_BORDER;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);

		} else if (x < NEG_BORDER) {
			x = X_MAX;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);
		} else if (y > Y_MAX) {
			y = NEG_BORDER;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);

		} else if (y < NEG_BORDER) {
			y = Y_MAX;
			batch.draw(img, x, y, WIDTH*3, HEIGHT*3);
		}
		batch.end();

	}

	/*checks for button presses for each direction
	also checks for space bar presses and adds according velocity modifier
	 */
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
			faceRight = true;
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
				xv = -MAX_VELOCITY * 1.5f;
			} else {
				xv = -MAX_VELOCITY;
			}
			faceRight = false;
		}


		float delta = Gdx.graphics.getDeltaTime();
		y+= yv * delta;
		x+= xv * delta;

		yv = decelerate(yv);
		xv = decelerate(xv);



	}

	// decreases velocity once button press has ended
	public float decelerate (float velocity) {
		velocity *= DECELERATION;
		if (Math.abs(velocity)<1) {
			velocity = 0;
		}
		return velocity;
	}
}
