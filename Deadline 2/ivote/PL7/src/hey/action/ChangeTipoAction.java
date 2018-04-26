package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class ChangeTipoAction extends Action {
	private static final long serialVersionUID = 4L;
	private String novo_tipo = null,id=null,department=null,faculty=null;
	
	
	
	public String execute() throws NumberFormatException, RemoteException {
			if(this.getHeyBean().checkEleicaoTipo(novo_tipo,Integer.parseInt(id),department,faculty)) {
				return SUCCESS;
			}
			else {
				return "error";
			}
	}


	public String getNovo_tipo() {
		return novo_tipo;
	}


	public void setNovo_tipo(String novo_tipo) {
		this.novo_tipo = novo_tipo;
	}


	public String getDepartment() {
		return department;
	}


	public void setDepartment(String department) {
		this.department = department;
	}


	public String getFaculty() {
		return faculty;
	}


	public void setFaculty(String faculty) {
		this.faculty = faculty;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


}
