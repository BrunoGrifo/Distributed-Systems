package server;
import java.rmi.*;

public interface RMI extends Remote {
	public String sayHello() throws java.rmi.RemoteException;
}