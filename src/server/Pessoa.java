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
public class Pessoa implements java.io.Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String nome;
    String cargo;
    String password;
    String departamento;
    String faculdade;
    int telefone;
    int numero_cc;
    String validade_cc;
    
    Pessoa(String nome, String cargo, String password, String departamento,String faculdade, int telefone, int numero_cc, String validade_cc){
        this.nome=nome;
        this.cargo=cargo;
        this.password=password;
        this.departamento=departamento;
        this.faculdade=faculdade;
        this.telefone=telefone;
        this.numero_cc=numero_cc;
        this.validade_cc=validade_cc;
    }
    @Override
    public String toString(){
        return "nome:"+nome+"\ttipo: "+cargo+"\tpass:"+password+"\tdepartamento:"+departamento+"\tfaculdade:"+faculdade+"\ttelefone:"+telefone+"\tnumero cartao:"+numero_cc+"\tvalidade_cc"+validade_cc;
    }
}
