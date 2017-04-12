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
			    	makeBoats();
			    	System.out.println(boats);
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

	private void makeBoats()
	{
		String boatNames[] = {"Destroyer", "Submarine", "Cruiser", "Battleship", "Carrier"};
        ArrayList<String> points = new ArrayList<String>();
        int orientation = 0;

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
                    placeBoatOnGrid(boat);
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // submarine (3 spaces)(4 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    placeBoatOnGrid(boat);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 4; index++) // cruisers (3 spaces)(3 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    placeBoatOnGrid(boat);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 4; index++) // cruisers (3 spaces)(3 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    placeBoatOnGrid(boat);
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 4; index++) // battleship (4 spaces)(2 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    placeBoatOnGrid(boat);
                }
            }
            else if (boatNames[i].equals("Carrier")) {
                for (index = 0; index < 4; index++) // carrier (4 spaces)(2 boats)
                {
                    points = ui.getPoints(boatNames[i]);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    placeBoatOnGrid(boat);
                }
            }
		}
	}

	private void placeBoatOnGrid(Boat b)
    {
        // initializing stuffs
        int columnPos = 0, rowPos = 0;
        boolean finished = false;
        String letters[] ={"A","B","C","D","E","F","G","H","I","J"};
        ArrayList<String> points = b.getPoints();
        System.out.println("POINTS OF BOAT: " + points);

        // loop placing points on grid
        for (int i = 0; i < points.size(); i++)
        {
            finished = false;
            String pointParts[] = points.get(i).split(","); // take parts of each point (0 is letter part, 1 is number part)
            System.out.println("Letter: " + pointParts[0] + " Number: " + pointParts[1]);
            // go through letters array to figure out the index of the letter part
            for (int j = 0; j < letters.length && !finished; j++)
            {
                if (pointParts[0].equals(letters[j]))
                {
                    columnPos = j;
                    finished = true;
                    System.out.println("COL: " + columnPos);
                }
            }
            rowPos = Integer.parseInt(pointParts[1]) - 1;
            grid[rowPos][columnPos] = "O";
        }
        ui.drawBoard(grid);
    }

	private void enterCoordinateToGrid(String c)
    {
		// TODO write code so that it updates the grid.

		int pos = 0;
		
    }
}
