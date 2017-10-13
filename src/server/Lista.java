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
public class Lista {
    ArrayList<Pessoa> estudantes = new ArrayList<>();
    ArrayList<Pessoa> docentes = new ArrayList<>();
    ArrayList<Pessoa> funcionarios = new ArrayList<>();
    
    Lista(ArrayList<Pessoa> estudantes,ArrayList<Pessoa> docentes,ArrayList<Pessoa> funcionarios){
        this.estudantes=estudantes;
        this.docentes=docentes;
        this.funcionarios=funcionarios;
    }

}
