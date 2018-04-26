package hey.action;
import java.rmi.RemoteException;
public class CreateFacultyAction extends Action{

	private static final long serialVersionUID = 1L;
	private String nameFac = null;

	@Override
	public String execute() {
		System.out.println(nameFac);
		if(this.getHeyBean().search_faculty(nameFac)==false) {
			this.getHeyBean().create_faculty(nameFac.toUpperCase());
			return SUCCESS;
		}else {
			return "createFaculty";
		}
	}
	
	public String getNameFac() {
		return nameFac;
	}

	public void setNameFac(String nameFac) {
		this.nameFac = nameFac;
	}
}
