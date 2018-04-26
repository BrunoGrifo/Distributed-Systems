package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class AddListaAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id=null,tipo=null,nome_lista=null;
	
	public String execute() throws NumberFormatException, RemoteException {
		if(this.getHeyBean().checkList(Integer.parseInt(id),tipo,nome_lista)) {
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

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getNome_lista() {
		return nome_lista;
	}

	public void setNome_lista(String nome_lista) {
		this.nome_lista = nome_lista;
	}

}