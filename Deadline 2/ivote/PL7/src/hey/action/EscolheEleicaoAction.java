package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class EscolheEleicaoAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id_eleicao=null;

	
	public String execute() throws NumberFormatException, RemoteException {
			if(this.getHeyBean().EleicoesUser(Integer.parseInt(id_eleicao))) {
				return SUCCESS;
			}
			else {
				return "error";
			}
	}


	public String getId_eleicao() {
		return id_eleicao;
	}


	public void setId_eleicao(String id_eleicao) {
		this.id_eleicao = id_eleicao;
	}

}
