// InstructionsMenu.java
// Wendy Xu 

// Displays real instructions

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class InstructionsMenu extends JFrame implements ActionListener{
	HelpMenu helpmenu;
	InstructionsPanel instrucpanel;
	javax.swing.Timer myTimer;
	
	public InstructionsMenu(HelpMenu helpmenu){
		super();
		this.helpmenu = helpmenu;
		setLayout(null);
		
		instrucpanel = new InstructionsPanel(this);
		instrucpanel.setSize(818,647);
		instrucpanel.setLocation(0,0);
		add(instrucpanel);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(818,647); 
    	
    	myTimer = new javax.swing.Timer(100,this);
		myTimer.start();
	}
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		instrucpanel.draw();
	}
	
	public void mainMenu(){
		helpmenu.setVisible(true);
		helpmenu.mainMenu();
		setVisible(false);
	}
	
	public static void main(String[] args){
		InstructionsMenu menu = new InstructionsMenu(null);
	}
}

class InstructionsPanel extends JPanel implements MouseListener, MouseMotionListener{
	InstructionsMenu instrucmenu;
	Image instrucPic = new ImageIcon("Images/Menus/Instructions.png").getImage();
	Image instrucHover = new ImageIcon("Images/Menus/InstructionsHover.png").getImage();
	boolean clicked;
	int[] pos;
	
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
	
	public InstructionsPanel(InstructionsMenu instrucmenu){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.instrucmenu = instrucmenu;
		clicked = false;
		pos = new int[]{0,0};
	}
	public void draw(){
		repaint();
	}
	public void paintComponent(Graphics g){
		
		Rectangle mainmenuRect = new Rectangle(325,555,153,38);
		if(mainmenuRect.contains(pos[0],pos[1])){	// mouse is in go back to main menu rect
			g.drawImage(instrucHover,0,0,this);		// draws hovering image
			if(clicked == true){					// go back to main menu click
				instrucmenu.mainMenu();
			}
		}
		else{
			g.drawImage(instrucPic,0,0,this);
		}
	}
}