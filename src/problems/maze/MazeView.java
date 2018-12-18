package problems.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import learning.Action;
import learning.LearningProblem;
import learning.State;
import utils.Position;
import visualization.ProblemView;
import visualization.ProblemVisualization;

/** This class allows showing a maze */
public class MazeView extends ProblemView{
	
	// Colors
	private static Color grassColor= new Color(102,153,51);
	private static Color boundsColor = new Color(200,200,200);
	private static Color holeColor = new Color(120,100,50);
	private static Color sandColor = new Color(255,222,173);
	private static Color waterColor = new Color(34, 79, 189);
	
	// Images
	public static final Image hamster = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/hamster.png");
	public static final Image cheese = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/queso.png");	
	public static final Image cat = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/cat.png");
	public static final Image cat2 = Toolkit.getDefaultToolkit().getImage("problems/maze/imgs/cat2.png");
	BufferedImage mazeImage;											// Image of the maze
	Image scaledHamster, scaledCheese, scaledCat, scaledCat2;		// Scaled images
	
	// Maze 	
	private Maze maze;				                // Maze	
	
	// Some measures of interest
	private int sizePx; 				        // Size of the view
	private int cellSizePx;						// Size of each cell	
	private int marginPx = 20;					// Size of the margin	
	private int boundWidthPx = 10;              // Size of the bounds
	private double speedPx;	                	// Speed of the hamster (pixels each 0.1s)		
	
	// Current Positions of the hamster, cheese and cats.
	// The positions of the hamster and the cats change with time. 
	private Position posHamster;			     // Position of the hamster.
	private Position posCheese;				 // Position of the cheese. 
	private ArrayList<Position> posCats;     // Positions of the cats.
	
	private Position posHamsterPx;		     // Position of the hamster in pixels.
	private Position posCheesePx;  	         // Position of the cheese in pixels.
	private ArrayList<Position> posCatsPx;   // Positions of the cats in pixels.	
	
	// If the hamster is hidden
	private boolean hiddenHamster = false;
	
	/**
	 * Builds the view panel given a maze and its size in pixels
	 */
	public MazeView(LearningProblem mazeProblem, int sizePx){
		this.maze = ((MazeProblem)mazeProblem).getMaze();
		this.sizePx = sizePx;		
		// Calculates dimensions
		cellSizePx = (sizePx-(2*marginPx)) / this.maze.size;
		speedPx = 1*((cellSizePx)/10);	                          // (Two cells/second)
		
		// Scales the images according to the size
		scaledHamster = hamster.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledCheese = cheese.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledCat = cat.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);
		scaledCat2 = cat2.getScaledInstance((int)(cellSizePx*0.5), (int)(cellSizePx*0.5), Image.SCALE_SMOOTH);	
		
		// Current state
		currentState = null;
		
		// Positions (some of them used for cleanliness)
		posHamster = null;
		posCheese = maze.posCheese;
		posCats = maze.posCats;
		
		// Positions of the images in pixels
		posHamsterPx = null;
		posCheesePx = posImageToPx(posCheese);
		posCatsPx = new ArrayList<Position>(posCats.size());
		for (Position catPos: posCats)
			posCatsPx.add(posImageToPx(catPos));
		
		// Generates the background
		generateMazeImage();	
	}
	
	/** Generates the main maze image (Background) */
	private void generateMazeImage(){
		// Creates the image
		mazeImage=new BufferedImage(sizePx, sizePx, BufferedImage.TYPE_INT_RGB);
		Graphics2D mazeGraphics2D = mazeImage.createGraphics();		
		mazeGraphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		
		// Creates the graphics
		mazeGraphics2D.setColor(sandColor);
		mazeGraphics2D.fillRect(0, 0, sizePx, sizePx);	
		
		// Paints the bounds (tiles)
		mazeGraphics2D.setColor(boundsColor);
		for (int pos=0;pos<maze.size;pos++){
			int posBoundPx= posToPx(pos);	
			mazeGraphics2D.fill3DRect(posBoundPx, marginPx/2, cellSizePx, marginPx/2,true);	
			mazeGraphics2D.fill3DRect(marginPx/2, posBoundPx, marginPx/2, cellSizePx,true);
			mazeGraphics2D.fill3DRect(sizePx-marginPx, posBoundPx, marginPx/2, cellSizePx,true);		
			// Output
			if (pos!=maze.size-1)
				mazeGraphics2D.fill3DRect(posBoundPx, sizePx-marginPx, cellSizePx, marginPx/2,true);			
		}
		// Corners
		mazeGraphics2D.fill3DRect(marginPx/2, marginPx/2, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(sizePx-marginPx, marginPx/2, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(marginPx/2, sizePx-marginPx, marginPx/2, marginPx/2,true);
		mazeGraphics2D.fill3DRect(sizePx-marginPx, sizePx-marginPx, marginPx/2, marginPx/2,true);	
		
		// Paints the walls
		mazeGraphics2D.setColor(grassColor);
		Position posWallPx;
		for (int posX=0;posX<maze.size;posX++){
			for (int posY=0;posY<maze.size;posY++)
				if (maze.cells[posX][posY]==Maze.WALL){
					posWallPx= posToPx(new Position(posX, posY));
					RoundRectangle2D wallShape = new RoundRectangle2D.Double(posWallPx.x+cellSizePx*0.01, posWallPx.y+cellSizePx*0.01,         // Position
							                                                 cellSizePx-cellSizePx*0.02, cellSizePx-cellSizePx*0.02, 4, 4);    // Shape
					mazeGraphics2D.fill(wallShape);		
				}
		}	
		
		// Paints the water
		mazeGraphics2D.setColor(waterColor);
		Position posWaterPx;
		for (int posX=0;posX<maze.size;posX++){
			for (int posY=0;posY<maze.size;posY++)
				if (maze.cells[posX][posY]==Maze.WATER){
					posWaterPx= posToPx(new Position(posX, posY));
					RoundRectangle2D wallShape = new RoundRectangle2D.Double(posWaterPx.x+cellSizePx*0.01, posWaterPx.y+cellSizePx*0.01,       // Position
							                                                 cellSizePx-cellSizePx*0.02, cellSizePx-cellSizePx*0.02, 4, 4);    // Shape
					mazeGraphics2D.fill(wallShape);		
				}
		}
		
		// Paints the holes
		mazeGraphics2D.setColor(holeColor);
		Position posHolePx;
		for (int posX=0;posX<maze.size;posX++){
			for (int posY=0;posY<maze.size;posY++)
				if (maze.cells[posX][posY]==Maze.HOLE){
					posHolePx= posToPx(new Position(posX, posY));
					Ellipse2D holeShape = new Ellipse2D.Double(posHolePx.x+cellSizePx*0.1, posHolePx.y+cellSizePx*0.1, cellSizePx*0.8, cellSizePx*0.8);
					mazeGraphics2D.fill(holeShape);			
				}
		}	
	}
	
	/** Paints the component (updates images)*/
	public void paintComponent(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,	RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.drawImage(mazeImage,0,0,this);	
			
		// Paints the cheese, which is always present
		if ((currentState==null) || !((MazeState)currentState).position.equals(posCheese))
			graphics2D.drawImage(scaledCheese, posCheesePx.x, posCheesePx.y, this);	
		
		// Paints the cats
		for (int idCat=0;idCat<maze.numCats;idCat++) {
			Position posCat = posCats.get(idCat);
			Position posCatPx = posCatsPx.get(idCat);
			if ((currentState!=null) &&  (posCat.equals(posHamster)))
				graphics2D.drawImage(scaledCat, posCatPx.x, posCatPx.y, this);	
			else
				graphics2D.drawImage(scaledCat2, posCatPx.x, posCatPx.y, this);	
		}
		
		// Paints the hamster
		if ((currentState!=null) && !hiddenHamster && (maze.cells[posHamster.x][posHamster.y]!=Maze.CAT))
			graphics2D.drawImage(scaledHamster, posHamsterPx.x, posHamsterPx.y, this);	
		
		
		graphics2D.dispose();		
	}
	
	// UTILS

	/** Changes a position to pixels */
	private int posToPx(int x){ 
		return (int) (x * cellSizePx + marginPx); 
	}
	
	/** Changes a position to pixels */
	private Position posToPx(Position position){
		return new Position((int) (position.x * cellSizePx + marginPx), (int) (position.y * cellSizePx + marginPx)); 
	}
	
	/** Calculates the position of an image, given the position of the cell.*/
	private Position posImageToPx(Position position){
		int x = (int) (position.x * cellSizePx + marginPx + cellSizePx*0.25);
		int y = (int) (position.y * cellSizePx + marginPx + cellSizePx*0.25);
		return new Position(x, y); 
	}
	
	/** Calculates the distance between two points. */
	private double distPositions(Position a, Position b) {
		// Calculates the distance
		int distX = a.x-b.x;
		int distY = a.y-b.y;
		return Math.sqrt(distX*distX+distY*distY);
	}
	
	/** Returns the dimension of the view */
    public Dimension getPreferredSize() {
        return new Dimension(sizePx, sizePx);
    }	
	
	/** Shows the current state. */
	@Override
	protected void showState() {
		posHamster = ((MazeState)currentState).position;
		posHamsterPx = posImageToPx(posHamster);
		repaint();
	}

	/** Shows the current action. */
	@Override
	protected void showAction(Action action, State toState) {
		// Reads the new state
		MazeState newState = (MazeState) toState;
		Position newPosHamster = newState.position;
		Position newPosHamsterPx = posImageToPx(newPosHamster);
		
		// Calculates the distance
		double dist = distPositions(posHamsterPx, newPosHamsterPx);
		// Calculates the number of frames.
		int numFrames = (int) (dist/speedPx);
		// If there is waters, movement is slower
		if (maze.cells[posHamster.x][posHamster.y]==Maze.WATER)
			numFrames = numFrames*2;
		// If the action is dive, hides the hamster and moves faster
		if (maze.cells[posHamster.x][posHamster.y]== Maze.HOLE && action==MazeAction.DIVE) {
			hiddenHamster = true;
			numFrames = numFrames/2;
		}
		
		for ( ; numFrames>0; numFrames--){
			posHamsterPx.x=posHamsterPx.x+(newPosHamsterPx.x-posHamsterPx.x)/numFrames;
			posHamsterPx.y=posHamsterPx.y+(newPosHamsterPx.y-posHamsterPx.y)/numFrames;
			// Waits 0.05 seconds
			repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {}
		}		
		
		// Whatever happens, the hamster is not hidden at the end. 
		hiddenHamster = false;
	}
	
	/** Main function, used for testing. */
	public static void main(String[] args) {
//		MazeProblem mazeProblem = new MazeProblem(10, 0);
//		ProblemVisualization windowMaze = new ProblemVisualization(mazeProblem, 600);
//
//		MazeState initialState = new MazeState(7,0);
//		windowMaze.setState(initialState);
//		// Notice that visualization does not depend on the action.
//		windowMaze.takeAction(null, new MazeState(7,1));
//		windowMaze.takeAction(null, new MazeState(7,2));
//		windowMaze.takeAction(MazeAction.DIVE, new MazeState(7,9));
//		windowMaze.takeAction(null, new MazeState(8,9));
//		windowMaze.takeAction(null, new MazeState(9,9));

	}	

}
