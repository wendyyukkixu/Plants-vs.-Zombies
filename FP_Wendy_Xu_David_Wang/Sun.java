// Enemy.java
// Wendy Xu

// Makes sun that the sunflowers create or the sky generates

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import java.io.*;

class Sun{
	private double[] pos; 
	private double land;		// where sun will land
	private int landCount;		// counts how long sun landed
	private boolean timeUp; 		// counts how long sun has landed
	private Image sunPic = new ImageIcon("Images/Game/sun.png").getImage();
	private Rectangle2D sunRect;
	
	public Sun(double[] pos, double land){
		this.pos = pos;
		landCount = 0;
		timeUp = false;
		sunRect = new Rectangle2D.Double(pos[0]-35,pos[1]-35,70,70);
		this.land = land;
	}
	
	public void move(){
		if(pos[1] < land){
			pos[1] += 0.3;
			sunRect.setRect(pos[0]-35,pos[1]-35,70,70);
		}
		else{
			landCount ++;
			if(landCount == 500){
				timeUp = true;
			}
		}
	}
	
	public boolean timeUp(){		// checks if time is up for sun to disappear
		return timeUp;
	}
	
	public boolean checkCollision(int[] pos){
		return sunRect.contains(pos[0],pos[1]);
	}
	
	public void draw(Graphics g, JPanel panel){		// draws sun
//		g.setColor(Color.RED);
//		g.drawRect((int)pos[0]-35,(int)pos[1]-35,70,70);	// draw rect
		g.drawImage(sunPic,(int)pos[0]-45,(int)pos[1]-45,panel);
	}
}