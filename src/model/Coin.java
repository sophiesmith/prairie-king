package model;

import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Coin class - droppable item
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Coin extends Actor {
	private double timer;
	
	public Coin(Game game) {
		super(game);
		width = 24;
		height = 24;
		timer = 0;
		setImage("coin");
	}
	
	@Override
	public void updateActor(double deltaTime) {
		//if player intersects with coin, update coin count
		if (this.intersects(game.getPlayer())) {
			setState(State.DEAD);
			game.getView().incrementCoins();
			Media sound = new Media(Paths.get("src/resources/coin.wav").toUri().toString()); 
			MediaPlayer mp = new MediaPlayer(sound);
			mp.play();
		}
		
		//coin disappears after 5 seconds
		timer += deltaTime;
		if (timer > 5) {
			setState(State.DEAD);
		}
	}
}
