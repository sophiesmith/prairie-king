package model;

import java.util.ArrayList;

import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Parent class for all objects in the game
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Actor {
	protected Game game; //game object
	protected State state; //state of the actor
	protected Image image; //image that represents the actor
	protected double positionX; //x-coordinate of actor's position
	protected double positionY; //y-coordinate of actor's position
	protected double velocityX; //x value of actor's velocity
	protected double velocityY; //y value of actor's velocity
	protected double width; //width of actor's image
	protected double height; //height of actor's image
	protected ArrayList<Image> images; //array of images for animated actors
	
	//State of the actor. allows for state pattern
	enum State {
		ACTIVE, DYING, DEAD;
	}
	
	public Actor(Game game) {
		this.game = game;
		this.state = State.ACTIVE;
		positionX = 0;
		positionY = 0;
		velocityX = 0;
		velocityY = 0;
		game.addActor(this);
	}
	
	//update actor
	public void update(double deltaTime) {
		if (state != State.DEAD) {
			if (deltaTime < 1) {
				positionX += velocityX * deltaTime;
	        	positionY += velocityY * deltaTime;
				updateActor(deltaTime);
			}
		}
	}
	
	//to be overridden by children - template method pattern
	public void updateActor(double deltaTime) {	}
	
	//process user input
	public void processInput(ArrayList<String> input) {
		if (state == State.ACTIVE) {
			actorInput(input);
		}
	}
	
	//to be overridden by children - template method pattern
	public void actorInput(ArrayList<String> input) {}
	
	//render actor to the canvas
	public void render(GraphicsContext gc)
    {
        gc.drawImage( image, positionX, positionY );
    }
	
	//get boundaries of the actor
    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }
    
    //tests for intersection with another actor
    public boolean intersects(Actor a)
    {
        return a.getBoundary().intersects( this.getBoundary() );
    }
    
    //fixes positions so two actors don't overlap
    public void fixCollision(Actor m) {
    	double dx1 = m.getX() - (positionX+width);
		double dx2 = m.getX()+m.getWidth() - positionX;
		double dy2 = m.getY()+m.getHeight() - positionY;
		double dy1 = m.getY() - (positionY+height);
		double dx = Math.min(Math.abs(dx1), Math.abs(dx2));
		double dy = Math.min(Math.abs(dy1), Math.abs(dy2));
		double newX = positionX;
		double newY = positionY;

		//collide with left or right
		if (dx < dy) {
			if (Math.abs(dx1) < Math.abs(dx2)) {
				newX = positionX + dx1;
			}
			else {
				newX = positionX + dx2;
			}
		}
		//collide with top or bottom
		else {
			if (Math.abs(dy1) < Math.abs(dy2)) {
				newY = positionY + dy1;
			}
			else {
				newY = positionY+ dy2;
			}
		}
		positionX = newX;
		positionY = newY;
    }
    
    //to be overridden
    public void startDeath() {}
    
    public void setPosition(double x, double y) {
		positionX = x;
		positionY = y;
	}
	
	public void setVelocity(double x, double y) {
		velocityX = x;
		velocityY = y;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void setImage(String img) {
		image = new Image("resources/"+img+".png");
	}
	
	public double getX() {
		return positionX;
	}
	
	public double getY() {
		return positionY;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public State getState() {
		return state;
	}
	
	
	
}
