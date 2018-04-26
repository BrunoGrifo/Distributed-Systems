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
	private String nome;
    private String cargo;
    private String password;
    private String morada;
    private int telefone;
    private int numero_cc;
    private boolean loggedIN;
    
    public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCargo() {
		return cargo;
	}
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMorada() {
		return morada;
	}
	public void setMorada(String morada) {
		this.morada = morada;
	}
	public int getTelefone() {
		return telefone;
	}
	public void setTelefone(int telefone) {
		this.telefone = telefone;
	}
	public int getNumero_cc() {
		return numero_cc;
	}
	public void setNumero_cc(int numero_cc) {
		this.numero_cc = numero_cc;
	}
	public boolean isLoggedIN() {
		return loggedIN;
	}
	public void setLoggedIN(boolean loggedIN) {
		this.loggedIN = loggedIN;
	}
	public Date getValidade_cc() {
		return validade_cc;
	}
	public void setValidade_cc(Date validade_cc) {
		this.validade_cc = validade_cc;
	}
	private Date validade_cc;
    
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
