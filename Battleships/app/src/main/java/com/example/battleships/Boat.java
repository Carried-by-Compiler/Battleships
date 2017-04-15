package com.example.battleships;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by johnr on 06/03/2017.
 */
public class Boat
{
    private ArrayList<Point> points;
    private String boatName;
    private boolean hasSunk;
    private int hitCounter;

    public Boat(String name, ArrayList<Point> p)
    {
        points = p;
        boatName = name;
        hasSunk = false;
        hitCounter = 0;
    }

    public ArrayList<Point> getPoints() {
        return  points;
    }

    public boolean checkIfHit(Point point)
    {
        boolean hit = false;

        for (int i = 0; i < points.size() && !hit; i++) {

            if (points.get(i).equals(point))
                hit = true;

        }

        /*if (points.contains(point))
            hit = true;*/
        if (hit) {
            hitCounter = hitCounter + 1;
            Log.d(boatName, "" + hitCounter);
        }

        return hit;
    }

    public boolean checkIfOverlapsWithOther(Point point)
    {
        boolean hit = false;
        for (int i = 0; i < points.size() && !hit; i++) {
            if (points.get(i).equals(point)) {
                hit = true;
            }

        }
        return hit;
    }

    public boolean checkIfSunk()
    {
        if (hitCounter == points.size())
            return true;
        else
            return false;
    }

    public void debugPoints() {
        String output = "[";
        for (Point p: points) {
            output += p.getCoordinate() + ", ";
        }
        output += "]";
        Log.d(boatName, "Boat Points: " + output);
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
