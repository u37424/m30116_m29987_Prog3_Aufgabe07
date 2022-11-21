package de.medieninformatik.Theatre;

import java.io.Serializable;

public class Theatre implements Serializable {
    private final Seat[][] seats;
    public Theatre(int columns, int rows) {
        seats = new Seat[rows][columns];
        for (int i = 0; i < seats.length; i++) {
            for (int j = 0; j < seats[i].length; j++) {
                seats[i][j] = new Seat(i,j);
            }
        }
    }

    public double getColumns() {
        return seats[0].length;
    }

    public double getRows() {
        return seats.length;
    }

    public Seat getSeat(int row, int column) {
        return seats[column][row];
    }

    public static class Seat implements Serializable{
        private int column;
        private int row;
        private boolean booked;
        private String name;

        public Seat(int column, int row) {
            this.column = column;
            this.row = row;
        }

        public boolean isBooked() {
            return booked;
        }

        public boolean book(String name){
            if(isBooked()) return false;
            booked = true;
            this.name = name;
            return true;
        }

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }

        public String getName() {
            return name;
        }
    }
}
