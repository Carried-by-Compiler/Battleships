package com.example.battleships;

import android.util.Log;

/**
 * Created by johnr on 05/04/2017.
 */

public class Point {

    private String letter;
    private String number;
    private String coordinate;


    public Point(String p) {
        coordinate = p;
        letter = p.substring(0, 1);

        if (p.length() == 3) {
            number = p.substring(1, p.length());
        } else {
            number = p.substring(p.length() - 1);
        }

    }

    public String getLetter() { return letter; }

    public int getIntLetter() {
        int i = (int)letter.charAt(0);

        Log.d("POINT", "Int of " + letter + " is: " + i);
        return i;
    }

    public int getNumber() { return Integer.parseInt(number); }

    public String getCoordinate() { return coordinate; }

    public int getLength(Point p2) {

        int d = 0;
        int p2Letter = p2.getIntLetter();
        int p2Number = p2.getNumber();


        // if same row with p2
        if (getIntLetter() == p2Letter) {

            // if p2's number is greater than this object's number
            if (p2Number > getNumber()) {
                d = (p2Number - getNumber()) + 1;
            } else { // this number is greater than or equal to p2's number
                d = (getIntLetter() - p2Number) + 1;
            }

        } else if (getNumber() == p2Number) { // if the same column

            if (p2Letter > getIntLetter()) {
                d = (p2Letter - getIntLetter()) + 1;
            } else {
                d = (getIntLetter() - p2Letter) + 1;
            }

        }
        Log.d("POINT", "Length: " + d);
        return d;
    }

    public int getOrientation(Point p2) {
        int orientation = 0; // 0 = horizontal, 1 = vertical
        int p2Letter = p2.getIntLetter();
        int p2Number = p2.getNumber();

        if (getIntLetter() == p2Letter) {
            orientation = 0;
        } else if (getNumber() == p2Number) {
            orientation = 1;
        }

        return orientation;
    }

    public boolean equals(Point p) {
        return p.getCoordinate().equals(coordinate);
    }
}
