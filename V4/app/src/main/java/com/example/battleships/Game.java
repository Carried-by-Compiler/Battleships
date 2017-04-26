package com.example.battleships;

import android.util.Log;

import java.util.ArrayList;

public class Game
{
	private ArrayList<Boat> boats;
    private boolean isPVPEnabled;
	private AI opponent;
	private int boatSunkHuman;
	private int boatSunkOpponent;
    private int gameStarted;
    private int score;
    private int counter = 1;

	public Game(AI ai)
	{
		opponent = ai;
		boats = new ArrayList<Boat>();
		boatSunkHuman = 0; // keeps record of the number of human boats  that has been sunk
		boatSunkOpponent = 0; // keeps record of the number of AI boats that has been sunk
        score = 0;
    }

    public Game(boolean pvp) {
        isPVPEnabled = pvp;
        boats = new ArrayList<Boat>();
        boatSunkHuman = 0;
        boatSunkOpponent = 0;
        gameStarted = 0;
    }

    public int getScore() { return score; }
    public void setScore(int n) { this.score += n; }
    public void incrementCounter() {this.counter++; }
    public void resetCounter() { this.counter = 1; }
    public int getCounter() { return counter;}

    public void removeBoats() { boats.clear(); }

    public int getGameStarted() { return gameStarted; }
    public void setGameStarted(int state) { gameStarted = state;}

    public Boat autoGenerateBoat(int boatNumber) {
        Boat b;
        String boatName;
        ArrayList<Point> points;

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

        boats.add(b);

        return b;
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
                    /*printOutPoints(points);*/
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
        printOutPoints(points);
        return points;
    }

    private void printOutPoints(ArrayList<Point> points) {
        String output = "[";
        for (Point p: points) {
            output += p.getCoordinate() + ", ";
        }
        output += "]";
        Log.d("GAME", "Original coordinate: " + output);
    }

    private boolean checkForOverlapping(ArrayList<Point> p) {
        boolean correct = true;
        Boat b;

        for (int i = 0; i < boats.size() && correct; i++) {

            b = boats.get(i);
            /*b.debugPoints();*/

            for (int j = 0; j < p.size() && correct; j++) { // goes through each point in list.
                // check if a point in the passed in list of points hit any of the boats.
                if (b.checkIfOverlapsWithOther(p.get(j)))
                    correct = false;
            }
        }
        return correct;
    }

    public boolean checkIfValid(Point p1, Point p2) {
        boolean correct = false;
        int length = 0;

        if (boats.isEmpty()) { // carrier
            /*Log.d("GAME", "Validate Carrier");*/
            if (p1.getLength(p2) == 5)
                correct = true;
        } else if (boats.size() >= 1 && boats.size() <= 2) { // battleship
            /*Log.d("GAME", "Validate Battleship");*/
            if (p1.getLength(p2) == 4)
                correct = true;
        } else if (boats.size() >= 3 && boats.size() <= 4) { // cruiser
            /*Log.d("GAME", "Validate Cruiser");*/
            if (p1.getLength(p2) == 3)
                correct = true;
        } else if (boats.size() == 5) { // submarine
            /*Log.d("GAME", "Validate Submarine");*/
            if (p1.getLength(p2) == 3)
                correct = true;
        } else { // destroyer
            /*Log.d("GAME", "Validate Destroyer");*/
            if (p1.getLength(p2) == 2)
                correct = true;
        }
        /*Log.d("GAME", "Correct: " + correct);*/
        return correct;
    }

    public Boat createBoat(Point p1, Point p2) {

        Boat b;
        String boatName = "";
        ArrayList<Point> points = new ArrayList<Point>();

        int length = p1.getLength(p2);

        switch (length) {
            case 2:
                boatName = "Destroyer";
                break;
            case 3:
                if (boats.size() >= 3 && boats.size() <= 4)
                    boatName = "Cruiser";
                else
                    boatName = "Submarine";
                break;
            case 4:
                boatName = "Battleship";
                break;
            case 5:
                boatName = "Carrier";
                break;
        }

        points = generatePoints(boatName, p1, p2);

        b = new Boat(boatName, points);
        boats.add(b);
        return b;
    }

    public int getNumberOfBoats() {
        return boats.size();
    }

    private ArrayList<Point> generatePoints(String bN, Point p1, Point p2) {


        ArrayList<Point> points = new ArrayList<Point>();
        String letterPart, numberPart;
        int orientation = p1.getOrientation(p2);
        int max, min;

        if (orientation == 0) { // horizontal

            letterPart = p1.getLetter(); // stays the same

            if (p1.getNumber() > p2.getNumber())
            {
                max = p1.getNumber();
                min = p2.getNumber();
            } else {
                max = p2.getNumber();
                min = p1.getNumber();
            }


            for (int i = min; i <= max; i++) {

                numberPart = String.valueOf(i);
                points.add(new Point(letterPart + numberPart));

            }


        } else {

            numberPart = String.valueOf(p1.getNumber());

            if (p1.getIntLetter() > p2.getIntLetter()) {
                max = p1.getIntLetter();
                min = p2.getIntLetter();
            } else {
                max = p2.getIntLetter();
                min = p1.getIntLetter();
            }

            for (int i = min; i <= max; i++) {

                letterPart = "" + (char)i;
                points.add(new Point(letterPart + numberPart));

            }



        }

        return points;
    }

    public String getNextBoatName() {
        String name = "";

        if (boats.isEmpty()) { // carrier
            name = "Carrier";
        } else if (boats.size() >= 1 && boats.size() <= 2) { // battleship
            name = "Battleship";
        } else if (boats.size() >= 3 && boats.size() <= 4) { // cruiser
            name = "Cruiser";
        } else if (boats.size() == 5) { // submarine
            name = "Submarine";
        } else { // destroyer
            name = "Destroyer";
        }

        return name;
    }


    public boolean[] checkIfBoatWasHit(Point p) {
        boolean[] hitArray = {false, false, false};
        Boat b;

        for (int i = 0; i < boats.size() && !hitArray[1]; i++) {
            b = boats.get(i);
            hitArray[1] = b.checkIfHit(p); // checks if a boat was hit

            if (hitArray[1]) // if hit, check if a boat sunk
                hitArray[0] = b.checkIfSunk();
        }

        if (hitArray[0]) { // if a boat sunk, check if you've lost
            boatSunkHuman++;
            hitArray[2] = checkIfOpponentWon();
        }


        return hitArray;
    }

    public boolean[] checkIfEnemyWasHit(Point p) {
        boolean[] hitArray = {false, false, false}; // hitArray[0] stores if a ship is sunk. hitArray[1] stores if ship was hit. hit[2] stores if game has ended

        opponent.checkIfBoatWasHit(p, hitArray);

        if (hitArray[0])
            boatSunkOpponent++;

        hitArray[2] = checkIfHumanWon();

        return hitArray;
    }

    public ArrayList<Boat> getBoats() { return boats; }

    private boolean checkIfHumanWon() {
        if (boatSunkOpponent == 7)
            return true;
        else
            return false;
    }
    public boolean checkIfOpponentWon() {
        if (boatSunkHuman == 7)
            return true;
        else
            return false;
    }

    public Point aiGenerateGuess() {
        return opponent.guess();
    }

    /*

    Ai makes a guess of a location. Game logic checks if any
    of its boats has been hit. If so, particular boat takes it into account.
    Game also checks if boat has been sunk and if the ai has all boats.

     */
    public boolean[] aiGuesses(Point aiPoint) {
        /*
        hitArray[0] = ship sunk
        hitArray[1] = ship hit
        hitArray[2] = ai wins
        */

        boolean hitArray[] = {false, false, false};

        for (int i = 0; i < boats.size() && !hitArray[1]; i++) {
            hitArray[1] = boats.get(i).checkIfHit(aiPoint);

            if (hitArray[1])
                hitArray[0] = boats.get(i).checkIfSunk();
        }

        if (hitArray[0])
            boatSunkHuman++;

        hitArray[2] = checkIfOpponentWon();

        return hitArray;
    }
}
