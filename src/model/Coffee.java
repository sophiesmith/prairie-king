package model;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Coffee class - droppable item
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Coffee extends Actor {
	private double timer;
	
	public Coffee(Game game) {
		super(game);
		width = 24;
		height = 24;
		setImage("coffee");
		timer = 0;
	}

	@Override
	public void updateActor(double deltaTime) {
		//if player intersects with coffee, increase player speed
		if (this.intersects(game.getPlayer())) {
			setState(State.DEAD);
			game.getPlayer().setSpeed(135);
			Media sound = new Media(Paths.get("src/resources/coffee.wav").toUri().toString()); 
			MediaPlayer mp = new MediaPlayer(sound);
			mp.play();
		}
		
		//disappears after 5 seconds
		timer += deltaTime;
		if (timer > 5) {
			setState(State.DEAD);
		}
	}
}
