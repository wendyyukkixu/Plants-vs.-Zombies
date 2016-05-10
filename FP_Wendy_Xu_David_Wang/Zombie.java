// Zombie.java
// Wendy Xu

// Makes zombies of 7 different types and contains functions to do their actions.

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*;

class Zombie{
	private int type,movecount,changepic,changepiccount;	// counts for when to move and changepics;
	private double[] pos;
	private int health,value,damage,paralyzecount;		// point value, how long zombie stays paralyzed
	private int spawningcount;				// counter until dancing zombie can spawn
	private boolean spawn,spawned;			// spawn = can spawn (counter is up), spawned = already spawned
	private double speed;
	private Image zombiePic;								// current zombie pic
	private boolean alive, eating, burned, destroyed,slowed,paralyzed;		// alive = if zombie is still on screen, destroyed = zombie disappears
	private Rectangle2D zombieRect;					
	
	private static final int[] changepiccounts = new int[]{8,1,2,3,6,3,3,3,3};
	private static final int[][] dimensions = new int[][]{{250,136},{68,150},{68,150},{68,150},{100,154},{122,149},{86,100}};
	public static final int ZOMBIE = 0, CONEHEAD = 1, BUCKETHEAD = 2, FLAG = 3, DANCING = 4, FOOTBALL = 5, IMP = 6;; // types of zombies
	private static final int[] healths = new int[]{200,400,600,200,200,1000,150};
	private static final double[] speeds = new double[]{1,1,1,2,1,5,4};
	private static final int[] values = new int[]{100,200,300,100,200,500,200};
	private static final int[] damages = new int[]{1,1,1,1,1,3,2};
	private static final Image snowflake = new ImageIcon("Images/Game/snowflake.png").getImage();
	private static final Image paralysis = new ImageIcon("Images/Game/paralysis.png").getImage();
	private static ArrayList<ArrayList<Image>> movePics = new ArrayList<ArrayList<Image>>();
	private static ArrayList<ArrayList<Image>> eatPics = new ArrayList<ArrayList<Image>>();
	private static ArrayList<ArrayList<Image>> deadPics = new ArrayList<ArrayList<Image>>();
	private static ArrayList<ArrayList<Image>> ashPics = new ArrayList<ArrayList<Image>>();
	
	public Zombie(int type, double[] pos){
		this.type = type;
		this.pos = pos;		// pos[0] is pizel value, pos[1] shows which lane zombie is in
		zombieRect = new Rectangle2D.Double(pos[0]-45,pos[1]*98+80,80,98);
		alive = true;
		destroyed = false;
		slowed = false;
		paralyzed = false;
		movecount = 0;
		changepic = changepiccounts[type];
		changepiccount = 0;
		
		if(type == DANCING){
			spawningcount = 0;
			spawn = false;
			spawned = false;
		}
		
		health = healths[type];
		speed = speeds[type];
		value = values[type];
		damage = damages[type];

		zombiePic = movePics.get(type).get(0);
	}
	
	public static void getImages(){	
		for(int i = 0; i < 9; i ++){
			ArrayList<Image> tmp1 = new ArrayList<Image>();
			movePics.add(tmp1);
			ArrayList<Image> tmp2 = new ArrayList<Image>();
			eatPics.add(tmp2);
			ArrayList<Image> tmp3 = new ArrayList<Image>();
			deadPics.add(tmp3);
			ArrayList<Image> tmp4 = new ArrayList<Image>();
			ashPics.add(tmp4);
		}
		
		// Normal Zombie
		for(int i = 1; i < 9; i++){ 
			movePics.get(0).add(new ImageIcon(String.format("Images/Game/Zombies/Normal Zombie/Moving/normalzombie%s.png",i)).getImage());
		}
		for(int i = 1; i < 23; i++){ 
			eatPics.get(0).add(new ImageIcon(String.format("Images/Game/Zombies/Normal Zombie/Eating/eatingnormalzombie%s.png",i)).getImage());
		}
		for(int i = 1; i < 56; i++){
			deadPics.get(0).add(new ImageIcon(String.format("Images/Game/Zombies/Normal Zombie/Dying/dyingzombie%s.png",i)).getImage());
		}
		for(int i = 1; i < 21; i ++){
			ashPics.get(0).add(new ImageIcon(String.format("Images/Game/Zombies/Normal Zombie/Ashes/Zombie_charred%s.png",i)).getImage());
		}	
			
		// Conehead Zombie
		for(int i = 1; i < 54; i ++){
			movePics.get(1).add(new ImageIcon(String.format("Images/Game/Zombies/Conehead Zombie/Moving/conehead%s.png",i)).getImage());
		}
		for(int i = 1; i < 17; i ++){
			eatPics.get(1).add(new ImageIcon(String.format("Images/Game/Zombies/Conehead Zombie/Eating/eatingconehead%s.png",i)).getImage());
		}
		
		// Buckethead Zombie
		for(int i = 1; i < 41; i ++){
			movePics.get(2).add(new ImageIcon(String.format("Images/Game/Zombies/Buckethead Zombie/Moving/buckethead%s.png",i)).getImage());
		}
		for(int i = 1; i < 21; i ++){
			eatPics.get(2).add(new ImageIcon(String.format("Images/Game/Zombies/Buckethead Zombie/Eating/eatingbuckethead%s.png",i)).getImage());
		}
		
		// Flag Zombie
		for(int i = 1; i < 31; i ++){
			movePics.get(3).add(new ImageIcon(String.format("Images/Game/Zombies/Flag Zombie/Moving/flagzombie%s.png",i)).getImage());
		}
		for(int i = 1; i < 27; i ++){
			eatPics.get(3).add(new ImageIcon(String.format("Images/Game/Zombies/Flag Zombie/Eating/eatingflag%s.png",i)).getImage());
		}
		
		// Dancing Zombie 
		for(int i = 1; i < 23; i ++){
			movePics.get(4).add(new ImageIcon(String.format("Images/Game/Zombies/Dancing Zombie/Moving/movingdancing%s.png",i)).getImage());
		}
		for(int i = 1; i < 11; i ++){
			eatPics.get(4).add(new ImageIcon(String.format("Images/Game/Zombies/Dancing Zombie/Eating/eatingdancing%s.png",i)).getImage());
		}
		
		// Football Zombie
		for(int i = 1; i < 8; i ++){
			movePics.get(5).add(new ImageIcon(String.format("Images/Game/Zombies/Football Zombie/Moving/footballmoving%s.png",i)).getImage());
		}
		for(int i = 1; i < 14; i ++){
			eatPics.get(5).add(new ImageIcon(String.format("Images/Game/Zombies/Football Zombie/Eating/footballeating%s.png",i)).getImage());
		}
		
		// Imp Zombie
		for(int i = 1; i < 31; i ++){
			movePics.get(6).add(new ImageIcon(String.format("Images/Game/Zombies/Imp Zombie/Moving/imp%s.png",i)).getImage());
		}
		for(int i = 1; i < 26; i ++){
			eatPics.get(6).add(new ImageIcon(String.format("Images/Game/Zombies/Imp Zombie/Eating/eatingimp%s.png",i)).getImage());
		}
		for(int i = 1; i < 21; i++){
			deadPics.get(6).add(new ImageIcon(String.format("Images/Game/Zombies/Imp Zombie/Dying/dyingimp%s.png",i)).getImage());
		}
		for(int i = 1; i < 18; i ++){
			ashPics.get(6).add(new ImageIcon(String.format("Images/Game/Zombies/Imp Zombie/Ashes/charredimp%s.png",i)).getImage());
		}	
	}
	
	public void move(){
		if(!paralyzed){		// only moves if not paralyzed;
			if(movecount == 8){
				if(!destroyed && !eating){
					pos[0] -= speed;
					zombieRect.setRect(pos[0]-45,pos[1]*98+80,80,98);
				}
				movecount = 0;
			}
			else{
				movecount ++;
			}
		}
		if(paralyzed && alive){
			paralyzecount ++;
			if(paralyzecount == 300){		// unparalyzes zombie after 3 seconds
				paralyzed = false;
				zombiePic = movePics.get(type).get(0);
			}
		}
	}

	public void setPic(){
		if(changepiccount == changepic && !paralyzed){
			if(!eating && alive && !destroyed){				// zombie is moving forward
				int index = movePics.get(type).indexOf(zombiePic);
				if(movePics.get(type).size()-1 == index){	// goes through arraylist again
					zombiePic = movePics.get(type).get(0);
				}
				else{
					zombiePic = movePics.get(type).get(index+1);	// next pic in arraylist
				}
			}
			else if(eating && !burned){								// zombie is eating plant
				int index = eatPics.get(type).indexOf(zombiePic);
				if(eatPics.get(type).size()-1 == index){
					zombiePic = eatPics.get(type).get(0);
				}
				else{
					zombiePic = eatPics.get(type).get(index+1);
				}
			}
			else if(burned){
				int index = ashPics.get(type).indexOf(zombiePic);
				if(ashPics.get(type).size()-1 != index){		// goes through ash images once
					zombiePic = ashPics.get(type).get(index+1);
				}
				else{							// after, it is "destroyed" (disappears from screen)
					destroyed = true;
				}
			}
			else if(!alive && !destroyed){					// zombie is dying
				int index = deadPics.get(type).indexOf(zombiePic);
				if(deadPics.get(type).size()-1 != index){		// goes through dead images once
					zombiePic = deadPics.get(type).get(index+1);
				}
				else{							// after, it is "destroyed" (disappears from screen)
					destroyed = true;
				}
			}
			changepiccount = 0;
		}
		else if(!paralyzed){
			changepiccount ++;
		}
	}
	
	public void getHit(int bdamage, int btype){		// bullet or plant hits zombie
		health -= bdamage;
		
		if(type == CONEHEAD || type == BUCKETHEAD || type == FOOTBALL){		// these zombies turn into
			if(health <= 200){												// normal zombies at 200 health
				type = ZOMBIE;
				changepic = changepiccounts[type];
				speed = speeds[type];
				zombiePic = movePics.get(type).get(0);
				if(slowed){
					changepic = changepiccounts[type]*4;
				}
			}
		}
		
		if(btype == Bullet.SNOWPEA && !slowed && alive){		// snowpeas slows down zombies
			speed = speed/4;
			slowed = true;
			changepic = changepic*4;
		}
		
		if(btype == Bullet.PIKAPEA && alive){				// pikapeas paralyze zombies
			paralyzed = true;
			paralyzecount = 0;
		}
		
		if(btype == Plant.SQUASH && health <= 0 && alive){	
			type = ZOMBIE;
			eating = false;
			alive = false;
			zombiePic = deadPics.get(type).get(10);
		}	
			
		if(health <= 0 && alive){
			paralyzed = false;
			if(slowed){
				changepiccount = 0;
				changepic = changepic/4;
			}
			dead();
		}
	}
	
	public void eat(){		// if zombie meets plant
		if(type == CONEHEAD){
			changepic = 8;
		}
		eating = true;
		zombiePic = eatPics.get(type).get(0);
	}
	
	public void doneEating(){
		if(type == CONEHEAD){		// changepic conehead adjustment because of amount of 
			changepiccount = 0;		// moving pics for this single zombie type
			changepic = 1;
		}
		eating = false;
		zombiePic = movePics.get(type).get(0);
	}
	
	public void dead(){		// zombie's health hits 0, it dies, goes through "dead" images
		if(type != IMP){
			type = ZOMBIE;		// all zombies use normal dead zombie pics. except imp
			changepic = changepiccounts[type];
			speed = speeds[type];
		}
		if(eating){
			zombiePic = deadPics.get(type).get(15);
		}
		else{
			zombiePic = deadPics.get(type).get(0);
		}
		eating = false;
		alive = false;
	}
	
	public void burnt(int pdamage){		// zombie is turned to ashes due to explosives
		health -= pdamage;
		if(type != IMP){
			type = ZOMBIE;					// all zombies have same burned images except imp
			changepic = changepiccounts[type];
			speed = speeds[type];
		}
		if(health <= 0 && alive){
			burned = true;
			alive = false;
			zombiePic = ashPics.get(type).get(0);		// zombie images turn into burned images
		}
	}
	
	public void spawnCount(){		// counts when dancing zombie can spawn more dancing zombies
		if(spawningcount == 1000){
			spawn = true;
		}
		else if(spawningcount != 1000 && !spawned){
			System.out.println(spawningcount);
			spawningcount ++;
		}
	}
	
	public boolean getSpawn(){
		return spawn;
	}
	
	public boolean getSpawned(){
		return spawned;
	}
	
	public void setSpawned(boolean spawned){
		this.spawned = spawned;					
	}
	
	public int getType(){
		return type;
	}
	
	public double[] getPos(){
		return pos;
	}
	
	public int getValue(){
		return value;
	}
	
	public int getDamage(){
		return damage;
	}
	
	public boolean checkCollision(double[] bpos){
		return zombieRect.contains(bpos[0],bpos[1]);
	}
	
	public boolean isAlive(){
		return alive;
	}
	
	public boolean isEating(){
		return eating;
	}
	
	public boolean isDestroyed(){
		return destroyed;
	}
	
	public boolean isParalyzed(){
		return paralyzed;
	}
	
	public void draw(Graphics g, JPanel panel){		// draws zombie
		g.drawImage(zombiePic,(int)(pos[0]-dimensions[type][0]/2),(int)(pos[1]*98+178-dimensions[type][1]),panel);
		if(slowed){
			g.drawImage(snowflake,(int)(pos[0]-25),(int)(pos[1]*98+5),panel);
		}
		if(paralyzed){
			g.drawImage(paralysis,(int)(pos[0]-25),(int)(pos[1]*98+5),panel);
		}
	}
}