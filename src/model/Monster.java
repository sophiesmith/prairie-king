package model;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.image.Image;

/**
 * Monster class - the AI characters
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Monster extends Actor {
	private double animTimer; //timer for sprite animation
	private double animSpeed; //speed of animation
	private double offScreenTimer; //timer for time spent off screen
	private double speed; //speed of monster
	
	public Monster(Game game) {
		super(game);
		width = 32;
		height = 34;
		animTimer = 0;
		animSpeed = 7;
		offScreenTimer = 0;
		if (game.getDifficulty()) {
			speed = 75;
		} else {
			speed = 50;
		}
		image = new Image("resources/orc.png");
		images = new ArrayList<Image>();
		images.add(new Image("resources/orc.png"));
		images.add(new Image("resources/orc1.png"));
		game.addMonster(this);
	}
	
	@Override
	public void actorInput(ArrayList<String> input) {
		if (game.getPlayer().getState() == State.ACTIVE) {
			//move toward the player
			if (game.getPlayer().getX() < positionX) {
				velocityX = -speed;
			} else if (game.getPlayer().getX() > positionX){
				velocityX = speed;
			} else {
				velocityX = 0;
			}
			if (game.getPlayer().getY() < positionY) {
				velocityY= -speed;
			} else if (game.getPlayer().getY() > positionY){
				velocityY = speed;
			} else {
				velocityY = 0;
			}
		}
	}
	
	@Override
	public void updateActor(double deltaTime) {
		//animation
		animTimer += animSpeed * deltaTime;
		if (animTimer > images.size()) {
			animTimer = 0.0;
			if (state == State.DYING) {
				setState(State.DEAD);
				animTimer = 5;
			}
		}
		int frame = (int)animTimer;
		image = images.get(frame);
		
		//make sure monsters don't overlap with each other
		CopyOnWriteArrayList<Actor> monsters = game.getMonsters();
		for (Actor m : monsters) {
			if (this.intersects(m) && this != m && m.getState() == State.ACTIVE) {
				fixCollision(m);
			}
		}
		
		//detect wall collision
		ArrayList<Tile> walls = game.getWalls();
		for (Actor w : walls) {
			if (this.intersects(w)) {
				fixCollision(w);
			}
		}
		
		//if a monster gets trapped off screen for more than 5 seconds, kill it
		if (positionX < 0 || positionX >= 512 || positionY < 0 || positionY >= 512) {
			offScreenTimer += deltaTime;
			if (offScreenTimer > 5) {
				this.setState(State.DEAD);
			}
		}
		
	}
	
	//starts the death animation
	@Override
	public void startDeath() {
		setState(State.DYING);
		image = new Image("resources/death/death1.png");
		images = new ArrayList<Image>();
		for (int i=1; i<7; i++) {
			images.add(new Image("resources/death/death"+i+".png"));
		}
		animTimer = 0;
		animSpeed = 10;
		velocityX = 0;
		velocityY = 0;
	}
	

}
