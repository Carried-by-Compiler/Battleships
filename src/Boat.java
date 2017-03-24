import java.util.ArrayList;

/**
 * Created by johnr on 06/03/2017.
 */
public class Boat
{
    private ArrayList<String> points;
    private String boatName;
    private boolean hasSunk;
    private int hitCounter;

    public Boat(String name, ArrayList<String> p)
    {
        points = p;
        boatName = name;
        hasSunk = false;
        hitCounter = 0;
    }

    public ArrayList<String> getPoints() {
        return  points;
    }

    public boolean checkIfHit(String point)
    {
        boolean hit = false;

        if (points.contains(point))
            hit = true;
        if (hit) hitCounter++;

        return hit;
    }

    public boolean checkIfSunk()
    {
        if (hitCounter == points.size())
            return true;
        else
            return false;
    }

    public String getName() { return boatName; }

    @Override
    public String toString() {
        return "Boat{" +
                "points=" + points +
                ", boatName='" + boatName + '\'' +
                '}';
    }
}
