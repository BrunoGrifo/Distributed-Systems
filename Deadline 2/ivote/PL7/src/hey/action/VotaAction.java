package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class VotaAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id_lista=null;
	
	public String execute() throws NumberFormatException, RemoteException {
		if(this.getHeyBean().registaVote(id_lista)) {
			return SUCCESS;
		}
		else {
			return "error";
		}
	}

	public String getId_lista() {
		return id_lista;
	}

	public void setId_lista(String id_lista) {
		this.id_lista = id_lista;
	}


}