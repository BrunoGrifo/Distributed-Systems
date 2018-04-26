package server;

import java.rmi.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface Admin_rmi_I extends Remote {
	public void imprimeNotificacao(String notification) throws RemoteException;
}