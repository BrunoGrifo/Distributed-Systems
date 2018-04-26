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
public class Lista_candidata implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String nome_lista;
	private String classe;
	private int num_votos;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome_lista() {
		return nome_lista;
	}

	public void setNome_lista(String nome_lista) {
		this.nome_lista = nome_lista;
	}

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public int getNum_votos() {
		return num_votos;
	}

	public void setNum_votos(int num_votos) {
		this.num_votos = num_votos;
	}

	Lista_candidata(int id,String nome_lista,String classe,int num_votos) {
		this.id=id;
		this.nome_lista = nome_lista;
		this.classe=classe;
		this.num_votos=num_votos;
	}

	public String toString() {
		return "id lista:"+id+" nome_lista:"+nome_lista+" Classe:"+classe;
	}
}
