package view;

import java.nio.file.Paths;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * View for the game over screen
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class EndView {
	private Scene scene;
	private Main main;
	private int coins; //number of coins collected
	private double seconds; //seconds survived
	
	public EndView(Main main, int coins, double seconds) {
		this.coins = coins;
		this.seconds = seconds;
		makeScene();
		this.main = main;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	//makes the scene
	public void makeScene() {
		VBox root = new VBox(20);
		root.setAlignment(Pos.CENTER);
		root.setBackground(new Background(new BackgroundFill(
				Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		scene = new Scene(root, 600, 600);
		
		Label gameOver = new Label("Game Over!"+"\n"+"You collected: "+coins+" coins"
				+"\n"+"You survived: "+(int)seconds+" seconds");
		gameOver.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 20));
		gameOver.setTextFill(Color.WHITE);
		gameOver.setTextAlignment(TextAlignment.CENTER);
		
		Button restartButton = new Button("Play Again?");
		restartButton.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 20));
		restartButton.setTextFill(Color.rgb(188, 51, 74));
		restartButton.setBackground(new Background(new BackgroundFill(
				Color.rgb(242, 188, 82), CornerRadii.EMPTY, Insets.EMPTY)));
		restartButton.setBorder(new Border(new BorderStroke(Color.rgb(188, 51, 74), 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2.5))));
		restartButton.setOnAction(evt -> {
			Media sound = new Media(Paths.get("src/resources/start.wav").toUri().toString()); 
			MediaPlayer mp = new MediaPlayer(sound);
			mp.play();
			main.restart();
		});
		
		root.getChildren().addAll(gameOver, restartButton);
	}
}
