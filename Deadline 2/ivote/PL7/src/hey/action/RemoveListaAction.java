package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class RemoveListaAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id=null;
	
	public String execute() throws NumberFormatException, RemoteException {
		if(this.getHeyBean().removeList(Integer.parseInt(id))) {
			return SUCCESS;
		}
		else {
			return "error";
		}
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}