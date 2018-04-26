package hey.action;
import java.rmi.RemoteException;
public class CreateDepAction extends Action{

	private static final long serialVersionUID = 1L;
	private String nameDep = null;

	@Override
	public String execute() {
		System.out.println(nameDep);
		if(this.getHeyBean().search_department(nameDep)==false) {
			this.getHeyBean().create_department(nameDep.toUpperCase());
			return SUCCESS;
		}else {
			return "createDep";
		}
	}

	public String getNameDep() {
		return nameDep;
	}

	public void setNameDep(String nameDep) {
		this.nameDep = nameDep;
	}
	
	
}
