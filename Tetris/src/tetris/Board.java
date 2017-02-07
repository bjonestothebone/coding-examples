package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import tetris.Shape.Tpiece;

// this class contains the game logic and guts for actually playing the game

public class Board extends JPanel implements ActionListener {

final int BoardWidth = 10;
final int BoardHeight = 22;
Timer timer;
boolean isFallingFinished = false;
boolean isStarted = false;
boolean isPaused = false;
int numLinesRemoved = 0;
int curX = 0;
int curY = 0;
int score = 0;
int Tcount = 0;
JLabel statusbar;
Shape myPiece;
Tpiece[] board;

	//constructor
	public Board(Tetris x){
		setFocusable(true);
		myPiece = new Shape();
		timer = new Timer (300, this);
		timer.start();
		
		statusbar = x.getStatusBar();
		board = new Tpiece[BoardWidth*BoardHeight];
		addKeyListener(new TAdapter());
	    clearBoard();  
			
	}//Board construct

	public void actionPerformed(ActionEvent e) {
        if (isFallingFinished) {
            isFallingFinished = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }
	
	int squareWidth() { return (int) getSize().getWidth() / BoardWidth; }
    int squareHeight() { return (int) getSize().getHeight() / BoardHeight; }
    Tpiece shapeAt(int x, int y) { return board[(y * BoardWidth) + x]; }	// little helper method for returning position of TPiece
    
    public void start()
    {
        if (isPaused)
            return;

        isStarted = true;
        isFallingFinished = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }//start()
    
    public void pause(){
    	if (!isStarted) return;
    	
    	isPaused = !isPaused;
    	if(isPaused){
    		timer.stop();
    		statusbar.setText("перерыв");}
    	else{
    		timer.start();
    		statusbar.setText(String.valueOf(numLinesRemoved));}
    	repaint();
    	
    }//pause()

     
    public void paint(Graphics g)
    { 
        super.paint(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();

        //paints all the shapes or remains of shapes that have fallen		
        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Tpiece shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != Tpiece.NoShape)
                    drawSquare(g, 0 + j * squareWidth(), boardTop + i * squareHeight(), shape);
            }
        }
        
        if (myPiece.getShape() != Tpiece.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + myPiece.x(i);
                int y = curY - myPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                           boardTop + (BoardHeight - y - 1) * squareHeight(),
                           myPiece.getShape());
            }
        }
        
        
     
    }//paint
    
    /* these next few methods describe the beahvior of pieces moving down the board*/
    
    private void dropDown(){
    	int newY = curY;
    	
    	//this is what actually spells out the movement
    	while(newY>0){
    		if (!tryMove(myPiece,curX, newY - 1))
    			break;
    		--newY;
    	}
    	pieceDropped();
    }
    
    private void oneLineDown(){if (!tryMove(myPiece, curX, curY - 1)) pieceDropped();}
    
    	
    private void pieceDropped(){
    	
    	for (int i = 0; i < 4; ++i) {
            int x = curX + myPiece.x(i);
            int y = curY - myPiece.y(i);
            board[(y * BoardWidth) + x] = myPiece.getShape();
        }

        removeFullLines(); //remove full lines if there

        if (!isFallingFinished)
            newPiece();
    }//piecedropped()
    
    /* now for the part everyone likes... actually filling a line and the needed removal of that line */
    
    private void removeFullLines(){
    	int numFullLines = 0;
    	//check for full lines from bottom up
    	for ( int i = BoardHeight - 1; i>=0; --i){
    		boolean lineFull = true;
    		for( int j = 0; j<BoardWidth; ++j){
    			if(shapeAt(j, i) == Tpiece.NoShape)
    				lineFull = false;
    				break;
    		}
    		
    		if (lineFull) {
                ++numFullLines;
                score+= 100;
                Tcount+=1;
                if(Tcount >= 5){
                	statusbar.setText("TETRIS");
                	Tcount =0;
                }
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                         board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }//if
        }
    	
    	if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText("Lines:"+" "+String.valueOf(numLinesRemoved)+" " + "Score"+ score);
            isFallingFinished = true;
            myPiece.setShape(Tpiece.NoShape);
            repaint();
        }
    	   	
    }//removeFullliens()
    
    
    
    //actually draws the tetris pieces. Need to look up RBG values ofr accruate tetris piece colors
    private void drawSquare(Graphics g, int x, int y, Tpiece shape){
        Color colors[] = { new Color(0, 0, 0), new Color(250, 102, 102), 
            new Color(102, 250, 102), new Color(102, 102, 250), 
            new Color(250, 250, 102), new Color(250, 102, 250), 
            new Color(102, 250, 250), new Color(218, 170, 0)};

        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

        g.setColor(color.brighter());
        g.drawLine(x, y + squareHeight() - 1, x, y);
        g.drawLine(x, y, x + squareWidth() - 1, y);

        g.setColor(color.darker());
        g.drawLine(x + 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + squareHeight() - 1);
        g.drawLine(x + squareWidth() - 1, y + squareHeight() - 1,
                         x + squareWidth() - 1, y + 1);
    }//drawsquare
    
    
	private void newPiece() {
		myPiece.setRandomShape();
		curX = BoardWidth/2 +1;
		curY = BoardHeight-1 +myPiece.minY();
		
		// technically checks for the losing condition in the game... cant make a new piece then game over
		 if (!tryMove(myPiece, curX, curY)) {
			 myPiece.setShape(Tpiece.NoShape);
	            timer.stop();
	            isStarted = false;
	            statusbar.setText("игра закончена");
	        }	
	}
	
	
	
	private boolean tryMove(Shape newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != Tpiece.NoShape)
                return false;
        }

        myPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

	private void clearBoard()
    {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = Tpiece.NoShape;
    }





	class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {

            if (!isStarted || myPiece.getShape() == Tpiece.NoShape) {  
                return;
            }

            int keycode = e.getKeyCode();

            if (keycode == 'p' || keycode == 'P') {
                pause();
                return;
            }

            if (isPaused)
                return;

            switch (keycode) {
            case KeyEvent.VK_LEFT:
                tryMove(myPiece, curX - 1, curY);
                break;
            case KeyEvent.VK_RIGHT:
                tryMove(myPiece, curX + 1, curY);
                break;
            case KeyEvent.VK_DOWN:
                tryMove(myPiece.rotateRight(), curX, curY);
                break;
            case KeyEvent.VK_UP:
                tryMove(myPiece.rotateLeft(), curX, curY);
                break;
            case KeyEvent.VK_SPACE:
                dropDown();
                break;
            case 'd':
                oneLineDown();
                break;
            case 'D':
                oneLineDown();
                break;
            }

        }
    }//TAdapter







	
	
}// class Board

