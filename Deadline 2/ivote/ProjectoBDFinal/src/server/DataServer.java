package server;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static java.lang.System.out;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.sql.PreparedStatement;

public class DataServer extends UnicastRemoteObject implements RMI, java.io.Serializable {
	public static int rmiRegistry;
	public static int rmiPort;
	public static int rmiPortSec;
	public static String rmiHost;
	public static String rmiHostSec;
	public Scanner inputI = new Scanner(System.in);
	public Scanner inputS = new Scanner(System.in);
	public static ArrayList<Server_rmi> servers = new ArrayList<Server_rmi>();
	public static ArrayList<Admin_rmi_I> admins = new ArrayList<Admin_rmi_I>();
	public static Connection conn = null;
	public static PreparedStatement mysql;
	public static ResultSet resultSet;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataServer() throws RemoteException {
		super();
	}

	public synchronized static void load_config() throws RemoteException {
		String file = "DataServerConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding DataServer configurations...");
		try {
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for (int i = 0; i < 5; i++) {
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line, "=");
				line = tokenizer.nextToken();
				if (line.equals("RMI Registry")) {
					rmiRegistry = Integer.parseInt(tokenizer.nextToken());
				} else if (line.equals("RMI UDP Port connection")) {
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				} else if (line.equals("RMI UDP Host connection")) {
					rmiHost = tokenizer.nextToken();
				} else if (line.equals("RMI UDP sec Port connection")) {
					rmiPortSec = Integer.parseInt(tokenizer.nextToken());
				} else if (line.equals("RMI UDP sec Host connection")) {
					rmiHostSec = tokenizer.nextToken();
				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File " + file + " not found");
			System.exit(0);
		} catch (IOException e) {
		}
		System.out.println("DataServerConfig.txt successfully uploaded.");
	}

	public synchronized void subscribe(Server_rmi mesa) {
		servers.add(mesa);
	}

	public synchronized void subscribeAdmin(Admin_rmi_I admin) {
		admins.add(admin);
	}

	public synchronized ArrayList<String> list_facultys() {
		ArrayList<String> faculdades = new ArrayList<String>();
		try {
			out.println("Searching Faculty...");
			mysql = conn.prepareStatement("SELECT nome FROM bd.faculdade;");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				faculdades.add(rQ.getString("nome"));
			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return faculdades;
		}
		return faculdades;

	}
	
	public synchronized ArrayList<Eleicoes> listElectionToDelete(Timestamp data){
		ArrayList<Eleicoes> eleicoes = new ArrayList<Eleicoes>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			out.println("Getting elections...");
			mysql = conn.prepareStatement("select * from eleicao e where data_inicio >= ? and e.ideleicao NOT IN (select eleitor_cc from eleitor_eleicao where e.ideleicao=eleitor_cc);");
			mysql.setTimestamp(1,data);
            ResultSet rQ = mysql.executeQuery();
            
            while(rQ.next()) {
            	eleicoes.add(new Eleicoes(rQ.getInt("ideleicao"),rQ.getString("tipo"),sdf.parse(rQ.getString("data_inicio")),sdf.parse(rQ.getString("data_final")),rQ.getString("titulo"),rQ.getString("resumo")));            
            }
	
		}catch(SQLException e) {
			out.println("Something's wrong!!!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return eleicoes;
		
	}
	 public synchronized HashMap<Integer,HashMap<String,Timestamp>> auditoria() {
			HashMap<Integer,HashMap<String,Timestamp>> auditoria = new HashMap<Integer,HashMap<String,Timestamp>>();
			String nome;
			try {
				out.println("Searching results...");
				mysql = conn.prepareStatement("SELECT eleitor_cc,mesa_de_voto_nome,moment FROM basedados.eleitor_mesa_insta;");
				ResultSet rQ = mysql.executeQuery();

				while (rQ.next()) {
					HashMap<String,Timestamp> temp = new HashMap<String,Timestamp>();
					temp.put(rQ.getString("mesa_de_voto_nome"), rQ.getTimestamp("moment"));
					auditoria.put(rQ.getInt("eleitor_cc"), temp);
				}

			} catch (SQLException e) {
				out.println("Something's wrong!!!");
				
			}
			return auditoria;
		}
  
  public synchronized ArrayList<Eleicoes> listElectionsEnded(Timestamp data){
		ArrayList<Eleicoes> eleicoes = new ArrayList<Eleicoes>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			out.println("Getting elections...");
			mysql = conn.prepareStatement("select * from eleicao where data_final <= ?;");
			mysql.setTimestamp(1,data);
            ResultSet rQ = mysql.executeQuery();
            
            while(rQ.next()) {
            	eleicoes.add(new Eleicoes(rQ.getInt("ideleicao"),rQ.getString("tipo"),sdf.parse(rQ.getString("data_inicio")),sdf.parse(rQ.getString("data_final")),rQ.getString("titulo"),rQ.getString("resumo")));            
            }
	
		}catch(SQLException e) {
			out.println("Something's wrong!!!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return eleicoes;
		
	}



public synchronized ArrayList<Eleicoes> listElectionsLive(Timestamp data){
		ArrayList<Eleicoes> eleicoes = new ArrayList<Eleicoes>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			out.println("Getting elections...");
			mysql = conn.prepareStatement("select * from eleicao where data_final > ?;");
			mysql.setTimestamp(1,data);
            ResultSet rQ = mysql.executeQuery();
            
            while(rQ.next()) {
            	eleicoes.add(new Eleicoes(rQ.getInt("ideleicao"),rQ.getString("tipo"),sdf.parse(rQ.getString("data_inicio")),sdf.parse(rQ.getString("data_final")),rQ.getString("titulo"),rQ.getString("resumo")));            
            }
	
		}catch(SQLException e) {
			out.println("Something's wrong!!!");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return eleicoes;
		
	}

	public synchronized ArrayList<String> mesas_eleicao(int id_eleicao) {
		ArrayList<String> mesas_eleicao = new ArrayList<String>();
		try {
			out.println("Searching mesas elections...");
			mysql = conn
					.prepareStatement("SELECT mesa_de_voto_nome FROM bd.votosbna where eleicao_ideleicao=?;");
			mysql.setInt(1, id_eleicao);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				mesas_eleicao.add(rQ.getString("mesa_de_voto_nome"));
			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return mesas_eleicao;
		}
		return mesas_eleicao;

	}

	public synchronized String get_type_eleicao_by_id(int id_eleicao) {
		String tipo = "Not found";
		try {
			out.println("Searching mesas elections...");
			mysql = conn.prepareStatement("SELECT tipo FROM bd.eleicao where ideleicao=?;");
			mysql.setInt(1, id_eleicao);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				tipo = rQ.getString("tipo");

			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return tipo;
		}
		return tipo;

	}

	public synchronized String get_location_election(int id_eleicao) {
		String location = "None";
		String tipo = get_type_eleicao_by_id(id_eleicao);
		if (tipo.equals("department") || tipo.equals("nucleo")) {
			try {
				out.println("Searching locatison election...");
				mysql = conn.prepareStatement(
						"SELECT departamento_nome FROM bd.eleicao_departamento where ideleicao1=?;");
				mysql.setInt(1, id_eleicao);
				ResultSet rQ = mysql.executeQuery();

				while (rQ.next()) {
					location = rQ.getString("departamento_nome");

				}
			} catch (SQLException e) {
				out.println("Something's wrong!!!");
			}
		} else if (tipo.equals("faculty")) {
			try {
				out.println("Searching location election...");
				mysql = conn
						.prepareStatement("SELECT faculdade_nome FROM bd.eleicao_faculdade where ideleicao1=?;");
				mysql.setInt(1, id_eleicao);
				ResultSet rQ = mysql.executeQuery();

				while (rQ.next()) {
					location = rQ.getString("faculdade_nome");

				}
			} catch (SQLException e) {
				out.println("Something's wrong!!!");
			}
		}
		return location;

	}

	public synchronized String get_department_eleitor(int cc) {
		String location = "None";
		try {
			out.println("Searching location department of elector...");
			mysql = conn.prepareStatement(
					"SELECT departamento_nome FROM bd.eleitor_departamento where eleitor_cc=?;");
			mysql.setInt(1, cc);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				location = rQ.getString("departamento_nome");

			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}
		return location;
	}

	public synchronized String get_faculdade_eleitor(int cc) {
		String location = "None";
		try {
			out.println("Searching location faculdade of elector...");
			mysql = conn.prepareStatement("SELECT faculdade_nome FROM bd.eleitor_faculdade where eleitor_cc=?;");
			mysql.setInt(1, cc);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				location = rQ.getString("faculdade_nome");

			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}
		return location;
	}

	public synchronized ArrayList<Lista_candidata> print_lists_for_person(int cc, String mesaVoto) {
		Pessoa pessoa_escolhida = null;
		for (Pessoa x : get_pessoas()) {
			if (cc == x.numero_cc) {
				pessoa_escolhida = x;
				break;
			}
		}
		int id = get_election_of_table(mesaVoto);
		String tipo = get_type_eleicao_by_id(id);
		String location = "None";
		ArrayList<Integer> ids_usados = new ArrayList<Integer>();
		try {
			mysql = conn
					.prepareStatement("SELECT eleicao_ideleicao FROM bd.eleitor_eleicao where eleitor_cc=?;");
			mysql.setInt(1, cc);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				ids_usados.add(rQ.getInt("eleicao_ideleicao"));

			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}

		ArrayList<Lista_candidata> lists = get_lists_election(id);
		ArrayList<Lista_candidata> lists_to_return = new ArrayList<Lista_candidata>();
		if (!ids_usados.contains(id)) {
			if (tipo.equals("geral")) {
				for (Lista_candidata x : lists) {
					if (x.classe.equals(pessoa_escolhida.cargo)) {
						lists_to_return.add(x);
					}
				}
			} else if (tipo.equals("faculty")) {
				location = get_location_election(id);
				if (pessoa_escolhida.cargo.equals("teacher")) {
					if (get_faculdade_eleitor(pessoa_escolhida.numero_cc).equals(location)) {
						for (Lista_candidata x : lists) {
							if (x.classe.equals("teacher")) {
								lists_to_return.add(x);
							}
						}
					}
				}
			} else if (tipo.equals("department")) {
				location = get_location_election(id);
				if (pessoa_escolhida.cargo.equals("teacher")) {
					if (get_department_eleitor(pessoa_escolhida.numero_cc).equals(location)) {
						for (Lista_candidata x : lists) {
							if (x.classe.equals("teacher")) {
								lists_to_return.add(x);
							}
						}
					}
				}
			} else if (tipo.equals("nucleo")) {
				location = get_location_election(id);
				if (pessoa_escolhida.cargo.equals("student")) {
					if (get_department_eleitor(pessoa_escolhida.numero_cc).equals(location)) {
						for (Lista_candidata x : lists) {
							if (x.classe.equals("student")) {
								lists_to_return.add(x);
							}
						}
					}
				}

			}
		}

		return lists_to_return;
	}

	public synchronized Integer votos_null(int id_eleicao) {
		int null_votos = 0;
		try {
			out.println("calculating null votes...");
			mysql = conn.prepareStatement(
					"SELECT sum(num_votos_nulos) as votos_nulos FROM bd.votosbna where eleicao_ideleicao=?;");
			mysql.setInt(1, id_eleicao);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				null_votos = rQ.getInt("votos_nulos");
			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return null_votos;
		}
		return null_votos;

	}

	public synchronized Integer votos_brancos(int id_eleicao) {
		int votos_blanc = 0;
		try {
			out.println("calculating null votes...");
			mysql = conn.prepareStatement(
					"SELECT sum(num_votos_branco) as votos_brancos FROM bd.votosbna where eleicao_ideleicao=?;");
			mysql.setInt(1, id_eleicao);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				votos_blanc = rQ.getInt("votos_brancos");
			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return votos_blanc;
		}
		return votos_blanc;

	}

	public synchronized ArrayList<String> dep_fac(String fac) {
		ArrayList<String> dep_fac = new ArrayList<String>();
		try {
			out.println("Departments of faculty....");
			mysql = conn.prepareStatement(
					"SELECT departamento_nome from bd.departamento_faculdade where faculdade_nome=?;");
			mysql.setString(1, fac);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				dep_fac.add(rQ.getString("departamento_nome"));
			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return dep_fac;
		}
		return dep_fac;
	}

	public synchronized HashMap<Integer, String> get_pessoas_mesas() {
		HashMap<Integer, String> cc_mesa = new HashMap<Integer, String>();
		try {
			out.println("Getting people on tables....");
			mysql = conn.prepareStatement("SELECT eleitor_cc,mesa_de_voto_nome from bd.eleitor_mesa_de_voto;");
			ResultSet rQ = mysql.executeQuery();
			while (rQ.next()) {
				cc_mesa.put(rQ.getInt("eleitor_cc"), rQ.getString("mesa_de_voto_nome"));
			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return cc_mesa;
		}
		return cc_mesa;
	}

	public synchronized ArrayList<String> listDepartments() {
		ArrayList<String> departments = new ArrayList<String>();
		try {
			out.println("Getting Departments...");
			mysql = conn.prepareStatement("select * from departamento;");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				departments.add(rQ.getString("nome"));
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}
		return departments;

	}

	public synchronized ArrayList<String> avaible_departments() {
		ArrayList<String> departments = new ArrayList<String>();
		try {
			out.println("Searching departments...");
			mysql = conn.prepareStatement(
					"SELECT nome from bd.departamento where not exists (select departamento_nome from bd.departamento_faculdade where nome = departamento_nome);");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				departments.add(rQ.getString("nome"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
			return departments;
		}
		return departments;

	}

	public synchronized ArrayList<String> departments_for_elections() {
		ArrayList<String> departments = new ArrayList<String>();
		try {
			out.println("Searching departments...");
			mysql = conn.prepareStatement(
					"SELECT nome from bd.departamento where exists (select departamento_nome from bd.departamento_faculdade where nome = departamento_nome);");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				departments.add(rQ.getString("nome"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
			return departments;
		}
		return departments;

	}

	public synchronized ArrayList<String> avaible_tables() {
		ArrayList<String> mesas = new ArrayList<String>();
		try {
			out.println("Searching avaible tables...");
			mysql = conn.prepareStatement(
					"select nome from bd.mesa_de_voto where nome NOT IN (select mesa_de_voto_nome from bd.votosbna);");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				mesas.add(rQ.getString("nome"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
			return mesas;
		}
		return mesas;

	}

	public synchronized Integer get_election_of_table(String name_table) {
		int id_eleicao = 0;
		try {
			out.println("getting election of table...");
			mysql = conn
					.prepareStatement("select eleicao_ideleicao from bd.votosbna where mesa_de_voto_nome=?;");
			mysql.setString(1, name_table);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				id_eleicao = rQ.getInt("eleicao_ideleicao");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		return id_eleicao;

	}

	public synchronized Integer count_number_of_persom_table(String name_table) {
		int num_pessoas = 0;
		try {
			out.println("getting table number of people...");
			mysql = conn.prepareStatement(
					"select count(eleitor_cc) as number from bd.eleitor_mesa_de_voto where mesa_de_voto_nome=?;");
			mysql.setString(1, name_table);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				num_pessoas = rQ.getInt("number");
				return num_pessoas;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		return num_pessoas;

	}

	public synchronized ArrayList<Lista_candidata> get_lists_election(int id_election) {
		ArrayList<Lista_candidata> lists_election = new ArrayList<Lista_candidata>();
		try {
			out.println("Searching lists election...");

			mysql = conn.prepareStatement(
					"select id,classe,nome,num_votos from candidato where id in ( select id_lista from tipo_eleitores_vota where ideleicao1=?);");
			mysql.setInt(1, id_election);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				Lista_candidata lista_add = new Lista_candidata(rQ.getInt("id"), rQ.getString("nome"),
						rQ.getString("classe"), rQ.getInt("num_votos"));
				lists_election.add(lista_add);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
			return lists_election;
		}
		return lists_election;

	}

	public synchronized void update_dep_fac(String department, String faculdade) {
		out.println("Adding Department...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.departamento_faculdade(departamento_nome,faculdade_nome) VALUES(?,?);");
			mysql.setString(1, department);
			mysql.setString(2, faculdade);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_eleicao_dep(int id, String departamento) {
		out.println("Adding Department of election...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleicao_departamento(ideleicao1,departamento_nome) VALUES(?,?);");
			mysql.setInt(1, id);
			mysql.setString(2, departamento);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_person_table(int cc, String mesa) {
		out.println("Adding person to table...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleitor_mesa_de_voto(eleitor_cc,mesa_de_voto_nome) VALUES(?,?);");
			mysql.setInt(1, cc);
			mysql.setString(2, mesa);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_fac_eleicao(int id, String faculdade) {
		out.println("Adding Faculty of election...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleicao_faculdade(ideleicao1,faculdade_nome) VALUES(?,?);");
			mysql.setInt(1, id);
			mysql.setString(2, faculdade);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void add_pessoa(int cc, String pessoa, String pass, int phone, Date expiration, String morada,
			String cargo) {
		out.println("Adding Person...");
		try {
			java.sql.Timestamp date = new java.sql.Timestamp(expiration.getTime());
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleitor(cc,nome,password,numtelefone,validadecc,morada,cargo) VALUES(?,?,?,?,?,?,?);");
			mysql.setInt(1, cc);
			mysql.setString(2, pessoa);
			mysql.setString(3, pass);
			mysql.setInt(4, phone);
			mysql.setTimestamp(5, date);
			mysql.setString(6, morada);
			mysql.setString(7, cargo);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void add_eleicao(int id, java.util.Date inicio, Date fim, String titulo, String resumo,
			String tipo) {
		out.println("Adding eleicao...");
		try {
			java.sql.Timestamp date = new java.sql.Timestamp(inicio.getTime());
			out.print(date);
			java.sql.Timestamp date2 = new java.sql.Timestamp(fim.getTime());
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleicao(ideleicao,data_inicio,data_final,titulo,resumo,tipo) VALUES(?,?,?,?,?,?);");
			mysql.setInt(1, id);
			mysql.setTimestamp(2, date);
			mysql.setTimestamp(3, date2);
			mysql.setString(4, titulo);
			mysql.setString(5, resumo);
			mysql.setString(6, tipo);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void delete_table_from_election(String nome_mesa) {
		out.println("Deleting table from votosbna...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.votosbna where mesa_de_voto_nome=?;");
			mysql.setString(1, nome_mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		out.println("Deleting table from votocandidatos...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.votoscandidatos where nome_mesa=?;");
			mysql.setString(1, nome_mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		out.println("Deleting table from faculty if exists...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.mesa_de_voto_faculdade where mesa_de_voto_nome=?;");
			mysql.setString(1, nome_mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		out.println("Deleting table from department if exists...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.departamento_mesa_de_voto where mesa_de_voto_nome=?;");
			mysql.setString(1, nome_mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		out.println("Deleting table from eleitor mesa de voto if exists...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.eleitor_mesa_de_voto where mesa_de_voto_nome=?;");
			mysql.setString(1, nome_mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void delete_table_from_department(String nome_mesa) {
		out.println("Deleting table from department...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.departamento_mesa_de_voto where mesa_de_voto_nome=?;");
			mysql.setString(1, nome_mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void delete_table_from_faculty(String nome_mesa) {
		out.println("Deleting table from faculty...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.mesa_de_voto_faculdade where mesa_de_voto_nome=?;");
			mysql.setString(1, nome_mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void delete_list(int id_lista) {
		out.println("Deleting list from tables...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.votoscandidatos where id_lista=?;");
			mysql.setInt(1, id_lista);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		out.println("Deleting listagem to election...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.tipo_eleitores_vota where id_lista=?;");
			mysql.setInt(1, id_lista);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		out.println("Deleting list from list of lists...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.candidato where id=?;");
			mysql.setInt(1, id_lista);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void add_mesa(String name) {
		out.println("Adding Table...");
		try {
			mysql = conn.prepareStatement("INSERT INTO bd.mesa_de_voto(nome) VALUES(?);");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_mesa_department(String name, String departamento) {
		out.println("Adding Table...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.departamento_mesa_de_voto(departamento_nome,mesa_de_voto_nome) VALUES(?,?);");
			mysql.setString(1, departamento);
			mysql.setString(2, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_mesa_faculdade(String name, String faculdade) {
		out.println("Adding Table...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.mesa_de_voto_faculdade(mesa_de_voto_nome,faculdade_nome) VALUES(?,?);");
			mysql.setString(1, name);
			mysql.setString(2, faculdade);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	/*
	 * public synchronized ArrayList<Integer> cc_numbers() {// o cc sao 8 nao 6
	 * mudar este para o return pessoas? out.println("Retriving cc numbers...");
	 * ArrayList<Integer> cc_numbers = new ArrayList<Integer>(); try { mysql =
	 * conn.prepareStatement("SELECT cc from bd.eleitor;"); ResultSet rQ =
	 * mysql.executeQuery();
	 * 
	 * while (rQ.next()) { cc_numbers.add(rQ.getInt("cc")); }
	 * 
	 * } catch (SQLException e) { e.printStackTrace();
	 * out.println("Something's wrong!!!"); } return cc_numbers; }
	 */

	public synchronized void insert_listas(int id_eleicao, String nome_lista, String tipo, int id_list) {
		out.println("Adding list to election...");
		try {
			mysql = conn.prepareStatement("INSERT INTO bd.candidato(id,classe,nome,num_votos) VALUES(?,?,?,?);");
			mysql.setInt(1, id_list);
			mysql.setString(2, tipo);
			mysql.setString(3, nome_lista);
			mysql.setInt(4, 0);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		try {
			mysql = conn
					.prepareStatement("INSERT INTO bd.tipo_eleitores_vota(id_lista,ideleicao1) VALUES(?,?);");
			mysql.setInt(1, id_list);
			mysql.setInt(2, id_eleicao);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_mesa_election(String nome_mesa, int id) {
		out.println("Adding mesa to election...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.votosbna(num_votos_branco,num_votos_nulos,mesa_de_voto_nome,eleicao_ideleicao) VALUES(?,?,?,?);");
			mysql.setInt(1, 0);
			mysql.setInt(2, 0);
			mysql.setString(3, nome_mesa);
			mysql.setInt(4, id);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_mesa_election_list_votes(String nome_mesa, int id_lista) {
		out.println("Adding mesa to election...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.votoscandidatos(nome_mesa,id_lista,num_votos) VALUES(?,?,?);");
			mysql.setString(1, nome_mesa);
			mysql.setInt(2, id_lista);
			mysql.setInt(3, 0);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_department(int cc, String department) {
		out.println("Adding Department to person...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleitor_departamento(eleitor_cc,departamento_nome) VALUES(?,?);");
			mysql.setInt(1, cc);
			mysql.setString(2, department);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void insert_faculty(int cc, String faculty) {
		out.println("Adding Faculty to person...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleitor_faculdade(eleitor_cc,faculdade_nome) VALUES(?,?);");
			mysql.setInt(1, cc);
			mysql.setString(2, faculty);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized ArrayList<Pessoa> get_pessoas() {
		out.println("Retriving Users...");
		ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
		try {
			mysql = conn.prepareStatement(
					"SELECT cc,nome,password,morada,cargo,numtelefone,validadecc from bd.eleitor;");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				Pessoa pessoa1 = new Pessoa(rQ.getString("nome"), rQ.getString("cargo"), rQ.getString("password"),
						rQ.getString("morada"), rQ.getInt("numtelefone"), rQ.getInt("cc"), rQ.getDate("validadecc"));
				pessoas.add(pessoa1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		return pessoas;
	}

	public synchronized String location_table(String name_table) {
		String location = "None";
		out.println("Retriving location...");
		try {
			mysql = conn.prepareStatement(
					"SELECT departamento_nome from bd.departamento_mesa_de_voto where mesa_de_voto_nome=?;");
			mysql.setString(1, name_table);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				location = rQ.getString("departamento_nome");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		if (location.equals("None")) {
			try {
				mysql = conn.prepareStatement(
						"SELECT faculdade_nome from bd.mesa_de_voto_faculdade where mesa_de_voto_nome=?;");
				mysql.setString(1, name_table);
				ResultSet rQ = mysql.executeQuery();

				while (rQ.next()) {
					location = rQ.getString("faculdade_nome");
				}

			} catch (SQLException e) {
				e.printStackTrace();
				out.println("Something's wrong!!!");
			}
		}
		return location;
	}

	public synchronized ArrayList<Integer> id_listas() {
		out.println("Retriving id lists...");
		ArrayList<Integer> ids_listas = new ArrayList<Integer>();
		try {
			mysql = conn.prepareStatement("SELECT id from bd.candidato;");
			ResultSet rQ = mysql.executeQuery();
			while (rQ.next()) {
				ids_listas.add(rQ.getInt("id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		return ids_listas;
	}

	public synchronized String update_person(Pessoa update_per) {
		out.println("Updating person...");
		java.sql.Timestamp date = new java.sql.Timestamp(update_per.validade_cc.getTime());
		try {
			mysql = conn.prepareStatement(
					"UPDATE bd.eleitor SET password = ?, numtelefone=?,validadecc=?, morada=?, cargo=? WHERE cc=?;");
			mysql.setString(1, update_per.password);
			mysql.setInt(2, update_per.telefone);
			mysql.setTimestamp(3, date);
			mysql.setString(4, update_per.morada);
			mysql.setString(5, update_per.cargo);
			mysql.setInt(6, update_per.numero_cc);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			return "Person has been deleted by another admin!\n";
		}
		return "Person updated\n";
	}

	public synchronized String update_tab_location_fac(String mesa, String faculdade) {
		out.println("Updating faculty of table...");
		try {
			mysql = conn.prepareStatement(
					"UPDATE bd.mesa_de_voto_faculdade SET faculdade_nome = ? WHERE mesa_de_voto_nome=?;");
			mysql.setString(1, faculdade);
			mysql.setString(2, mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			return "table has been deleted by another admin!\n";
		}
		return "table location updated\n";
	}

	public synchronized String update_tab_location_dep(String mesa, String departamento) {
		out.println("Updating department of table...");
		try {
			mysql = conn.prepareStatement(
					"UPDATE bd.departamento_mesa_de_voto SET departamento_nome = ? WHERE mesa_de_voto_nome=?;");
			mysql.setString(1, departamento);
			mysql.setString(2, mesa);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			return "table has been deleted by another admin!\n";
		}
		return "table location updated\n";
	}

	public synchronized String update_election(Eleicoes eleicao) {
		out.println("Updating eleicao...");
		try {
			java.sql.Timestamp date = new java.sql.Timestamp(eleicao.start.getTime());
			java.sql.Timestamp date2 = new java.sql.Timestamp(eleicao.end.getTime());
			mysql = conn.prepareStatement(
					"UPDATE bd.eleicao SET data_inicio=?,data_final=?, titulo=?, resumo=?,tipo=? WHERE ideleicao=?;");
			mysql.setTimestamp(1, date);
			mysql.setTimestamp(2, date2);
			mysql.setString(3, eleicao.titulo);
			mysql.setString(4, eleicao.resumo);
			mysql.setString(5, eleicao.tipo);
			mysql.setInt(6, eleicao.id);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Impossible update since election has been deleted by another admin!!!");
		}
		return "Election updated\n";
	}

	public synchronized String update_department_eleicao(int id, String department) {
		out.println("Updating department of election...");
		try {
			mysql = conn.prepareStatement(
					"UPDATE bd.eleicao_departamento SET departamento_nome = ? WHERE ideleicao1=?;");
			mysql.setString(1, department);
			mysql.setInt(2, id);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Person has been deleted by another admin!\n";
		}
		return "Person updated\n";
	}

	public synchronized String update_faculty_eleicao(int id, String faculty) {
		out.println("Updating faculty of election...");
		try {
			mysql = conn
					.prepareStatement("UPDATE bd.eleicao_faculdade SET faculdade_nome = ? WHERE ideleicao1=?;");
			mysql.setString(1, faculty);
			mysql.setInt(2, id);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Person has been deleted by another admin!\n";
		}
		return "Person updated\n";
	}

	public synchronized String update_department(String departamento, int cc) {
		out.println("Updating department of person...");
		try {
			mysql = conn.prepareStatement(
					"UPDATE bd.eleitor_departamento SET departamento_nome = ? WHERE eleitor_cc=?;");
			mysql.setString(1, departamento);
			mysql.setInt(2, cc);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Person has been deleted by another admin!\n";
		}
		return "Person updated\n";
	}

	public synchronized String del_dep_person(int cc) {
		out.println("Deleting department of person...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.eleitor_departamento WHERE eleitor_cc=?;");
			mysql.setInt(1, cc);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Person has been deleted by another admin!\n";
		}
		return "Person updated\n";
	}

	public synchronized void del_local_election_fac(int id_election) {
		try {
			mysql = conn.prepareStatement("DELETE from bd.eleicao_faculdade WHERE ideleicao1=?;");
			mysql.setInt(1, id_election);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public synchronized void del_local_election_dep(int id_election) {
		out.println("Deleting local of election...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.eleicao_departamento WHERE ideleicao1=?;");
			mysql.setInt(1, id_election);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized String get_location_election_depar(int id_election) {
		out.println("Getting local of election...");
		try {
			mysql = conn.prepareStatement(
					"Select from departamento_nome bd.eleicao_departamento WHERE ideleicao1=?;");
			mysql.setInt(1, id_election);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "not found";
		}
		return "found";
	}

	public synchronized String get_location_election_fac(int id_election) {
		out.println("Getting local of election...");
		try {
			mysql = conn.prepareStatement("Select faculdade_nome from bd.eleicao_faculdade WHERE ideleicao1=?;");
			mysql.setInt(1, id_election);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "not found";
		}
		return "found";
	}

	public synchronized String del_person_dep(String departamento) {
		out.println("Deleting department of person...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.eleitor_departamento WHERE departamento_nome=?;");
			mysql.setString(1, departamento);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Person has been deleted by another admin!\n";
		}
		return "Person updated\n";
	}

	public synchronized ArrayList<String> return_available_mesas() {
		ArrayList<String> mesas = new ArrayList<String>();
		try {
			out.println("Getting all available tables for election...");
			mysql = conn.prepareStatement(
					"select nome from bd.mesa_de_voto where nome NOT IN (select mesa_de_voto_nome from departamento_mesa_de_voto);");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				mesas.add(rQ.getString("nome"));
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}
		return mesas;

	}

	public synchronized ArrayList<String> mesas_prontas_abrir() {
		ArrayList<Integer> eleicoes = new ArrayList<Integer>();
		Date date = new Date();
		for (Eleicoes x : return_eleicoes()) {
			if (x.start.before(date) && x.end.after(date)) {
				eleicoes.add(x.id);
			}
		}
		ArrayList<String> mesas = new ArrayList<String>();
		try {
			out.println("Getting all available tables for election...");
			mysql = conn.prepareStatement("select mesa_de_voto_nome,eleicao_ideleicao from bd.votosbna;");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				if (eleicoes.contains(rQ.getInt("eleicao_ideleicao"))) {
					if (count_number_of_persom_table(rQ.getString("mesa_de_voto_nome")) == 3) {
						mesas.add(rQ.getString("mesa_de_voto_nome"));
					}
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}
		return mesas;

	}

	public synchronized boolean searchVoterForDelete(int cc) {
		int idBD;
		out.println("Checking if a voter as any activity...");
		try {
			mysql = conn.prepareStatement("SELECT eleitor_cc FROM bd.eleitor_eleicao WHERE eleitor_cc = ?;");
			mysql.setInt(1, cc);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				idBD = rQ.getInt("eleitor_cc");
				if (idBD == cc) {
					out.println("Voter as already history...can't be deleted!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return false;
		}
		try {
			mysql = conn
					.prepareStatement("SELECT eleitor_cc FROM bd.eleitor_mesa_de_voto WHERE eleitor_cc = ?;");
			mysql.setInt(1, cc);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				idBD = rQ.getInt("eleitor_cc");
				if (idBD == cc) {
					out.println("Voter is in a table at the moment...can't be deleted!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return false;
		}
		return true;
	}

	public synchronized boolean searchMesaVBNA(String name) {
		String idBD;
		try {
			out.println("Checking if table is ok to delete...");
			mysql = conn
					.prepareStatement("SELECT mesa_de_voto_nome FROM bd.votosbna WHERE mesa_de_voto_nome = ?;");
			mysql.setString(1, name);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				idBD = rQ.getString("mesa_de_voto_nome");
				if (idBD.equals(name)) {
					out.println("There was a match...can't delete!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong searching table!!!");
			return false;
		}
		return true;
	}

	public synchronized boolean deleteTable(String name) {
		try {
			out.println("Deleting table...");
			mysql = conn.prepareStatement("DELETE FROM mesa_de_voto WHERE nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();
			out.println("Table deleted...");

		} catch (SQLException e) {
			out.println("Something's wrong trying to delete table!!!");
			return false;
		}
		return true;
	}

	public synchronized boolean deleteVoter(int cc) {
		out.println("Deleting voter...");
		try {
			mysql = conn.prepareStatement("DELETE FROM eleitor_departamento WHERE eleitor_cc=?;");
			mysql.setInt(1, cc);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting form eleitor_departamento!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM eleitor_faculdade WHERE eleitor_cc=?;");
			mysql.setInt(1, cc);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from eleitor_faculdade!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM eleitor WHERE cc=?;");
			mysql.setInt(1, cc);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from eleitor!!!");
			return false;
		}
		out.println("Voter Deleted...");
		return true;
	}

	public synchronized ArrayList<String> listTables() {
		ArrayList<String> mesas = new ArrayList<String>();
		try {
			out.println("Getting Tables...");
			mysql = conn.prepareStatement("select * from mesa_de_voto;");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				mesas.add(rQ.getString("nome"));
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}
		return mesas;

	}

	public synchronized ArrayList<Eleicoes> return_eleicoes() {
		ArrayList<Eleicoes> eleicoes = new ArrayList<Eleicoes>();
		try {
			out.println("Getting all elections...");
			mysql = conn.prepareStatement(
					"select ideleicao,data_inicio,data_final,titulo,resumo,tipo from bd.eleicao;");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				Eleicoes nova_eleicao = new Eleicoes(rQ.getInt("ideleicao"), rQ.getString("tipo"),
						rQ.getTimestamp("data_inicio"), rQ.getTimestamp("data_final"), rQ.getString("titulo"),
						rQ.getString("resumo"));
				eleicoes.add(nova_eleicao);
			}
		} catch (SQLException e) {
			out.println("Something's wrong!!!");
		}
		return eleicoes;
	}

	public synchronized String del_dep_fac(String departamento) {
		out.println("Deleting department of faculty...");
		try {
			mysql = conn.prepareStatement("DELETE from bd.departamento_faculdade WHERE departamento_nome=?;");
			mysql.setString(1, departamento);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "mudar estes prints!\n";
		}
		return "Person updated\n";
	}

	public synchronized String update_faculty(String faculty, int cc) {
		out.println("Updating faculty of person...");
		try {
			mysql = conn
					.prepareStatement("UPDATE bd.eleitor_faculdade SET faculdade_nome = ? WHERE eleitor_cc=?;");
			mysql.setString(1, faculty);
			mysql.setInt(2, cc);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "Person has been deleted by another admin!\n";
		}
		return "Person updated\n";
	}

	public synchronized String fac_pessoa(int cc) {
		out.println("Retriving faculty of User...");
		String faculty = "None";
		try {
			mysql = conn.prepareStatement("SELECT faculdade_nome from bd.eleitor_faculdade where eleitor_cc=?;");
			mysql.setInt(1, cc);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				faculty = rQ.getString("faculdade_nome");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		return faculty;
	}

	public synchronized String dep_pessoa(int cc) {
		out.println("Retriving department of user...");
		String department = "None";
		try {
			mysql = conn.prepareStatement(
					"SELECT departamento_nome from bd.eleitor_departamento where eleitor_cc=?;");
			mysql.setInt(1, cc);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				department = rQ.getString("departamento_nome");
			}

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		return department;
	}

	public synchronized ArrayList<String> tables() {
		out.println("Retriving existing tables...");
		ArrayList<String> exit_tab = new ArrayList<String>();
		try {
			mysql = conn.prepareStatement("SELECT nome from bd.mesa_de_voto;");
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				exit_tab.add(rQ.getString("nome"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		return exit_tab;
	}

	public synchronized boolean search_faculty(String name) {
		String nome;
		try {
			out.println("Searching Faculty...");
			mysql = conn.prepareStatement("SELECT nome FROM bd.faculdade WHERE nome = ?;");
			mysql.setString(1, name);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				nome = rQ.getString("nome");
				if (nome.equals(name)) {
					out.println("There was a match...sending back to the user!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return false;
		}
		return true;
	}

	public synchronized boolean search_department(String name) {
		String nome;
		try {
			out.println("Searching Department...");
			mysql = conn.prepareStatement("SELECT nome FROM bd.departamento WHERE nome = ?;");
			mysql.setString(1, name);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				nome = rQ.getString("nome");
				if (nome.equals(name)) {
					out.println("There was a match...sending back to the user!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong!!!");
			return false;
		}
		return true;
	}

	public synchronized void create_faculty(String name) {
		out.println("Creating Faculty...");
		try {
			mysql = conn.prepareStatement("INSERT INTO bd.faculdade(nome) VALUES(?);");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void add_election_fac(String faculty, int id) {
		out.println("Adding election to fac...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleicao_faculdade(ideleicao1,faculdade_nome) VALUES(?,?);");
			mysql.setInt(1, id);
			mysql.setString(2, faculty);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void add_election_dep(String department, int id) {
		out.println("Adding election to dep...");
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleicao_departamento(ideleicao1,departamento_nome) VALUES(?,?);");
			mysql.setInt(1, id);
			mysql.setString(2, department);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void delete_person_from_table(String nome_mesa, int cc) {
		out.println("Deleting table from department...");
		try {
			mysql = conn.prepareStatement(
					"DELETE from bd.eleitor_mesa_de_voto where mesa_de_voto_nome=? and eleitor_cc=?;");
			mysql.setString(1, nome_mesa);
			mysql.setInt(2, cc);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	public synchronized void AddDepartments(String name) {
		out.println("Creating Department...");
		try {
			mysql = conn.prepareStatement("INSERT INTO bd.departamento(nome) VALUES(?);");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
	}

	/*
	 * public synchronized boolean search_election() { int idBD; try {
	 * out.println("Searching Election..."); mysql = conn.
	 * prepareStatement("SELECT ideleicao FROM bd.eleicao WHERE ideleicao = ?;"
	 * ); mysql.setInt(1, id); ResultSet rQ = mysql.executeQuery();
	 * 
	 * while (rQ.next()) { idBD = rQ.getInt("ideleicao"); if (idBD == id) {
	 * out.println("There was a match...sending back to the user!"); return false; }
	 * }
	 * 
	 * } catch (SQLException e) { out.println("Something's wrong!!!"); return false;
	 * } return true; }
	 */

	// =========================================================
	public static void main(String args[]) throws RemoteException {
		load_config();
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bd", "root", "2014228262");

		} catch (SQLException ex) {
			// handle any errors
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		try {
			conn.setAutoCommit(false);
		} catch (SQLException ex) {
			// conn.rollback();
		}
		try {
			Registry createRMIRegistry = LocateRegistry.createRegistry(rmiRegistry);
			DataServer dataserver = new DataServer();
			createRMIRegistry.rebind("rmi", dataserver);
			System.out.println("Hello Server ready.");
		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
		System.out.println("\n\nData Server is ready for business!!!");
	}

	public void notifyAdmins(String notify) {
		for (Admin_rmi_I x : admins) {
			try {
				x.imprimeNotificacao(notify);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean getATerminal(int cc) {
		for (Pessoa x : get_pessoas()) {
			if (x.numero_cc == cc) {
				return true;

			}
		}
		return false;

	}

	public synchronized String register_vote(int cc, String list, String table) throws RemoteException {
		ArrayList<Lista_candidata> lists = print_lists_for_person(cc, table);
		int check = 0;
		int id_lista = 0;
		for (Lista_candidata x : lists) {
			if (x.nome_lista.equals(list)) {
				check = 1;
				id_lista = x.id;
			}
		}
		if (list.equals("blanc")) {
			int votos_blanc = 0;
			try {
				mysql = conn
						.prepareStatement("SELECT num_votos_branco from bd.votosbna WHERE mesa_de_voto_nome=?;");
				mysql.setString(1, table);
				ResultSet rQ = mysql.executeQuery();

				while (rQ.next()) {
					votos_blanc = rQ.getInt("num_votos_branco");
				}

			} catch (SQLException e) {
				return "erro1?!\n";
			}
			try {
				mysql = conn.prepareStatement(
						"UPDATE bd.votosbna SET num_votos_branco = ? WHERE mesa_de_voto_nome=?;");
				mysql.setInt(1, votos_blanc + 1);
				mysql.setString(2, table);
				mysql.executeUpdate();
				conn.commit();
			} catch (SQLException e) {
				return "erro2!\n";
			}
			adicional(cc, table, id_lista);
			return "blanc vote registed";
		} else {
			if (check == 1) {
				int votos = 0;
				try {
					mysql = conn.prepareStatement("SELECT num_votos from bd.candidato WHERE id=?;");
					mysql.setInt(1, id_lista);
					ResultSet rQ = mysql.executeQuery();

					while (rQ.next()) {
						votos = rQ.getInt("num_votos");
					}

				} catch (SQLException e) {
					return "erro69?!\n";
				}
				try {
					mysql = conn.prepareStatement("UPDATE bd.candidato SET num_votos = ? WHERE id=?;");
					mysql.setInt(1, votos + 1);
					mysql.setInt(2, id_lista);
					mysql.executeUpdate();
					conn.commit();
				} catch (SQLException e) {
					return "erro2!\n";
				}
				int votos1 = 0;
				try {
					mysql = conn.prepareStatement("SELECT num_votos from bd.votoscandidatos WHERE id_lista=? and nome_mesa=?;");
					mysql.setInt(1, id_lista);
					mysql.setString(2, table);
					ResultSet rQ = mysql.executeQuery();

					while (rQ.next()) {
						votos1 = rQ.getInt("num_votos");
					}

				} catch (SQLException e) {
					return "erro44444?!\n";
				}
				try {
					mysql = conn.prepareStatement(
							"UPDATE bd.votoscandidatos SET num_votos = ? WHERE id_lista=? and nome_mesa=?;");
					mysql.setInt(1, votos1 + 1);
					mysql.setInt(2, id_lista);
					mysql.setString(3, table);
					mysql.executeUpdate();
					conn.commit();
				} catch (SQLException e) {
					return "erro2!\n";
				}
				adicional(cc, table, id_lista);
				return "vote registered";
			} else {
				int votos_null = 0;
				try {
					mysql = conn.prepareStatement(
							"SELECT num_votos_nulos from bd.votosbna WHERE mesa_de_voto_nome=?;");
					mysql.setString(1, table);
					ResultSet rQ = mysql.executeQuery();

					while (rQ.next()) {
						votos_null = rQ.getInt("num_votos_nulos");
					}

				} catch (SQLException e) {
					return "erro900909090?!\n";
				}
				try {
					mysql = conn.prepareStatement(
							"UPDATE bd.votosbna SET num_votos_nulos = ? WHERE mesa_de_voto_nome=?;");
					mysql.setInt(1, votos_null + 1);
					mysql.setString(2, table);
					mysql.executeUpdate();
					conn.commit();
				} catch (SQLException e) {
					return "erro2!\n";
				}
				adicional(cc, table, id_lista);
				return "vote registed as null";
			}
		}
	}

	public synchronized void adicional(int cc, String table, int id_lista) throws RemoteException {
		try {
			Date date = new Date();
			java.sql.Timestamp date1 = new java.sql.Timestamp(date.getTime());
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleitor_mesa_insta(eleitor_cc,mesa_de_voto_nome,moment) VALUES(?,?,?);");
			mysql.setInt(1, cc);
			mysql.setString(2, table);
			mysql.setTimestamp(3, date1);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		int id_eleicao = 0;
		try {
			mysql = conn.prepareStatement("SELECT ideleicao1 from bd.tipo_eleitores_vota WHERE id_lista=?;");
			mysql.setInt(1, id_lista);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				id_eleicao = rQ.getInt("ideleicao1");
			}

		} catch (SQLException e) {
			out.println("error");
		}
		try {
			mysql = conn.prepareStatement(
					"INSERT INTO bd.eleitor_eleicao(eleitor_cc,eleicao_ideleicao) VALUES(?,?);");
			mysql.setInt(1, cc);
			mysql.setInt(2, id_eleicao);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong!!!");
		}
		notifyAdmins("The person with the cc number of:" + cc + " has votted on the table:" + table);
		// falta aqui uma notificaçao
	}

	public void loginDone(String cc, String mesa) throws RemoteException {
		for (Pessoa x : get_pessoas()) {
			if (x.numero_cc == Integer.parseInt(cc)) {
				out.println(x.numero_cc+"="+Integer.parseInt(cc));
				notifyAdmins("The person:" + x.nome + " has logged in the table:" + mesa);
				break;
			}
		}
	}

	public synchronized boolean searchFacultyForDelete(String name) {
		String idBD;
		out.println("Checking if faculty is ok to delete...");
		// Verifica se têm mesas associadas
		try {
			mysql = conn.prepareStatement(
					"SELECT faculdade_nome FROM bd.mesa_de_voto_faculdade WHERE faculdade_nome = ?;");
			mysql.setString(1, name);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				idBD = rQ.getString("faculdade_nome");
				if (idBD.equals(name)) {
					out.println("Faculty have a table within...can't be deleted!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong analysing tables from faculty!!!");
			return false;
		}
		// Verifica se têm eleitores registados
		try {
			mysql = conn.prepareStatement(
					"SELECT faculdade_nome FROM bd.eleitor_faculdade WHERE faculdade_nome = ?;");
			mysql.setString(1, name);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				idBD = rQ.getString("faculdade_nome");
				if (idBD.equals(name)) {
					out.println("Faculty have people on their data base ...can't be deleted!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong analysing voters residents at faculty!!!");
			return false;
		}
		// Verifica se contem departamentos
		try {
			mysql = conn.prepareStatement(
					"SELECT faculdade_nome FROM bd.departamento_faculdade WHERE faculdade_nome = ?;");
			mysql.setString(1, name);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				idBD = rQ.getString("faculdade_nome");
				if (idBD.equals(name)) {
					out.println("Faculty have departments on their data base ...can't be deleted!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong analysing departments from faculty!!!");
			return false;
		}

		return true;
	}

	public synchronized boolean searchDepartmentForDelete(String name) {
		String idBD;
		out.println("Checking if department is ok to delete...");
		try {
			mysql = conn.prepareStatement(
					"SELECT departamento_nome FROM bd.departamento_mesa_de_voto WHERE departamento_nome = ?;");
			mysql.setString(1, name);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				idBD = rQ.getString("departamento_nome");
				if (idBD.equals(name)) {
					out.println("Departament have a table within...can't be deleted!");
					return false;
				}
			}

		} catch (SQLException e) {
			out.println("Something's wrong analysing department!!!");
			return false;
		}
		return true;
	}

	public synchronized boolean deleteDepartment(String name) {
		out.println("Deleting Department...");
		try {
			mysql = conn.prepareStatement("DELETE FROM eleitor_departamento WHERE departamento_nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from eleitor_departamento!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM departamento_faculdade WHERE departamento_nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from departamento_faculdade!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM departamento WHERE nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from departamento!!!");
			return false;
		}
		out.println("Department Deleted...");
		return true;
	}

	public synchronized boolean deleteFaculty(String name) {
		out.println("Deleting Faculty...");
		try {
			mysql = conn.prepareStatement("DELETE FROM faculdade WHERE nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting faculty!!!");
			return false;
		}
		out.println("Faculty deleted...");
		return true;
	}

	public synchronized boolean login(String username, String password) {
		for (Pessoa x : get_pessoas()) {
			if (x.nome.equals(username)) {
				if (x.password.equals(password)) {
					x.loggedIN = true;
					return true;
				}
			}
		}
		return false;
	}

	public synchronized boolean deleteElection(int id) {
		String name;
		out.println("Deleting election...");
		// Eliminar do eleicao tabela candidato_eleicao
		try {
			mysql = conn.prepareStatement("DELETE FROM tipo_eleitores_vota WHERE ideleicao1=?;");
			mysql.setInt(1, id);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong deleting from candidato_eleicao!!!");
			return false;
		}
		// Eliminar eleicao da mesa
		// arranja mesa
		try {
			mysql = conn.prepareStatement("select mesa_de_voto_nome from votosbna where eleicao_ideleicao=?;");
			mysql.setInt(1, id);
			ResultSet rQ = mysql.executeQuery();

			while (rQ.next()) {
				name = rQ.getString("mesa_de_voto_nome");
				freeTablesBeforeDelete(name);
			}

		} catch (SQLException e) {
			out.println("Something's wrong on votosbna!!!");
			return false;
		}
		//////
		try {
			mysql = conn.prepareStatement("DELETE FROM votosbna WHERE eleicao_ideleicao=?;");
			mysql.setInt(1, id);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from votosbna!!!");
			return false;
		}
		// apagar do departamento e faculdade
		try {
			mysql = conn.prepareStatement("DELETE FROM eleicao_departamento WHERE ideleicao1=?;");
			mysql.setInt(1, id);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong deleting from department!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM eleicao_faculdade WHERE ideleicao1=?;");
			mysql.setInt(1, id);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong deleting from eleicao!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM eleicao WHERE ideleicao=?;");
			mysql.setInt(1, id);
			mysql.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("Something's wrong deleting from eleicao!!!");
			return false;
		}

		out.println("Election deleted...");
		return true;
	}

	public synchronized boolean freeTablesBeforeDelete(String name) {
		out.println("Freeing tables...");
		try {
			mysql = conn.prepareStatement("DELETE FROM eleitor_mesa_de_voto WHERE mesa_de_voto_nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from eleitor_mesa_de_voto!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM departamento_mesa_de_voto WHERE mesa_de_voto_nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from departamento_mesa_de_voto!!!");
			return false;
		}
		try {
			mysql = conn.prepareStatement("DELETE FROM mesa_de_voto_faculdade WHERE mesa_de_voto_nome=?;");
			mysql.setString(1, name);
			mysql.executeUpdate();
			conn.commit();

		} catch (SQLException e) {
			out.println("Something's wrong deleting from mesa_de_voto_faculdade!!!");
			return false;
		}
		return true;
	}
}
