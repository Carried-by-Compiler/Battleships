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

	public ArrayList<String> getPoints()
    {
        ArrayList<String> points = new ArrayList<String>();
        String message1 = "Enter starting coordinate: ";
        String message2 = "Enter ending coordinate: ";
        String p1, p2;

        System.out.print(message1);
        p1 = in.nextLine();

        System.out.print(message2);
        p2 = in.nextLine();



        return points;
    }
}
