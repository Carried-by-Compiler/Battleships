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
        ArrayList<String> points = new ArrayList<String>();

		for (int i = 0; i < boatNames.length; i++)
		{
			int index;

			if (boatNames[i].equals("Destroyer"))
            {
                for (index = 0; index < 5; index++) // destroyer boats (2 spaces)(5 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // submarine (3 spaces)(4 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 4; index++) // cruisers (3 spaces)(3 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 4; index++) // cruisers (3 spaces)(3 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 4; index++) // battleship (4 spaces)(2 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Carrier"))
            {
                for (index = 0; index < 4; index++) // carrier (4 spaces)(2 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
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
