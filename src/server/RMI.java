package server;
import java.rmi.*;

public interface RMI extends Remote {
	public String sayHello(String username,String password) throws RemoteException;
	public void regista_pessoa_teste_inputs() throws RemoteException;
	public void create_faculty() throws RemoteException;
	public void create_department() throws RemoteException;
}