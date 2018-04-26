package server;
import java.util.ArrayList;
import java.util.Date;
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
public class Eleicoes implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int id;
	String tipo;
    Date start;
    Date end;
    String titulo;
    String resumo;
    
    Eleicoes(int id,String tipo,Date start,Date end,String titulo,String resumo){
    	this.id=id;
    	this.tipo=tipo;
        this.start=start;
        this.end=end;
        this.titulo=titulo;
        this.resumo=resumo;
    }
    
    public String toString(){
        return "Title of election: "+titulo+"\t|\ttype of election: "+tipo+"\nresume of election:\n"
                +resumo+ "\nStarting data:"+start+"\nEnding date:"+end;
    }
}
