import java.util.ArrayList;

/**
 * Created by johnr on 23/03/2017.
 */
public class AI
{
    private ArrayList<Boat> boats;
    private String[][] grid;
    private UserInterface ui; // for debugging purposes only

    public AI()
    {
        grid = new String[10][10];
        boats = new ArrayList<Boat>();
        ui = new UserInterface();
        /*

        for (int i = 0; i < 10; i++)
        {
            for (int j = 0; j < 10; j++)
            {
                grid[i][j] = " ";
            }
        }*/

    }

    public void generateBoats()
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
                    points = generateBoatCoordinates(boatNames[i], 5);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 2; index++) // Battleship (4 spaces)(2 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 4);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 3; index++) // cruisers (3 spaces)(3 boats)
                {
                   points = generateBoatCoordinates(boatNames[i], 3);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // battleship (3 spaces)(4 boats)
                {
                   points = generateBoatCoordinates(boatNames[i], 3);
                   Boat boat = new Boat(boatNames[i], points);
                   boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                }
            }
            else if (boatNames[i].equals("Destroyer"))
            {
                for (index = 0; index < 5; index++) // carrier (2 spaces)(5 boats)
                {
                    points = generateBoatCoordinates(boatNames[i], 2);
                    Boat boat = new Boat(boatNames[i], points);
                    boats.add(boat);
                    //displayAiBoats(boat);
                    //System.out.println(boat.getPoints());
                }
            }

        }
    }

    public boolean[] checkIfHit(String coordinate)
    {
        boolean hit = false;
        boolean sunk = false;
        boolean results[] = new boolean[2];
        Boat b;

        // go through each boat, checking if the entered coordinate matches the position coordinate of any boat
        for (int i = 0; i < boats.size() && !hit; i++)
        {
            b = boats.get(i);               // takes a boat from the list of boats
            hit = b.checkIfHit(coordinate); // checks if entered coordinate has hit a part of that boat
            // if it has been hit, check if it has sunk
            if (hit)
                sunk = b.checkIfSunk();
        }
        results[0] = hit;
        results[1] = sunk;

        // check if the boat has been sunk

        return results;
    }

    private void displayAiBoats(Boat b)
    {
        ArrayList<String> points = b.getPoints();
        String letters[] ={"A","B","C","D","E","F","G","H","I","J"};
        String parts[];
        String n = "";
        int pos = 0;

        for (int i = 0; i < points.size(); i++)
        {
            switch (b.getName())
            {
                case "Destroyer": n ="D"; break;
                case "Submarine": n = "S"; break;
                case "Cruiser": n = "C"; break;
                case "Battleship": n = "B"; break;
                case "Carrier": n = "A"; break;
            }

            parts = points.get(i).split(",");
            for (int j = 0; j < letters.length; j++)
            {
                if (parts[0].equals(letters[j]))
                    pos = j;
            }
            grid[pos][Integer.parseInt(parts[1])] = n;
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
        ArrayList<String> points = new ArrayList<String>();
        //int lettersAsInts[] = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74};
        int pos = 0;
        int orientation = 0;
        int letter = 0;
        int number = 0;
        int distance;
        boolean correct = false;

        // Determine orientation
        do
        {
            correct = true;
            points = new ArrayList<String>();

            // Determine initial letter position
            letter = (int)(Math.random() * ((74 - 65) + 1)) + 65;
            // Determine initial number position
            number = (int)((Math.random() * 10) + 1);
            //Determine orientation of boat
            orientation = (int)((Math.random() * 2) + 1);

            if (letter + length > 74 || number + length > 10)
            {
                correct = false;
            }
            else
            {
                int j;
                if (orientation == 1)
                {
                    for (j = 1; j <= length; j++)
                    {
                        points.add((char)letter + "," + number);
                        letter++;
                    }
                    correct = checkForOverlapping(points);
                }
                else
                {
                    for (j = 0; j < length; j++)
                    {
                        points.add((char)letter + "," + number);
                        number++;
                    }
                    correct = checkForOverlapping(points);
                }
            }

        } while (!correct);

        return points;
    }

    private boolean checkForOverlapping(ArrayList<String> p)
    {
        boolean correct = true;
        for (int i = 0; i < boats.size() && correct; i++)
        {
            Boat b = boats.get(i);

            for (int j = 0; j < p.size(); j++)
                if (b.checkIfHit(p.get(j)) == true)
                    correct = false;
        }
        return correct;
    }
}
