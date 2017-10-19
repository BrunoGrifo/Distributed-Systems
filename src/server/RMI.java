package server;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Date;

public interface RMI extends Remote {
	public String sayHello(String username,String password) throws RemoteException;
	public String print_facultys() throws java.rmi.RemoteException;
	public void add_pessoas (String name, String cargo, String password,Department departamento, Faculty chosen_faculty,int telefone,int numero_cc,Date validade_cc) throws java.rmi.RemoteException;
	public ArrayList<Pessoa> return_pessoa() throws java.rmi.RemoteException;
	public ArrayList<Faculty> return_facultys() throws java.rmi.RemoteException;
	public String print_department_faculty(Faculty departs_print) throws RemoteException;
	public String imprime_pessoas() throws RemoteException;
	public boolean search_department(String name) throws RemoteException;
	public void AddDepartments(String name) throws RemoteException;
	public boolean search_faculty(String name) throws RemoteException;
	public void create_faculty(String name) throws RemoteException;
	public Eleicoes procura_eleicao(String titulo_eleicao) throws RemoteException;
	public ArrayList<Mesas> getMesas_votos_todas() throws RemoteException;
	public void addNewElection(Eleicoes new_e) throws RemoteException;
	public void removePerson(Pessoa pessoa) throws RemoteException;
	public ArrayList<Department> return_departments() throws RemoteException;
	public void removeDepartment(Department dep) throws RemoteException;
	public void removeMesaVoto(Mesas mesa) throws RemoteException;
	public void assing_departments_to_facultys(Faculty faculty_chosen) throws RemoteException;
	public void remove_departments_from_facultys(Faculty faculty_chosen) throws RemoteException;
	public String print_elections() throws RemoteException;
	public String print_tables() throws RemoteException;
	
}