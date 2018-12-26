package problems.maze;

import java.util.ArrayList;
import java.util.Random;

import utils.*;

/** This class allows representing and generating a maze. */
public class Maze{

	/* Types of cells */
	public static final int EMPTY = 0;
	public static final int WALL = 1;
	public static final int HOLE = 2;
	public static final int WATER = 3;
	public static final int CAT = 4;
	public static final int CHEESE = 5;
		
	/* Size */
	protected int size = 11;
	
	/* Cells of the maze */
	protected int[][] cells;

	/* Position of the hamster. Used as fixed initial position if necessary.  */
	protected Position posHamster;	
	
	/* Position of the cheese */
	protected Position posCheese;
	
	/* Number of cats */
	protected int numCats;
	
	/* Initial position of the cats */
	protected ArrayList<Position> posCats;	
	
	/* Number of holes */
	protected int numHoles;	
	
	/* List of holes */
	protected ArrayList<Position> holeList = new ArrayList<Position>();

	/** Creates a maze with seed equals 0 */
	public Maze(int size){
		this.size = size;
		this.cells = new int[size][size];
		generate(0);
	}
	/** Creates a maze with a given seed */
	public Maze(int size, int seed){
		this.size = size;
		this.cells = new int[size][size];
		generate(seed);
	}	
	/** Returns the hamster position. */
	public Position hamsterPosition(){
		return posHamster;
	}	
	
	/** Generates the maze method. */
	private void generate(int seed){
		Random random = new Random();
		random.setSeed(seed);
		
		// Everything is empty at the beginning 
		for(int posX=0;posX<size;posX++)
			for(int posY=0;posY<size;posY++)
				cells[posX][posY]=EMPTY;
		
		// Generates two pools
		// 10% of the cells must be water
		int numPools = 2;
		int sizePool = (int) (size*size*0.1)/2;
		for (int nPool=1; nPool<=numPools; nPool++){
			int[][] pool = new int[sizePool][2];

			// Origin of the pool.
			int poolX; int poolY;
			do{
				poolX=random.nextInt(size);
				poolY=random.nextInt(size);
			}while (cells[poolX][poolY]!=EMPTY);
			cells[poolX][poolY] = WATER;
			pool[0][0]=poolX;
			pool[0][1]=poolY;
			
			// Grows 
			int cSizePool = 1;
			while(cSizePool<sizePool){
				// Selects one position of the pool randomly.
				int aux = random.nextInt(cSizePool);
				// Selects a coordinate to grow;
				int coord = random.nextInt(2);
				// Selects the direction;
				int dir = random.nextBoolean() ? 1 : -1;
				// New cell
				pool[cSizePool][0] = pool[aux][0];
				pool[cSizePool][1] = pool[aux][1];
				// New coordinate
				pool[cSizePool][coord] = pool[cSizePool][coord] + dir;
				// The new position must be inside the limits
				if (pool[cSizePool][coord]<=0 || pool[cSizePool][coord]>=size)
						continue;
				// If the position is valid, fills with water.
				if (cells[pool[cSizePool][0]][pool[cSizePool][1]] == EMPTY){
					cells[pool[cSizePool][0]][pool[cSizePool][1]] = WATER;
					cSizePool++;
				}
			}	
		}
		
		// Places the walls 
		for(int posX=0;posX<size;posX++)
			for(int posY=0;posY<size;posY++)
				if (cells[posX][posY]==EMPTY) 
					if (random.nextDouble()<0.2)
						cells[posX][posY] = WALL;
		
        // Holes 2% of the positions are holes.
		numHoles = Math.max(2, (int) ((size*size)*0.02)) +1;
		holeList = new ArrayList<Position>();
		for (int nHole=1; nHole<=numHoles-1; nHole++){
			int holeX; int holeY;
			do{
				holeY=random.nextInt(size);
				holeX=random.nextInt(size);
			}while (cells[holeX][holeY]!=EMPTY);
			cells[holeX][holeY] = HOLE;
			holeList.add(new Position(holeX, holeY));
		}
		// A hole besides the cheese so that the maze can always be solved
		cells[size-2][size-1]=HOLE;
		holeList.add(new Position(size-2, size-1));		
		
		// The cheese!
		cells[size-1][size-1]=CHEESE;
		posCheese = new Position(size-1,size-1);	
		
		// Places the cats 
		posCats = new ArrayList<Position>();
		numCats = 0;
		for(int posX=0;posX<size;posX++)
			for(int posY=0;posY<size;posY++)
				if (cells[posX][posY]==EMPTY) 
					if (random.nextDouble()<0.1) {
						cells[posX][posY] = CAT;
						posCats.add(new Position(posX,posY));
						numCats++;
					}		

		// Generates hamster position
		int hamsterX;
		int hamsterY;
		do{
			hamsterX=random.nextInt(size);
			hamsterY=random.nextInt(size);
		}while (cells[hamsterX][hamsterY]!=EMPTY);
		posHamster = new Position(hamsterX, hamsterY);		
	}

	/** Transforms the maze into a string. */
	public String toString(){
		char[] cellType = {' ', '*', 'o', '+', 'c', 'h'};
		String mazeAsStr = new String();
		int posX, posY;
		for(posY=0;posY<size;posY++){
			for(posX=0;posX<size;posX++)
				mazeAsStr+=cellType[cells[posY][posX]];
			mazeAsStr+='\n';
		}
		return mazeAsStr;	
	}	
	
	/** Main function, used for testing. */
	public static void main(String[] args) {
		Maze maze = new Maze(14,5);
		System.out.println(maze);
	}
}

	


