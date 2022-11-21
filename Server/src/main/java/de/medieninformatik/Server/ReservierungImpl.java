package de.medieninformatik.Server;

import de.medieninformatik.Theatre.Reservierung;
import de.medieninformatik.Theatre.Theatre;

import java.rmi.RemoteException;
import java.util.concurrent.*;

public class ReservierungImpl implements Reservierung {
    private Theatre theatre;
    private ExecutorService executor;

    public ReservierungImpl(int col, int row) {
        theatre = new Theatre(col, row);
        System.err.println("Created");
        executor = Executors.newCachedThreadPool();
    }

    @Override
    public boolean reservieren(int row, int column, String name) throws RemoteException {
        if (theatre.getSeat(row, column).isBooked()) return false;
        theatre.getSeat(row,column).book(name);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
