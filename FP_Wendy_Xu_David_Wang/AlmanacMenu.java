// AlmanacMenu.java
// David Wang

// Displays almanac of plants and zombies.

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class AlmanacMenu extends JFrame implements ActionListener{
	MainMenu menu;
	PvZ pvz;
	PlantAlmanac plantmenu;
	ZombieAlmanac zombiemenu;
	AlmanacPanel almanacpanel;
	String comingfrom;

	javax.swing.Timer myTimer;
	
	public AlmanacMenu(MainMenu menu,String comingfrom,PvZ pvz){
		super();
		this.menu = menu;
		this.comingfrom = comingfrom;
		this.pvz = pvz;
		setLayout(null);

		almanacpanel = new AlmanacPanel(this);
		almanacpanel.setSize(818,647);
		almanacpanel.setLocation(0,0);
		add(almanacpanel);
		
		setVisible(true);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    	setSize(818,647);
	
    	
    	myTimer = new javax.swing.Timer(100,this);
		myTimer.start(); 
	}
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		almanacpanel.draw();
	}
	
	public void mainMenu(){
		// checks to see where the user came from
		// if the user came from the menu, it brings them back to the menu
		// when they hit close
		// if they came from the game
		// it brings them back to the game
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
	
	
	public void plantAlmanac(){
		plantmenu = new PlantAlmanac(menu,comingfrom,pvz);
		setVisible(false);
	}
	
	public void zombieAlmanac(){
		zombiemenu = new ZombieAlmanac(menu,comingfrom,pvz);
		setVisible(false);
	}
	
	public static void main(String[] args){
		AlmanacMenu menu = new AlmanacMenu(null,null,null);
	}
}

class AlmanacPanel extends JPanel implements MouseListener,MouseMotionListener{

	AlmanacMenu menu;
	Image AlmanacSelect,PlantsHover,ZombieHover,CloseHover;
	int[] pos;
	boolean clicked;
	
	public void keyPressed(KeyEvent evt){}
	public void keyTyped(KeyEvent evt){}
	public void keyReleased(KeyEvent evt){}

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
	
	
	public AlmanacPanel(AlmanacMenu menu){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.menu = menu;
		pos = new int[]{0,0};
		clicked = false;
		
		AlmanacSelect = new ImageIcon("Images/Menus/AlmanacIndex.png").getImage();
		PlantsHover = new ImageIcon("Images/Menus/AlmanacIndex - Plants.png").getImage();
		ZombieHover = new ImageIcon("Images/Menus/AlmanacIndex - Zombies.png").getImage();
		CloseHover = 	new ImageIcon("Images/Menus/AlmanacIndex - Close.png").getImage();
	}
	
	public void draw(){
		repaint();
	}
	
	public void paintComponent(Graphics g){
		
		Rectangle plantAlmanac = new Rectangle(130,345,150,35);
		Rectangle zombieAlmanac = new Rectangle(490,345,150,35);
		Rectangle menuexit = new Rectangle(678,570,85,22);
		
		g.drawImage(AlmanacSelect,0,0,this);
		
		if(plantAlmanac.contains(pos[0],pos[1])){	// go back to main menu
			g.drawImage(PlantsHover,0,0,this);
			if(clicked){
				menu.plantAlmanac();
			}
		}
		
		else if(zombieAlmanac.contains(pos[0],pos[1])){		// view zombies
			g.drawImage(ZombieHover,0,0,this);
			if(clicked){
				menu.zombieAlmanac();
			}
		}
		
		else if(menuexit.contains(pos[0],pos[1])){		// view plants
			g.drawImage(CloseHover,0,0,this);
			if(clicked){
				menu.mainMenu();	
			}
		}
	}
}