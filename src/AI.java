import java.util.ArrayList;

/**
 * Created by johnr on 23/03/2017.
 */
public class AI
{
    private ArrayList<String> boats;
    private String[][] grid;

    public AI()
    {
        grid = new String[10][10];
        boats = new ArrayList<String>();
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
                   // points = generateBoatCoordinates(boatNames[i]);
                    //Boat boat = new Boat(boatNames[i], points);
                    //boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Submarine"))
            {
                for (index = 0; index < 4; index++) // submarine (3 spaces)(4 boats)
                {
                   // points = ui.getPoints(boatNames[i]);
                    //Boat boat = new Boat(boatNames[i], points);
                    //boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Cruiser"))
            {
                for (index = 0; index < 3; index++) // cruisers (3 spaces)(3 boats)
                {
                   // points = ui.getPoints(boatNames[i]);
                    //Boat boat = new Boat(boatNames[i], points);
                    //boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Battleship"))
            {
                for (index = 0; index < 2; index++) // battleship (4 spaces)(2 boats)
                {
                   // points = ui.getPoints(boatNames[i]);
                   /// Boat boat = new Boat(boatNames[i], points);
                    //boats.add(boat);
                }
            }
            else if (boatNames[i].equals("Carrier"))
            {
                for (index = 0; index < 1; index++) // carrier (4 spaces)(1 boats)
                {
                    //points = //ui.getPoints(boatNames[i]);
                  //  Boat boat = new Boat(boatNames[i], points);
                   // boats.add(boat);
                }
            }
        }
    }

    private ArrayList<String> generateBoatCoordinates(String boatName)
    {
        ArrayList<String> p = new ArrayList<String>();

        //int letter = (int)Math.random()

        return p;
    }
}
