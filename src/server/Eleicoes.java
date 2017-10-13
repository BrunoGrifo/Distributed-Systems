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
public class Eleicoes {
    String tipo;
    String inicio;
    String fim;
    String titulo;
    String resumo;
    Lista listas;
    ArrayList<Mesas> mesas_votos;
    
    Eleicoes(String tipo,String inicio,String fim,String titulo,String resumo,Lista listas,ArrayList<Mesas> mesas_votos){
        this.tipo=tipo;
        this.inicio=inicio;
        this.fim=fim;
        this.titulo=titulo;
        this.resumo=resumo;
        this.listas=listas;
        this.mesas_votos=mesas_votos;
    }
}
