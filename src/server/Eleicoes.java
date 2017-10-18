package server;
import java.util.ArrayList;

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
	public String tipo;
    public String inicio;
    public String fim;
    public String titulo;
    public String resumo;
    public Lista listas;
    public ArrayList<Mesas> mesas_votos;
    public int votos_total;
    public int votos_branco;
    
    public Eleicoes(String tipo,String inicio,String fim,String titulo,String resumo,Lista listas,ArrayList<Mesas> mesas_votos, int votos_total,int votos_branco){
        this.tipo=tipo;
        this.inicio=inicio;
        this.fim=fim;
        this.titulo=titulo;
        this.resumo=resumo;
        this.listas=listas;
        this.mesas_votos=mesas_votos;
        this.votos_total=votos_total;
        this.votos_branco=votos_branco;
    }
}
