// ZombieAlmanac.java
// David Wang

// Displays all the zombies in the almanac
// User can interact with the tombs

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class ZombieAlmanac extends JFrame implements ActionListener{
	MainMenu menu;
	AlmanacMenu almanac;
	ZombieAlmanacPanel zombiepanel;
	String comingfrom;
	PvZ pvz;
	
	javax.swing.Timer myTimer;
	
	public ZombieAlmanac (MainMenu menu,String comingfrom, PvZ pvz){
		super();
		this.menu = menu;
		this.comingfrom = comingfrom;	// must know if coming from pause or main menu 
		this.pvz = pvz;					// to know what to set back to visible when almanac is closed
		setLayout(null);
		
		zombiepanel = new ZombieAlmanacPanel(this);
		zombiepanel.setSize(818,647);
		zombiepanel.setLocation(0,0);
		add(zombiepanel);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(818,647);
		
		myTimer = new javax.swing.Timer(100,this);
		myTimer.start(); 
	}
	
	public void mainMenu(){
		if(comingfrom.equals("menu")){
			menu.stopAlmanacMusic();
			menu.playMusic();
			menu.setVisible(true);
			setVisible(false);
		}
		if(comingfrom.equals("game")){
			pvz.setVisible(true);
			pvz.getPanel().setInAlmanac(false);
			pvz.getPanel().setMusicLoop();
			setVisible(false);
		}
	}
	public void backToAlmanac(){
		almanac = new AlmanacMenu(menu,comingfrom,pvz);
		setVisible(false);
	}
	
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		zombiepanel.draw();
	}
	
	public static void main(String[]args){
		ZombieAlmanac menu = new ZombieAlmanac(null,null,null);
	}
}

class ZombieAlmanacPanel extends JPanel implements MouseListener, MouseMotionListener{
	ZombieAlmanac menu;
	Image ZombieAlmanacPic, AlmanacPic, QuitPic;
	int[] pos;
	boolean clicked;
	
	ArrayList<Image> zombies = new ArrayList<Image>();
	Rectangle[] zombieCards = new Rectangle[11];
	boolean[] displayZombies = new boolean[11];
	
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
	
	
	public ZombieAlmanacPanel(ZombieAlmanac menu){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.menu = menu;
		pos = new int[]{0,0};
		clicked = false;
		
		ZombieAlmanacPic = new ImageIcon("Images/Menus/ZombieAlmanac.png").getImage();
		AlmanacPic = new ImageIcon("Images/Menus/ReturnToAlmanac.png").getImage();
		QuitPic = new ImageIcon("Images/Menus/ExitAlmanac.png").getImage();
		
		for(int i = 1; i < 12; i ++){
			zombies.add(new ImageIcon(String.format("Images/Menus/Zombies Almanac Entries/almanac%s.png",i)).getImage());
		}
		
		for(int i = 0; i < 5; i++){
			zombieCards[i] = new Rectangle(30+(i*85),95,60,55);
		}
		
		for(int i = 5; i < 7; i ++){
			zombieCards[i] = new Rectangle(30+((i%5)*85),175,60,55);
		}
			
		for(int i = 0; i < displayZombies.length; i ++){
			displayZombies[i] = false;
		}
	}
	
	public void checkZombieRectCollide(){
		
		for(int i = 0; i < zombieCards.length; i ++){
			if(zombieCards[i] != null){
				if(zombieCards[i].contains(pos[0],pos[1])){
					for(int j = 0; j < displayZombies.length; j ++){
						displayZombies[j] = false;
					}
					displayZombies[i] = true;
				}
			}	
		}
	}
	
	public void draw(){
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(ZombieAlmanacPic,0,0,this);
		
		Rectangle exit = new Rectangle(35,570,157,20);
		Rectangle menuexit = new Rectangle(678,570,85,22);
		
		if(exit.contains(pos[0],pos[1])){		// go back to almanac menu
			g.drawImage(AlmanacPic,0,0,this);
			if(clicked){
				menu.backToAlmanac();
			}
		}
		
		if(menuexit.contains(pos[0],pos[1])){		// go back to main menu or pause screen
			g.drawImage(QuitPic,0,0,this);
			if(clicked){
				menu.mainMenu();
			}
		}
	
		for(int i = 0; i < zombies.size(); i ++){
			if(displayZombies[i]){
				g.drawImage(zombies.get(i),457,81,this);
			}
		}
		
		if(clicked){
			checkZombieRectCollide();
			clicked = false;
		}
		
		if(exit.contains(pos[0],pos[1])){
			g.drawImage(AlmanacPic,0,0,this);
			if(clicked){
				menu.backToAlmanac();
			}
		}
	}
}