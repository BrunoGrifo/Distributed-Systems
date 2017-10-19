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
}