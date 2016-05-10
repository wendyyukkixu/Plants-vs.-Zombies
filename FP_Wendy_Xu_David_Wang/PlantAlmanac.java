// PlantAlmanac.java
// David Wang
// Displays all the plants in the plant almanac
// User can interact with the different seeds

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class PlantAlmanac extends JFrame implements ActionListener{
	MainMenu menu;
	AlmanacMenu almanac;
	PlantAlmanacPanel plantpanel; 
	PvZ pvz;
	String comingfrom;
		
	javax.swing.Timer myTimer;
	
	public PlantAlmanac(MainMenu menu,String comingfrom,PvZ pvz){
		super();
		this.menu = menu;
		this.pvz = pvz;
		this.comingfrom = comingfrom;		// must know if coming from pause or main menu 
		setLayout(null);					// to know what to set back to visible when almanac is closed
		
		plantpanel = new PlantAlmanacPanel(this);
		plantpanel.setSize(818,647);
		plantpanel.setLocation(0,0);
		add(plantpanel);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(818,647);
		
		myTimer = new javax.swing.Timer(100,this);
		myTimer.start(); 
	}
	
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		plantpanel.draw();
		
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
	
	public static void main(String[]args){
		PlantAlmanac menu = new PlantAlmanac(null,null,null);
	}
	
}

class PlantAlmanacPanel extends JPanel implements MouseListener,MouseMotionListener{

	PlantAlmanac menu;
	Image PlantsAlmanacPic, AlmanacPic, QuitPic;
	int[] pos;
	boolean clicked;

	ArrayList<Image> plants = new ArrayList<Image>();
	Rectangle[] plantCards = new Rectangle[20];
	boolean[] displayPlants = new boolean[20];
	
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
	
	
	public PlantAlmanacPanel(PlantAlmanac menu){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.menu = menu;
		pos = new int[]{0,0};
		clicked = false;
		
		PlantsAlmanacPic = new ImageIcon("Images/Menus/PlantsAlmanac.png").getImage();
		AlmanacPic = new ImageIcon("Images/Menus/ReturnToAlmanac.png").getImage();
		QuitPic = new ImageIcon("Images/Menus/ExitAlmanac.png").getImage();
		
		for(int i = 1; i < 19; i ++){
			plants.add(new ImageIcon(String.format("Images/Menus/Plants Almanac Entries/plant%s.png",i)).getImage());
		}
		
		for(int i = 0; i < 8; i ++){
			plantCards[i] = new Rectangle(26+(i*52),92,50,70);
		}
		
		for(int i = 8; i < 16; i ++){
			plantCards[i] = new Rectangle(26+((i%8)*52),170,50,70);
		}
		
		for(int i = 16; i < 18; i ++){
			plantCards[i] = new Rectangle(26+((i%8)*52),248,50,70);
		}
		
		// set all plants display to false at first
		for(int i = 0; i < displayPlants.length; i ++){
			displayPlants[i] = false;
		}	
	}	

	public void checkPlantRectCollide(){
		for(int i = 0; i < plantCards.length; i ++){
			if(plantCards[i] != null){
				if(plantCards[i].contains(pos[0],pos[1])){
					for(int j = 0; j < displayPlants.length; j ++){
						displayPlants[j] = false;
					}
					displayPlants[i] = true;
				}
			}
		}
	}	

	public void draw(){
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(PlantsAlmanacPic,0,0,this);
		
		Rectangle exit = new Rectangle(35,570,157,20);
		Rectangle menuexit = new Rectangle(678,570,85,22);
		
		if(exit.contains(pos[0],pos[1])){			// go back to almanac menu
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
		
		if(clicked){
			checkPlantRectCollide();
			clicked = false;
		}
		
		for(int i=0; i < plants.size(); i++){
			if(displayPlants[i]){
				g.drawImage(plants.get(i),450,90,this);
			}
		}
	}
}