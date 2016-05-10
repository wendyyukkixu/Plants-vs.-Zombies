// HelpMenu.java
// Wendy Xu 

// Displays help menu (a note from the zombies)

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

class HelpMenu extends JFrame implements ActionListener{
	MainMenu menu;
	HelpPanel helppanel;
	InstructionsMenu instruc;
	javax.swing.Timer myTimer;
	
	public HelpMenu(MainMenu menu){
		super();
		this.menu = menu;
		setLayout(null);
		
		helppanel = new HelpPanel(this);
		helppanel.setSize(818,647);
		helppanel.setLocation(0,0);
		add(helppanel);
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(818,647); 
    	
    	myTimer = new javax.swing.Timer(100,this);
		myTimer.start();
	}
	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		helppanel.draw();
	}
	
	public void instructionsMenu(){
		instruc = new InstructionsMenu(this);
		setVisible(false);
	}
	
	public void mainMenu(){
		menu.setVisible(true);
		setVisible(false);
	}
	
	public static void main(String[] args){
		HelpMenu menu = new HelpMenu(null);
	}
}

class HelpPanel extends JPanel implements MouseListener, MouseMotionListener{
	HelpMenu menu;
	Image helpPic = new ImageIcon("Images/Menus/helpmenu.png").getImage();
	Image helpHover = new ImageIcon("Images/Menus/helpmenu - hover.png").getImage();
	Image hereHover = new ImageIcon("Images/Menus/helpmenu - here.png").getImage();
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
	
	public HelpPanel(HelpMenu menu){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.menu = menu;
		clicked = false;
		pos = new int[]{0,0};
	}
	public void draw(){
		repaint();
	}
	public void paintComponent(Graphics g){
		
		Rectangle mainmenuRect = new Rectangle(325,522,155,38);
		Rectangle instrucRect = new Rectangle(515,12,58,25);
		
		if(mainmenuRect.contains(pos[0],pos[1])){	// mouse is in go back to main menu rect
			g.drawImage(helpHover,0,0,this);		// draws hovering image
			if(clicked == true){					// go back to main menu click
				menu.mainMenu();
			}
		}
		else if(instrucRect.contains(pos[0],pos[1])){	// go view real instructions
			g.drawImage(hereHover,0,0,this);
			if(clicked == true){
				menu.instructionsMenu();
			}
		}
		else{
			g.drawImage(helpPic,0,0,this);
		}
	}
}