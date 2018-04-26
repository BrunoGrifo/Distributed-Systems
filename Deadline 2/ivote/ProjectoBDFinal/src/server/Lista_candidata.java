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
	int id;
	String nome_lista;
	String classe;
	int num_votos;

	Lista_candidata(int id,String nome_lista,String classe,int num_votos) {
		this.id=id;
		this.nome_lista = nome_lista;
		this.classe=classe;
		this.num_votos=num_votos;
	}

	public String toString() {
		return nome_lista;
	}
}
