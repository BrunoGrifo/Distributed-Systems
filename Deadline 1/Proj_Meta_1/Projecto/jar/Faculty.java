

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ASUS
 */
public class Faculty implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
	ArrayList<Department> departments;

	Faculty(String name) {
		this.name = name;
		departments = new ArrayList<Department>();
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String toString() {
		String string_departments = "";
		for (Department x : departments) {
			string_departments += "\n->" + x.name;
		}
		if (!string_departments.equals("")) {
			return name + "\nDepartments: " + string_departments;
		} else {
			return name + "\nDepartments: none found";
		}
	}
}
