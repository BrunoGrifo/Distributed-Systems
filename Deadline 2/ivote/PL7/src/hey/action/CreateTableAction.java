package hey.action;
import java.rmi.RemoteException;
import java.util.ArrayList;
public class CreateTableAction extends Action{

	private static final long serialVersionUID = 1L;
	private String nameTable = null;

	@Override
	public String execute() {
		System.out.println(nameTable);
		if(search_table(nameTable)) {
			this.getHeyBean().add_mesa(nameTable);
			return SUCCESS;
		}else
			return "createTable";
		
		
	}

	public String getNameTable() {
		return nameTable;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}
	
	public boolean search_table(String name) {
		ArrayList<String> tabs = this.getHeyBean().get_tables();
		for (String x : tabs) {
			if (x.toUpperCase().equals(name.toUpperCase())) {
				return false;
			}
		}
		return true;
	}

	
	
}