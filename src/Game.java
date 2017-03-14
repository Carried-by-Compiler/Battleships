import java.util.ArrayList;

public class Game
{
	private UserInterface ui;
	private ArrayList<Boat> boats;
	private  String[][] grid;

	public Game(UserInterface ui)
	{
		this.ui = ui;
		boats = new ArrayList<Boat>();
		grid = new String[10][10];

		for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid[0].length; j++) {
		        grid[i][j] = " ";
            }
        }
    }

	public void start() {

		boolean finished = false;

		do
		{
		    int choice = ui.displayOptions();
		    if (choice == 1) {
		    	finished = false;
		    	do
		    	{
		    		finished = false;
		    		// TODO put option to put in boats
			    	ui.drawBoard(grid);
			    	placeBoatsOnBoard();
	                String coordinate = ui.enterCoordinate();
	                if (coordinate.equals("q,0") || coordinate.equals("Q,0"))
	                	finished = true;
	                else
	                	enterCoordinateToGrid(coordinate);
		    	} while (!finished);
		    	finished = false;
		        // TODO put option to put in boats
		    	ui.drawBoard(grid);
                String coordinate = ui.enterCoordinate();
                enterCoordinateToGrid(coordinate);
            }
		    else
			    finished = true;
		} while (!finished);
	}

	private void placeBoatsOnBoard()
	{
		String boatNames[] = {"Destroyer", "Submarine", "Cruiser", "Battleship", "Carrier"};

		for (int i = 0; i < boatNames.length; i++)
		{
			int index;

			for (index = 0; index < 5; index++) // destroyer boats (2 spaces)(5 boats)
            {
                ArrayList<String> points = new ArrayList<String>();
                points = ui.getPoints();
            }
		}
	}

	private void enterCoordinateToGrid(String c)
    {
		// TODO write code so that it updates the grid.
		String letters[] ={"A","B","C","D","E","F","G","H","I","J"};
		int pos = 0;
		
    }
}
