//Pick Plants
//David Wang

// Displays a panel where the user picks the plants
// they want to use in game before the game starts

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class PickPlants extends JFrame implements ActionListener{
	
	PvZ play;
	PickPanel picktheplants;
	boolean music,sound,start;
	
	public PickPlants(PvZ play){
		super();
		this.play = play;
		setLayout(null);
		
		picktheplants = new PickPanel(this);
		picktheplants.setSize(818,647);
		picktheplants.setLocation(0,0);
		add(picktheplants);
		start = false;
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(818,647);
	}
	
	public void playGame(){
		start = true;
		play.setVisible(true);
		setVisible(false);
		play.startTimer();
		play.getPanel().setInAlmanac(false);
		play.getPanel().gameStart();
		play.getPanel().setLevel(picktheplants.getLevel());
		play.getPanel().getMap().setSeeds(picktheplants.getSeeds());
		
	}
	
	public PickPanel getPanel(){
		return picktheplants;
	}
	
	public PvZ getPlay(){
		return play;
	}
	
	public void mainMenu(){
		play.getMenu().setVisible(true);
		play.getMenu().stopAlmanacMusic();
		play.getMenu().playMusic();
		play.getMenu().getMenuPanel().setInGame(false);
		setVisible(false);
	}

	public boolean getStart(){
		return start;
	}
	
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		picktheplants.draw();
		
	}
	
	public static void main(String[]args){
		PickPlants picked = new PickPlants(null);
	}
}


class PickPanel extends JPanel implements MouseListener, MouseMotionListener{
	PickPlants menu;
	Image background,selectplants,continuebutton,chooselevel1,chooselevel2,chooselevel3,topShovel,mainMenuPic;

	ArrayList<PlantCard>pickSeeds = new ArrayList<PlantCard>();
	PlantCard[] seedPackets = new PlantCard[7];
	Rectangle exit = new Rectangle(334,453,153,38);
	Rectangle menuBack = new Rectangle(524,462,78,20);
	Rectangle level1 = new Rectangle(202,134,88,53);
	Rectangle level2 = new Rectangle(370,134,88,53);
	Rectangle level3 = new Rectangle(531,134,88,53);
	int[] pos;
	int level = 1;
	
	public PickPanel(PickPlants menu){
		super();
		setFocusable(true);
		grabFocus();
		addMouseMotionListener(this);
		addMouseListener(this);
		this.menu = menu;
		
		PlantCard.getImages();
		
		background = new ImageIcon("Images/Game/lawn.png").getImage();
		selectplants = new ImageIcon("Images/Game/selectbox.png").getImage();
		continuebutton = new ImageIcon("Images/Game/selectboxrock.png").getImage();
		chooselevel1 = new ImageIcon("Images/Game/selectlevel1.png").getImage();
		chooselevel2 = new ImageIcon("Images/Game/selectlevel2.png").getImage();
		chooselevel3 = new ImageIcon("Images/Game/selectlevel3.png").getImage();
		topShovel = new ImageIcon("Images/Game/Shovel.png").getImage();
		mainMenuPic = new ImageIcon("Images/Game/selectbox. - mainmenu.png").getImage();
		
		for(int i=0; i<18; i++){
			if(i >= 0 && i < 8){
				pickSeeds.add(new PlantCard(new Rectangle(205+(i%8)*52,245,50,70),i));
			}
			
			if(i >= 8 && i < 16){
				pickSeeds.add(new PlantCard(new Rectangle(205+(i%8)*52,317,50,70),i));
			}
			
			if(i >= 16){
				pickSeeds.add(new PlantCard(new Rectangle(205+(i%8)*52,389,50,70),i));
			}
		}			
		pos = new int[]{0,0};
	}

	public void pickPlants(){
		// pick seeds to use for ingame from a predetermined pool
		for(int i = 0; i < pickSeeds.size(); i ++){
			if(pickSeeds.get(i).getRect().contains(pos[0],pos[1])){
				if(pickSeeds.get(i).getActive()){   
					for(int j = 0; j < seedPackets.length; j ++){
						if(seedPackets[j] == null){
							seedPackets[j] = new PlantCard(new Rectangle(83+(j*52),8,50,70),pickSeeds.get(i).getType());
							pickSeeds.get(i).setActive(false);
							break;
						}
					}		
				}
			}
		}
	}
	
	public void removePlants(){
		// removes plants from the seed toolbar
		for(int i = 0; i < seedPackets.length; i ++){
			if(seedPackets[i] != null){	
				if(seedPackets[i].getRect().contains(pos[0],pos[1])){
					pickSeeds.get(seedPackets[i].getType()).setActive(true);
					seedPackets[i] = null;
				}
			}
		}
	}
	
	public boolean checkSeedAmount(){
		// if the user has atleast one plant selected it will return true
		// this will later be used to check if they can continue
		// to play the game
		for(int i = 0; i < seedPackets.length; i ++){
			if(seedPackets[i] != null){
				return true;
			}
		}
		return false;
	}
	
	public void continueOn(){
		// goes onto the gamepanel to play the game
		if(exit.contains(pos[0],pos[1])){
			menu.playGame();

		}
		
	}
	
	public void backToMenu(){
		if (menuBack.contains(pos[0],pos[1])){
			menu.mainMenu();
		}
	}
	
	public void selectLevel(){
		if(level1.contains(pos[0],pos[1])){
			level = 1;
		}
		if(level2.contains(pos[0],pos[1])){
			level = 2;
		}
		if(level3.contains(pos[0],pos[1])){
			level = 3;
		}
	}
	
	public int getLevel(){
		return level;
	}
	
	public PlantCard[] getSeeds(){
		return seedPackets;
	}
	
	public void setSeeds(PlantCard[] seeds){
		seedPackets = seeds;
	}
	
	public void draw(){
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(background,0,0,this);
		g.drawImage(selectplants,180,210,this);
		g.drawImage(topShovel,454,0,this);
		if (menu.getPlay().getPanel().getMode().equals("levels")){
			if(level == 1){
				g.drawImage(chooselevel1,180,92,this);
			}
			
			if(level == 2){
				g.drawImage(chooselevel2,180,92,this);
			}
			
			if(level == 3){
				g.drawImage(chooselevel3,180,92,this);
			}
		}
		
		for(int i=0; i<pickSeeds.size(); i++){
			if(pickSeeds.get(i).getActive()){
				pickSeeds.get(i).draw(g,this);
			}
		}
		
		for(int i=0; i<seedPackets.length; i++){
			if(seedPackets[i] != null){
				seedPackets[i].draw(g,this);
			}
		}
		
		if(exit.contains(pos[0],pos[1])){
			g.drawImage(continuebutton,333,452,this);
		}
		if (menuBack.contains(pos[0],pos[1])){
			g.drawImage(mainMenuPic,180,210,this);
		}
	}

	public void mouseClicked(MouseEvent evt){}
	public void mouseExited(MouseEvent evt){}
	public void mouseEntered(MouseEvent evt){}
	
	public void mouseReleased(MouseEvent evt){
		pos = new int[]{evt.getX(),evt.getY()};
		pickPlants();
		removePlants();
		selectLevel();
		backToMenu();
		if(checkSeedAmount()){
			continueOn();
		}
		repaint();
	}
	public void mousePressed(MouseEvent evt){
		System.out.println(evt.getX()+" "+evt.getY());
	}
	
	public void mouseMoved(MouseEvent evt){
		pos = new int[]{evt.getX(),evt.getY()};
		repaint();
	}
	
	public void mouseDragged(MouseEvent evt){
		pos = new int[]{evt.getX(),evt.getY()};
		repaint();
	}
}