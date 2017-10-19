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
	
	HashMap<String,HashMap<Lista_candidata,Integer>> todas_listas;

    Lista(HashMap<String,HashMap<Lista_candidata,Integer>> todas_listas){
        this.todas_listas=todas_listas;
    }
    public String toString(){
        String to_print="";
        for(HashMap.Entry<String, HashMap<Lista_candidata,Integer>> entry : todas_listas.entrySet()){
            to_print+=entry.getKey()+":\n";
            for(HashMap.Entry<Lista_candidata,Integer> entry2 : entry.getValue().entrySet()){
                to_print+="List: "+ entry2.getKey().toString() + "\t\tvotos until now: " + entry2.getValue() + "\n";
            }
        }
        return to_print;
    }

}
