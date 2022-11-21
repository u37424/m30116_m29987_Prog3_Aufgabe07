package de.medieninformatik.Server;

import de.medieninformatik.Theatre.Reservierung;
import de.medieninformatik.Theatre.Theatre;

import java.rmi.RemoteException;

public class ReservierungImpl implements Reservierung {
    private Theatre theatre;

    public ReservierungImpl(int col, int row) {
        theatre = new Theatre(col, row);
        System.err.println("Created");
    }

    @Override
    public boolean reservieren(int row, int column, String name) throws RemoteException {
        if (theatre.getSeat(row, column).isBooked()) return false;
        theatre.getSeat(row,column).book(name);
        return true;
    }

    @Override
    public String getName(int row, int column) throws RemoteException {
        return theatre.getSeat(row, column).getName();
    }

    @Override
    public Theatre updateTheatre() throws RemoteException {
        return theatre;
    }
}
