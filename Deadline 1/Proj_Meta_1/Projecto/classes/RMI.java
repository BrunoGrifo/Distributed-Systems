

import java.rmi.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface RMI extends Remote {
	 
    public void AddDepartments(String name) throws java.rmi.RemoteException;
 
    public Lista cria_listas(HashMap<String, ArrayList<Lista_candidata>> listas) throws java.rmi.RemoteException;
 
    public void add_table(String name, Department department) throws java.rmi.RemoteException;
 
    public void addNewElection(Eleicoes new_election) throws java.rmi.RemoteException;
 
    public void add_pessoas(String name, String cargo, String password, Department departamento, Faculty chosen_faculty, int telefone, int numero_cc, Date validade_cc) throws java.rmi.RemoteException;
 
    public void create_faculty(String name) throws java.rmi.RemoteException;
 
    public boolean search_election(String title) throws java.rmi.RemoteException;
 
    public boolean search_faculty(String name) throws java.rmi.RemoteException;
 
    public boolean search_department(String name) throws java.rmi.RemoteException;
 
    public boolean search_table(String name) throws java.rmi.RemoteException;
 
    public String remove_faculty(String nome) throws java.rmi.RemoteException;
 
    public String remove_election(String titulo) throws java.rmi.RemoteException;
 
    public String removePerson(int cc) throws java.rmi.RemoteException;
 
    public String removeDepartment(String name) throws java.rmi.RemoteException;
 
    public String removeMesaVoto(String name) throws java.rmi.RemoteException;
 
    public String print_departments() throws java.rmi.RemoteException;
 
    public String print_facultys() throws java.rmi.RemoteException;
 
    public String print_tables() throws java.rmi.RemoteException;
 
    public String print_election2() throws java.rmi.RemoteException;
 
    public String print_elections() throws java.rmi.RemoteException;
 
    public String imprime_pessoas() throws java.rmi.RemoteException;
 
    public String print_department_faculty(Faculty departs_print) throws java.rmi.RemoteException;
 
    public ArrayList<Faculty> return_facultys() throws java.rmi.RemoteException;
 
    public ArrayList<Pessoa> return_pessoa() throws java.rmi.RemoteException;
 
    public ArrayList<Mesas> return_mesas() throws java.rmi.RemoteException;
 
    public ArrayList<Department> return_departments() throws java.rmi.RemoteException;
 
    public ArrayList<Eleicoes> return_elections() throws java.rmi.RemoteException;
 
    public String update_person(Pessoa update_person) throws java.rmi.RemoteException;
 
    public String update_faculty(Faculty update_faculty) throws java.rmi.RemoteException;
 
    public String update_election(Eleicoes update_election) throws java.rmi.RemoteException;
 
    public String update_table(Mesas update_table) throws java.rmi.RemoteException;
    
    public boolean getATerminal(int cc) throws RemoteException;
    
    public  String register_vote(String cc,String eleicao,String list, Mesas table) throws java.rmi.RemoteException;
    
    public String print_elections_for_person(String cc, Mesas table) throws java.rmi.RemoteException;
    
    public String auditoria(Eleicoes election) throws java.rmi.RemoteException;
    
    public void loginDone(String cc,Mesas mesa) throws java.rmi.RemoteException;
    
    public void notifyAdmins(String notify) throws java.rmi.RemoteException;

    
    
   public void subscribe(Server_rmi mesa) throws RemoteException;
   public void subscribeAdmin(Admin_rmi_I admin) throws RemoteException;
   public boolean status(String cc) throws RemoteException;
   public boolean login(String username, String password) throws RemoteException;
   public ArrayList<Mesas> getMesasVoto() throws RemoteException;

}