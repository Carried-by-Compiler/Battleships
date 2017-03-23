import sun.java2d.pipe.OutlineTextRenderer;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface
{
	private Scanner in;
	
	public UserInterface() {
		in = new Scanner(System.in);
	}

	public int displayOptions()
    {
        int choice = 0;
        boolean done;
        do {
            done = true;

            try
            {
                System.out.print("Please choose:\n1) Start Game\n2) Quit\nYour choice: ");
                choice = Integer.parseInt(in.nextLine());
                System.out.println();
                if (choice < 1 || choice > 2) {
                    done = false;
                    System.out.println("Wrong input! Please try again");

                }

            }
            catch(NumberFormatException e)
            {
                System.out.println("Wrong input! Please try again");
                done = false;
            }
        } while(!done);


        return choice;
    }

	public void drawBoard(String[][] grid)
    {
        int i, j;
        System.out.print("\t" + " ");
        for (i = 65; i <= 74; i++)
            System.out.print((char)i + " ");
        System.out.println();

        // rows
        for (i = 0; i < grid.length; i++) 
        {
        	System.out.print((i + 1) + "\t|");
            // columns
        	for (j = 0; j < grid[0].length; j++)
        	{
        		System.out.print(grid[i][j] + "|");
        	}
        	System.out.println();
        	System.out.print("\t" + "-");
        	for (int z = 0; z < 10; z++)
        	{
        		System.out.print("--");
        	}
        	System.out.println();
        }
    }
	
	public String enterCoordinate()
	{
		boolean correct = true;
		String pattern = "[A-Z]{1},[1-10]{1}";
		String input;
		
		do
		{
			System.out.print("Enter a coordinate (E.g \"A1\"): ");
			input = in.nextLine();

			if (input.isEmpty())
			{
				correct = false;
				System.out.println("Please enter a coordinate!\n");
				input = "";
				
			}
			else if (input.matches(pattern))
			{
				correct = false;
				System.out.println("Incorrect input!\n");
				input = "";
			}
		} while (!correct);
		
		return input;
	}

	/*
    Obtains the points on where each boat should be placed.

    returns a list of points as an arraylist
	 */
	public ArrayList<String> getPoints(String boatName)
    {
        // intializing
        ArrayList<String> points = new ArrayList<String>();
        String message1 = "Enter starting coordinate (E.g \"A,1\"): ";
        String message2 = "Enter ending coordinate (E.g \"A,3\"): ";
        String error = "Incorrect placement of ship! Please try again.";
        String p1, p2;
        String partsEnter[], partsEnd[];

        boolean correct;

        do { // continue with loop until the boat's coordinates have been obtained successfully

            correct = true;
            // takes in input
            System.out.print(message1);
            p1 = in.nextLine();

            System.out.print(message2);
            p2 = in.nextLine();

            //validation of input

            partsEnter = p1.split(","); // starting coordinate
            partsEnd = p2.split(","); // ending coordinate

            // if same column
            if (partsEnter[0].equalsIgnoreCase(partsEnd[0]))
            {
                System.out.println(partsEnd[0].charAt(0));
                int distance = Integer.parseInt(partsEnd[1]) - Integer.parseInt(partsEnter[1]);
                System.out.println("DISTANCE: " + distance);
                switch(boatName)
                {
                    case "Destroyer":
                        if (distance != 2)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Submarine":
                        if (distance != 3)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Cruiser":
                        if (distance != 3)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Battleship":
                        if (distance != 4)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Carrier":
                        if (distance != 5)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                }
            }
            else if (partsEnter[1].equalsIgnoreCase(partsEnd[1]))  // if same row
            {
                System.out.println("ENTERED IF 2");
                int distance = (int)partsEnd[0].charAt(0) - (int)partsEnter[0].charAt(0);
                System.out.println("DISTANCE: " + distance);
                switch(boatName)
                {
                    case "Destroyer":
                        if (distance != 2)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Submarine":
                        if (distance != 3)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Cruiser":
                        if (distance != 3)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Battleship":
                        if (distance != 4)
                            correct = false;
                        else
                           points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                    case "Carrier":
                        if (distance != 5)
                            correct = false;
                        else
                            points = placePointsIntoArrayList(distance, partsEnter, partsEnd);
                        break;
                }
            }
            else
                correct = false;

            if (!correct)
                System.out.println(error + "\n");


        } while (!correct);





        return points;
    }

    private ArrayList<String> placePointsIntoArrayList(int d, String[] pS, String[] pE)
    {
        ArrayList<String> points = new ArrayList<String>();

        if (pS[1].equals(pE[1])) // same row
        {
            int startingLetter = (int)pS[0].charAt(0);
            int endingLetter = (int)pE[0].charAt(0);

            for (int i = startingLetter; i <= endingLetter; i++)
            {
                points.add((char)i + "," + pS[1]);
            }
        }
        else if (pS[0].equalsIgnoreCase(pE[0]))
        {
            for (int i = Integer.parseInt(pS[1]); i <= Integer.parseInt(pE[1]); i++)
            {
                points.add(pS[0] + "," + i);
            }
        }

        return points;
    }
}
