// Shooter.java
// Wendy Xu

// Purple peashooter that the player controls to shoot zombies

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*; 

class Shooter{
	private static final int[] dimensions = new int[]{90,78};		// dimensions of ship shooter (center)
	private int[] pos = new int[]{10,2};		// default position of shooter (x is pixel, y is coordinate)
	private Image shooterPic;	
	private int movecount,prepcount;				
	
	private static ArrayList<Image> shooterPics = new ArrayList<Image>();
	public static final int UP = 0, DOWN = 1;
	
	public Shooter(){
	
		for(int i = 1; i < 17; i++){ 
			shooterPics.add(new ImageIcon(String.format("Images/Game/Player/purplepea%s.png",i)).getImage());
		}
		
		shooterPic = shooterPics.get(0);
		movecount = 0;
		prepcount = 0;
	}
	
	// moves player
	public void move(int dir){
		if(dir == UP){
			pos[1] = Math.max(0,pos[1]-1);		// checks boundaries
		}
		else if(dir == DOWN){
			pos[1] = Math.min(4,pos[1]+1);
		}
	}
	
	public void setPic(){
		if(movecount == 3){
			int index = shooterPics.indexOf(shooterPic);
			if(shooterPics.size()-1 != index){
				shooterPic = shooterPics.get(index+1);
			}
			else{
				shooterPic = shooterPics.get(0);
			}
			movecount = 0;
		}
		else{
			movecount ++;
		}
	}
	
	public void actionCounter(){
		if(prepcount == 140){
			prepcount = 0;
		}
		else{
			prepcount ++;
		}
	}
	
	public boolean isReady(){	// player is ready to shoot a pea
		if(prepcount == 140){
			return true;
		}
		return false;
	}
	
	public int[] getPos(){
		return pos;
	}
	
	// draws shooter onto gamepanel
	public void draw(Graphics g,JPanel panel){					
		g.drawImage(shooterPic,pos[0]-dimensions[0]/2,pos[1]*98+129-dimensions[1]/2,panel);
	}
}