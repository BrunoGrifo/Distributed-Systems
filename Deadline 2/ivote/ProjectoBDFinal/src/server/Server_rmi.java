package server;

import java.rmi.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface Server_rmi extends Remote {
	public String getMesaVoto() throws java.rmi.RemoteException;
}