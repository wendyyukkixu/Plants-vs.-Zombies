// HighScore.java
// David Wang & Wendy Xu 

// Organizes the highscores from a text file into the game
// displays the top 5 high scores

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.io.*;


class HighScore extends JFrame implements ActionListener{

	MainMenu menu;
	HighScorePanel highscorepanel;
	
	javax.swing.Timer myTimer;

    public HighScore(MainMenu menu){
    	super();
    	this.menu = menu;
    	setLayout(null);
    	
    	highscorepanel = new HighScorePanel(this);
    	highscorepanel.setSize(818,647);
    	highscorepanel.setLocation(0,0);
    	add(highscorepanel);
    	
    	setVisible(true);
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setSize(818,647);
    	
    	myTimer = new javax.swing.Timer(100,this);
		myTimer.start(); 
    }
    
    public void actionPerformed(ActionEvent evt){
    	Object source = evt.getSource();
    	highscorepanel.draw();
    }
    
    public void mainMenu(){
    	menu.playMusic();
    	menu.setVisible(true);
    	setVisible(false);
    }
    
    public static void main(String[]args){
    	HighScore highscore = new HighScore(null);
    }
}

class HighScorePanel extends JPanel implements MouseListener,MouseMotionListener{
	HighScore highscoreframe;
	int[] pos;
	private Rectangle backButton;
	private Image highscorePic;
	private Image highScoreHover;
	boolean clicked; 
	
	private ArrayList<Image> scoreNums = new ArrayList<Image>();		// image of numbers (to print scores)
	
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
	
	
	public HighScorePanel(HighScore highscoreframe){
		super();
		setFocusable(true);
		grabFocus();
		addMouseListener(this);
		addMouseMotionListener(this);
		this.highscoreframe = highscoreframe;
		highscorePic = new ImageIcon("Images/Game/Highscores/Highscores.png").getImage();
		highScoreHover = new ImageIcon("Images/Game/Highscores/HighscoresBack.png").getImage();
		backButton = new Rectangle(95,17,144,112);
		pos = new int[]{0,0};
		clicked = false;
		
		for(int i = 0; i < 10; i ++){
			scoreNums.add(new ImageIcon(String.format("Images/Game/Highscores/%s.png",i)).getImage());
		}
	}
	
	public void draw(){
		repaint();
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(highscorePic,0,0,this);
		
		if (backButton.contains(pos[0],pos[1])){
			g.drawImage(highScoreHover,0,0,this);
			if(clicked){
				highscoreframe.mainMenu();	
			}
		}
		clicked = false;
		
		ArrayList<Integer> scores = new ArrayList<Integer>();

		try{
			Scanner inFile = new Scanner(new BufferedReader(new FileReader("highscores.txt")));
			
			while(inFile.hasNextInt()){		// takes all scores saved in textfile
				scores.add(inFile.nextInt());
			}
			inFile.close();
		}
		catch(Exception excep){
				System.out.println("Oh no! "+excep);
		}
		
		for(int i = 0; i < Math.min(scores.size(),5); i++){		// only display top 5 high scores
			String scoreDigits = ""+scores.get(i);			// use to get number of digits
			int temp = scores.get(i);						// use temp score to draw single digits
			
			for(int j = 0; j < scoreDigits.length(); j ++){
				g.drawImage(scoreNums.get(temp%10),630-50*j,248+i*60,this);
				temp /= 10;			// take away a single digit every time
			}
		}	
	}
}