import java.util.ArrayList;

/**
 * Created by johnr on 06/03/2017.
 */
public class Boat
{
    private ArrayList<String> points;
    private String boatName;
    private int orientation;

    public Boat(String name, ArrayList<String> p)
    {
        points = p;
        boatName = name;
        //orientation = o;
    }

    public ArrayList<String> getPoints()
    {
        return points;
    }

    public String getName() { return boatName; }

    public int getOrientation()
    {
        return orientation;
    }

    @Override
    public String toString() {
        return "Boat{" +
                "points=" + points +
                ", boatName='" + boatName + '\'' +
                '}';
    }
}
