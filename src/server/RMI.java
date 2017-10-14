package server;
import java.rmi.*;

public interface RMI extends Remote {
	public String sayHello(String username,String password) throws java.rmi.RemoteException;
}