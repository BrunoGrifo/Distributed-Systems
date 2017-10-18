package server;
import java.util.HashMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ASUS
 */
public class Lista implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	HashMap<String,HashMap<String,Integer>> todas_listas;

    Lista(HashMap<String,HashMap<String,Integer>> todas_listas){
        this.todas_listas=todas_listas;
    }

}
