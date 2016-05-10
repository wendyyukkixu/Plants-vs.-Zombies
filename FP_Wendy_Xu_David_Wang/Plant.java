// Plants.java
// Wendy Xu 

// Makes plants of 18 different types and contains functions of everything they do in game.

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*; 

public class Plant{
	private int[] pos; 		// plant position on grid
	private int health,damage,prep,explodecount;
	private int prepcount;		// how long plant takes to do its action 
	private int movecount;		// counts when the plant switches moving pics
	private int type;		// sunflower, shooter, wallnut, potato mine, etc
	private int shot;		// how many consecutive peas the gatling pea shot 
	private boolean alive,exploded,squashed;
	private Image plantPic;		// current plant pic
	private int[] picSize;
	private Rectangle2D plantRect;

	private static final int[] movecounts = new int[]{3,3,11,0,5,3,3,3,3,5,11,12,8,3,3,14,3,3};
	private static final int[][] dimensions = new int[][]{{90,78},{80,80},{298,160},{80,80},{100,100},{90,78},{80,116},{90,78},
														  {108,72},{250,150},{1460,80},{100,48},{80,92},{120,110},{100,100},
														  {140,104},{100,100},{170,78}};
	public static final int PEASHOOTER = 0, SUNFLOWER = 1, CHERRYBOMB = 2, WALLNUT = 3, THREEPEATER = 4, SNOWPEA = 5, TALLNUT = 6,
						    REPEATER = 7, POTATOMINE = 8, SQUASH = 9, JALAPENO = 10, SPIKEWEED = 11, TORCHWOOD = 12, 
						    TWINSUNFLOWER = 13, GATLINGPEA = 14, BONKCHOY = 15, PIKAPEA = 16, FIREPEA = 17;
	public static ArrayList<ArrayList<Image>> plantPics = new ArrayList<ArrayList<Image>>();
	public static ArrayList<ArrayList<Image>> actionPics = new ArrayList<ArrayList<Image>>();
	public static ArrayList<ArrayList<Image>> action2Pics = new ArrayList<ArrayList<Image>>();
	
	public Plant(int type,int[] pos){
		this.type = type;
		this.pos = pos;
		prepcount = 0;
		movecount = 0;
		alive = true;
		plantRect = new Rectangle2D.Double(pos[0]*80+35,pos[1]*98+80,80,98);	
				
		// SHOOTERS
		if(type == PEASHOOTER){
			health = 300;
			damage = 20;
			prep = 140;
		}
		if(type == SNOWPEA){
			health = 300;
			damage = 20;
			prep = 140;
		}
		if(type == PIKAPEA){
			health = 300;
			damage = 20;
			prep = 500;
		}
		if(type == FIREPEA){
			health = 300;
			damage = 40;
			prep = 140;
		}
		if(type == REPEATER){
			health = 300;
			damage = 20;
			prep = 140;
		}
		if(type == THREEPEATER){
			health = 300;
			damage = 20;
			prep = 140;
		}
		if(type == BONKCHOY){
			health = 300;
			damage = 20;
			prep = 20;
		}
		
		// SUN MAKERS
		if(type == SUNFLOWER){
			health = 100;
			prep = 2400;
		}
		
		//EXPLOSIVES
		if(type == CHERRYBOMB){
			health = 300;
			damage = 1800;
			prep = 100;
		}
		if(type == POTATOMINE){
			explodecount = 0;
			exploded = false;
			health = 300;
			damage = 1800;
			prep = 2400;
		}
		if(type == SQUASH){
			squashed = false;
			health = 300;
			damage = 1800;
			prep = 1500;
		}
		if(type == JALAPENO){
			health = 300;
			damage = 1800;
			prep = 100;
		}
		
		// DEFENSES
		if(type == WALLNUT){
			health = 3600;
		}
		if(type == TALLNUT){
			health = 7200;
		}
		
		// BOOSTS
		if(type == TORCHWOOD){
			health = 300;
		}
		if(type == SPIKEWEED){
			damage = 20;
			prep = 50;
		}
		plantPic = plantPics.get(type).get(0);
	}
	
	public static void getImages(){		// loads all plant images
		for(int i = 0; i < 18; i ++){
			ArrayList<Image> tmp = new ArrayList<Image>();
			plantPics.add(tmp);
		}
		for(int i = 0; i < 18; i ++){
			ArrayList<Image> tmp = new ArrayList<Image>();
			actionPics.add(tmp);
		}
		for(int i = 0; i < 18; i ++){
			ArrayList<Image> tmp = new ArrayList<Image>();
			action2Pics.add(tmp);
		}
		
		// Peashooter, Sunflower, Snowpea, Repeater, Twinsunflower, Gatlingpea, Pikeapea, Firepea
		for(int i = 1; i < 17; i++){ 
			plantPics.get(0).add(new ImageIcon(String.format("Images/Game/Plants/Peashooter/peashooter%s.png",i)).getImage());
			plantPics.get(1).add(new ImageIcon(String.format("Images/Game/Plants/Sunflower/sunflower%s.png",i)).getImage());
			plantPics.get(5).add(new ImageIcon(String.format("Images/Game/Plants/Snowpea/snowpea%s.png",i)).getImage());
			plantPics.get(7).add(new ImageIcon(String.format("Images/Game/Plants/Repeater/repeater%s.png",i)).getImage());
			plantPics.get(13).add(new ImageIcon(String.format("Images/Game/Plants/Twinsunflower/twinsunflower%s.png",i)).getImage());
			plantPics.get(14).add(new ImageIcon(String.format("Images/Game/Plants/Gatlingpea/gatlingpea%s.png",i)).getImage());
			plantPics.get(16).add(new ImageIcon(String.format("Images/Game/Plants/Pikapea/pikapea%s.png",i)).getImage());
			plantPics.get(17).add(new ImageIcon(String.format("Images/Game/Plants/Firepea/firepea%s.png",i)).getImage());
		}
		
		// Wallnut, Tallnut, Torchwood
		for(int i = 1; i < 4; i++){
			plantPics.get(3).add(new ImageIcon(String.format("Images/Game/Plants/Wallnut/wallnut%s.png",i)).getImage());
			plantPics.get(6).add(new ImageIcon(String.format("Images/Game/Plants/Tallnut/tallnut%s.png",i)).getImage());
			plantPics.get(12).add(new ImageIcon(String.format("Images/Game/Plants/Torchwood/torchwood%s.png",i)).getImage());
		}
		
		// Cherrybomb, Jalapeno
		for(int i = 1; i < 21; i ++){
			plantPics.get(2).add(new ImageIcon(String.format("Images/Game/Plants/Cherrybomb/cherrybomb%s.png",i)).getImage());
			plantPics.get(10).add(new ImageIcon(String.format("Images/Game/Plants/Jalapeno/jalapeno%s.png",i)).getImage());
		}
		
		// Threepeater, Potatomine, 
		for(int i = 1; i < 13; i ++){
			plantPics.get(4).add(new ImageIcon(String.format("Images/Game/Plants/Threepeater/threepeater%s.png",i)).getImage());
			plantPics.get(8).add(new ImageIcon(String.format("Images/Game/Plants/Potatomine/potatomine%s.png",i)).getImage());
		}
		
		// Squash
		for(int i = 1; i < 11; i ++){
			plantPics.get(9).add(new ImageIcon(String.format("Images/Game/Plants/Squash/squash%s.png",i)).getImage());
		}
		for(int i = 1; i < 32; i ++){
			actionPics.get(9).add(new ImageIcon(String.format("Images/Game/Plants/Squash/squashright%s.png",i)).getImage());
			action2Pics.get(9).add(new ImageIcon(String.format("Images/Game/Plants/Squash/squashleft%s.png",i)).getImage());
		}
		
		// Bonk Choy, Spikeweed
		for(int i = 1; i < 5; i ++){
			plantPics.get(11).add(new ImageIcon(String.format("Images/Game/Plants/Spikeweed/spikeweed%s.png",i)).getImage());
			plantPics.get(15).add(new ImageIcon(String.format("Images/Game/Plants/Bonkchoy/bonkchoy%s.png",i)).getImage());
		}
		for(int i = 1; i < 3; i ++){
			actionPics.get(15).add(new ImageIcon(String.format("Images/Game/Plants/Bonkchoy/bonkchoyright%s.png",i)).getImage());
			actionPics.get(15).add(new ImageIcon(String.format("Images/Game/Plants/Bonkchoy/bonkchoyleft%s.png",i)).getImage());
		}
	}
	
	public void setPic(){		// animates only plants that move
		if(movecount == movecounts[type]){
			if(type != WALLNUT && type != POTATOMINE && type != TALLNUT){
				int index = plantPics.get(type).indexOf(plantPic);
				
				if(type == SQUASH && actionPics.get(type).contains(plantPic)){
					System.out.println("yo");
					int sindex = actionPics.get(type).indexOf(plantPic);	
					if(actionPics.get(type).size()-1 != sindex){
						plantPic = actionPics.get(type).get(sindex+1);
					}
					else{
						alive = false;
					}
				}		
					
				else if(plantPics.get(type).size()-1 != index){		// next pic in array
					plantPic = plantPics.get(type).get(index+1);
				}
				
				else{					// goes through array again
					if(type != CHERRYBOMB && type != JALAPENO){
						plantPic = plantPics.get(type).get(0);
					}
					else{
						alive = false;
					}
				}				
			}
			movecount = 0;
		}
		else{
			movecount ++;
		}
	}
	
	public void getEaten(int damage){
		// plant is being eaten by zombies
		if(type != SPIKEWEED){		// spikeweeds don't take damage
			health -= damage;
			if(health <= 0){
				alive = false;
			}
		}
		if(type == WALLNUT){
			if(health <= 1200){
				plantPic = plantPics.get(type).get(2);
			}
			else if(health <= 2400){
				plantPic = plantPics.get(type).get(1);
			}
		}
		if(type == TALLNUT){
			if(health <= 2400){
				plantPic = plantPics.get(type).get(2);
			}
			else if(health <= 4800){
				plantPic = plantPics.get(type).get(1);
			}
		}
	}
	
	public void actionCounter(){		// counts when plant can do their action (punch, shoot pea, explode)
		if(prepcount == prep){
			if(type == REPEATER){
				prepcount = 0;
				if(prep == 140){
					prep = 30;
				}
				else if(prep == 30){
					prep = 140;
				}
			}
			if(type == GATLINGPEA){
				prepcount = 0;
				shot ++;
				if(prep == 140){
					prep = 20;
				}
				else if(prep == 20 && shot == 4){
					prep = 140;
					shot = 0;
				}
			}
			if(type == POTATOMINE){
				if(plantPic == plantPics.get(type).get(0)){
					plantPic = plantPics.get(type).get(1);
				}
			}
			else{
				prepcount = 0;
			}
		}
		else{
			prepcount ++;
		}
	}
	
	public boolean isReady(){		// plant is ready to do action
		if(prepcount >= prep){
			return true;
		}
		return false;
	}
	
	public void punch(String dir){		// bonk choy punches
		if(dir.equals("right")){
			if(plantPic == actionPics.get(type).get(0)){
				plantPic = actionPics.get(type).get(1);
			}
			else if(plantPic == actionPics.get(type).get(1)){
				plantPic = actionPics.get(type).get(0);
			}
			else{
				plantPic = actionPics.get(type).get(0);
			}
		}
		if(dir.equals("left")){
			if(plantPic == actionPics.get(type).get(2)){
				plantPic = actionPics.get(type).get(3);
			}
			else if(plantPic == actionPics.get(type).get(3)){
				plantPic = actionPics.get(type).get(2);
			}
			else{
				plantPic = actionPics.get(type).get(2);
			}
		}
	}
	
	public void explode(){
		exploded = true;
		plantPic = plantPics.get(type).get(2);
	}
	
	public void explodedTimer(){	// explosives die after exploding
		explodecount ++;
		if(explodecount == 200){
			alive = false;
		}
	}
	
	public void squashed(String side){		// squash squashes zombies
		squashed = true;
		if(side.equals("right")){
			plantPic = actionPics.get(type).get(0);
		}
		if(side.equals("left")){
			plantPic = action2Pics.get(type).get(0);
		}
	}
	
	public void upgrade(){	// upgrade sunflower to twinsunflower, repeater to gatlingpea
		if(type == SUNFLOWER){
			type = TWINSUNFLOWER;
		}
		else if(type == REPEATER){
			damage = 20;
			prep = 140;
			shot = 0;
			type = GATLINGPEA;
		}
	}
	
	public boolean getExplode(){
		return exploded;
	}
	
	public boolean getSquash(){
		return squashed;
	}
	
	public int getType(){
		return type;
	}
	
	public int[] getPos(){
		return pos;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public int getSun(){
		if(type == SUNFLOWER){
			return 1;
		}
		else if(type == TWINSUNFLOWER){
			return 2;
		}
		return 0;
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public int[] getDimensions(){
		return dimensions[type];
	}
	
	public int[] getDimensions(int x){
		return dimensions[x];
	}

	public boolean checkCollision(int[] pos){
		return plantRect.contains(pos[0],pos[1]);
	}
	
	public void draw(Graphics g,JPanel panel){
		g.drawImage(plantPic,(int)pos[0]*80+75-dimensions[type][0]/2,(int)pos[1]*98	+178-dimensions[type][1],panel);
	}
}