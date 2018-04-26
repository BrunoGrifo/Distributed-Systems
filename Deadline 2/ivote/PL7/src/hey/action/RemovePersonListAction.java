package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class RemovePersonListAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id_lista=null,cc=null;
	
	public String execute() throws NumberFormatException, RemoteException {
		if(this.getHeyBean().removePessoaLista(Integer.parseInt(id_lista),Integer.parseInt(cc))) {
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

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

}