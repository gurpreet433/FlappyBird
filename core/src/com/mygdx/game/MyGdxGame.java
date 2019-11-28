package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;


	BitmapFont font;

	int score = 0;
	int scoringtube = 0;

	// bird
    Circle birdCircle;
    ShapeRenderer shapeRenderer;

	Texture []birds;
	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	float gravity = 1f;
	Float gap = 400f;
	int numOfTubes = 4;
	float tubeVelocity = 4;
	float[] tubeX = new float[numOfTubes];

	float maxTubeOffset;
	int gameState  = 0;

	Random randomGenerator;
	float[] tubeOffset = new float[numOfTubes];


	Rectangle[] topTubeRect, bottomTubeRect;

	float distanceBetweenTubes;
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gameOver = new Texture("gameover.jpg");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		topTubeRect = new Rectangle[numOfTubes];
		bottomTubeRect = new Rectangle[numOfTubes];

		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap/2 - 100;

		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() / 2 + 250f;



        birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight() / 2;

        for (int i = 0;  i < numOfTubes; i++){
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + i * distanceBetweenTubes + Gdx.graphics.getWidth();
			topTubeRect[i] = new Rectangle();
			bottomTubeRect[i] = new Rectangle();
		}
	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0,0,
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1){

            if (tubeX[scoringtube] < Gdx.graphics.getWidth()/2){
                score++;
                Gdx.app.log("Score", score+"");

                if (scoringtube < numOfTubes - 1)
                    scoringtube++;
                else scoringtube = 0;
            }

			if (Gdx.input.justTouched()){
				velocity = -20;
			}

			for (int i = 0;  i < numOfTubes; i++) {

				if (tubeX[i] < - topTube.getWidth()){
					tubeX[i] += numOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) *(Gdx.graphics.getHeight() - gap - 200);
				}
				else {
					tubeX[i] = tubeX[i] - tubeVelocity;
				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRect[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
												topTube.getWidth(), topTube.getHeight());

				bottomTubeRect[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
												bottomTube.getWidth(), bottomTube.getHeight());
			}

			if (birdY > 0 )
			{
				velocity += gravity;
				birdY -=velocity;
			}
			else {
			    gameState = 2;
            }
		}
		else if(gameState == 0){

            if (Gdx.input.justTouched()){
                gameState = 1;
            }
		}
		else if (gameState == 2)
        {
            batch.draw(gameOver, Gdx.graphics.getWidth() /2 - gameOver.getWidth() /2,
                    Gdx.graphics.getHeight() /2 - gameOver.getHeight() /2);

            if (Gdx.input.justTouched()){
                gameState = 1;

                birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight() / 2;

                for (int i = 0;  i < numOfTubes; i++) {
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                    tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + i * distanceBetweenTubes + Gdx.graphics.getWidth();
                    topTubeRect[i] = new Rectangle();
                    bottomTubeRect[i] = new Rectangle();

                }
                score = 0;
                scoringtube = 0;
                velocity = 0;

            }
        }

		if (flapState == 0) {
			flapState = 1;
		}
		else {
			flapState = 0;
		}

		batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth() / 2,
				birdY);

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2,
				birds[flapState].getWidth() / 2);

		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);



		for (int i = 0; i < numOfTubes; i++)
		{
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],
				//	topTube.getWidth(), topTube.getHeight());

			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
				//	bottomTube.getWidth(), bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle, topTubeRect[i]) || Intersector.overlaps(birdCircle, bottomTubeRect[i])){
				Gdx.app.log("Collision", "Yes!");
				gameState = 2;
			}
		}

      //  shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
