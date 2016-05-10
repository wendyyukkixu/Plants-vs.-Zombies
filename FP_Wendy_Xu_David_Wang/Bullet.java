// Bullet.java
// Wendy Xu

// Makes bullets that the plants make

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*;

class Bullet{
	private int type, damage;
	private double[] pos;
	private ArrayList<Integer> flamedPlants = new ArrayList<Integer>();		// list of x-coordinates of torchwoods bullet passed through
	private Image shadowPic = new ImageIcon("Images/Game/bulletshadow.png").getImage();
	
	private static final int[][] picSizes = new int[][]{{4,26},{4,26},{60,34},{140,36},{4,26}};
	public static final int PEASHOOTER = 0, SNOWPEA = 1, PIKAPEA = 2, FIREPEA = 3, SHOOTER = 4;
	private static final Image[] bulletPics = new Image[]{new ImageIcon("Images/Game/Bullets/normalbullet.png").getImage(),
														  new ImageIcon("Images/Game/Bullets/snowbullet.png").getImage(),
														  new ImageIcon("Images/Game/Bullets/electricbullet.png").getImage(),
														  new ImageIcon("Images/Game/Bullets/firebullet.png").getImage(),
														  new ImageIcon("Images/Game/Bullets/purplebullet.png").getImage()};
	
	public Bullet(int type, double[] pos){	// pos[0] is pixel value, pos[1] is which lane the bullet is in
		this.type = type;
		this.pos = pos;
		if(type == FIREPEA){
			damage = 40;
		}
		else{
			damage = 20;
		}
	}
	
	public void move(){
		pos[0] += 2.1;
	}
	
	public double[] getPos(){
		return new double[]{pos[0],(pos[1]*98+129)};
	}
	
	public void torched(int x){		// when bullet passes through torchwood
		if(!flamedPlants.contains(x)){
			if(type == PEASHOOTER || type == SHOOTER){
				damage = 40;
				type = FIREPEA;
			}
			if(type == SNOWPEA){
				type = PEASHOOTER; 
			}
			flamedPlants.add(x);	
		}
	}

	public int getDamage(){
		return damage;
	}
	
	public int getType(){
		return type;
	}
	
	public void draw(Graphics g, JPanel panel){		// draws bullet
		g.drawImage(bulletPics[type],(int)pos[0]-picSizes[type][0]/2,(int)(pos[1]*98+129)-picSizes[type][1]/2,panel);
	}
}