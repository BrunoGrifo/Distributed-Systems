package hey.action;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;

import server.Eleicoes;

public class ChangeResumeAction extends Action {
	private static final long serialVersionUID = 4L;
	private String id=null,resume = null;
	
	public String execute() throws NumberFormatException, RemoteException {
		if(this.getHeyBean().updateResumos(Integer.parseInt(id),resume)) {
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

	public String getResume() {
		return resume;
	}

	public void setResume(String resume) {
		this.resume = resume;
	}

}