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
	private int id;
	private String tipo;
	private Date start;
	private Date end;
	private String titulo;
	private String resumo;
    
    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

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
