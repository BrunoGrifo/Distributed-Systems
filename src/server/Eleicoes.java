package server;
import java.util.ArrayList;
import java.util.Date;

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
	String tipo;
    Date start;
    Date end;
    String titulo;
    String resumo;
    Lista listas;
    ArrayList<Mesas> mesas_votos;
    int votos_total;
    int votos_branco;
    
    Eleicoes(String tipo,Date start,Date end,String titulo,String resumo,Lista listas,ArrayList<Mesas> mesas_votos, int votos_total,int votos_branco){
        this.tipo=tipo;
        this.start=start;
        this.end=end;
        this.titulo=titulo;
        this.resumo=resumo;
        this.listas=listas;
        this.mesas_votos=mesas_votos;
        this.votos_total=votos_total;
        this.votos_branco=votos_branco;//falta o print das datas
    }
    
    public String toString(){
        String tables="";
        for(Mesas x:mesas_votos){
            tables+=x.toString()+"\n";
        }
        return "Title of election: "+titulo+"\t|\ttype of election: "+tipo+"\nresume of election:\n"
                +resumo+ "\nStarting data:"+start+"\nEnding date:"+end+"\n\nLists up to vote:\n"+listas.toString()+"\ntables avaible:\n"+tables+"\ntotal votes: "
                +votos_total+"\nblanc votes: "+votos_branco+"\n";
    }
}
