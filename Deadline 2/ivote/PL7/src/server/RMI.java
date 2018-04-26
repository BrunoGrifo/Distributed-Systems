
package server;

import java.rmi.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface RMI extends Remote {
	//public void subscribe(Server_rmi mesa) throws RemoteException;

	//public void subscribeAdmin(Admin_rmi_I admin) throws RemoteException;

	public String get_location_election(int id_eleicao) throws RemoteException;
	public String get_faculdade_eleitor(int cc) throws RemoteException;
	public String get_department_eleitor(int cc) throws RemoteException;
	 public void addPessoaLista(int id_lista,int cc_user) throws RemoteException;
	    public ArrayList<Integer> pessoas_lista(int id_lista) throws RemoteException;
	    public void delete_pessoa_list(int id_lista,int cc) throws RemoteException;
	    
	    public void apaga_listas(String tipo, ArrayList<Lista_candidata> lists) throws RemoteException;
	

	public ArrayList<String> avaible_departments() throws RemoteException;

	public void update_dep_fac(String department, String faculdade) throws RemoteException;

	public ArrayList<String> dep_fac(String fac) throws RemoteException;

	public ArrayList<Eleicoes> listElectionsLive(Date data) throws RemoteException;

	 public ArrayList<Integer> cc_numbers() throws RemoteException;
	 public ArrayList<Integer> ids_votados(int cc) throws RemoteException;
	 public String register_vote_web(int id_eleicao,int id_lista,String list,int cc) throws RemoteException;


	public ArrayList<Pessoa> get_pessoas() throws RemoteException;

	public String update_person(Pessoa update_per) throws RemoteException;

	public void insert_faculty(int cc, String faculty) throws RemoteException;

	public void insert_department(int cc, String department) throws RemoteException;

	public String dep_pessoa(int cc) throws RemoteException;
	 public HashMap<Integer,HashMap<String,Timestamp>> auditoria() throws RemoteException;

	public String fac_pessoa(int cc) throws RemoteException;

	public String update_faculty(String faculty, int cc) throws RemoteException;

	public String update_department(String departamento, int cc) throws RemoteException;

	public String del_dep_person(int cc) throws RemoteException;
	public void update_dep_fac_user(int cc, String dep) throws RemoteException;
	public String del_fac_person(int cc) throws RemoteException;

	public String del_person_dep(String departamento) throws RemoteException;

	public String del_dep_fac(String departamento) throws RemoteException;

	 public ArrayList<String> return_available_mesas() throws RemoteException;


	public ArrayList<String> avaible_tables() throws RemoteException;


	public void insert_listas(int id_eleicao, String nome_lista, String tipo, int id_list) throws RemoteException;

	public ArrayList<Integer> id_listas() throws RemoteException;

	public void insert_mesa_election(String nome_mesa, int id) throws RemoteException;

	public void insert_mesa_election_list_votes(String nome_mesa, int id_lista) throws RemoteException;

	public ArrayList<Lista_candidata> get_lists_election(int id_election) throws RemoteException;

	public ArrayList<String> departments_for_elections() throws RemoteException;

	public void insert_fac_eleicao(int id, String faculdade) throws RemoteException;

	public void insert_eleicao_dep(int id, String departamento) throws RemoteException;

	public void delete_list(int id_lista) throws RemoteException;

	public String update_election(Eleicoes eleicao) throws RemoteException;

	public ArrayList<String> mesas_eleicao(int id_eleicao) throws RemoteException;

	public void delete_table_from_election(String nome_mesa) throws RemoteException;

	public String location_table(String name_table) throws RemoteException;

	public void insert_mesa_faculdade(String name, String faculdade) throws RemoteException;

	public void insert_mesa_department(String name, String departamento) throws RemoteException;

	public void delete_table_from_faculty_and_department(String nome_mesa) throws RemoteException;

	public void del_local_election(int id_election) throws RemoteException;

	public String update_department_eleicao(int id, String department) throws RemoteException;

	public String update_faculty_eleicao(int id, String faculty) throws RemoteException;

	public void del_local_election_fac(int id_election) throws RemoteException;

	public void del_local_election_dep(int id_election) throws RemoteException;

	public void add_election_fac(String faculty, int id) throws RemoteException;

	public void add_election_dep(String department, int id) throws RemoteException;

	public void delete_table_from_department(String nome_mesa) throws RemoteException;

	public void delete_table_from_faculty(String nome_mesa) throws RemoteException;

	public String update_tab_location_fac(String mesa, String faculdade) throws RemoteException;

	public String update_tab_location_dep(String mesa, String departamento) throws RemoteException;

	public HashMap<Integer, String> get_pessoas_mesas() throws RemoteException;

	public Integer get_election_of_table(String name_table) throws RemoteException;

	public Integer count_number_of_persom_table(String name_table) throws RemoteException;// tenho de por isto na
																							// condiçao

	public void insert_person_table(int cc, String mesa) throws RemoteException;

	public void delete_person_from_table(String nome_mesa, int cc) throws RemoteException;

	public Integer votos_null(int id_eleicao) throws RemoteException;

	public Integer votos_brancos(int id_eleicao) throws RemoteException;

	public ArrayList<String> listDepartments() throws RemoteException;

	public ArrayList<String> listTables() throws RemoteException;

	public boolean searchVoterForDelete(int cc) throws RemoteException;

	public boolean deleteVoter(int cc) throws RemoteException;

	public boolean searchMesaVBNA(String name) throws RemoteException;

	public boolean deleteTable(String name) throws RemoteException;

	public ArrayList<String> mesas_prontas_abrir() throws RemoteException;

	public void notifyAdmins(String notify) throws java.rmi.RemoteException;

	public boolean getATerminal(int cc) throws RemoteException;

	public void loginDone(String cc, String mesa) throws java.rmi.RemoteException;

	public Pessoa getPersonbyID(int cc) throws RemoteException;

	public String register_vote(int cc, String list, String table) throws java.rmi.RemoteException;

	public ArrayList<Lista_candidata> print_lists_for_person(int cc, String mesaVoto) throws java.rmi.RemoteException;

	public boolean searchFacultyForDelete(String name) throws RemoteException;

	public boolean deleteFaculty(String name) throws RemoteException;

	public boolean searchDepartmentForDelete(String name) throws RemoteException;

	public boolean deleteDepartment(String name) throws RemoteException;

	public ArrayList<Eleicoes> listElectionsLive(Timestamp data) throws RemoteException;

	public ArrayList<Eleicoes> listElectionToDelete(Timestamp data) throws RemoteException;

	public boolean deleteElection(int id) throws RemoteException;
	
	public boolean login(int username, String password) throws RemoteException;
	public boolean login2(String username, String password) throws RemoteException;
	
	public void insere_web(int id_eleicao) throws RemoteException;
	
	
	public void add_pessoa(int cc, String pessoa, String pass, int phone, Date expiration, String morada, String cargo)
			throws RemoteException;

	public ArrayList<String> list_facultys() throws RemoteException;
	
	public ArrayList<String> getAllUsers()  throws RemoteException;
	
	public boolean search_faculty(String name) throws java.rmi.RemoteException;
	
	public boolean search_department(String name) throws java.rmi.RemoteException;
	
	public void add_eleicao(int id, Date inicio, Date fim, String titulo, String resumo, String tipo)
			throws RemoteException;
	
	public ArrayList<Eleicoes> return_eleicoes() throws RemoteException;
	
	public void create_faculty(String name) throws java.rmi.RemoteException;
	
	public void AddDepartments(String name) throws java.rmi.RemoteException;
	
	public void add_mesa(String name) throws RemoteException;
	
	public ArrayList<String> tables() throws RemoteException;

}