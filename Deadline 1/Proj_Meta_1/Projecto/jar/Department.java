/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class Department implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String name;
    Department(String name){
        this.name=name;
    }
    public String toString(){
        return name;
    }
}
