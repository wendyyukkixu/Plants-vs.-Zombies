// PlantCard.java
// David Wang
// Makes the plant seeds to click to plant plants

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*; 
import java.util.*;
import java.awt.geom.*;

public class PlantCard{
	private Rectangle cardRect;
	private int plantType;
	private int cost,recharge,charge;
	public Image image, unactiveImage;
	private boolean active;
	
	public static final int PEASHOOTER = 0, SUNFLOWER = 1, CHERRYBOMB = 2, WALLNUT = 3, THREEPEATER = 4, SNOWPEA = 5, TALLNUT = 6,
						    REPEATER = 7, POTATOMINE = 8, SQUASH = 9, JALAPENO = 10, SPIKEWEED = 11, TORCHWOOD = 12, 
						    TWINSUNFLOWER = 13, GATLINGPEA = 14, BONKCHOY = 15, PIKAPEA = 16, FIREPEA = 17;
	public static final int[] costs = new int[]{100,50,150,50,325,175,125,200,25,50,125,100,175,150,250,150,175,175};
	public static final int[] recharges = new int[]{750,750,5000,3000,750,750,3000,750,3000,3000,5000,750,750,5000,5000,750,750,750};
	
	public static ArrayList<Image> seedPackets = new ArrayList<Image>();
	
    public PlantCard(Rectangle rect, int type){
    	plantType = type;
    	cardRect = rect;
    	active = true;
    	cost = costs[type];
    	recharge = recharges[type];
    	charge = 0;
    	
    	image = seedPackets.get(type);
    	unactiveImage = new ImageIcon("Images/Game/Seeds/seedcover.png").getImage();	
    }
    
    public static void getImages(){
    	for (int i = 1; i <19; i++){
    		seedPackets.add(new ImageIcon(String.format("Images/Game/Seeds/seed%s.png",i)).getImage());
    	}
    }
   
    public void setRect(int x, int y, int width, int height){
    	cardRect = new Rectangle(x,y,width,height);
    }
    
    public boolean getActive(){
    	return active;
    }
    
    public void setActive(boolean newAc){
    	active = newAc;
    }
    
    public int getCost(){
    	return cost; 
    }
    
    public Rectangle getRect(){
    	return cardRect;
    }  
    
    public int getType(){
    	return plantType;
    }
    
    public int getRecharge(){
    	return recharge;
    }
    
    public int getCharge(){
    	return charge;
    }
    
    public void addCharge(){
    	charge ++;
    }
    
    public void setCharge(int newcharge){
    	charge = newcharge;
    }
    
    
    public void draw(Graphics g,JPanel j){
    	g.drawImage(image,(int)cardRect.getX(),(int)cardRect.getY(),j);
    	if (!active){
    		g.drawImage(unactiveImage,(int)cardRect.getX(),(int)cardRect.getY(),j);
    	}
    }
}
