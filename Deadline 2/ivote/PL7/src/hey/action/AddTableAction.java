package hey.action;

import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class AddTableAction extends Action {
	private static final long serialVersionUID = 4L;
	private String nome_mesa = null,id=null;

	
	public String execute() {
			if(this.getHeyBean().checkIDtables(nome_mesa,Integer.parseInt(id))) {
				return SUCCESS;
			}
			else {
				return "error";
			}
	}

	public String getNome_mesa() {
		return nome_mesa;
	}

	public void setNome_mesa(String nome_mesa) {
		this.nome_mesa = nome_mesa;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
