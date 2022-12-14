package de.medieninformatik.Server;

import de.medieninformatik.Theatre.Theatre;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

public class Server {
    public static void main(String[] args) {
        try {
            final int port = Registry.REGISTRY_PORT;
            LocateRegistry.createRegistry(port);
            Remote remote = new ReservierungImpl(35,20);
            UnicastRemoteObject.exportObject(remote, 50000);
            Naming.rebind("//:"+port+"/Reservierung", remote);
            System.out.println("Server gestartet.");
        } catch (MalformedURLException e) {

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
