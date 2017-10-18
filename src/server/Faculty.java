package server;
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
public class Faculty implements java.io.Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String name;
    public ArrayList<Department> departments;

    public Faculty(String name) {
        this.name = name;
        departments = new ArrayList<>();
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String string_departments = "";
        for (Department x : departments) {
            string_departments += "->   " + x.name + "\n\t\t";
        }
        if (!string_departments.equals("")) {
            return "Faculty: " + name + "\nDepartments: " + string_departments;
        }else{
            return "Faculty: " + name + "\nDepartments: none found\n";
        }
    }
}
