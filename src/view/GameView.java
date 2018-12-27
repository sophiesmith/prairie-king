package view;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * View that shows gameplay
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class GameView {
	private Scene scene;
	ArrayList<String> input; //list of keys pressed by user
	GraphicsContext gc;
	private Label lives, coins;
	int numCoins;
	Main main;
	
	public GameView(Main main) {
		numCoins = 0;
		this.main = main;
		makeScene();
	}
	
	public Scene getScene() {
		return scene;
	}
	
	//makes the game scene
	private void makeScene() {
		BorderPane root = new BorderPane();
		scene = new Scene(root, 600, 600);
		
		Canvas canvas = new Canvas(512,512);
		gc = canvas.getGraphicsContext2D();
		root.setCenter(canvas);
		root.setBackground(new Background(new BackgroundFill(
				Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
		
		lives = new Label();
		lives.setGraphic(new ImageView(new Image("/resources/extralife.png")));
		lives.setText("x3");
		lives.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 14));
		lives.setTextFill(Color.WHITE);
		coins = new Label();
		coins.setGraphic(new ImageView(new Image("/resources/coin.png")));
		coins.setText("x"+numCoins);
		coins.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 14));
		coins.setTextFill(Color.WHITE);
		
		Button back = new Button("Back");
		back.setFont(Font.loadFont(getClass()
                .getResourceAsStream("/resources/Early-GameBoy.ttf"), 14));
		back.setTextFill(Color.rgb(188, 51, 74));
		back.setBackground(new Background(new BackgroundFill(
				Color.rgb(242, 188, 82), CornerRadii.EMPTY, Insets.EMPTY)));
		back.setBorder(new Border(new BorderStroke(Color.rgb(188, 51, 74), 
	            BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2.5))));
		back.setOnAction(evt -> {
			main.shutdownGame();
			main.restart();
		});
		
		HBox bottom = new HBox(20);
		bottom.setAlignment(Pos.TOP_CENTER);
		bottom.setPadding(new Insets(0, 0, 10, 0));
		ImageView iv = new ImageView(new Image("/resources/instructions.png"));
		bottom.getChildren().addAll(lives, coins, iv, back);
		root.setBottom(bottom);
		
		setupKeys();
	}
	
	//gets user input
	private void setupKeys() {
		//put all keys pressed into array so multiple keys can register at once
		input = new ArrayList<String>();
		 
        scene.setOnKeyPressed(e -> {
        	String code = e.getCode().toString();
 
            if ( !input.contains(code) ) {
               input.add( code );
            }
        });
 
        scene.setOnKeyReleased(e -> {
        	String code = e.getCode().toString();
            input.remove( code );
        });
	}
	
	public ArrayList<String> getInput() {
		return input;
	}
	
	public GraphicsContext getGraphics() {
		return gc;
	}
	
	public void setLives(int n) {
		lives.setText("x"+n);
	}
	
	public void incrementCoins() {
		numCoins++;
		coins.setText("x"+numCoins);
	}
	
	public int getCoins() {
		return numCoins;
	}
}
