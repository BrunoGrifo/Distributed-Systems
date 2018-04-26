

import java.rmi.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface Server_rmi extends Remote {
	public Mesas getMesaVoto() throws java.rmi.RemoteException;
}