import java.util.ArrayList;

public class Game
{
	private UserInterface ui;
	private ArrayList<Boat> boats;
	private  String[][] grid;
	private AI opponent;
	private int boatSunkHuman;
	private int boatSunkAi;

	public Game(UserInterface ui, AI ai)
	{
		this.ui = ui;
		opponent = ai;
		boats = new ArrayList<Boat>();
		boatSunkHuman = 0; // keeps record of the number of human boats  that has been sunk
		boatSunkAi = 0; // keeps record of the number of AI boats that has been sunk
		grid = new String[10][10];

		for (int i = 0; i < grid.length; i++) {
		    for (int j = 0; j < grid[0].length; j++) {
		        grid[i][j] = " ";
            }
        }
    }

	public void start() {

		boolean finished = false;
		int turn = 0;
		String coordinate = "";
		boolean[] results;

		do
		{
		    int choice = ui.displayStartMenu();
		    if (choice == 1) {
		        opponent.generateBoats();
		    	finished = false;
		    	do
		    	{
		    		finished = false;
		    		// TODO Write code for auto generating boats for human
		    		int accept = ui.autoGenerateBoats();
		    		if (accept == 1)
                    {

                    }
			    	ui.drawBoard(grid);
			    	placeBoatsOnBoard();
			    	if (turn == 0)
                    {

                        coordinate = ui.enterCoordinate(); // Human Guesses
                        if (coordinate.equals("q,0") || coordinate.equals("Q,0"))
                            turn = 2;
                        else
                        {
                            results = opponent.checkIfHit(coordinate);
                            if (results[0] == true)
                                ui.displayHit(coordinate);
                            else
                                ui.displayMiss(coordinate);
                            if (results[1] == true)
                                ui.displaySunk();
                            turn = 1;
                        }
                    }

			    	if (turn == 1)
                    {
                        // TODO write code for AI's Turn
                        //coordinate = opponent.getCoordinate(); // AI Guesses
                        turn = 0;
                    }
                    // TODO Code for end game
		    	} while (turn != 2);
		    	finished = false;
		    	/*ui.drawBoard(grid);
                String coordinate = ui.enterCoordinate();
                enterCoordinateToGrid(coordinate);*/
            }
		    else
			    finished = true;
		} while (!finished);
	}

	private void placeBoatsOnBoard()
	{
		String boatNames[] = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        ArrayList<String> points = new ArrayList<String>();

		for (int i = 0; i < boatNames.length; i++)
		{
			int index;

			if (boatNames[i].equals("Carrier"))
            {
                for (index = 0; index < 1; index++) // Carrier boats (5 spaces)(1 boats)
                {
                    points = ui.getPoints(boatNames[i], 5);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 2; index++) // Battleship (4 spaces)(2 boats)
                {
                    points = ui.getPoints(boatNames[i], 4);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 3; index++) // cruisers (3 spaces)(3 boats)
                {
                    points = ui.getPoints(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // Submarine (3 spaces)(4 boats)
                {
                    points = ui.getPoints(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
            else if (boatNames[i].equals("Destroyer"))
            {
                for (index = 0; index < 5; index++) // Destroyer (2 spaces)(5 boats)
                {
                    points = ui.getPoints(boatNames[i], 2);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    drawBoatOntoGrid(boat, boatNames[i]);
                }
            }
		}
	}

    private void drawBoatOntoGrid(Boat boat, String boatName) {
	    String identification = "";
        String letters[] ={"A","B","C","D","E","F","G","H","I","J"};
        String parts[] = new String[0];
        boolean found;
        int letterPos = 0;

	    switch(boatName)
        {
            case "Destroyer":   identification = "D"; break;
            case "Submarine":   identification = "S"; break;
            case "Cruiser":     identification = "C"; break;
            case "Battleship":  identification = "B"; break;
            case "Carrier":     identification = "A"; break;
        }

        ArrayList<String> points = boat.getPoints();
	    System.out.println("POINTS: " + points);
	    for (int i = 0; i < points.size(); i++)
        {
            parts = points.get(i).split(",");
            found = false;
            for (int j = 0; j < letters.length && !found; j++)
            {
                if (parts[0].equalsIgnoreCase(letters[j]))
                {
                    found = true;
                    letterPos = j;
                }
            }
            grid[Integer.parseInt(parts[1]) - 1][letterPos] = identification;
        }
	    ui.drawBoard(grid);
    }
}
