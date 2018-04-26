package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class ChangeStartAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id=null,data_inicio = null;
	
	public String execute() throws NumberFormatException, RemoteException {
		if(this.getHeyBean().checkDataFinal(Integer.parseInt(id),data_inicio)) {
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

	

	public String getData_inicio() {
		return data_inicio;
	}


	public void setData_inicio(String data_inicio) {
		this.data_inicio = data_inicio;
	}


}