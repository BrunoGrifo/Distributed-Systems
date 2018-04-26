/**
 * Raul Barbosa 2014-11-07
 */
package hey.model;

import java.util.ArrayList;
import java.util.Date;
import java.rmi.Naming;
import java.rmi.NotBoundException;

import static java.lang.System.out;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import server.Eleicoes;
import server.Lista_candidata;
import server.Pessoa;
import server.RMI;

public class HeyBean extends UnicastRemoteObject{
	private RMI server = null;
	private int loggedIN;
	private String username; // username and password supplied by the user
	private String password;
	private int eleicao;

	public int getEleicao() {
		return eleicao;
	}

	public void setEleicao(int eleicao) {
		this.eleicao = eleicao;
	}

	public HeyBean() throws RemoteException{
		try {
			server = (RMI) Naming.lookup("rmi://localhost:6500/rmi");
		}
		catch(NotBoundException|MalformedURLException|RemoteException e) {
			e.printStackTrace(); // what happens *after* we reach this line?
		}
	}
	
	public boolean updateResumos(int id, String resumo) throws RemoteException {
		for (Eleicoes x : ids_eleicao_not_started()) {
			if (x.getId() == id) {
				x.setResumo(resumo);
				server.update_election(x);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<String> checkLocation(String tipo) throws RemoteException {
		ArrayList<String> faculdades = server.list_facultys();
		if (tipo.equals("department") || tipo.equals("nucleo")) {
			ArrayList<String> send_back = new ArrayList<String>();
			for (String x : faculdades) {
				for (String k : server.dep_fac(x)) {
					send_back.add(k);
				}
			}
			return send_back;
		} else if (tipo.equals("faculty")) {
			return faculdades;
		}
		return null;
	}
	public boolean checkEleicaoTipo(String new_tipo, int id, String department, String faculty) throws RemoteException {
		for (Eleicoes x : ids_eleicao_not_started()) {
			if (x.getId() == id) {
				ArrayList<Lista_candidata> lists = server.get_lists_election(x.getId());
				if (new_tipo != "geral") {
					String tipo = "geral";
					if (new_tipo.equals("department") || new_tipo.equals("faculty")) {
						tipo = "teacher";
					} else if (new_tipo.equals("nucleo")) {
						tipo = "student";
					}
					server.apaga_listas(tipo, lists);
				}
				String previous_type = x.getTipo();
				x.setTipo(new_tipo);
				server.update_election(x);
				if (new_tipo.equals("geral")) {
					if (previous_type.equals("faculty")) {
						server.del_local_election_fac(x.getId());
					} else if (previous_type.equals("department") || previous_type.equals("nucleo")) {
						server.del_local_election_dep(x.getId());
					}
				} else if ((previous_type.equals(new_tipo)
						&& (new_tipo.equals("department") || new_tipo.equals("nucleo")))
						|| (previous_type.equals("nucleo") && new_tipo.equals("department"))
						|| previous_type.equals("department") && new_tipo.equals("nucleo")) {
					if (checkLocation(new_tipo).contains(department)) {
						server.update_department_eleicao(x.getId(), department);
						return true;
					} else {
						return false;
					}
				} else if (previous_type.equals(new_tipo) && new_tipo.equals("faculty")) {
					if (checkLocation(new_tipo).contains(faculty)) {
						server.update_faculty_eleicao(x.getId(), faculty);
						return true;
					} else {
						return false;
					}

				} else if ((previous_type.equals("department") || previous_type.equals("nucleo"))
						&& new_tipo.equals("faculty")) {
					if (checkLocation(new_tipo).contains(faculty)) {
						server.del_local_election_dep(x.getId());
						server.add_election_fac(faculty, x.getId());
						return true;
					} else {
						return false;
					}
				} else if (previous_type.equals("faculty")
						&& (new_tipo.equals("department") || new_tipo.equals("nucleo"))) {
					if (checkLocation(new_tipo).contains(department)) {
						server.del_local_election_fac(x.getId());
						server.add_election_dep(department, x.getId());
						return true;
					} else {
						return false;
					}
				} else if (previous_type.equals("geral")
						&& (new_tipo.equals("department") || new_tipo.equals("nucleo"))) {
					if (checkLocation(new_tipo).contains(department)) {
						server.add_election_dep(department, x.getId());
						return true;
					} else {
						return false;
					}

				} else if (previous_type.equals("geral") && new_tipo.equals("faculty")) {
					if (checkLocation(new_tipo).contains(faculty)) {
						server.add_election_fac(faculty, x.getId());
						return true;
					} else {
						return false;
					}
				}

				return true;
			}
		}
		return false;
	}
	public ArrayList<String> getAllDep() throws RemoteException{//feito
		ArrayList<String> one= new ArrayList<>();
		one.add("Nothing to show");
		ArrayList<String> dep_fac=new ArrayList<String>();
		dep_fac.add("\nDepartmantos com faculdade:\n");
		ArrayList<String> departments = server.listDepartments();
		for(String x:server.list_facultys()) {
			for(String k:server.dep_fac(x)) {
				departments.remove(k);
				dep_fac.add("Departamento:"+k+" localizado na faculdade:"+x+"\n");
			}
		}
		dep_fac.add("Departamentos sem faculdade:\n");
		for(String x: departments) {
			dep_fac.add("Departamento:"+x+"\n");
		}
		if(server.listDepartments().isEmpty()) {
			return one;
		}else {
			return dep_fac;
		} 
	}
	public ArrayList<String> getEleicaoTipo() throws RemoteException {
		String one = "Nothing to show";
		ArrayList<String> send_back = new ArrayList<>();
		for (Eleicoes x : ids_eleicao_not_started()) {
			send_back.add("ID:" + x.getId() + " Nome:" + x.getTitulo() + " Tipo atual:" + x.getTipo());
		}
		if (send_back.isEmpty()) {
			send_back.add(one);
		}
		return send_back;
	}
  	public ArrayList<String> getListaDepartment() throws RemoteException {
		ArrayList<String> send_back = new ArrayList<String>();
		send_back.add("Departments avaible");
		send_back.addAll(checkLocation("department"));
		if (checkLocation("department").isEmpty()) {
			send_back.add("None to show");
		}
		return send_back;
	}
  	public ArrayList<String> getListaFaculdades() throws RemoteException {
		ArrayList<String> send_back = new ArrayList<String>();
		send_back.add("Faculty avaible");
		send_back.addAll(checkLocation("faculty"));
		if (checkLocation("faculty").isEmpty()) {
			send_back.add("None to show");
		}
		return send_back;
	}
	
  		
	
	
	public ArrayList<String> getAllFac() throws RemoteException{//feito
		String one="Nothing to show";
		int check=0;
		ArrayList<String> facultys = server.list_facultys();
		ArrayList<String> send_back = new ArrayList<String>();
		for(String x:facultys) {
			check=0;
			send_back.add("Faculty:"+x);
			send_back.add("Departments of faculty:");
			for(String k:server.dep_fac(x)) {
				check=1;
				send_back.add(k);
			}
			if(check==0) {
				send_back.add(one);
			}
		}
		if(send_back.isEmpty()) {
			send_back.add(one);
		}
		return send_back;
	}
	public ArrayList<String> getAllEleicoes() throws RemoteException{
		ArrayList<String> one= new ArrayList<>();
		one.add("Nothing to show");
		ArrayList<String> eleicoes=new ArrayList<String>();
		eleicoes.add("\nEleicoes:\n");
		String location="None";
		for(Eleicoes x:server.return_eleicoes()) {
			if(!x.getTipo().equals("geral")) {
				location=server.get_location_election(x.getId());
			}
			eleicoes.add(x.toString() + " location:"+location);
			eleicoes.add("Mesas:");
			String mesas="None";
			int check=0;
			for(String k:server.listTables()) {//mesas_eleicao()
				if(server.get_election_of_table(k)==x.getId()) {
					mesas="Mesa:"+k+" Location:"+server.location_table(k)+" Number of people ate the table:"+server.count_number_of_persom_table(k);
					eleicoes.add(mesas);
					check=1;
				}
			}
			if(check==0) {
				eleicoes.add(mesas);
			}
			check=0;
			eleicoes.add("Listas:");
			String lista="None";
			for(Lista_candidata k:server.get_lists_election(x.getId())) {
				lista=k.toString();
				eleicoes.add(lista);
				check=1;
			}
			if(check==0) {
				eleicoes.add(lista);
			}
		}
		return eleicoes;
	}
	public ArrayList<String> getAllTables() throws RemoteException{//feita
		String one="Nothing to show";
		String two="";
		ArrayList<String> mesas_n_loc_n_eleicao = new ArrayList<String>();
		for(String x:server.listTables()) {
			two="Mesa:"+x+" Election id:";
			if(server.get_election_of_table(x)==0) {
				two+="None";
			}else{
				two+=server.get_election_of_table(x);
			}
			two+=" Location:"+server.location_table(x);
			mesas_n_loc_n_eleicao.add(two);
		}
		if(mesas_n_loc_n_eleicao.isEmpty()) {
			mesas_n_loc_n_eleicao.add(one);
		}
		return mesas_n_loc_n_eleicao;
	}
	public ArrayList<String> getAllPessoas() throws RemoteException{
		ArrayList<String> send_back=new ArrayList<String>();
		String one = "Nothing to show";
		String pessoa="";
		for(Pessoa x: server.get_pessoas()) {
			pessoa=x.toString()+" Faculdade:"+server.get_faculdade_eleitor(x.getNumero_cc()) + " Departamento:"+server.get_department_eleitor(x.getNumero_cc());
			send_back.add(pessoa);
		}
		if(send_back.isEmpty()) {
			send_back.add(one);
		}
		return send_back;
	}
	
	public boolean registaVote(String id_lista) throws NumberFormatException, RemoteException {
		int id_tenta = 0;
		try {
			id_tenta = Integer.parseInt(id_lista);
		} catch (Exception e) {
			System.out.println("Impossivel converter para int");
		}

		Date date = new Date();
		for (Pessoa k : server.get_pessoas()) {
			if (k.getNumero_cc() == Integer.parseInt(this.username)) {
				System.out.println("passa");
				for (Eleicoes x : server.listElectionsLive(date)) {
					if (x.getId() == this.eleicao) {
						System.out.println("passa2");
						if (!server.ids_votados(k.getNumero_cc()).contains(x.getId())) {
							System.out.println("passa4");
							if (id_tenta != 0) {
								System.out.println("passa3");
								ArrayList<Lista_candidata> lists = server.get_lists_election(x.getId());
								for (Lista_candidata m : lists) {
									if (m.getClasse().equals(k.getCargo())) {
										System.out.println("passa5");
										if(m.getId()==id_tenta) {
											System.out.println("passa6");
											server.register_vote_web(this.eleicao,id_tenta,"pass",Integer.parseInt(this.username));
											return true;
										}
									}
								}
							}else {
								System.out.println("passa7");
								server.register_vote_web(this.eleicao,0,id_lista,Integer.parseInt(this.username));
								return true;
							}

						}
					}
				}
			}
		}
		return false;
	}
	public boolean EleicoesUser(int id) throws NumberFormatException, RemoteException {
		for (Pessoa x : server.get_pessoas()) {
			if (Integer.parseInt(this.username) == x.getNumero_cc()) {
				Date date = new Date();
				for (Eleicoes k : server.listElectionsLive(date)) {
					if (k.getId() == id) {
						if (!server.ids_votados(x.getNumero_cc()).contains(k.getId())) {
							if (k.getTipo().equals("geral")) {
								ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
								for (Lista_candidata m : lists) {
									if (m.getClasse().equals(x.getCargo())) {
										this.setEleicao(k.getId());
										return true;
									}
								}
							} else if (k.getTipo().equals("department")
									&& server.get_location_election(k.getId())
											.equals(server.get_department_eleitor(x.getNumero_cc()))
									&& x.getCargo().equals("teacher") && !server.get_location_election(k.getId()).equals("None")) {
								ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
								for (Lista_candidata m : lists) {
									if (m.getClasse().equals(x.getCargo())) {
										this.setEleicao(k.getId());
										return true;
									}
								}
							} else if (k.getTipo().equals("faculty")
									&& server.get_location_election(k.getId())
											.equals(server.get_faculdade_eleitor(x.getNumero_cc()))
									&& x.getCargo().equals("teacher") && !server.get_location_election(k.getId()).equals("None")) {
								ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
								for (Lista_candidata m : lists) {
									if (m.getClasse().equals(x.getCargo())) {
										this.setEleicao(k.getId());
										return true;
									}
								}
							} else if (k.getTipo().equals("nucleo")
									&& server.get_location_election(k.getId())
											.equals(server.get_department_eleitor(x.getNumero_cc()))
									&& x.getCargo().equals("student") && !server.get_location_election(k.getId()).equals("None")) {
								ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
								for (Lista_candidata m : lists) {
									if (m.getClasse().equals(x.getCargo())) {
										this.setEleicao(k.getId());
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
  
  
  
  public ArrayList<String> getCenasUser() throws NumberFormatException, RemoteException {
		ArrayList<String> send_back = new ArrayList<String>();
		for (Pessoa x : server.get_pessoas()) {
			if (Integer.parseInt(this.username) == x.getNumero_cc()) {
				Date date = new Date();
				for (Eleicoes k : server.listElectionsLive(date)) {
					if (!server.ids_votados(x.getNumero_cc()).contains(k.getId())) {
						if (k.getTipo().equals("geral")) {
							send_back.add("ID:" + k.getId() + " Titulo:" + k.getTitulo());
							ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
							send_back.add("Listas:");
							for (Lista_candidata m : lists) {
								String pessoas = "";
								if (m.getClasse().equals(x.getCargo())) {
									send_back.add("ID:" + m.getId() + " Nome:" + m.getNome_lista());
									for (Integer l : server.pessoas_lista(m.getId())) {
										for (Pessoa d : server.get_pessoas()) {
											if (l == d.getNumero_cc()) {
												pessoas += "(Nome:" + d.getNome() + " CC:" + d.getNumero_cc();
											}
										}
									}
									send_back.add(pessoas);
								}
							}
						} else if (k.getTipo().equals("department")
								&& server.get_location_election(k.getId())
										.equals(server.get_department_eleitor(x.getNumero_cc()))
								&& x.getCargo().equals("teacher")) {
							send_back.add("ID:" + k.getId() + " Titulo:" + k.getTitulo());
							ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
							send_back.add("Listas:");
							for (Lista_candidata m : lists) {
								String pessoas = "";
								if (m.getClasse().equals(x.getCargo())) {
									send_back.add("ID:" + m.getId() + " Nome:" + m.getNome_lista());
									for (Integer l : server.pessoas_lista(m.getId())) {
										for (Pessoa d : server.get_pessoas()) {
											if (l == d.getNumero_cc()) {
												pessoas += "(Nome:" + d.getNome() + " CC:" + d.getNumero_cc();
											}
										}
									}
									send_back.add(pessoas);
								}
							}
						} else if (k.getTipo().equals("faculty")
								&& server.get_location_election(k.getId())
										.equals(server.get_faculdade_eleitor(x.getNumero_cc()))
								&& x.getCargo().equals("teacher")) {
							send_back.add("ID:" + k.getId() + " Titulo:" + k.getTitulo());
							ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
							send_back.add("Listas:");
							for (Lista_candidata m : lists) {
								String pessoas = "";
								if (m.getClasse().equals(x.getCargo())) {
									send_back.add("ID:" + m.getId() + " Nome:" + m.getNome_lista());
									for (Integer l : server.pessoas_lista(m.getId())) {
										for (Pessoa d : server.get_pessoas()) {
											if (l == d.getNumero_cc()) {
												pessoas += "(Nome:" + d.getNome() + " CC:" + d.getNumero_cc();
											}
										}
									}
									send_back.add(pessoas);
								}
							}
						} else if (k.getTipo().equals("nucleo")
								&& server.get_location_election(k.getId())
										.equals(server.get_department_eleitor(x.getNumero_cc()))
								&& x.getCargo().equals("student")) {
							send_back.add("ID:" + k.getId() + " Titulo:" + k.getTitulo());
							ArrayList<Lista_candidata> lists = server.get_lists_election(k.getId());
							send_back.add("Listas:");
							for (Lista_candidata m : lists) {
								String pessoas = "";
								if (m.getClasse().equals(x.getCargo())) {
									send_back.add("ID:" + m.getId() + " Nome:" + m.getNome_lista());
									for (Integer l : server.pessoas_lista(m.getId())) {
										for (Pessoa d : server.get_pessoas()) {
											if (l == d.getNumero_cc()) {
												pessoas += "(Nome:" + d.getNome() + " CC:" + d.getNumero_cc();
											}
										}
									}
									if (server.pessoas_lista(m.getId()).isEmpty()) {
										send_back.add("No persons at this list");
									}
									send_back.add(pessoas);
								}
							}
						}
					}
				}
			}
		}
		return send_back;
	}
	
	public ArrayList<String> getAllResultados() throws RemoteException{//acabar
		Date date = new Date();
		ArrayList<String> send_back=new ArrayList<String>();
		int i = 0;
		int votos_total=0;
		int votos_brancos;
		int votos_null;
		int check=0;
		for (Eleicoes x : server.return_eleicoes()) {
			votos_total = 0;
			if (x.getEnd().before(date)) {
				++i;
				send_back.add("ID:"+x.getId() + "-" + x.getTitulo());
				ArrayList<Lista_candidata> listas = server.get_lists_election(x.getId());
				for (Lista_candidata k : listas) {
					votos_total += k.getNum_votos();
				}
				votos_brancos = server.votos_null(x.getId());
				votos_total += votos_brancos;
				votos_null = server.votos_brancos(x.getId());
				votos_total += votos_null;
				float percentagem = 0;
				send_back.add("null votes:" + votos_null);
				send_back.add("blanc votes:" + votos_brancos);
				if (x.getTipo().equals("geral")) {
					check=0;
					send_back.add("Students lists:");
					for (Lista_candidata k : listas) {
						if (k.getClasse().equals("student")) {
							check=1;
							if (votos_total != 0) {
								percentagem = (((float)k.getNum_votos() / votos_total)) * 100;
							} else {
								percentagem = 0;
							}
							send_back.add("List:" + k.getNome_lista() + "\tnumber of votes:" + k.getNum_votos()
									+ "\tpercentage of votes:" + percentagem);
						}
					}
					if(check==0) {
						send_back.add("Nothing to show");
					}
					check=0;
					send_back.add("teacher lists:");
					for (Lista_candidata k : listas) {
						if (k.getClasse().equals("teacher")) {
							check=1;
							if (votos_total != 0) {
								percentagem = (((float)k.getNum_votos() / votos_total)) * 100;
							} else {
								percentagem = 0;
							}
							send_back.add("List:" + k.getNome_lista() + "\tnumber of votes:" + k.getNum_votos()
									+ "\tpercentage of votes:" + percentagem);
						}
					}
					if(check==0) {
						send_back.add("Nothing to show");
					}
					check=0;
					send_back.add("staff lists:");
					for (Lista_candidata k : listas) {
						if (k.getClasse().equals("staff")) {
							check=1;
							if (votos_total != 0) {
								percentagem = (((float)k.getNum_votos() / votos_total)) * 100;
							} else {
								percentagem = 0;
							}
							send_back.add("List:" + k.getNome_lista() + "\tnumber of votes:" + k.getNum_votos()
									+ "\tpercentage of votes:" + percentagem);
						}
					}
					if(check==0) {
						send_back.add("Nothing to show");
					}
				} else if (x.getTipo().equals("department") || x.getTipo().equals("faculty")) {
					send_back.add("teacher lists:");
					check=0;
					for (Lista_candidata k : listas) {
						if (k.getClasse().equals("teacher")) {
							check=1;
							if (votos_total != 0) {
								percentagem = (((float)k.getNum_votos() / votos_total)) * 100;
							} else {
								percentagem = 0;
							}
							send_back.add("List:" + k.getNome_lista() + "\tnumber of votes:" + k.getNum_votos()
									+ "\tpercentage of votes:" + percentagem);
						}
					}
					if(check==0) {
						send_back.add("Nothing to show");
					}
				} else if (x.getTipo().equals("nucleo")) {
					check=0;
					send_back.add("Students lists:");
					for (Lista_candidata k : listas) {
						if (k.getClasse().equals("student")) {
							check=1;
							if (votos_total != 0) {
								percentagem = (((float)k.getNum_votos() / votos_total)) * 100;
							} else {
								percentagem = 0;
							}
							send_back.add("List:" + k.getNome_lista() + "\tnumber of votes:" + k.getNum_votos()
									+ "\tpercentage of votes:" + percentagem);
						}
					}
					if(check==0) {
						send_back.add("Nothing to show");
					}
				}
			}
		}
		if (i == 0) {
			send_back.add("No elections done at the moment");
		}
		return send_back;
	}

	public ArrayList<String> getAllUsers() throws RemoteException {
		//return server.getAllUsers(); // are you going to throw all exceptions?
		return server.list_facultys();
	}

	public boolean getUserMatchesPassword(String username, String password) throws RemoteException {
		try {
			System.out.println("Entra");
			int tenta = 0;
			tenta = Integer.parseInt(username);
			if (server.login(tenta, password)) {
				System.out.println("Login with sucess!");
				// server.loginDone(cc,"Web");
				return true;
			} else {
				System.out.println("Username and Password do not correspond");
				return false;
			}
		} catch (Exception e) {
			System.out.println("no rmi connection");
			return false;
		}
		// return server.userMatchesPassword(this.username, this.password);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean registerVoter(String name,String cc, String ccv, String password, String phone, String adress,String role) {
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
		try {
			if(checkCCnumber(Integer.parseInt(cc), server.get_pessoas())) {
				server.add_pessoa(Integer.parseInt(cc), name, password, Integer.parseInt(phone), dt.parse(ccv), adress, role);
				return true;
			}
		} catch (NumberFormatException | RemoteException | ParseException e) {
			System.out.println("Failed creating voter");
			return false;
		}
		return false;
	}
	
	
	public boolean remove_person_from_old_faculty(int cc) {
		try {
			System.out.println(server.del_dep_person(cc));
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String update_person(Pessoa pessoa){
		System.out.println("Sending data to DataServer");
		try {
			return server.update_person(pessoa);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return "null";
	}
	public void update_props_voter(int cc, String dep) {
		try {
			server.update_dep_fac_user(cc, dep);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	public void update_props_voter_fac(int cc, String fac) {
		try {
			server.insert_faculty(cc, fac);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public boolean remove_person_from_old_department(int cc) {
		try {
			System.out.println(server.del_fac_person(cc));
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean checkCCnumber(int number_cc, ArrayList<Pessoa> list_person) {
		for (Pessoa x : list_person) {
			if (x.getNumero_cc() == number_cc) {
				out.println("Number of cc already exist!");
				return false;
			}
		}
		return true;
	}
	
	public boolean search_faculty(String name) {
		try {
			return !server.search_faculty(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void create_faculty(String name) {
		try {
			server.create_faculty(name);
		} catch (RemoteException e) {
			System.out.println("Couldn't create faculty on web");
		}
	}
	
	
	public boolean search_department(String name) {
		try {
			return !server.search_department(name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void create_department(String name) {
		try {
			server.AddDepartments(name);
		} catch (RemoteException e) {
			System.out.println("Couldn't create faculty on web");
		}
	}
	
	public void add_eleicao( Date inicio, Date fim, String titulo, String resumo, String tipo) {
		int id = 1;
		int max=0;
		ArrayList<Eleicoes> eleicoes;
		try {
			eleicoes = server.return_eleicoes();
			if (!eleicoes.isEmpty()) {
				
					for (Eleicoes x : eleicoes) {
						if (x.getId()>max) {
							max=x.getId();
						}
						
					}
					
				if(max!=0 ) {
					id=max+1;
				}
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		try {
			server.add_eleicao(id, inicio, fim, titulo, resumo, tipo);
			server.insere_web(id);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
	}
	
	public ArrayList<String> get_tables() {
		ArrayList<String> list =null;
		try {
			list = server.tables();
			return list;
		} catch (RemoteException e) {
			System.out.println("Couldn't get the list of tables");
			e.printStackTrace();
		}
		return list;
	}
	public void add_mesa(String name) {
		try {
			server.add_mesa(name);
		} catch (Exception e) {
			System.out.println("Couldn't create table....");
			e.printStackTrace();
		};
	}
	
	public Pessoa getPersonbyID(int cc) {

		try {
			return server.getPersonbyID(cc);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean add_lists_election(String name_list, String tipo, Eleicoes eleicao) throws RemoteException {
		ArrayList<Lista_candidata> lista_existentes = null;
		int check = 0;
		try {
			lista_existentes = server.get_lists_election(eleicao.getId());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		for (Lista_candidata x : lista_existentes) {
			if (x.getClasse().equals(tipo) && x.getNome_lista().equals(name_list)) {
				return false;
			}
		}
		if (check == 0) {
			int id_lista = id_lista();
			server.insert_listas(eleicao.getId(), name_list, tipo, id_lista);
			ArrayList<String> mesas = server.mesas_eleicao(eleicao.getId());
			for (String x : mesas) {
				server.insert_mesa_election_list_votes(x, id_lista);
			}
			return true;
		}
		return false;
	}
	public boolean checkList(int id, String tipo, String nome_lista) throws RemoteException {
        for (Eleicoes x : ids_eleicao_not_started()) {
            if (x.getId() == id) {
                if (x.getTipo().equals("nucleo") && tipo.equals("student")) {
                    if (add_lists_election(nome_lista, "student", x)) {
                        return true;
                    }
                } else if ((x.getTipo().equals("faculty") || x.getTipo().equals("department"))
                        && tipo.equals("teacher")) {
                    if (add_lists_election(nome_lista, "teacher", x)) {
                        return true;
                    }
                } else if (x.getTipo().equals("geral")) {
                    if (tipo.equals("student")) {
                        if (add_lists_election(nome_lista, "student", x)) {
                            return true;
                        }
                    } else if (tipo.equals("teacher")) {
                        if (add_lists_election(nome_lista, "teacher", x)) {
                            return true;
                        }
                    } else if (tipo.equals("staff")) {
                        if (add_lists_election(nome_lista, "staff", x)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
 
  public ArrayList<String> getListas() throws RemoteException {
        ArrayList<String> send_back = new ArrayList<String>();
        String one = "Nothing to show";
        for (Eleicoes x : ids_eleicao_not_started()) {
            send_back.add("ID:" + x.getId() + " Titulo:" + x.getTitulo() + " Tipo eleicao:" + x.getTipo());
            send_back.add("Lists:");
            for (Lista_candidata k : server.get_lists_election(x.getId())) {
                send_back.add("ID:" + k.getId() + " Classe:" + k.getClasse() + " Nome:" + k.getNome_lista());
            }
            if (server.get_lists_election(x.getId()).isEmpty()) {
                send_back.add("Nothing to show");
            }
        }
        if (send_back.isEmpty()) {
            send_back.add(one);
        }
        return send_back;
    }
 
    public ArrayList<String> getResumos() throws RemoteException {
        String one = "Nothing to show";
        ArrayList<String> send_back = new ArrayList<String>();
        send_back.add("Eleicoes;");
        for (Eleicoes x : ids_eleicao_not_started()) {
            send_back.add("ID:" + x.getId() + " Titulo:" + x.getTitulo() + " Resume:" + x.getResumo());
        }
        if (ids_eleicao_not_started().isEmpty()) {
            send_back.add(one);
        }
        return send_back;
    }
 
    public boolean checkDataFinal(int id, String data_inicial) throws RemoteException {
        for (Eleicoes x : ids_eleicao_not_started()) {
            if (x.getId() == id) {
                try {
                    Date write_date_format = new Date(0, 0, 0, 0, 0, 0);
                    SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
                    write_date_format = dt.parse(data_inicial);
                    Date date = new Date();
                    if (date.after(write_date_format)) {
                        return false;
                    } else {
                        if (x.getEnd().after(write_date_format)) {
                            x.setStart(write_date_format);
                            server.update_election(x);
                            return true;
                        } else {
                            return false;
                        }
                    }
                } catch (ParseException e) {
                    return false;
                }
            }
        }
        return false;
    }
 
    public boolean checkDataInicial(int id, String data_final) throws RemoteException {
        for (Eleicoes x : ids_eleicao_not_started()) {
            if (x.getId() == id) {
                try {
                    Date write_date_format = new Date(0, 0, 0, 0, 0, 0);
                    SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
                    write_date_format = dt.parse(data_final);
                    Date date = new Date();
                    if (date.after(write_date_format)) {
                        return false;
                    } else {
                        if (x.getStart().before(write_date_format)) {
                            x.setEnd(write_date_format);
                            server.update_election(x);
                            return true;
                        } else {
                            return false;
                        }
                    }
                } catch (ParseException e) {
                    return false;
                }
            }
        }
        return false;
    }
 
    public ArrayList<String> getDataFinal() throws RemoteException {
        String one = "Nothing to show";
        ArrayList<String> send_back = new ArrayList<String>();
        for (Eleicoes x : ids_eleicao_not_started()) {
            send_back.add("ID:" + x.getId() + " Titulo:" + x.getTitulo() + " Data Inicial:" + x.getStart()
                    + " Data final:" + x.getEnd());
        }
        if (send_back.isEmpty()) {
            send_back.add(one);
        }
        return send_back;
    }
 
    public ArrayList<String> getAvaibleTables() throws RemoteException {
        String one = "No tables avaible";
        ArrayList<String> out_put_tables = new ArrayList<String>();
        out_put_tables.add("Tables avaible to be added:");
        for (String x : server.avaible_tables()) {
            out_put_tables.add(x);
        }
        if (server.avaible_tables().isEmpty()) {
            out_put_tables.add(one);
        }
        return out_put_tables;
    }
 
    public boolean checkIDtables(String name, int id) {
        int check = 0;
        try {
            for (Eleicoes x : ids_eleicao_not_started()) {
                if (x.getId() == id) {
                    check = 1;
                }
            }
            if (check == 0) {
                return false;
            } else {
                if (getAvaibleTables().contains(name)) {
                    server.insert_mesa_election(name, id);
                    ArrayList<Lista_candidata> listas = server.get_lists_election(id);
                    for (Lista_candidata x : listas) {
                        server.insert_mesa_election_list_votes(name, id);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        } catch (NumberFormatException | RemoteException e) {
            System.out.println("Failed creating voter");
            return false;
        }
    }
 
    public boolean checkTableDelete(String name, int id) {
        int check = 0;
        try {
            for (Eleicoes x : ids_eleicao_not_started()) {
                if (x.getId() == id) {
                    check = 1;
                }
            }
            if (check == 0) {
                return false;
            } else {
                if (server.mesas_eleicao(id).contains(name)) {
                    server.delete_table_from_election(name);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (NumberFormatException | RemoteException e) {
            System.out.println("Failed creating voter");
            return false;
        }
    }
 
    public ArrayList<String> getTablesDelete() throws RemoteException {
        String one = "Nothing to show";
        ArrayList<String> eleicoes = new ArrayList<String>();
        eleicoes.add("\nEleicoes:\n");
        for (Eleicoes x : ids_eleicao_not_started()) {
            if (!server.mesas_eleicao(x.getId()).isEmpty()) {
                eleicoes.add("ID:" + x.getId() + " Titulo:" + x.getTitulo());
                eleicoes.add("Mesas:");
                String mesas = "None";
                for (String k : server.mesas_eleicao(x.getId())) {
                    eleicoes.add("Mesa: " + k);
                }
            }
        }
        if (ids_eleicao_not_started().isEmpty()) {
            eleicoes.add(one);
        }
        return eleicoes;
    }
 
    public ArrayList<String> getElectionsNotStartedDelet() throws RemoteException {
        String one = "No elections avaible to delete tables";
        ArrayList<String> out_put_election = new ArrayList<String>();
        out_put_election.add("Elections avaible and tables:");
        for (Eleicoes x : ids_eleicao_not_started()) {
            out_put_election.add("ID:" + x.getId() + " Nome eleicao:" + x.getTitulo());
            out_put_election.add("Mesas da eleicao:");
 
        }
        if (ids_eleicao_not_started().isEmpty()) {
            out_put_election.add(one);
        }
        return out_put_election;
    }
 
    public ArrayList<String> getElectionsNotStarted() throws RemoteException {
        String one = "No elections avaible to add tables";
        ArrayList<String> out_put_election = new ArrayList<String>();
        out_put_election.add("Elections avaible:");
        for (Eleicoes x : ids_eleicao_not_started()) {
            out_put_election.add("ID:" + x.getId() + " Nome eleicao:" + x.getTitulo());
        }
        if (ids_eleicao_not_started().isEmpty()) {
            out_put_election.add(one);
        }
        return out_put_election;
    }
 
  public int id_lista() throws RemoteException {// corrigir isto no normal que esta mal
        int id_lista = 1;
        ArrayList<Integer> id_listas = server.id_listas();
        if (!id_listas.isEmpty()) {
            int max = 0;
            for (Integer k : id_listas) {
                if (k > max) {
                    max = k;
                }
            }
            return max + 1;
 
        } else {
            return id_lista;
        }
    }
 
 
  public boolean removeList(int id_lista) throws RemoteException {
        for (Eleicoes x : ids_eleicao_not_started()) {
            for (Lista_candidata k : server.get_lists_election(x.getId())) {
                if (k.getId() == id_lista) {
                    server.delete_list(id_lista);
                    return true;
                }
            }
        }
        return false;
    }
 
   
    public ArrayList<String> getPessoasRemove() throws RemoteException{;
        String pessoas="";
        ArrayList<String> send_back = new ArrayList<String>();
        send_back.add("Eleicoes:");
        for(Eleicoes x:ids_eleicao_not_started()) {
            send_back.add("ID:"+x.getId()+" Titulo:"+x.getTitulo());
            for (Lista_candidata k : server.get_lists_election(x.getId())) {
                send_back.add("ID lista:"+k.getId()+" Nome:"+k.getNome_lista()+" Tipo:"+k.getClasse());
                pessoas="";
                for(Pessoa m:server.get_pessoas()) {
                    if(server.pessoas_lista(k.getId()).contains(m.getNumero_cc())) {
                        pessoas+="(Nome:"+m.getNome()+" CC:"+m.getNumero_cc()+") ";
                    }
                }
                if(pessoas.equals("")) {
                    send_back.add("No one at this list");
                }else {
                    send_back.add(pessoas);
                }
            }
            if(server.get_lists_election(x.getId()).isEmpty()) {
                send_back.add("Nothing to show in this elextion");
            }
        }
        if(ids_eleicao_not_started().isEmpty()) {
            send_back.add("Nothing to show");
        }
        return send_back;
    }
 
    public boolean removePessoaLista(int id_lista,int cc) throws RemoteException {
        for (Eleicoes x : ids_eleicao_not_started()) {
            for (Lista_candidata k : server.get_lists_election(x.getId())) {
                if(k.getId()==id_lista) {
                    if (server.pessoas_lista(k.getId()).contains(cc)) {
                        server.delete_pessoa_list(id_lista,cc);
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public boolean addPessoaLista(int id_lista, int cc) throws RemoteException {
        System.out.println(id_lista);
        System.out.println(cc);
        for (Eleicoes x : ids_eleicao_not_started()) {
            for (Lista_candidata k : server.get_lists_election(x.getId())) {
                if (id_lista == k.getId()) {
                    String location = server.get_location_election(x.getId());
                    if (x.getTipo().equals("geral")) {
                        if (k.getClasse().equals("student")) {
                            for (Pessoa m : server.get_pessoas()) {
                                if (m.getCargo().equals("student")) {
                                    if (m.getNumero_cc() == cc) {
                                        for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                            if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                                return false;
                                            }
                                        }
                                        server.addPessoaLista(id_lista, cc);
                                        return true;
 
                                    }
                                }
                            }
                        } else if (k.getClasse().equals("teacher")) {
                            for (Pessoa m : server.get_pessoas()) {
                                if (m.getCargo().equals("teacher")) {
                                    if (m.getNumero_cc() == cc) {
                                        for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                            if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                                return false;
                                            }
                                        }
                                        server.addPessoaLista(id_lista, cc);
                                        return true;
 
                                    }
                                }
                            }
                        } else if (k.getClasse().equals("staff")) {
                            for (Pessoa m : server.get_pessoas()) {
                                if (m.getCargo().equals("staff")) {
                                    if (m.getNumero_cc() == cc) {
                                        for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                            if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                                return false;
                                            }
                                        }
                                        server.addPessoaLista(id_lista, cc);
                                        return true;
 
                                    }
                                }
                            }
                        }
                    } else if (x.getTipo().equals("department")) {
                        for (Pessoa m : server.get_pessoas()) {
                            if (m.getCargo().equals("teacher")) {
                                if (m.getNumero_cc() == cc) {
                                    for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                        if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                            return false;
                                        }
                                    }
                                    if (server.get_department_eleitor(m.getNumero_cc()).equals(location)) {
                                        server.addPessoaLista(id_lista, cc);
                                        return true;
                                    }
                                }
                            }
                        }
                    } else if (x.getTipo().equals("faculty")) {
                        for (Pessoa m : server.get_pessoas()) {
                            if (m.getCargo().equals("teacher")) {
                                if (m.getNumero_cc() == cc) {
                                    for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                        if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                            return false;
                                        }
                                    }
                                    if (server.get_faculdade_eleitor(m.getNumero_cc()).equals(location)) {
                                        server.addPessoaLista(id_lista, cc);
                                        return true;
                                    }
 
                                }
                            }
                        }
                    } else if (x.getTipo().equals("nucleo")) {
                        for (Pessoa m : server.get_pessoas()) {
                            if (m.getCargo().equals("student")) {
                                if (m.getNumero_cc() == cc) {
                                    for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                        if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                            return false;
                                        }
                                    }
                                    if (server.get_department_eleitor(m.getNumero_cc()).equals(location)) {
                                        server.addPessoaLista(id_lista, cc);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
 
    public ArrayList<String> getPessoasAdicionar() throws RemoteException {
        ArrayList<String> send_back = new ArrayList<String>();
        int check = 0;
        for (Eleicoes x : ids_eleicao_not_started()) {
            String location = server.get_location_election(x.getId());
            send_back.add("ID:" + x.getId() + " Titulo:" + x.getTitulo() + " Tipo:" + x.getTipo());
            for (Lista_candidata k : server.get_lists_election(x.getId())) {
                check = 0;
                send_back.add("ID lista:" + k.getId() + " Lista:" + k.getNome_lista());
                send_back.add("Pessoas disponiveis para adicionar:");
                ArrayList<Integer> cc = server.pessoas_lista(k.getId());
                if (x.getTipo().equals("geral")) {
                    if (k.getClasse().equals("student")) {
                        for (Pessoa m : server.get_pessoas()) {
                            if (m.getCargo().equals("student")) {
                                if (!cc.contains(m.getNumero_cc())) {
                                    int check2 = 0;
                                    for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                        if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                            check2 = 1;
                                        }
                                    }
                                    if (check2 == 0) {
                                        check = 1;
                                        send_back.add("Nome:" + m.getNome() + " CC:" + m.getNumero_cc());
                                    }
                                }
                            }
                        }
                        if (check == 0) {
                            send_back.add("No persons avaible to add in this list");
                        }
 
                    } else if (k.getClasse().equals("teacher")) {
                        for (Pessoa m : server.get_pessoas()) {
                            if (m.getCargo().equals("teacher")) {
                                if (!cc.contains(m.getNumero_cc())) {
                                    int check2 = 0;
                                    for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                        if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                            check2 = 1;
                                        }
                                    }
                                    if (check2 == 0) {
                                        check = 1;
                                        send_back.add("Nome:" + m.getNome() + " CC:" + m.getNumero_cc());
                                    }
                                }
                            }
                        }
                        if (check == 0) {
                            send_back.add("No persons avaible to add in this list");
                        }
                    } else if (k.getClasse().equals("staff")) {
                        for (Pessoa m : server.get_pessoas()) {
                            if (m.getCargo().equals("staff")) {
                                if (!cc.contains(m.getNumero_cc())) {
                                    int check2 = 0;
                                    for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                        if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                            check2 = 1;
                                        }
                                    }
                                    if (check2 == 0) {
                                        check = 1;
                                        send_back.add("Nome:" + m.getNome() + " CC:" + m.getNumero_cc());
                                    }
                                }
                            }
                        }
                        if (check == 0) {
                            send_back.add("No persons avaible to add in this list");
                        }
                    }
                } else if (x.getTipo().equals("department")) {
                    for (Pessoa m : server.get_pessoas()) {
                        if (m.getCargo().equals("teacher")) {
                            if (!cc.contains(m.getNumero_cc())
                                    && server.get_department_eleitor(m.getNumero_cc()).equals(location)) {
                                int check2 = 0;
                                for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                    if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                        check2 = 1;
                                    }
                                }
                                if (check2 == 0) {
                                    check = 1;
                                    send_back.add("Nome:" + m.getNome() + " CC:" + m.getNumero_cc());
                                }
                            }
                        }
                    }
                    if (check == 0) {
                        send_back.add("No persons avaible to add in this list");
                    }
                } else if (x.getTipo().equals("faculty")) {
                    for (Pessoa m : server.get_pessoas()) {
                        if (m.getCargo().equals("teacher")) {
                            if (!cc.contains(m.getNumero_cc())
                                    && server.get_faculdade_eleitor(m.getNumero_cc()).equals(location)) {
                                int check2 = 0;
                                for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                    if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                        check2 = 1;
                                    }
                                }
                                if (check2 == 0) {
                                    check = 1;
                                    send_back.add("Nome:" + m.getNome() + " CC:" + m.getNumero_cc());
                                }
                            }
                        }
                    }
                    if (check == 0) {
                        send_back.add("No persons avaible to add in this list");
                    }
                } else if (x.getTipo().equals("nucleo")) {
                    for (Pessoa m : server.get_pessoas()) {
                        if (m.getCargo().equals("student")) {
                            if (!cc.contains(m.getNumero_cc())
                                    && server.get_department_eleitor(m.getNumero_cc()).equals(location)) {
                                int check2 = 0;
                                for (Lista_candidata o : server.get_lists_election(x.getId())) {
                                    if (server.pessoas_lista(o.getId()).contains(m.getNumero_cc())) {
                                        check2 = 1;
                                    }
                                }
                                if (check2 == 0) {
                                    check = 1;
                                    send_back.add("Nome:" + m.getNome() + " CC:" + m.getNumero_cc());
                                }
                            }
                        }
                    }
                    if (check == 0) {
                        send_back.add("No persons avaible to add in this list");
                    }
                }
 
            }
            if (server.get_lists_election(x.getId()).isEmpty()) {
                send_back.add("Nothing avaible on the lists of this election");
            }
 
        }
        if (ids_eleicao_not_started().isEmpty()) {
            send_back.add("Nothing to show");
        }
        return send_back;
    }
    
    public ArrayList<Eleicoes> ids_eleicao_not_started() throws RemoteException {
        ArrayList<Eleicoes> ids = new ArrayList<Eleicoes>();
        Date date = new Date();
        for (Eleicoes x : server.return_eleicoes()) {
          if (date.before(x.getStart())) {
            ids.add(x);
          }
        }
        return ids;
      }
	
}
