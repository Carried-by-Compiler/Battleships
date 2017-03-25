
public class Main
{

	public static void main(String[] args)
	{
		UserInterface ui = new UserInterface();
		AI ai = new AI();
		Game game = new Game(ui, ai);
		game.start();
	}

}
