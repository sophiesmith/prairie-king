package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javafx.animation.AnimationTimer;
import model.Actor.State;
import view.GameView;
import view.Main;

/**
 * Game class - controls overall game logic
 * 
 * @author Sophie Smith
 * ITP 368, Fall 2018
 * Assignment XX
 * sophiesm@usc.edu
 */

public class Game {
	private boolean isRunning;
	private CopyOnWriteArrayList<Actor> actors;
	private CopyOnWriteArrayList<Actor> monsters;
	private AnimationTimer timer;
	private long ticks;
	private GameView view;
	private Player player;
	private ArrayList<Tile> walls;
	private Main main;
	int level;
	private Clip clip;
	private boolean isHardMode;
	private double seconds;
	
	public Game(boolean hardMode, GameView view, Main main) {
		isRunning = true;
		actors = new CopyOnWriteArrayList<Actor>();
		monsters = new CopyOnWriteArrayList<Actor>();
		walls = new ArrayList<Tile>();
		ticks = 0;
		this.view = view;
		this.main = main;
		this.isHardMode = hardMode;
		level = 0;
		seconds = 0;
	}
	
	//initialize game with level and actors
	public boolean initialize() {
		int row = 0;
		//read in level from file
		try (BufferedReader br = new BufferedReader(new FileReader("src/resources/level.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
				for (int col = 0; col < line.length(); col++) {
					double xPos, yPos;
					xPos = 32 * col;
					yPos = 32 * row;
					
					Tile t = new Tile(this);
					t.setPosition(xPos, yPos);
					
					if (line.charAt(col) == 'B') {
						t.setImage("bush");
						walls.add(t);
					}
					else if (line.charAt(col) == 'G') {
						double random = Math.random() * 10;
						if (random <= 1) {
							t.setImage("ground1");
						} else {
							t.setImage("ground");
						}
					}
				}
				row++;
		    }
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
		player = new Player(this);
		generateMonsters(level);
		
		//looping background music
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("src/resources/overworld.wav")));
			clip.start();
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		return true;
	}
	
	//removes all monsters from screen
	public void killAllMonsters() {
		ArrayList<Actor> copy = new ArrayList<Actor>();
		for (Actor m: monsters) {
			m.setState(State.DEAD);
			copy.add(m);
		}
		generateMonsters(level%4);
	}
	
	//generates monsters where n is how many monsters appear on one side
	public void generateMonsters(int n) {
		if (n == 0) {
			formFour(0, 0);
		} else if (n == 1) {
			formFour(0, 0);
			formFour(0, 1);
		} else if (n==2){
			formFour(0, 0);
			formFour(0, 1);
			formFour(1, 0);
		} else {
			formFour(0, 0);
			formFour(0, 1);
			formFour(1, 0);
			formFour(1, 1);
		}
	}
	
	//generates a set of 4 monsters - one on each side of the map
	public void formFour(int n, int k) {
		Monster m = new Monster(this);
		m.setPosition(-50-n*32, 240+k*34);
		Monster m2 = new Monster(this);
		m2.setPosition(240+k*34, -50-n*32);
		Monster m4 = new Monster(this);
		m4.setPosition(552+n*32, 240+k*34);
		Monster m7 = new Monster(this);
		m7.setPosition(240+k*34, 552+n*32);
	}
	
	public void addActor(Actor a) {
		actors.add(a);
	}
	
	public void addMonster(Actor a) {
		monsters.add(a);
	}
	
	//main game loop
	public void runLoop() {
		timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				if (isRunning) {
					processInput();
					updateGame(now);
					generateOutput();
				}
			}
			
		};
		timer.start();
	}
	
	//quit game
	public void shutdown() {
		timer.stop();
		isRunning = false;
		clip.stop();
		main.endGame();
	}
	
	//process user input
	private void processInput() {
		for (Actor a : actors)
		{
			a.processInput(view.getInput());
		}
	}
	
	//update game
	private void updateGame(long now) {
		double deltaTime = (now - ticks) / 1000000000.0;
		if (deltaTime < 1) {
			seconds += deltaTime;
		}
		ticks = now;
		// Update all actors
		for (Actor a : actors)
		{
			a.update(deltaTime);
		}

		// Add any dead actors to a temp vector
		ArrayList<Actor> deadActors = new ArrayList<Actor>();
		for (Actor a : actors)
		{
			if (a.getState() == State.DEAD)
			{
				deadActors.add(a);
			}
		}

		// Delete any of the dead actors
		actors.removeAll(deadActors);
		monsters.removeAll(deadActors);
		
		//if no more monsters, generate new ones
		if (monsters.isEmpty()) {
			level++;
			generateMonsters(level%4);
		} 
	}
	
	//display output
	private void generateOutput() {
		view.getGraphics().clearRect(0, 0, 512, 512);
		for (Actor a : actors)
		{
			a.render(view.getGraphics());
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public CopyOnWriteArrayList<Actor> getMonsters() {
		return monsters;
	}
	
	public ArrayList<Tile> getWalls() {
		return walls;
	}
	
	public GameView getView() {
		return view;
	}
	
	public boolean getDifficulty() {
		return isHardMode;
	}
	
	public double getSeconds() {
		return seconds;
	}
}
