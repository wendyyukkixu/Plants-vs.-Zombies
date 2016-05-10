// MainMenu.java
// Wendy Xu

// Displays main menu of game
// Player can start game from here (adventure or survival),
// change settings via options, view help or quit the game.

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.applet.*;
import javax.sound.sampled.AudioSystem;

class MainMenu extends JFrame implements ActionListener{
	MenuPanel menupanel;
	HelpMenu helpmenu;
	AlmanacMenu almanac;
	HighScore highscore;
	
	AudioClip menumusic,pickingmusic;
	boolean music,sound;

	PvZ play;
	PickPlants pick;
	javax.swing.Timer myTimer;
	
	public MainMenu(PvZ play){
		super();
		this.play = play;
		setLayout(null);
		
		menupanel = new MenuPanel(this);
		menupanel.setSize(818,647);
		menupanel.setLocation(0,0);
		add(menupanel);
		
		menumusic = Applet.newAudioClip(getClass().getResource("MP3/Music/MainMenu.wav"));
		pickingmusic = Applet.newAudioClip(getClass().getResource("MP3/Music/ChoosingPlants.wav"));
		music = true;
		sound = true;
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(818,647); 
    		
    	myTimer = new javax.swing.Timer(100,this);
		myTimer.start();
	}
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		menupanel.draw();
		if(pick != null){
			if(pick.getStart()){
				pickingmusic.stop();
			}
		}
	}
	
	// goes to play adventure mode
	public void adventure(){
		stopMusic();
		if(getMusic()){
			pickingmusic.loop();
		}
		pick = new PickPlants(play);
		play.getPanel().setMode("levels");
		play.getPanel().getMap().setMode("levels");
		pick.setVisible(true);
		setVisible(false);
		
	}
	
	public void setMenu(){
		setVisible(true);
	}
	
	public void HighScore(){
		highscore = new HighScore(this);
		highscore.setVisible(true);
		setVisible(false);
	}
	
	public MenuPanel getMenuPanel(){
		return menupanel;
	}
	
	
	// goes to play survival mode
	public void survival(){
		stopMusic();
		if(getMusic()){
			pickingmusic.loop();
		}
		pick = new PickPlants(play);
		play.getPanel().setMode("survival");
		play.getPanel().getMap().setMode("survival");
		pick.setVisible(true);
		setVisible(false);
		
	}
	// goes to the help menu
	public void help(){
		helpmenu = new HelpMenu(this);
		setVisible(false);
	}
	
	// goes to the almanac
	public void almanac(){
		stopMusic();
		System.out.println("meow");
		if(getMusic()){
			play.getPanel().setInAlmanac(true);
			pickingmusic.loop();
		}
		
		almanac = new AlmanacMenu(this,"menu",play);
		setVisible(false);
	}
	
	// quits the game
	public void exit(){
		setVisible(false);
		dispose();
		System.exit(0);
	}
	
	public boolean getMusic(){
		return menupanel.getMusic();
	}
	
	public void setMusic(boolean music){
		menupanel.setMusic(music);
		this.music = music;
	}
	
	public boolean getSound(){
		return menupanel.getSound();
	}
	
	public void setSound(boolean sound){
		menupanel.setSound(sound);
		this.sound = sound;
	}
	
	public void playMusic(){
		menumusic.loop();
	}
	
	public void stopMusic(){
		menumusic.stop();
	}
	
	public void stopAlmanacMusic(){
		pickingmusic.stop();
	}
	
	public void startAlmanacMusic(){
		pickingmusic.loop();
	}
	
	public static void main(String[] args){
		MainMenu menu = new MainMenu(null);
	}
}

class MenuPanel extends JPanel implements MouseListener, MouseMotionListener{
	MainMenu menu;
	Image menuPic;		// menu image
	boolean clicked,options,mplaying, ingame;
;	Image almanacHover = new ImageIcon("Images/Menus/AlmanacHover.png").getImage();
	Image adventureHover = new ImageIcon("Images/Menus/StartAdventureHover.png").getImage();
	Image survivalHover = new ImageIcon("Images/Menus/SurvivalHover.png").getImage();
	Image optionsHover = new ImageIcon("Images/Menus/OptionsHover.png").getImage();
	Image helpHover = new ImageIcon("Images/Menus/HelpHover.png").getImage();
	Image quitHover = new ImageIcon("Images/Menus/QuitHover.png").getImage();
	Image optionsPic,optionsSound,optionsMusic,optionsOK;		// options images
	int[] pos;
	
	boolean sound = true;
	boolean music = true;
	
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
	}
	public void mouseMoved(MouseEvent evt){
		pos = new int[]{evt.getX(),evt.getY()};
	}	
	public void mouseDragged(MouseEvent evt){
		pos = new int[]{evt.getX(),evt.getY()};
	}
	
	public MenuPanel(MainMenu menu){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.menu = menu;
		mplaying = false;
		ingame = false;
		menuPic = new ImageIcon("Images/Menus/mainmenu.png").getImage();
		clicked = false;
		pos = new int[]{0,0};
		
		optionsPic = new ImageIcon("Images/Menus/OptionsMenu.png").getImage();
		optionsSound = new ImageIcon("Images/Menus/OptionsMenu - Sound.png").getImage();
		optionsMusic = new ImageIcon("Images/Menus/OptionsMenu - Music.png").getImage();
		optionsOK = new ImageIcon("Images/Menus/OptionsMenu - OK.png").getImage();
	}
	
	public boolean getSound(){
		return sound;
	}
	
	public void setSound(boolean sound){
		this.sound = sound;
	}
	
	public boolean getMusic(){
		return music;
	}
	
	public void setMusic(boolean music){
		this.music = music;
	}
	
	public void setInGame(boolean ingame){
		this.ingame = ingame;
	}
	
	public void draw(){
		repaint();
		if(music && !mplaying && !ingame){
			
			menu.playMusic();
			mplaying = true;
		}
		else if(!music){
			menu.stopMusic();
			mplaying = false;
		}
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(menuPic,0,0,this);		
		
		Rectangle adventureRect = new Rectangle(415,100,320,100);
		Rectangle survivalRect = new Rectangle(415,320,260,80);
		Rectangle almanacRect = new Rectangle(390,440,110,120);
		Rectangle optionsRect = new Rectangle(565,475,78,56);
		Rectangle helpRect = new Rectangle(645,495,60,60);
		Rectangle exitRect = new Rectangle(710,488,65,60);
		
		Rectangle musicRect = new Rectangle(486,304,34,30);
		Rectangle soundRect = new Rectangle(488,350,34,30);
		Rectangle okRect = new Rectangle(262,456,310,60);
		
		if(!options){
			if(almanacRect.contains(pos[0],pos[1])){	// mouse is in almanac rect
				g.drawImage(almanacHover,383,436,this);		// draws hovering image
				if(clicked == true){					// goes to almanac
					menu.almanac();
				}
			}
			
			if(adventureRect.contains(pos[0],pos[1])){	// mouse is in start adventure rect
				g.drawImage(adventureHover,407,75,this);		// draws hovering image
				if(clicked == true){					// starts adventure
					menu.adventure();
					ingame = true;
				}
			}
			
			if(survivalRect.contains(pos[0],pos[1])){	// mouse is in survival rect
				g.drawImage(survivalHover,413,300,this);		// draws hovering image
				if(clicked == true){					// starts survival
					menu.survival();
					ingame = true;
				}
			}
			
			if(helpRect.contains(pos[0],pos[1])){	// mouse is in help rect
				g.drawImage(helpHover,637,519,this);		// draws hovering image
				if(clicked == true){					// goes to help
					menu.help();
				}
			}
			
			if(exitRect.contains(pos[0],pos[1])){	// mouse is in exit rect
				g.drawImage(quitHover,712,516,this);		// draws hovering image
				if(clicked == true){					// exits
					menu.exit();
				}
			}
			
			if(optionsRect.contains(pos[0],pos[1])){	// mouse is in options rect
				g.drawImage(optionsHover,556,481,this);		// draws hovering image
				if(clicked == true){					// goes to options
					options = true;
				}
			}
		}
		
		if(options){		// draws options images if options is on
			g.drawImage(optionsPic,200,50,this);

			if(!music){
				g.drawImage(optionsPic,200,50,this);
				if(clicked && musicRect.contains(pos[0],pos[1])){
					music = true;
					clicked = false;
				}
			}
			if(!sound){
				g.drawImage(optionsPic,200,50,this);
				if(clicked && soundRect.contains(pos[0],pos[1])){
					sound = true;
					clicked = false;
				}
			}
			if(music){
				g.drawImage(optionsMusic,200,50,this);
				if(clicked && musicRect.contains(pos[0],pos[1])){
					music = false;
					mplaying = false;
					clicked = false;
				}
			}
			if(sound){
				g.drawImage(optionsSound,200,50,this);
				if(clicked && soundRect.contains(pos[0],pos[1])){
					sound = false;
					clicked = false;
				}
			}
			if(okRect.contains(pos[0],pos[1])){
				g.drawImage(optionsOK,200,50,this);
				if(clicked){
					options = false;
					clicked = false;
				}
			}
		}
		
		if(clicked){
			clicked = false;
		}
	}
}