package model;

import java.nio.file.Paths;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Pellet class - what player shoots
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Pellet extends Actor {
	public Pellet(Game game) {
		super(game);
		width = 7;
		height = 7;
		image = new Image("resources/pellet.png");
	}
	
	@Override
	public void updateActor(double deltaTime) {	
		//test for collision with monsters
		CopyOnWriteArrayList<Actor> monsters = game.getMonsters();
		for (Actor m: monsters) {
			if (this.intersects(m) && m.getState() == State.ACTIVE) {
				m.startDeath();
				this.setState(State.DEAD);
				
				//random item drop
				double random = Math.random() * 100;
				if (random < 30) {
					Coin c = new Coin(game);
					c.setPosition(m.getX(), m.getY());
				} else if (random < 33) {
					ExtraLife e = new ExtraLife(game);
					e.setPosition(m.getX(), m.getY());
				} else if (random < 38) {
					Coffee cf = new Coffee(game);
					cf.setPosition(m.getX(), m.getY());
				}
				//play monster death sound
				Media sound = new Media(Paths.get("src/resources/kill.wav").toUri().toString()); 
				MediaPlayer mp = new MediaPlayer(sound);
				mp.play();
				break;
			}
		}
	}
}
