package view;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import javafx.stage.Stage;
import model.Game;

/**
 * Main controller, also contains start view
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Main extends Application {
	private Stage stage;
	private GameView gameView;
	private Scene startScene;
	private MediaPlayer bgMusic;
	private Game game;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		VBox pane = new VBox(20);
		startScene = new Scene(pane, 600, 600);
		stage.setTitle("Journey of the Prairie King");
		makeStartScene(pane);
		
		Media music = new Media(Paths.get("src/resources/intro.mp3").toUri().toString()); 
		bgMusic = new MediaPlayer(music);
		bgMusic.play();
		
		stage.setScene(startScene);
		stage.show();
	}
	
	//makes the start scene
	public void makeStartScene(VBox pane) {
		pane.setAlignment(Pos.CENTER);
		pane.setBackground(new Background(new BackgroundFill(
				Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		
		ImageView title = new ImageView(new Image("resources/title.png"));
		
		Button startButton = new Button("Normal Mode");
		startButton.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 20));
		startButton.setTextFill(Color.rgb(188, 51, 74));
		startButton.setBackground(new Background(new BackgroundFill(
				Color.rgb(242, 188, 82), CornerRadii.EMPTY, Insets.EMPTY)));
		startButton.setBorder(new Border(new BorderStroke(Color.rgb(188, 51, 74), 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2.5))));
		startButton.setOnAction(evt -> {
			startGame(false);
		});
		
		Button hardButton = new Button("Hard Mode");
		hardButton.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 20));
		hardButton.setTextFill(Color.rgb(188, 51, 74));
		hardButton.setBackground(new Background(new BackgroundFill(
				Color.rgb(242, 188, 82), CornerRadii.EMPTY, Insets.EMPTY)));
		hardButton.setBorder(new Border(new BorderStroke(Color.rgb(188, 51, 74), 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2.5))));
		hardButton.setOnAction(evt -> {
			startGame(true);
		});
		
		Label ctrl = new Label("Controls");
		ctrl.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 14));
		ctrl.setTextFill(Color.WHITE);
		ctrl.setPadding(new Insets(20, 0, 0, 0));
		ImageView instr = new ImageView(new Image("resources/instructions.png"));
		pane.getChildren().addAll(title, startButton, hardButton, ctrl, instr);
	}
	
	//starts game either in hard or normal mode
	public void startGame(boolean hardMode) {
		bgMusic.stop();
		Media sound = new Media(Paths.get("src/resources/start.wav").toUri().toString()); 
		MediaPlayer mp = new MediaPlayer(sound);
		mp.play();
		gameView = new GameView(this);
		stage.setScene(gameView.getScene());
		game = new Game(hardMode, gameView, this);
		boolean success = game.initialize();
		if (success) {
			game.runLoop();
		}
		
	}
	
	//switches to game over view
	public void endGame() {
		EndView end = new EndView(this, gameView.getCoins(), game.getSeconds());
		stage.setScene(end.getScene());
		bgMusic.play();
	}
	
	//called if player selects play again
	public void restart() {
		stage.setScene(startScene);
	}
	
	//quits game
	public void shutdownGame() {
		game.shutdown();
	}

}
