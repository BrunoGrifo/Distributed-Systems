package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class ChangeEndAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id=null,data_fim = null;
	
	public String execute() throws NumberFormatException, RemoteException {
		if(this.getHeyBean().checkDataInicial(Integer.parseInt(id),data_fim)) {
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

	public String getData_fim() {
		return data_fim;
	}

	public void setData_fim(String data_fim) {
		this.data_fim = data_fim;
	}


}