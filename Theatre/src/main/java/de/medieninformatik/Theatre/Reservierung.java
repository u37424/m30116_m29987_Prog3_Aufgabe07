package de.medieninformatik.Theatre;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.Future;

public interface Reservierung extends Remote {
    Theatre updateTheatre() throws RemoteException;

    boolean reservieren(int row, int column, String result) throws RemoteException;

    String getName(int row, int column) throws RemoteException;
}
