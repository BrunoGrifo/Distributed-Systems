/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author ASUS
 */
public class Pessoa implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nome;
    String cargo;
    String password;
    String morada;
    int telefone;
    int numero_cc;
    public boolean loggedIN;
    Date validade_cc;
    
    Pessoa(String nome, String cargo, String password,String morada, int telefone, int numero_cc, Date validade_cc){
        this.nome=nome;
        this.cargo=cargo;
        this.password=password;
        this.telefone=telefone;
        this.morada=morada;
        this.numero_cc=numero_cc;
        this.validade_cc=validade_cc;
        loggedIN=false;
    }
    @Override
    public String toString(){
        return "nome:"+nome+"\ttipo: "+cargo+"\tpass:"+password+"\ttelefone:"+telefone+"\tnumero cartao:"+numero_cc+"\tvalidade_cc"+validade_cc;
    }
}
