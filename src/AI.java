import java.util.ArrayList;

/**
 * Created by johnr on 23/03/2017.
 */
public class AI
{
    private ArrayList<Boat> boats;
    private String[][] grid;

    public AI()
    {
        grid = new String[10][10];
        boats = new ArrayList<Boat>();
    }

    private void generateBoats()
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
                    points = generateBoatCoordinates(boatNames[i], 2);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // submarine (3 spaces)(4 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 3; index++) // cruisers (3 spaces)(3 boats)
                {
                   points = generateBoatCoordinates(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 2; index++) // battleship (4 spaces)(2 boats)
                {
                   points = generateBoatCoordinates(boatNames[i], 4);
                   Boat boat = new Boat(boatNames[i], points);
                   boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Carrier"))
            {
                for (index = 0; index < 1; index++) // carrier (4 spaces)(1 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 5);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                }
            }
        }
    }

    /*
    generate points for each boat
    boatname is passed in to determine which boat to create points for.

    Determine whether the boat is going vertical or horizontal
    Generate a random string for column part (65 - 74)
    Generate a random number for row part (1 - 10)
    check if there is space for the boat
    - If space
      - if horizontal
        increment the letters
      - else
        increment the numbers
    - else
      start again

     */
    private ArrayList<String> generateBoatCoordinates(String boatName, int length)
    {
        // initializing
        ArrayList<String> p = new ArrayList<String>();
        int orientation = 0;
        int distance = 0;
        int letterPart = 0;
        int numberPart = 0;
        int letterJ = 74;
        int letterA = 65;
        boolean correct = false;

        do {
            correct = true;
            // if a lot of duplicates, consider taking them out and make a singular point

            // Getting the orientation
            orientation = (int)(Math.random() * (2 - 1)) + 1;
            // Getting inital position of boat
            letterPart = (int)(Math.random() * (letterJ - letterA)) + letterA;
            System.out.println("LETTER GENERATED: " + letterPart);
            numberPart = (int)(Math.random() * (10 - 1)) + 1;
            System.out.println("NUMBER GENERATED: " + numberPart);

            if (orientation == 1) // horizontal
            {
                distance = letterJ - (letterPart - 1);
                System.out.println("DISTANCE: " + distance);
                if (distance > length)
                    correct = false;
                else {

                    for (int i = letterPart; i <= letterPart + length; i++)
                    {
                        p.add((char)i + "," + numberPart);
                    }
                }
            }
            else // vertical
            {
                distance = 10 - (numberPart - 1);
                System.out.println("DISTANCE: " + distance);
                if (distance > length)
                    correct = false;
                else {

                    for (int i = letterPart; i <= letterPart + length; i++)
                    {
                        System.out.println("CURRENT POS: " + (char)i + "," + numberPart);
                        p.add((char)i + "," + numberPart);
                    }
                }
            }


        } while (!correct);
        grid[letterPart][numberPart] = "O";

        return p;
    }
}
