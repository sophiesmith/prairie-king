package model;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Player class - character that user controls
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Player extends Actor {
	
	private double animTimer; //timer for sprite animation
	private double animSpeed; //speed of animation
	private boolean shootPressed;
	private int lives;
	private double speed; //speed of player
	private double speedTimer; //timer used for coffee power up
	private ArrayList<Image> walking, death; //two separate sprite animations
	
	public Player(Game game) {
		super(game);
		positionX = 256;
		positionY = 256;
		animTimer = 0;
		animSpeed = 0;
		width = 30;
		height = 32;
		lives = 3;
		speed = 100;
		speedTimer = 0;
		shootPressed = false;
		image = new Image("resources/player/player2.png");
		walking = new ArrayList<Image>();
		for (int i=1; i<5; i++) {
			walking.add(new Image("resources/player/player"+i+".png"));
		}
		death = new ArrayList<Image>();
		for (int i=1; i<5; i++) {
			death.add(new Image("resources/player_death/death"+i+".png"));
		}
		images = walking;
	}
	
	@Override
	public void actorInput(ArrayList<String> input) {
		//WASD - movement
		if (input.contains("A")) {
			velocityX = -speed;
		}
		else if (input.contains("D")) {
			velocityX = speed;
		} else {
			velocityX = 0;
		}
		if (input.contains("W")) {
			velocityY = -speed;
		}
		else if (input.contains("S")) {
			velocityY = speed;
		} else {
			velocityY = 0;
		}
		
		if (velocityX == 0 && velocityY == 0) {
			animSpeed = 0;
		} else {
			animSpeed = 7;
		}
		
		double velX = 0;
		double velY = 0;
		//up left right down - shooting
		if (!shootPressed) {
			if (input.contains("LEFT")) {
				velX = -150;
			} else if (input.contains("RIGHT")) {
				velX = 150;
			} else if (input.contains("UP")) {
				velY = -150;
			} else if (input.contains("DOWN")) {
				velY = 150;
			} 
			if (velX != 0 || velY != 0) {
				Pellet p = new Pellet(game);
				p.setPosition(positionX + width/2, positionY + height/2);
				p.setVelocity(velX, velY);
				shootPressed = true;
			}
		}
		//keep track of key pressed - so user can't hold down shoot
		shootPressed = input.contains("LEFT") || input.contains("RIGHT") || input.contains("UP") || input.contains("DOWN"); 
	}
	
	@Override
	public void updateActor(double deltaTime) {	
		//animation
		animTimer += animSpeed * deltaTime;
		if (animTimer > images.size()) {
			animTimer = 0.0;
			if (state == State.DYING) {
				setState(State.ACTIVE);
				images = walking;
				positionX = 256;
				positionY = 256;
				game.killAllMonsters();
				if (lives == -1) {
					game.shutdown();
				}
			}
		}
		int frame = (int)animTimer;
		image = images.get(frame);
		
		if (positionX > 512-width) {
			positionX = 512-width;
		} else if (positionX < 0) {
			positionX = 0;
		}
		if (positionY > 512-height) {
			positionY = 512-height;
		} else if (positionY < 0) {
			positionY = 0;
		}
		
		//if player got coffee, return to normal speed after 5 seconds
		if (speed > 100) {
			speedTimer += deltaTime;
			if (speedTimer > 5) {
				speed = 100;
				speedTimer = 0;
			}
		}
		
		//for wall collisions
		ArrayList<Tile> walls = game.getWalls();
		for (Actor w : walls) {
			if (this.intersects(w)) {
				fixCollision(w);
			}
		}
		
		//for monster collisions
		CopyOnWriteArrayList<Actor> monsters = game.getMonsters();
		for (Actor m : monsters) {
			//if intersect with monster, lose life and respawn
			if (this.intersects(m) && m.getState() == State.ACTIVE && state == State.ACTIVE) {
				lives--;
				game.getView().setLives(lives);
				startDeath();
				//play death sound
				Media sound = new Media(Paths.get("src/resources/death.wav").toUri().toString()); 
				MediaPlayer mp = new MediaPlayer(sound);
				mp.play();
			}
		}
	}
	
	//starts the death animation
	@Override
	public void startDeath() {
		setState(State.DYING);
		image = new Image("resources/player_death/death1.png");
		images = death;
		animTimer = 0;
		animSpeed = 10;
		velocityX = 0;
		velocityY = 0;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void setLives(int n) {
		lives = n;
		game.getView().setLives(lives);
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
