import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
public class ConwaysLife extends JFrame implements Runnable, MouseListener {
// member data
private BufferStrategy strategy;
private Graphics offscreenBuffer;
//2D Boolean Array
private boolean gameState[][] = new boolean[40][40];


private boolean isGameInProgress = false;

// constructor
public ConwaysLife () {
 //Display the window, centred on the screen
 Dimension screensize =
java.awt.Toolkit.getDefaultToolkit().getScreenSize();
 int x = screensize.width/2 - 400;
 int y = screensize.height/2 - 400;
 setBounds(x, y, 800, 800);
 setVisible(true);
 this.setTitle("Conway's game of life");

 // initialise double-buffering
 createBufferStrategy(2);
 strategy = getBufferStrategy();
 offscreenBuffer = strategy.getDrawGraphics();

 // register the Jframe itself to receive mouse events
 addMouseListener(this);

 // initialise the game state
 // this loops and makes them all dead
 for (x=0;x<40;x++) {
 for (y=0;y<40;y++) {
 gameState[x][y]=false;
 }
 }
 // create and start our animation thread
 Thread t = new Thread(this);
 t.start();
}



// thread's entry point
public void run() {
while ( 1==1 ) { // while true
// 1: sleep for 1/5 sec
try {
Thread.sleep(200);
} catch (InterruptedException e) { }
// 2: animate game objects 
if(isGameInProgress) {
	conwayRules(); // when playing apply rules
}
// 3: force an application repaint
this.repaint();
}
}



// mouse events which must be implemented for MouseListener
public void mousePressed(MouseEvent e) {
	// modify to check for button clicks and add the functionality
	//where click location
    int x = e.getX();
    int y = e.getY();
    Random rd = new Random();
    //start button co-ords/location - click to start 
    if (!isGameInProgress) {
    	   // start button location/dimension (10, 40, 80, 40);
        if (x >= 10 && x <= 90 && y >= 40 && y <= 80) {
            isGameInProgress = true; // start the game
            //random button co-ords/location - click to randomise
                   //g.fillRect(100, 40, 80, 40);
        } else if (x >= 100 && x <= 180 && y >= 40 && y <= 80) {
            for (int i = 0; i < 40; i++) {// iterate through
                for (int j = 0; j < 40; j++) {
                    gameState[i][j] = rd.nextBoolean(); // randomise the state of game,
                 
                }
            }//load
            //load button loc/dim(190, 40, 80, 40);
        } else if (x >= 190 && x <= 270 && y >= 40 && y <= 80) {
            loadGame(); 
        }
        // Save button
         //save button loc/dim(280, 40, 80, 40);
        else if (x >= 280 && x <= 360 && y >= 40 && y <= 80) {
            saveGame();
        }
    }

  
    	// determine which cell of the gameState array was clicked on
    	// IDE change recommend 
    	 int x1 = e.getX()/20;
    	  int y1 = e.getY()/20;
    	    //;
     // toggle the state of the cell
        gameState[x1][y1] = !gameState[x1][y1]; 
     // request an extra repaint, to get immediate visual feedback
        this.repaint();
    
}


// mouse events that must be implemented for MouseListener - workshop - do not need
 public void mouseReleased(MouseEvent e) { }
 public void mouseEntered(MouseEvent e) { }
 public void mouseExited(MouseEvent e) { }
 public void mouseClicked(MouseEvent e) { }
 
 
 
//
// application's paint method
public void paint(Graphics g) {
g = offscreenBuffer; // draw to offscreen buffer
// clear the canvas with a big black rectangle
g.setColor(Color.BLACK);
g.fillRect(0, 0, 800, 800);

// redraw all game objects
g.setColor(Color.WHITE);
 for (int x=0;x<40;x++) { // iterate
 for (int y=0;y<40;y++) {
 if (gameState[x][y]) { // if alive
 g.fillRect(x*20, y*20, 20, 20); // fill white
 }
 }
 }
//draw the Start and Random buttons
if (!isGameInProgress) {
	  //start button
	  g.setColor(Color.GREEN); // all after will use
	  g.fillRect(10, 40, 80, 40); //local and dimensions
	  g.setColor(Color.BLACK); 
	  g.drawString("Start", 35, 65); //string location
	  //random button
	  g.setColor(Color.GREEN); // all after will use
	  g.fillRect(100, 40, 80, 40); //local and dimensions
	  g.setColor(Color.BLACK);
	  g.drawString("Random", 115, 65); //string location 
	  //load but
	  g.setColor(Color.GREEN); // all after will use
	  g.fillRect(190, 40, 80, 40); //local and dimensions
	  g.setColor(Color.BLACK);
	  g.drawString("Load", 215, 65); //string location 
	  //save but
	  g.setColor(Color.GREEN); // all after will use
	  g.fillRect(280, 40, 80, 40);  //local and dimensions
	  g.setColor(Color.BLACK);
	  g.drawString("Save", 305, 65); //string location 
}
// flip the buffers
strategy.show();
} // end paint
private void conwayRules() { 
  /*Any live cell with fewer than two live neighbours dies as if caused by underpopulation.
	Any live cell with two or three live neighbours  lives on to the next generation.
	Any live cell with more than three live neighbours  dies, as if by overpopulation.
	Any dead cell with exactly three live neighbours  becomes a live cell, as if by reproduction.*/

	boolean[][] future = new boolean[40][40];
	// replace the M & N for 40
	for (int l = 0; l < 40; l++) {
		for (int m = 0; m < 40; m++) {
			 // Finding no of alive neighbours 
			int aliveNeighbours = 0;
			for (int i = -1; i <= 1; i++) {
			 for (int j = -1; j <= 1; j++) {
					// Ensure grid bounds
				if ((l + i >= 0 && l + i < 40) && (m + j >= 0 && m + j < 40)) {
						// Increment the alive neighbours count if the neighbour cell is alive
					if (gameState[l + i][m + j]) {
							aliveNeighbours++;
					}//if
				}//if
			}
			}//for

			  // The cell needs to be subtracted from
            // its neighbours as it was counted before
			//checks if the cell is alive and subtracts 1
			if (gameState[l][m] == true) {
			    aliveNeighbours -= 1;
			}

			// Implementing the Rules of Life

			// Cell is lonely and dies
			if ((gameState[l][m]) && (aliveNeighbours < 2)) {
				future[l][m] = false;
			}
			// Cell dies due to overpopulation
			 else if ((gameState[l][m]) && (aliveNeighbours > 3)) {
				future[l][m] = false;
			}
			// A new cell is born
			 else if ((!gameState[l][m]) && (aliveNeighbours == 3)) {
				future[l][m] = true;
			}
			// Remains the same
			 else {
				future[l][m] = gameState[l][m];
			}
	 }
	}
	// update gameState array
	gameState = future;
}


private void saveGame() {
	String filename = "lifegame.txt";
    try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        // Loop through the grid's row and column 
        for (int x = 0; x < 40; x++) {
            for (int y = 0; y < 40; y++) {
            	 // if game state is true write '1' else '0' 
               if(gameState[x][y]) {
            	   writer.write("1");
                }else {
                	  writer.write("0");
                }
                }
            writer.newLine();
        }//end for
        writer.close();
    } catch (IOException e) {
    	System.out.println("An error occurred.");
        e.printStackTrace();
    }
}

private void loadGame() {
	
	String line = null;
	String filename = "lifegame.txt";
    try {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
      // Loop through the grid's rows
        for (int x = 0; x < 40; x++) {
        	//read line
            line = reader.readLine();
            //// Loop through the grid's columns
            for (int y = 0; y < 40; y++) {
            	// Set gamestate cell to true if ==1
            	if (line.charAt(y) == '1') {
            	    gameState[x][y] = true;
            	} else {
            	    gameState[x][y] = false;
            	}
            }
        }//end for and close
        reader.close();
    } catch (IOException e) {
    	 System.out.println("An error occurred.");
         e.printStackTrace();
    }
}


// application entry point
public static void main(String[] args) {
ConwaysLife w = new ConwaysLife();
}
}