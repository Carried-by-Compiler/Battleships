package com.example.battleships;

import android.util.Log;

import java.util.ArrayList;

/*
 * Created by johnr on 23/03/2017.
 *
 */


public class AI {
    private ArrayList<Boat> boats;

    public AI() {
        boats = new ArrayList<Boat>();

        generateBoats();
    }

    private void generateBoats() {
        Boat b;
        String boatName;
        ArrayList<Point> points;
        int boatNumber;

        for (boatNumber = 0; boatNumber < 7; boatNumber++) {

            if (boatNumber == 0) {
                boatName = "Carrier";
                points = generatePoints(5);
            } else if (boatNumber >= 1 && boatNumber <= 2) {
                boatName = "Battleship";
                points = generatePoints(4);
            } else if (boatNumber >= 3 && boatNumber <= 4) {
                boatName = "Cruiser";
                points = generatePoints(3);
            } else if (boatNumber == 5) {
                boatName = "Submarine";
                points = generatePoints(3);
            } else {
                boatName = "Destroyer";
                points = generatePoints(2);
            }



            b = new Boat(boatName, points);
            b.debugPoints();
            boats.add(b);
        }

    }

    private ArrayList<Point> generatePoints(int length) {

        boolean correct;
        ArrayList<Point> points = new ArrayList<Point>();
        int startingLetter, startingNumber, orientation;
        Point newPoint;

        do {
            correct = true;


            // getting letter for initial coordinate.
            startingLetter = (int)(Math.random() * ((74 - 65) + 1)) + 65;
            // getting number for initial coordinate.
            startingNumber = (int)((Math.random() * 10) + 1);
            // determine orientation of boat
            orientation = (int)((Math.random() * 2) + 1);

            if (startingLetter + length > 74 || startingNumber + length > 10) {
                correct = false;
            } else {
                int j;
                points = new ArrayList<Point>();

                if (orientation == 1) { // vertical orientation -
                    for (j = 1; j <= length; j++) {
                        newPoint = new Point((char)startingLetter + String.valueOf(startingNumber));
                        points.add(newPoint);
                        startingLetter++;


                    }
                   /* printOutPoints(points);*/
                    correct = checkForOverlapping(points); // pass in created points to verify if valid.
                } else if (orientation == 2) {
                    for (j = 1; j <= length; j++) {
                        newPoint = new Point((char)startingLetter + String.valueOf(startingNumber));
                        points.add(newPoint);
                        startingNumber++;

                    }
                    /*printOutPoints(points);*/
                    correct = checkForOverlapping(points); // pass in created points to verify if valid.
                }

            }


        } while (!correct);

        for (int i = 0; i < points.size(); i++) {
            Log.d("AI", points.get(i).getCoordinate());
        }
        Log.d("AI", "AI passes through");

        return points;
    }

    private boolean checkForOverlapping(ArrayList<Point> p) {
        boolean hit = false;
        boolean correct = true;
        Boat b;

        for (int i = 0; i < boats.size() && hit == false; i++) {

            b = boats.get(i);
            /*b.debugPoints();*/

            for (int j = 0; j < p.size() && hit == false; j++) { // goes through each point in list.
                // check if a point in the passed in list of points hit any of the boats.
                if (b.checkIfOverlapsWithOther(p.get(j)) == true)
                    correct = false;
            }
        }
        return correct;
    }

    public void checkIfBoatWasHit(Point p, boolean[] hitArray) {

        for (int i = 0; i < boats.size() && !hitArray[1]; i++) {
            hitArray[1] = boats.get(i).checkIfHit(p);

            if (hitArray[1])
                hitArray[0] = boats.get(i).checkIfSunk();
        }

    }

    // Ai generates a random coordinate
    public Point guess() {
        int letter, number;
        Point aiGuess;

        letter = (int)(Math.random() * ((74 - 65) + 1)) + 65;
        number = (int)((Math.random() * 10) + 1);
        aiGuess = new Point((char)letter + "" + number);

        return aiGuess;
    }
}

