/**
 * 
 * Percolation
 * 
 * @author Ana Paula Centeno
 * @author Haolin (Daniel) Jin
 */

public class Percolation {

	private boolean[][] grid;          // gridSize by gridSize grid of sites; 
	                                   // true = open site, false = closed or blocked site
	private WeightedQuickUnionFind wquFind; // 
	private int 		gridSize;      // gridSize by gridSize is the size of the grid/system 
	private int         gridSquared;
	private int         virtualTop;    // virtual top    index on WeightedQuckUnionFind arrays
	private int         virtualBottom; // virtual bottom index on WeightedQuckUnionFind arrays

	/**
	* Constructor.
	* Initializes all instance variables
	*/
	public Percolation ( int n ){
		gridSize 	  = n;
		gridSquared   = gridSize * gridSize;
		wquFind       = new WeightedQuickUnionFind(gridSquared + 2);
		grid          = new boolean[gridSize][gridSize];   // every site is initialized to closed/blocked
		virtualTop    = gridSquared;
		virtualBottom = gridSquared + 1;
	} 

	/**
	* Getter method for GridSize 
	* @return integer representing the size of the grid.
	*/
	public int getGridSize () {
		return gridSize;
	}

	/**
	 * Returns the grid array
	 * @return grid array
	 */
	public boolean[][] getGridArray () {
		return grid;
	}

	/**
	* Open the site at postion (x,y) on the grid to true and add an edge
	* to any open neighbor (left, right, top, bottom) and/or top/bottom virtual sites
	* Note: diagonal sites are not neighbors
	*
	* @param row grid row
	* @param col grid column
	* @return void
	*/
	public void openSite (int row, int col) {

		grid[row][col] = true; //opens up site
		int index = row * grid.length + col; // index
		
		// checking virtual top
		if(row == 0) {
			wquFind.union(virtualTop, col);
		}

		// checking virtual bottom
		if(row == grid.length-1){
			wquFind.union(index,virtualBottom);
		}

		if(col+1 < grid.length) { // checking RIGHT if its not out of bounds
			if(grid[row][col+1] == true) {
				//System.out.println("There is an open square LEFT of the index.");
				int right = (row * grid.length) + col+1; // index of open site from right
				wquFind.union(index,right); // call functions from data structure
			}
		}

		if(col-1 >= 0) { // checking left
			if(grid[row][col-1] == true) {
				//System.out.println("There is an open square RIGHT of the index.");
				int left = (row * grid.length) + col-1; // index of open site from the LEFT
				wquFind.union(index,left); // call functions from data structure
			}
		}

		if(row+1 <= grid.length-1) { // checking bottom
			if(grid[row+1][col] == true) {
				//System.out.println("There is an open square BOTTOM of the index.");
				int bot = row+1 * grid.length + col; // index of open site from bottom
				wquFind.union(index,bot); // call functions from data structure
			}
		}

		if(row-1 >= 0) { // checking top
			if(grid[row-1][col] == true) {
				//System.out.println("There is an open square TOP of the index.");
				int top = ( (row-1) * grid.length ) + col; // index of open site from top
				wquFind.union(index,top); // call functions from data structure
			}
		}

		System.out.println("----------");

		

		// check if any open neighbors next to initial xy
		// left
		// right
		// top
		// bottom
		// add edge to each that show open
		// this can be done recursively? 

		return;
	}

	/**
	* Check if the system percolates (any top and bottom sites are connected by open sites)
	* @return true if system percolates, false otherwise
	*/
	public boolean percolationCheck () {
		
		// goes through and checks from top site to bottom site if theres a continuous path
		// can start from top left and go down to check path
				// note the roots and if size follows parent root
				// if root does not reach till end then move on to another root
		// if doesnt work then move one to the right and check the till the bottom until one path reaches
		// if none then percolation = false

		if(wquFind.find(virtualTop) == wquFind.find(virtualBottom)) { // checks if theres a valid path between each virtual top and bottom
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Iterates over the grid array openning every site. 
	 * Starts at [0][0] and moves row wise 
	 * @param probability
	 * @param seed
	 */
	public void openAllSites (double probability, long seed) {

		// Setting the same seed before generating random numbers ensure that
		// the same numbers are generated between different runs
		StdRandom.setSeed(seed); // DO NOT remove this line
		System.out.println(probability);

		// WRITE YOUR CODE HERE, DO NOT remove the line above
				// call openSite() in openAllSites()
		for(int row = 0; row < grid.length; row++) { // goes through grid array
			for (int col = 0; col < grid[row].length; col++) {

				double randNum = StdRandom.uniform();
				System.out.println(randNum);

				if(randNum >= probability) {
					System.out.println("CLOSED");
					grid[row][col] = false;
				}
				else {
					openSite(row,col);
				}
			}
		}

	}

	/**
	* Open up a new window and display the current grid using StdDraw library.
	* The output will be colored based on the grid array. Blue for open site, black for closed site.
	* @return: void 
	*/
	public void displayGrid () {
		double blockSize = 0.9 / gridSize;
		double zeroPt =  0.05+(blockSize/2), x = zeroPt, y = zeroPt;

		for ( int i = gridSize-1; i >= 0; i-- ) {
			x = zeroPt;
			for ( int j = 0; j < gridSize; j++) {
				if ( grid[i][j] ) {
					StdDraw.setPenColor( StdDraw.BOOK_LIGHT_BLUE );
					StdDraw.filledSquare( x, y ,blockSize/2);
					StdDraw.setPenColor( StdDraw.BLACK);
					StdDraw.square( x, y ,blockSize/2);		
				} else {
					StdDraw.filledSquare( x, y ,blockSize/2);
				}
				x += blockSize; 
			}
			y += blockSize;
		}
	}

	/**
	* Main method, for testing only, feel free to change it.
	*/
	public static void main ( String[] args ) {

		double p = 0.47;
		Percolation pl = new Percolation(5);
		// first num is row = top, bottom
		// second num is col = left, right
		// pl.openSite(0,2); //index
		// pl.openSite(1,2); // bottom of index
		// // pl.openSite(3,2); // below middle square
		// // pl.openSite(4,2); // bottom of grid
		// // pl.openSite(2,3); // right to middle square
		// // pl.openSite(2,1); // left to middle square

		/* 
		 * Setting a seed before generating random numbers ensure that
		 * the same numbers are generated between runs.
		 *
		 * If you would like to reproduce Autolab's output, update
		 * the seed variable to the value Autolab has used.
		 */
		long seed = System.currentTimeMillis();
		pl.openAllSites(p, seed);

		System.out.println("The system percolates: " + pl.percolationCheck());
		pl.displayGrid();
	}
}