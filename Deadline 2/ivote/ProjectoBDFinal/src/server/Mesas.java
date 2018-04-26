/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;
/**
 *
 * @author ASUS
 */
public class Mesas implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
    Department departamento;

	Mesas(String name, Department departamento) {
        this.name = name;
        this.departamento = departamento;
    }

    public String toString() {
        if (departamento != null) {
            return "name: " + name + "\tdepartment located: " + departamento.toString();
        } else {
            return "name: " + name + "\tdepartment located: null";
        }
    }
}
