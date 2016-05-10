// PvZ.java
// David Wang & Wendy Xu

// Plants Vs. Zombies is a tower defense game in which the user must plant plants on their front lawn
// in order to protect again zombies. With 18 different plants and 7 different zombies, the user must 
// come up with a strategy in order to defeat the zombies successfully and successfully keep their 
// brains from being eaten.
// This is the plants vs zombies game main class. It runs everything that the game needs to function.

import java.util.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.applet.*;
import javax.sound.sampled.AudioSystem;

public class PvZ extends JFrame implements ActionListener{
	GamePanel gamepanel;
	MainMenu menu;
	boolean sound, music,mplaying;
	AudioClip playingmusic;

	javax.swing.Timer myTimer;
	
	public PvZ(){
		super("Plants Vs. Zombies");
		setLayout(null);
		setSize(818,647);
		menu = new MainMenu(this);
		
		gamepanel = new GamePanel(this);
		gamepanel.setSize(818,647);
		gamepanel.setLocation(0,0);
		add(gamepanel);
		setSize(818,647);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myTimer = new javax.swing.Timer(10,this);
		
		mplaying = false;
		playingmusic = Applet.newAudioClip(getClass().getResource("MP3/Music/InGame.wav"));		// music while playing game
	}
	
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		gamepanel.play();
		music = menu.getMusic();		// music & sound must be held constand throughout the entire game and menues,
		sound = menu.getSound();		// must constantly be checking if the user changed them through menu using options
	}
	
	public void startTimer(){
		myTimer.start();
	}
	
	public void setMenu(){
		menu.setVisible(true);
		menu.playMusic();
	}
	
	public MainMenu getMenu(){
		return menu;
	}
	
	public GamePanel getPanel(){
		return gamepanel;
	}
	
	public boolean getMusic(){
		return music;
	}
	
	public boolean getSound(){
		return sound;
	}
	
	public void setMusic(boolean music){
		menu.setMusic(music);
		this.music = music;
	}
	
	public void setSound(boolean sound){
		menu.setSound(sound);
		this.sound = sound;
	}
	
	public static void main(String[] args){
		PvZ PlantsVsZombies = new PvZ();
	}
}


class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener{
	private PvZ pvz;
	private GameMap game;
	private Shooter player;		// in this game the user is a peashooter and can shooter bullets too
	private boolean clicked,lose,gamestarted,loaded,paused,win,inalmanac,scoreAdded;
	private int score;
	String mode = "levels";
	private int[] pos = new int[]{0,0};
	private boolean[] keys;
	private Rectangle2D startgame = new Rectangle2D.Double(700,500,50,50); 
	
	// rectangles for pause menu
	private Rectangle pauseBackToGame = new Rectangle(245,458,330,76); 
	private Rectangle pauseAlmanacRect = new Rectangle(308,309,201,37);
	private Rectangle pauseRestartRect = new Rectangle(308,351,201,37);
	private Rectangle pauseMainRect = new Rectangle(308,393,201,37); 
	private Rectangle musicCheckBox = new Rectangle(480,275,34,30);
	private Rectangle soundFXCheck = new Rectangle(480,244,34,30);
		
	// rectangles for levels mode end game
	private Rectangle levelsLoseWinMenu = new Rectangle(427,550,151,34);
	private Rectangle levelsLoseWinAgain = new Rectangle(224,550,151,34);
	
	// rectangles for survival mode end game
	private Rectangle survivalLoseWinAgain = new Rectangle(127,550,151,34);
	private Rectangle survivalLoseWinMenu = new Rectangle(330,550,151,34);
	private Rectangle survivalLoseWinScore = new Rectangle(533,550,151,34);
	
	
	// variables for levels & survival
	private int survivalwave = 0, survivalwavecount = 0, level;
	
	
	// sound variables
	private boolean music,sound,mplaying;		// mplaying checks if music is already playing
	private AudioClip playingmusic,losemusic,pickingmusic;
	
	
	public void mouseClicked(MouseEvent evt){
      	clicked = true;
	}
	public void mouseExited(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){
      	clicked = true;
	}
	public void mousePressed(MouseEvent evt){
		clicked = false;
		System.out.println(evt.getX()+" "+evt.getY());
	}
	
	public void mouseMoved(MouseEvent evt){
		pos = new int[]{evt.getX(),evt.getY()};
	}	
	public void mouseDragged(MouseEvent evt){
		pos = new int[]{evt.getX(),evt.getY()};
	}
	
	public void keyPressed(KeyEvent evt){
		int i = evt.getKeyCode();
		keys[i] = true;
	}
	public void keyTyped(KeyEvent evt){
	}
	public void keyReleased(KeyEvent evt){
		int i = evt.getKeyCode();
		keys[i] = false;
	}
	
	
	public GamePanel(PvZ pvz){
		super();
		setFocusable(true);
		grabFocus();
		game = new GameMap();
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		keys = new boolean[2000];
		this.pvz = pvz;
		player = game.getPlayer();
		clicked = false;
		lose = false;
		win = false;
		loaded = false;
		gamestarted = false;
		paused = false;
		inalmanac = false;
		scoreAdded = false;
		
		playingmusic = Applet.newAudioClip(getClass().getResource("MP3/Music/InGame.wav"));
		losemusic = Applet.newAudioClip(getClass().getResource("MP3/Sound FX/GameOver.wav"));
		pickingmusic = Applet.newAudioClip(getClass().getResource("MP3/Music/ChoosingPlants.wav"));
		mplaying = false;
	}
	
	public void gameStart(){
		gamestarted = true;
	}
	
	public GameMap getMap(){
		return game;
	}
	
	public void pause(){
		paused = true;
	}
	
	public String getMode(){
		return mode;
	}
	public void setMode(String mode){
		this.mode = mode;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void setInAlmanac(boolean inalmanac){
		this.inalmanac = inalmanac;
	}
	
	public void setMusicLoop(){
		playingmusic.loop();
		mplaying = true;
	}
	
	public void resetEverything(){
		// resets the game stats for starting a new game
		survivalwave = 0;
		survivalwavecount = 0;
		score = 0;
		lose = false;
		loaded = false;
		gamestarted = false;
		paused = false;
		win = false;
		scoreAdded = false; 
		game.resetEverything();
	}

	
	public void play(){
		// plays the game as well as pauses, and ends the game
		music = pvz.getMusic();
		sound = pvz.getSound();
		game.setMusic(music);
		game.setSound(sound);

		if(gamestarted){
			
			// manages the playing of the sounds here
			if(music && inalmanac && !mplaying){
				pickingmusic.loop();
				mplaying = true;
			}
			if (music && !inalmanac){
				pickingmusic.stop();
				if (!mplaying){
					playingmusic.loop();
				}				
			}
			if(music && !mplaying && !lose && !inalmanac){
				playingmusic.loop();
				mplaying = true;
			}
			if(!music){
				playingmusic.stop();
				pickingmusic.stop();
				mplaying = false;
			}
			
			if(!lose && !win){
				// if the player hasn't won or lost, game is being played
				if (!paused){
					if(keys[KeyEvent.VK_W]){		// move player up
						player.move(Shooter.UP);
						keys[KeyEvent.VK_W] = false;
					}
					if(keys[KeyEvent.VK_S]){		// move player down
						player.move(Shooter.DOWN);
						keys[KeyEvent.VK_S] = false;
					}
					if(keys[KeyEvent.VK_SPACE]){	// player shoots peas
						game.playerShoot();
						keys[87] = false;
					}
					if(keys[KeyEvent.VK_ESCAPE] || keys[KeyEvent.VK_P]){	// pauses game
						paused = true;
						game.setPaused(true);
						keys[KeyEvent.VK_ESCAPE] = false;

					}
					
					// all functions needed for game to be played 
					game.makeSun();
					game.moveSun();
					game.rechargePlantCards();
					game.actionCount();
					game.plantsAction();
					game.moveZombies();
					game.checkSquash();
					game.checkDancing();
					game.checkFirePeas();
					game.moveBullets();
					game.checkBulletCollisions();
					game.checkZombieCollisions();
					game.checkDeaths();
					game.setPics();
					
					if(clicked){
						game.clickPlant(pos[0],pos[1]);		// checks if seed packet was clicked
						game.clickShovel(pos[0],pos[1]);	// checks if shoverl was clicked
						
						if (pos[1] > 87 || pos[0] > 454){		// plants plants or removes plants with shovel
							game.plantPlant(pos[0],pos[1]);
							game.removePlant(pos[0],pos[1]);
						}
						game.addSun(pos);			// checks if user clicked on sun
						clicked = false;
					}
					
					// different game modes
					if(mode.equals("survival")){
						game.loadZombiesSurvival(survivalwave);
						if(survivalwavecount > 6000){		// if 60 seconds passed, start a new wave of zombies
							int random = (int)(Math.random()*5);
							if(random == 0){
								survivalwave ++;
								survivalwavecount = 0;
							}	
						}
						survivalwavecount ++;
					}
					
					if(mode.equals("levels")){
						if(!loaded){
							game.loadZombies(level);
							loaded = true;
						}
						game.playLevel(level);
						
						// checks to see if the user has won
						if(game.checkWin()){
							win = true;
							game.setWin(true);
						}					
					}
					
					// checks to see if the user has lost
					if(game.checkLose()){
						lose = true;
						game.setLose(true);
						playingmusic.stop();
						if(sound){
							losemusic.play();
						}
					}		
				}
				
				if(paused){
					// user can enter the almanac, exit the game, or restart the game from the pause menu
					
					if(keys[KeyEvent.VK_ESCAPE] || keys[KeyEvent.VK_P]){		// unpauses game
						paused = false;
						game.setPaused(false);
						keys[KeyEvent.VK_ESCAPE] = false;
					}
					
					if(clicked){
						if(pauseBackToGame.contains(pos[0],pos[1])){		// unpauses game
							paused = false;
							game.setPaused(false);
						}
						if(pauseAlmanacRect.contains(pos[0],pos[1])){		// view almanac
							mplaying = false;
							playingmusic.stop();
							inalmanac = true;
							AlmanacMenu newalmanac = new AlmanacMenu(pvz.getMenu(),"game",pvz);
							pvz.dispose();
						}
						
						if(pauseRestartRect.contains(pos[0],pos[1])){		// restart game
							if(mode.equals("levels")){
								mplaying = false;
								playingmusic.stop();
								pvz.getMenu().adventure();
								pvz.dispose();
								resetEverything();
							}
							else{
								mplaying = false;
								playingmusic.stop();
								pvz.getMenu().survival();
								pvz.dispose();
								resetEverything();
							}
						}
						
						if(pauseMainRect.contains(pos[0],pos[1])){			// main menu
							mplaying = false;
							pvz.getMenu().getMenuPanel().setInGame(false);
							playingmusic.stop();
							pvz.setMenu();
							pvz.dispose();
							resetEverything();
						}
						
						if(musicCheckBox.contains(pos[0],pos[1])){		// changing music 
							if (!music){
								pvz.setMusic(true);
								music = true;
								game.setMusic(music);
								clicked = false;
							}
							
							else if(music){
								pvz.setMusic(false);
								music = false;
								mplaying = false;
								game.setMusic(music);
								clicked = false;
							}
						}
						
						if(soundFXCheck.contains(pos[0],pos[1])){		// changing sound effects
							if(!sound){
								pvz.setSound(true);
								sound = true;
								game.setSound(sound);
								clicked = false;
							}
							
							else if(sound){
								pvz.setSound(false);
								sound = false;
								game.setSound(sound);
								clicked = false;
							}
						}
						clicked = false;
					}
				}
			}
			if(lose || win){
				playingmusic.stop();
				mplaying = false;
				if(clicked){
					if(mode.equals("levels")){
						// if the user has lost, they can choose to play
						// again or exit to menu
						if(levelsLoseWinMenu.contains(pos[0],pos[1])){		// go back to main menu
							losemusic.stop();
							mplaying = false;
							pvz.setMenu();
							pvz.dispose();
							resetEverything();
						}
						if(levelsLoseWinAgain.contains(pos[0],pos[1])){		// play again
							losemusic.stop();
							mplaying = false;
							pvz.getMenu().adventure();
							pvz.dispose();
							resetEverything();
						}
					}
					
					if(mode.equals("survival")){
						score = game.getScore();
						
						if(!scoreAdded){			// adds score to list of scores in highscores.txt
							ArrayList<Integer> scores = new ArrayList<Integer>();
							scores.add(score);
							scoreAdded = true;
							try{
								Scanner inFile = new Scanner(new BufferedReader(new FileReader("highscores.txt")));
								
								while(inFile.hasNextInt()){		// takes all scores saved in textfile
									scores.add(inFile.nextInt());
								}
								inFile.close();
								
								Collections.sort(scores);		// sorts highscores
								Collections.reverse(scores);	// reverses order from highest to lowest
								
								PrintWriter outFile = new PrintWriter(new BufferedWriter(new FileWriter("highscores.txt")));
								
								for(int i = 0; i < Math.min(scores.size(),50); i ++){		// rewrites new sorted highscores 
									outFile.println(scores.get(i));
								}
								outFile.close();
							}
							catch(Exception excep){
								System.out.println("Oh no! "+excep);
							}
						}

						if(survivalLoseWinAgain.contains(pos[0],pos[1])){		// play again
							losemusic.stop();
							mplaying = false;
							pvz.getMenu().survival();
							pvz.dispose();
							resetEverything();
						}
						
						if(survivalLoseWinMenu.contains(pos[0],pos[1])){		// main menu
							losemusic.stop();
							mplaying = false;
							pvz.setMenu();
							pvz.getMenu().getMenuPanel().setInGame(false);
							pvz.dispose();
							resetEverything();
						}
						
						if(survivalLoseWinScore.contains(pos[0],pos[1])){		// view high scores
							losemusic.stop();
							mplaying = false;
							pvz.getMenu().HighScore();
							pvz.getMenu().getMenuPanel().setInGame(false);
							pvz.dispose();
							resetEverything();
						}
					}
					clicked = false;
				}
			}
		}
		repaint();
	}
	
	public void paintComponent(Graphics g){
		if (gamestarted){
			game.draw(g,this,pos[0],pos[1]);
		}
	}
}

class GameMap{
	
	// game map controls & manipulates all the variables in the game through functions, it also controls the drawing
	private boolean lose,paused,win;
	private boolean music,sound;
	private Shooter player;
	private String mode;
	
	private Image backgroundPic,currentPlant,topShovel,usedShovel,mouseShovel;
	
	private Image pauseTomb,pauseMenu,pauseMusic,pauseBack,pauseAlmanac,pauseRestart;
	private Image musicPic,soundFXPic;
	
	private Image levelsLosePic,levelsLoseMenuPic,levelsLoseAgainPic;
	private Image levelsWinPic,levelsWinMenuPic,levelsWinAgainPic;
	
	private Image survivalLosePic,survivalLoseMenuPic,survivalLoseAgainPic,survivalLoseScorePic;

	private Image winScreen,winMenuPic;
	
	private int plantingPlant,sunCost;		// plant chosen to be planted on grid
	private int sunEnergy,suncounter,spawnCount;
	private int score;
	
	private Rectangle shovel = new Rectangle(454,0,70,72);
	
	// pause menu rectangles
	private Rectangle pauseBackToGame = new Rectangle(245,458,330,76); 
	private Rectangle pauseAlmanacRect = new Rectangle(308,309,201,37);
	private Rectangle pauseRestartRect = new Rectangle(308,351,201,37);
	private Rectangle pauseMainRect = new Rectangle(308,393,201,37);
	
	// rectangles for end game options in the levels game mode
	private Rectangle levelsLoseWinMenu = new Rectangle(427,550,151,34);
	private Rectangle levelsLoseWinAgain = new Rectangle(224,550,151,34);

	// rectangles for end game options in the survival game mode
	private Rectangle survivalLoseWinAgain = new Rectangle(127,550,151,34);
	private Rectangle survivalLoseWinMenu = new Rectangle(330,550,151,34);
	private Rectangle survivalLoseWinScore = new Rectangle(533,550,151,34);

	private Plant[][] plants = new Plant[5][9];														// grid of plants
	private PlantCard[] seeds = new PlantCard[7]; 													// seeds that the player will be using
	private ArrayList<ArrayList<Zombie>> zombies = new ArrayList<ArrayList<Zombie>>(); 				// zombies on the field
	private ArrayList<Zombie> zombiesWait = new ArrayList<Zombie>(); 								// zombies lined up to be deployed onto the field
	private ArrayList<ArrayList<Bullet>> plantBullets = new ArrayList<ArrayList<Bullet>>(); 		// bullets in game
	private ArrayList<Sun> suns = new ArrayList<Sun>(); 											// sun in game
	private	ArrayList<Integer> level1ZombieCome = new ArrayList<Integer>(Arrays.asList(2500,2500,2500,2000,2000,2000,1500,1500,1500,1500)); // times for zombies to come in on level 1
	private	ArrayList<Integer> level2ZombieCome = new ArrayList<Integer>(Arrays.asList(2500,2000,2500,2000,2000,1500,1500,1500,1500,1500,1000,1000,1000,1000,1000,750,750,750,750,750)); // times for zombies to come in on level 2
	
	// times for zombies to come in on level 3
	private ArrayList<Integer> level3ZombieCome = new ArrayList<Integer>(Arrays.asList(2500,200,2500,2000,2000,1500,1500,1000,1000,1000,1000,750,750,750,750,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500)); 

	private static Random random = new Random();
	private static final int[][] dimensions = new int[][]{{90,78},{80,80},{298,160},{80,80},{100,100},{90,78},{80,116},{90,78},
													  {108,72},{250,150},{1460,80},{100,48},{80,92},{120,110},{100,100},
													  {140,104},{100,100},{170,78}}; // dimensions of the plant images
	
	public GameMap(){	
		lose = false;
		win = false;
		paused = false;
		mode = "levels";
		plantingPlant = -1;
		player = new Shooter();
		backgroundPic = new ImageIcon("Images/Game/lawn.png").getImage();
		sunEnergy = 50;
		suncounter = 0;
		score = 0;
		setArrayLists();
		Plant.getImages();
		Zombie.getImages();
		
		// shovel images
		topShovel = new ImageIcon("Images/Game/Shovel.png").getImage();
		usedShovel = new ImageIcon("Images/Game/SelectedShovel.png").getImage();
		mouseShovel = new ImageIcon("Images/Game/MouseShovel.png").getImage();

		// pause menu images
		pauseTomb = new ImageIcon("Images/Game/PauseMenu.png").getImage();
		pauseMenu = new ImageIcon("Images/Game/PauseMenu - MainMenu.png").getImage();
		pauseMusic = new ImageIcon("Images/Game/PauseMenu - Music.png").getImage();
		pauseBack = new ImageIcon("Images/Game/PauseMenu - Resume.png").getImage();
		pauseAlmanac = new ImageIcon("Images/Game/PauseMenu - Almanac.png").getImage();
		pauseRestart = new ImageIcon("Images/Game/PauseMenu - Restart.png").getImage();
		musicPic = new ImageIcon("Images/Game/PauseMenu - Music.png").getImage();
		soundFXPic = new ImageIcon("Images/Game/PauseMenu - SoundFX.png").getImage();
		
		// images for losing when playing the levels mode
		levelsLosePic = new ImageIcon("Images/Game/Levels/Lose.png").getImage();
		levelsLoseMenuPic = new ImageIcon("Images/Game/Levels/LoseMainMenu.png").getImage();
		levelsLoseAgainPic = new ImageIcon("Images/Game/Levels/LosePlayAgain.png").getImage();	
		
		// images for winning when playing the levels mode
		levelsWinPic = new ImageIcon("Images/Game/Levels/Win.png").getImage();
		levelsWinMenuPic = new ImageIcon("Images/Game/Levels/WinMainMenu.png").getImage();
		levelsWinAgainPic = new ImageIcon("Images/Game/Levels/WinPlayAgain.png").getImage();
		
		//images for losing when playing survival mode
		survivalLosePic = new ImageIcon("Images/Game/Survival/Lose.png").getImage();
		survivalLoseMenuPic = new ImageIcon("Images/Game/Survival/LoseMainMenu.png").getImage();
		survivalLoseAgainPic = new ImageIcon("Images/Game/Survival/LosePlayAgain.png").getImage();
		survivalLoseScorePic = new ImageIcon("Images/Game/Survival/LoseHighscores.png").getImage();
		
		suns.add(new Sun(new double[]{500,120},230));		// there is always a single sun at the start of a game
	}
	
	public void setLose(boolean lose){
		this.lose = lose;
	}
	
	public boolean getLose(){
		return lose;
	}
	
	public void setWin(boolean win){
		this.win = win;
	}
	
	public void setPaused(boolean paused){
		this.paused = paused;
	}
	
	public void setMode(String mode){
		this.mode = mode;
	}
	
	public void setMusic(boolean music){
		this.music = music;
	}
	
	public void setSound(boolean sound){
		this.sound = sound;
	}
	
	public int getScore(){
		return score;
	}
	
	public void resetEverything(){
		//resets the game and changes all the variables to their defaults
		lose = false;
		win = false;
		paused = false;
		plantingPlant = -1;

		sunEnergy = 50;
		suncounter = 0;
		spawnCount = 0;
		score = 0;
		plants = new Plant[5][9];
		seeds = new PlantCard[7];
		zombies = new ArrayList<ArrayList<Zombie>>();
		zombiesWait = new ArrayList<Zombie>();
		plantBullets = new ArrayList<ArrayList<Bullet>>();
		suns = new ArrayList<Sun>();
		suns.add(new Sun(new double[]{500,120},230));
		setArrayLists();	
		level1ZombieCome = new ArrayList<Integer>(Arrays.asList(2500,2500,2500,2000,2000,2000,1500,1500,1500,1500));
		level2ZombieCome = new ArrayList<Integer>(Arrays.asList(2500,2500,2500,2500,2000,2000,2000,2000,2000,2000,2000,2000,2000,1500,2000,2500,2000,1500,1000,1000));
		level3ZombieCome = new ArrayList<Integer>(Arrays.asList(2500,200,2500,2000,2000,1500,1500,1000,1000,1000,1000,750,750,750,750,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,750,750,750,750,750,500,500,500,500,500));
	}
	
	public void setArrayLists(){
		//sets up all the arraylists
		for(int i = 0; i < 5; i ++){
			ArrayList<Zombie> tmpZ = new ArrayList<Zombie>();
			ArrayList<Bullet> tmpB = new ArrayList<Bullet>();
			zombies.add(tmpZ);
			plantBullets.add(tmpB);
		}
	}
	
	public void setSeeds(PlantCard[] seeds){
		this.seeds = seeds;
	}

	public void addPlant(int[] pos, int plant){		// plant a plant onto the grid
		plants[pos[1]][pos[0]] = new Plant(plant,pos);
		for (int i=0; i<seeds.length; i++){
			if (seeds[i] != null){
				if (seeds[i].getType() == plantingPlant){
					seeds[i].setActive(false);
				}
			}
		}
	}
	
	public void rechargePlantCards(){
		// recharges plantcards that have been used
		// goes through the entire list of seeds, if they are unactive, check to see
		// if they are ready to become active, if so activate them again
		for(int i=0; i<seeds.length; i++){
			if(seeds[i] != null){
				if(!seeds[i].getActive()){
					if(seeds[i].getCharge() >= seeds[i].getRecharge()){
						seeds[i].setActive(true);
						seeds[i].setCharge(0);
					}
					else{
						seeds[i].addCharge();
					}
				}
			}
		}
	}
	
	public void removePlant(int[] pos){		// player uses shovel to get rid of plant on grid
		plants[pos[1]][pos[0]] = null;
	}
	
	public void makeSun(){
		// makes a sun on the map every 10 seconds
		if(suncounter == 1000){
			int pos = random.nextInt(500)+100;
			int land = random.nextInt(300)+230;
			suns.add(new Sun(new double[]{pos,120},land));
			suncounter = 0;
		}
		else{
			suncounter ++;
		}
	}
	
	public void moveSun(){
		// moves sun down the screen
			for(int i = suns.size()-1; i > -1; i--){
			suns.get(i).move();
			if(suns.get(i).timeUp()){		// if user doesn't pick up the sun, it eventually disappears
				suns.remove(i);
			}
		}
	}

	public void addSun(int[] pos){	
		// adds sun to sunEnergy	
		for(int i = suns.size()-1; i > -1; i--){
			if(suns.get(i).checkCollision(pos)){
				sunEnergy += 25;
				suns.remove(i);
				break;
			}
		}
	}
	
	public void plantsAction(){	
		// controls the actions of all the plants in the game
		// peashooters are programmed to shoot, sunflowers make sun,
		// cherrybombs explode, etc.
		for(int i =0; i <5; i ++){
			for(int j = 0; j < 9; j++){
				if(plants[i][j] != null){
					if(plants[i][j].isReady()){
						int[] ppos = plants[i][j].getPos();
						
						if(plants[i][j].getType() == Plant.PEASHOOTER || plants[i][j].getType() == Plant.REPEATER || plants[i][j].getType() == Plant.GATLINGPEA){
							if(zombies.get(i).size() > 0){
								plantBullets.get(ppos[1]).add(new Bullet(Bullet.PEASHOOTER,new double[]{ppos[0]*80+115,ppos[1]}));	
							}
						}
						
						if(plants[i][j].getType() == Plant.SUNFLOWER || plants[i][j].getType() == Plant.TWINSUNFLOWER){
							for(int a = 0; a < plants[i][j].getSun(); a++){
								int landx = random.nextInt(61)-30;
								int landy = random.nextInt(31);
								suns.add(new Sun(new double[]{ppos[0]*80+52+landx,ppos[1]*98+110},ppos[1]*98+120+landy));
							}
						}
						
						if(plants[i][j].getType() == Plant.CHERRYBOMB && !plants[i][j].getExplode()){
							for(int a = i+1; a > i-2; a--){			// damages zombies around it
								if(a > -1 && a < 5){
									if(zombies.get(a).size() > 0){
										for(int b = zombies.get(a).size()-1; b >-1; b--){
											double[] zpos = zombies.get(a).get(b).getPos();
											if(zpos[0] < (ppos[0]+2)*80+35 && zpos[0] > (ppos[0]-2)*80+35){
												zombies.get(a).get(b).burnt(plants[i][j].getDamage());
											}
										}
									}
								}
							}
						}
						
						if(plants[i][j].getType() == Plant.JALAPENO && !plants[i][j].getExplode()){		// damages zombies in same row
							for(int a = zombies.get(i).size()-1; a >-1; a--){
								zombies.get(i).get(a).burnt(plants[i][j].getDamage());
							}
						}
				
						if(plants[i][j].getType() == Plant.SNOWPEA){
							if(zombies.get(i).size() > 0){
								plantBullets.get(ppos[1]).add(new Bullet(Bullet.SNOWPEA,new double[]{ppos[0]*80+115,ppos[1]}));	
							}
						}
						
						if(plants[i][j].getType() == Plant.PIKAPEA){
							if(zombies.get(i).size() > 0){
								plantBullets.get(ppos[1]).add(new Bullet(Bullet.PIKAPEA,new double[]{ppos[0]*80+115,ppos[1]}));	
							}
						}
						
						if(plants[i][j].getType() == Plant.FIREPEA){
							if(zombies.get(i).size() > 0){
								plantBullets.get(ppos[1]).add(new Bullet(Bullet.FIREPEA,new double[]{ppos[0]*80+115,ppos[1]}));	
							}
						}
						
						if(plants[i][j].getType() == Plant.THREEPEATER){
							//for the threepeater if there is a zombie on the lane or on any of the two lanes adjacent to it,
							//it will fire one pea down each lane
							boolean shoot = false;
							for(int a = i+1; a > i-2; a--){			// checks lanes
								if(a > -1 && a < 5 && zombies.get(a).size() > 0){
									for(int b = zombies.get(a).size()-1; b > -1; b--){
										double[] zpos = zombies.get(a).get(b).getPos();
										if(zpos[0] >= ppos[0]*80+115){
											shoot = true;
										}
									}
								}
							}
							if(shoot){			// if there is even one zombie, shoots down all 3 rows
								for(int a = i+1; a > i-2; a--){
									if(a > -1 && a < 5){
										plantBullets.get(a).add(new Bullet(Bullet.PEASHOOTER,new double[]{ppos[0]*80+115,a}));	
									}
								}
							}
						}
						
						if(plants[i][j].getType() == Plant.BONKCHOY){
							int[] pos = plants[i][j].getPos();
							for(Zombie zombie:zombies.get(i)){
								for(int a = (pos[0]-1)*80+75; a < (pos[0]+1)*80+75; a++){
									if(zombie.checkCollision(new double[]{a,pos[1]*98+129})){		// checks from left to right, zombies to left of bonk 
																									// choy have priority
										zombie.getHit(plants[i][j].getDamage(),Plant.PEASHOOTER);	// treated as normal damage, like peashooter
										if(a < pos[0]*80+75){
											plants[i][j].punch("left");	
											break;
										}
										else if(a >= pos[0]*80+75){
											plants[i][j].punch("right");
											break;
										}
									}
								}
							}
						}					
					}
				}
			}
		}
	}
	
	public void actionCount(){		// counter for when plants do their actions
		for(int i =0; i <5; i ++){
			for(int j = 0; j < 9; j++){
				if(plants[i][j] != null){
					plants[i][j].actionCounter();
				}
			}
		}
		player.actionCounter();
	}
		
	public void checkFirePeas(){ 
		// turns normal peas that pass through the torchwood
		// into fire peas and snowpeas into normal peas
		for(int i = 0; i <5; i ++){
			for(int j = 0; j < 9; j++){
				if(plants[i][j] != null && plants[i][j].getType() == Plant.TORCHWOOD){
					for(Bullet bullet:plantBullets.get(i)){
						double[] bpos = bullet.getPos();
						if(bpos[0] > j*80+35 && bpos[0] < (j+1)*80+35){
							bullet.torched(j);
						}
					}
				}
			}
		}
	}
	
	public void checkSquash(){
		// squash checks to see if there are zombies to the left or right one tile away and jumps on them and crushes them
		for(int i = 0; i <5; i ++){
			for(int j = 0; j < 9; j++){
				if(plants[i][j] != null && plants[i][j].getType() == Plant.SQUASH && !plants[i][j].getSquash()){
					for(Zombie zombie:zombies.get(i)){
						int[] ppos = plants[i][j].getPos();
						for(int a = (ppos[0]-1)*80+75; a < (ppos[0]+1)*80+75; a++){
							if(zombie.checkCollision(new double[]{a,ppos[1]*98+80})){		// checks from left to right, zombies to left have priority
								zombie.getHit(plants[i][j].getDamage(),plants[i][j].getType());
								if(a < ppos[0]*80+75){
									plants[i][j].squashed("left");	
									break;
								}
								else if(a >= ppos[0]*80+75){
									plants[i][j].squashed("right");	
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void checkDancing(){
		// makes dancing zombies spawn other dancing zombies
		for(int i = 4; i > -1; i --){
			for(int j = zombies.get(i).size()-1; j > -1; j--){
				if(zombies.get(i).get(j).getType() == Zombie.DANCING){
					System.out.println(zombies.get(i).get(j).getSpawn()+","+zombies.get(i).get(j).getSpawned());
					if(zombies.get(i).get(j).getSpawn() && !zombies.get(i).get(j).getSpawned()){		// original dancing zombie can only spawn once
						double[] zpos = zombies.get(i).get(j).getPos();

						zombies.get(i).get(j).setSpawned(true);

						if(zpos[1] - 1 > -1){
							Zombie dancing = new Zombie(Zombie.DANCING,new double[]{zpos[0],zpos[1]-1});	// dancing zombies spawned can't spawn their own
							dancing.setSpawned(true);			// set true as if it already spawned so it can't spawn anymore
							zombies.get((int)zpos[1]-1).add(dancing);
							
						}
						if(zpos[1] + 1 < 5){
							Zombie dancing = new Zombie(Zombie.DANCING,new double[]{zpos[0],zpos[1]+1});
							dancing.setSpawned(true);	
							zombies.get((int)zpos[1]+1).add(dancing);
						}
					}
					else{
						zombies.get(i).get(j).spawnCount();		// counts when original dancing zombie will spawn zombies
					}
				}
			}
		}
	}
	
	public void setPics(){
		// sets all images of everything in game
		
		player.setPic();
		
		// plant pics
		for(int i =0; i <5; i ++){
			for(int j = 0; j < 9; j++){
				if(plants[i][j] != null){
					plants[i][j].setPic();
					if(plants[i][j].getType() == Plant.POTATOMINE && plants[i][j].getExplode()){
						plants[i][j].explodedTimer();
					}
				}
			}
		}
		
		// zombie pics
		for(int i = 4; i > -1; i--){
			for(int j = zombies.get(i).size()-1; j > -1; j--){
				zombies.get(i).get(j).setPic();
			}
		}
	}
	
	public void moveZombies(){
		// move zombies down the lawn towards the house
		for(int i = 0; i < 5; i ++){
			for(int j = 0; j < zombies.get(i).size(); j++){
				if(zombies.get(i).get(j) != null){
					zombies.get(i).get(j).move();
				}
			}
		}
	}
	
	public void moveBullets(){
		// move bullets in the game
		for(int i = 4; i > -1; i--){
			for(int j = plantBullets.get(i).size()-1; j > -1; j --){
				plantBullets.get(i).get(j).move();
				if(plantBullets.get(i).get(j).getPos()[0] >= 820){
					plantBullets.get(i).remove(plantBullets.get(i).get(j));
				}
			}
		}
	}

	public void checkBulletCollisions(){
		// checks to see if the bullets collide with zombies
		for(int a = 4; a > -1; a--){
			for(int b = zombies.get(a).size()-1; b > -1; b--){
				for(int c = plantBullets.get(a).size()-1; c > -1; c --){
					Zombie zombie = zombies.get(a).get(b);
					Bullet bullet = plantBullets.get(a).get(c);
					double[] bpos = bullet.getPos();
					if(zombie.checkCollision(bpos)){
						zombie.getHit(bullet.getDamage(),bullet.getType());
						plantBullets.get(a).remove(bullet);
					}
				}
			}
		}
	}

	public void checkZombieCollisions(){
		// checks to see if the zombies collide with any plants in order to eat them
		// or plants explode or do damage
		for(int a = 4; a > -1; a--){
			for(int b = zombies.get(a).size()-1; b > -1; b--){
				Zombie zombie = zombies.get(a).get(b);
				double[] ppos = zombie.getPos();		// coordinate in front of zombie on grid
				int c = (int)ppos[1];
				int d = (int)((ppos[0]-35-40)/80);
				if(d < 9){
					if(zombie.isAlive() && plants[c][d] != null){
						
						if(plants[c][d].getType() == Plant.POTATOMINE && !plants[c][d].getExplode() && plants[c][d].isReady()){
							zombie.burnt(plants[c][d].getDamage());
							plants[c][d].explode();
						}
						
						if(plants[c][d].getType() == Plant.SPIKEWEED){
							if(plants[c][d].isReady()){
								zombie.getHit(plants[c][d].getDamage(),plants[c][d].getType());
							}
						}
						else if(!zombie.isParalyzed() && plants[c][d].getType() != Plant.CHERRYBOMB && plants[c][d].getType() != Plant.JALAPENO){
							plants[c][d].getEaten(zombie.getDamage());
							if(!zombie.isEating()){
								zombies.get(a).get(b).eat();
							}
							if(!plants[c][d].isAlive()){
								zombie.doneEating();
							}
						}
					}
					if(plants[c][d] == null && zombie.isEating()){		// zombie goes back to moving if it's done eating
						zombie.doneEating();
					}
				}
			}
		}
	}

	public void checkDeaths(){
		
		// check plant deaths
		for(int i =0; i <5; i ++){
			for(int j = 0; j < 9; j++){
				if(plants[i][j] != null && !plants[i][j].isAlive()){
					plants[i][j] = null;
				}
			}
		}
		
		// check zombie deaths
		for(int i = 4; i > -1; i--){
			for(int j = zombies.get(i).size()-1; j > -1; j--){
				if(zombies.get(i).get(j).isDestroyed()){
					score += zombies.get(i).get(j).getValue();
					zombies.get(i).remove(zombies.get(i).get(j));
				}
			}
		}
	}
	
	public void playerShoot(){
		// allows the player character to shoot at zombies
		if(player.isReady()){
			int[] ppos = player.getPos();
			plantBullets.get(ppos[1]).add(new Bullet(Bullet.SHOOTER, new double[]{ppos[0],ppos[1]}));
		}
	}
	
	
	public Shooter getPlayer(){
		return player;
	}
	
	public void clickPlant(int x,int y){
		// changes the current plant ready to be planted to the one
		// the user clicks
		for(int i=0; i<seeds.length; i++){
			if(seeds[i] != null){
				if(seeds[i].getRect().contains(x,y)){
					if(seeds[i].getActive()){
						if(seeds[i].getType() != plantingPlant){
							if(sunEnergy >= seeds[i].getCost()){
								plantingPlant = seeds[i].getType();
								sunCost =  seeds[i].getCost();
							}
						}
						else{
							plantingPlant = -1;
						}
					}				
				}
			}
		}
	}
	
	public void clickShovel(int x,int y){
		// changes the current tool to a shovel
		if(plantingPlant != -2){
			if(shovel.contains(x,y)){
				plantingPlant = -2;
			}
		}
		else{
			if(shovel.contains(x,y)){
				plantingPlant = -1;
			}
		}
	}
	
	public void plantPlant(int x, int y){
		// allows the user to plant a plant on the field
		// checks to see if it is on the field and if it is a 
		// plantable plant and not an upgrade
		// if it is an upgrade the upgrade is planted over the current plant
		int indexx = (int)(x-35)/80;
		int indexy = (int)(y-80)/98;
		if (plantingPlant > -1){		// user is planting plant
			if (indexy < 5 && indexy > -1 && indexx < 9 && indexx > -1){
				if (plants[indexy][indexx] == null){
					if (plantingPlant != Plant.GATLINGPEA && plantingPlant != Plant.TWINSUNFLOWER){
						addPlant(new int[] {indexx,indexy},plantingPlant);
						sunEnergy -= sunCost;
						plantingPlant = -1;
					}
				}
				
				// upgrade plants
				else{
					if (plantingPlant == Plant.GATLINGPEA){
						if (plants[indexy][indexx].getType() == Plant.REPEATER){
							plants[indexy][indexx].upgrade();
							sunEnergy -= sunCost;
							for (int i=0; i<seeds.length; i++){
								if (seeds[i] != null){
									if (seeds[i].getType() == plantingPlant){
										seeds[i].setActive(false);
									}
								}
							}
							plantingPlant = -1;
						}
					}
					if (plantingPlant == Plant.TWINSUNFLOWER){
						if (plants[indexy][indexx].getType() == Plant.SUNFLOWER){
							plants[indexy][indexx].upgrade();
							sunEnergy -= sunCost;
							for (int i=0; i<seeds.length; i++){
								if (seeds[i] != null){
									if (seeds[i].getType() == plantingPlant){
										seeds[i].setActive(false);
									}
								}
							}
							plantingPlant = -1;
						}
					}
				}
			}
		}
	}
	
	public void removePlant(int x, int y){
		// uses the shovel to remove a plant on the field
		int indexx = (int)(x-35)/80;
		int indexy = (int)(y-80)/98;
		if (y < 80){
			indexy = (int)(y-170)/98;
		}

		if (plantingPlant == -2){	// shovel tool
			if (indexy < 5 && indexy > -1 && indexx < 9 && indexx > -1){
				if (plants[indexy][indexx] != null){
					plants[indexy][indexx] = null;
					plantingPlant = -1;
				}
			}
		}
	}
	
	public void loadZombiesSurvival(int wave){
		// randomly spawns zombies in survival mode
		// the higher the wave, the more rapid zombies come and the higher amount of zombies
		// allowed on the field.
		// the higher the wave, the more difficult zombies are spawned
		if (wave == 0){
			int random = (int)(Math.random()*500);
			if (random == 0){
				if (countZombies() < 1){
					int randomypos = (int)(Math.random()*5);
					zombies.get(randomypos).add(new Zombie(Zombie.ZOMBIE, new  double[]{800,randomypos}));
				}
			}
		}
		
		if (wave == 1){
			int random = (int)(Math.random()*500);
			if  (random == 0){
				if (countZombies() < 3){
					int randomypos = (int)(Math.random()*5);
					int randomzombie = (int)(Math.random()*2);
					zombies.get(randomypos).add(new Zombie(randomzombie, new double[]{800,randomypos}));
				}
			}
		}
		
		if (wave == 2){
			int random = (int)(Math.random()*500);
			if (random == 0){
				if (countZombies() < 5){				
					int randomypos = (int)(Math.random()*5);
					int randomzombie = (int)(Math.random()*3);
					zombies.get(randomypos).add(new Zombie(randomzombie, new double[]{800,randomypos}));
				}
			}
		}
		
		if (wave == 3){
			int random = (int)(Math.random()*400);
			if (random == 0){
				if (countZombies() < 7){
					int randomypos = (int)(Math.random()*5);
					int randomzombie = (int)(Math.random()*4);
					zombies.get(randomypos).add(new Zombie(randomzombie, new double[]{800,randomypos}));
				}
			}
		}
		if (wave >= 4 && wave < 6){
			int random = (int)(Math.random()*400);
			if (random == 0){
				if (countZombies() < 9){
					int randomypos = (int)(Math.random()*5);
					int randomzombie = (int)(Math.random()*7);
					zombies.get(randomypos).add(new Zombie(randomzombie, new double[]{800,randomypos}));
				}
			}
		}
		
		if (wave >= 6 && wave < 8){
			int random = (int)(Math.random()*300);
			if (random == 0){
				int randomypos = (int)(Math.random()*5);
				int randomzombie = (int)(Math.random()*6+1);
				zombies.get(randomypos).add(new Zombie(randomzombie, new double[]{800,randomypos}));
			}
		}
		
		if (wave >= 8 && wave < 10){
			int random = (int)(Math.random()*300);
			if (random == 0){
				int randomypos = (int)(Math.random()*5);
				int randomzombie = (int)(Math.random()*5+2);
				zombies.get(randomypos).add(new Zombie(randomzombie, new double[]{800,randomypos}));
			}
		}
		
		if (wave >= 10){
			int random = (int)(Math.random()*200);
			if (random < 5){
				int randomypos = (int)(Math.random()*5);
				int randomzombie = (int)(Math.random()*1+5);
				zombies.get(randomypos).add(new Zombie(randomzombie,new double[]{800,randomypos}));
			}
		}
	}
	
	public int countZombies(){
		// counts the zombies on the field
		int count = 0;
		for (int i=0; i<5; i++){
			for (int j=0; j<zombies.get(i).size(); j++){
				count += 1;
			}
		}
		return count;
	}
	
	public void loadZombies(int level){
		// loads the level arrangement from a text file into
		// an arraylist
		try{
			BufferedReader inFile = new BufferedReader(new FileReader("level"+level+".txt"));
			int numZombies = Integer.parseInt(inFile.readLine());
			for (int i=0; i < numZombies; i++){
				String[]zombieInfo = inFile.readLine().split(" ");
				zombiesWait.add(new Zombie(Integer.parseInt(zombieInfo[0]),new double[]{Double.parseDouble(zombieInfo[1]),Double.parseDouble(zombieInfo[2])}));
				
			}
		}
		catch (Exception exception){
			System.out.println(exception);
		}
		
	}
	
	public void playLevel(int level){
		// plays the loaded level
		// looks through arraylists of times to spawn zombies in
		// and spawns them at that time
		if (level == 1){
			if (level1ZombieCome.size() > 0){
				if (spawnCount >= level1ZombieCome.get(0)){
					zombies.get((int)(zombiesWait.get(0).getPos()[1])).add(zombiesWait.get(0));
					zombiesWait.remove(0);	
					level1ZombieCome.remove(0);
					spawnCount = 0;				
				}
			}

		}
		
		// plays through level 2
		if (level == 2){
			if (level2ZombieCome.size() > 0){
				if (spawnCount >= level2ZombieCome.get(0)){
					zombies.get((int)(zombiesWait.get(0).getPos()[1])).add(zombiesWait.get(0));
					zombiesWait.remove(0);	
					level2ZombieCome.remove(0);		
					spawnCount = 0;
				}
			}
		}
		
		//plays through level 3
		if (level == 3){
			if (level3ZombieCome.size() > 0){
				if (spawnCount >= level3ZombieCome.get(0)){
					zombies.get((int)(zombiesWait.get(0).getPos()[1])).add(zombiesWait.get(0));
					zombiesWait.remove(0);	
					level3ZombieCome.remove(0);		
					spawnCount = 0;
				}
			}

		}
		spawnCount ++;
	}
	
	public boolean checkWin(){
		// checks to see if the user has won
		// checks to see if there are anymore zombies left
		// or any on the field
		if (zombiesWait.size() == 0){
			for (int i=0; i<5; i++){
				if (zombies.get(i).size() > 0){
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public boolean checkLose(){
		// checks to see if any of the zombies have reached the house
		// if they have the user has lost and it returns true
		for (int i=0; i<5; i++){
			for (int j=0; j<zombies.get(i).size(); j++){
				if (zombies.get(i).get(j).getPos()[0] <= 0){
					return true;
				}
			}
		}
		return false;
	}
	

	public void draw(Graphics g,JPanel panel,int x, int y){		// draws everything in game
		
		g.drawImage(backgroundPic,0,0,panel);	// draws background image
	
		for (int i=0; i<seeds.length;  i++){
			if (seeds[i]!= null){
				if (seeds[i].getCost() > sunEnergy){	// if user doesn't have enough sun, red borders the seed packet
					g.setColor(Color.RED);
					g.fillRect((int)(seeds[i].getRect().getX())-2,(int)(seeds[i].getRect().getY())-2,54,74);
				}
			}
		}
		
		// draws seeds
		for (int i=0; i<seeds.length; i++){
			if (seeds[i] != null){
				seeds[i].draw(g,panel);
			}
			
		}
		
		// draws plants 
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 9; j++){
				if(plants[i][j] != null){
					plants[i][j].draw(g,panel);
				}
			}
		}
		
		// draws zombies
		for(int i = 0; i < 5; i ++){
			for(int j = 0; j < zombies.get(i).size(); j++){
				zombies.get(i).get(j).draw(g,panel);
			}
		}
		
		// draws bullets
		for(int i = 0; i < 5; i ++){
			for(int j = 0; j < plantBullets.get(i).size(); j++){
				plantBullets.get(i).get(j).draw(g,panel);
			}
		}
		
		if (plantingPlant > -1){
			currentPlant = Plant.plantPics.get(plantingPlant).get(0);
			g.drawImage(currentPlant,x-dimensions[plantingPlant][0]/2,y-dimensions[plantingPlant][1]/2,panel);
		}
		
		g.drawImage(topShovel,454,0,panel);
		if (plantingPlant == -2){
			g.drawImage(usedShovel,454,0,panel);
			g.drawImage(mouseShovel,x-5,y-65,panel);
		}
		
		// draws sun 
		for(int i = 0; i < suns.size(); i++){
			suns.get(i).draw(g,panel);
		}
		
		// draws player
		player.draw(g,panel);
		
		// draws the sun amount
		g.setFont(new Font("Calibri", Font.PLAIN, 24));
		g.setColor(Color.BLACK);
    	g.drawString(sunEnergy+"",28,83);
		
		if (paused){
			// draws the display options and hovers
			// for the pause menu
			g.drawImage(pauseTomb,175,50,panel);
			if (!music){
				g.drawImage(pauseTomb,175,50,panel);
			}
			if (!sound){
				g.drawImage(pauseTomb,175,50,panel);
			}
			if (pauseBackToGame.contains(x,y)){
				g.drawImage(pauseBack,175,50,panel);
			}
			if (pauseAlmanacRect.contains(x,y)){
				g.drawImage(pauseAlmanac,175,50,panel);
			}
			if (pauseRestartRect.contains(x,y)){
				g.drawImage(pauseRestart,175,50,panel);
			}
			if (pauseMainRect.contains(x,y)){
				g.drawImage(pauseMenu,175,50,panel);
			}
			if (music){
				g.drawImage(musicPic,175,50,panel);
			}
			if (sound){
				g.drawImage(soundFXPic,175,50,panel);
			}
		}
		
		
		if (lose && !win){
			// draws the images and hovers for all the display options
			// for when the user loses
			if (mode.equals("levels")){
				// images for losing in levels
				g.drawImage(levelsLosePic,0,0,panel);
				if (levelsLoseWinMenu.contains(x,y)){
					g.drawImage(levelsLoseMenuPic,0,0,panel);
				}
				if (levelsLoseWinAgain.contains(x,y)){
					g.drawImage(levelsLoseAgainPic,0,0,panel);
				}
			}
			if (mode.equals("survival")){
				// images for losing in survival	
				g.drawImage(survivalLosePic,0,0,panel);
				if (survivalLoseWinAgain.contains(x,y)){
					g.drawImage(survivalLoseAgainPic,0,0,panel);
				}
				if (survivalLoseWinMenu.contains(x,y)){
					g.drawImage(survivalLoseMenuPic,0,0,panel);
				}
				if (survivalLoseWinScore.contains(x,y)){
					g.drawImage(survivalLoseScorePic,0,0,panel);
				}
			}
		}
		
		if (win && !lose){
			// draws the images and hovers for all the display options
			// for when the user wins
			if (mode.equals("levels")){
				g.drawImage(levelsWinPic,0,0,panel);
				if (levelsLoseWinMenu.contains(x,y)){
					g.drawImage(levelsWinMenuPic,0,0,panel);
				}
				if (levelsLoseWinAgain.contains(x,y)){
					g.drawImage(levelsWinAgainPic,0,0,panel);
				}
			}
		}
	}
}