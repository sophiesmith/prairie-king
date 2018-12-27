package model;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Extra life class - droppable item
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class ExtraLife extends Actor {
	private double timer;
	
	public ExtraLife(Game game) {
		super(game);
		width = 32;
		height = 25;
		timer = 0;
		setImage("extralife");
	}
	
	@Override
	public void updateActor(double deltaTime) {
		//if player intersects with extra life, update lives
		if (this.intersects(game.getPlayer())) {
			setState(State.DEAD);
			game.getPlayer().setLives(game.getPlayer().getLives()+1);
			Media sound = new Media(Paths.get("src/resources/oneup.wav").toUri().toString()); 
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
