
public class Main
{

	public static void main(String[] args)
	{
		UserInterface ui = new UserInterface();
		Game game = new Game(ui);
		game.start();
	}

}
