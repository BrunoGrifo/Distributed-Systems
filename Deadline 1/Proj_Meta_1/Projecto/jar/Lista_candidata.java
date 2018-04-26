/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ASUS
 */
public class Lista_candidata implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nome_lista;

	Lista_candidata(String nome_lista) {
		this.nome_lista = nome_lista;
	}

	public String toString() {
		return nome_lista;
	}
}
