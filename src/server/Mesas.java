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
	String departamento;
    Mesas(String departamento){
        this.departamento=departamento;
    }
}
