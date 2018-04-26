/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
    Department departamento;
    Faculty faculdade;
    int telefone;
    int numero_cc;
    public boolean loggedIN;
    Date validade_cc;
    ArrayList<Eleicoes> elections_done;
    
    Pessoa(String nome, String cargo, String password, Department departamento,Faculty faculdade, int telefone, int numero_cc, Date validade_cc){
        this.nome=nome;
        this.cargo=cargo;
        this.password=password;
        this.departamento=departamento;
        this.faculdade=faculdade;
        this.telefone=telefone;
        this.numero_cc=numero_cc;
        this.validade_cc=validade_cc;
        elections_done = new ArrayList<Eleicoes>();
        loggedIN=false;
    }
    @Override
    public String toString(){
        return "nome:"+nome+"\ttipo: "+cargo+"\tpass:"+password+"\tdepartamento:"+departamento+"\tfaculdade:"+faculdade+"\ttelefone:"+telefone+"\tnumero cartao:"+numero_cc+"\tvalidade_cc"+validade_cc;
    }
}
