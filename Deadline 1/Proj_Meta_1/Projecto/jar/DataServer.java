

import java.io.*;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import static java.lang.System.out;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class DataServer extends UnicastRemoteObject implements RMI, java.io.Serializable {
	public static int rmiRegistry;
	public static int rmiPort;
	public static int rmiPortSec;
	public static String rmiHost;
	public static String rmiHostSec;
	public Scanner inputI = new Scanner(System.in);
	public Scanner inputS = new Scanner(System.in);
	public static ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
	public static ArrayList<Eleicoes> eleitores = new ArrayList<Eleicoes>();
	public static ArrayList<Mesas> mesas_votos_todas = new ArrayList<Mesas>();
	public static ArrayList<Faculty> faculdades = new ArrayList<Faculty>();
	public static ArrayList<Department> departments = new ArrayList<Department>();
	public static ArrayList<Server_rmi> servers = new ArrayList<Server_rmi>();
	public static ArrayList<Admin_rmi_I> admins = new ArrayList<Admin_rmi_I>();

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataServer() throws RemoteException {
		super();
	}

	public synchronized static void pings(int escolha) {
		if (escolha == 0) {
			System.out.println("tentando receber");
			new UDPConnectionReceive(rmiHost, rmiPort, rmiPortSec);
		} else {
			System.out.println("Tornou-se principal");
			new UDPConnectionSend(rmiHost, rmiPort, rmiPortSec);
		}
	}

	public synchronized static void load_config() throws RemoteException {
		read();
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

	public synchronized boolean status(String cc) {
		for (Pessoa x : pessoas) {
			if (x.loggedIN) {
				return true;
			}
		}
		return false;
	}

	public synchronized String election_done() {
		String election = "";
		int i = 0;
		Date date = new Date();
		for (Eleicoes x : eleitores) {
			if (x.end.before(date)) {
				election += ++i + "-" + x.titulo + "\n";
			}
		}
		if (!election.equals("")) {
			return election;
		} else {
			return "No elections avaible";
		}
	}

	public synchronized String auditoria(Eleicoes election) {
		int check = 0;
		String auditoria = "Election:" + election.titulo + "\n";
		for (HashMap.Entry<Pessoa, HashMap<Mesas, Date>> entry : election.auditoria.entrySet()) {
			auditoria += "Pessoa: " + entry.getKey().nome + " CC: " + entry.getKey().numero_cc + " | ";
			for (HashMap.Entry<Mesas, Date> entry2 : entry.getValue().entrySet()) {
				auditoria += "Table: " + entry2.getKey().name + " Date: " + entry2.getValue() + "\n";
				check = 1;
			}
		}
		if (check == 1) {
			return auditoria;
		} else {
			return "No data to show";
		}
	}

	public synchronized String print_elections_for_person(String cc, Mesas table) {
		int i = 0;
		String final_lists = "";
		Pessoa select_person = get_person(cc);
		Date date = new Date();
		for (Eleicoes x : eleitores) {
			if (x.start.before(date) && x.end.after(date)) {
				if (!select_person.elections_done.contains(x)) {
					for (HashMap.Entry<String, HashMap<Lista_candidata, Integer>> entry : x.listas.todas_listas
							.entrySet()) {
						if (select_person.cargo.equals(entry.getKey())) {
							for (Mesas k : x.mesas_votos) {
								if (k.name.equals(table.name)) {
									for (HashMap.Entry<Lista_candidata, Integer> entry2 : entry.getValue().entrySet()) {
										++i;
										final_lists += "Election: " + x.titulo + " List: " + entry2.getKey().nome_lista
												+ "\n";
									}
								}
							}
						}
					}
				}
			}
		}
		if (i == 0) {
			return "No elections or list avaible for you to vote";
		} else {
			return final_lists + "In case of blanc vote type 'blanc' ";
		}
	}

	public synchronized String register_vote(String cc, String eleicao, String list, Mesas table) {
		int check = 0, check2 = 0;
		Date date = new Date();
		ArrayList<Eleicoes> elections = return_elections();
		Pessoa select_person = get_person(cc);
		for (Eleicoes x : elections) {
			if (eleicao.equals(x.titulo)) {
				check = 1;
				if (select_person.elections_done.contains(x) || x.start.after(date)
						|| !x.listas.todas_listas.containsKey(select_person.cargo)) {
					return "Election not avaible!";
				}
				System.out.println("teste3");
				if (!list.equals("blanc")) {
					for (HashMap.Entry<Lista_candidata, Integer> entry : x.listas.todas_listas.get(select_person.cargo)
							.entrySet()) {
						if (entry.getKey().nome_lista.equals(list)) {
							check2 = 1;
						}
					}
					System.out.println("teste4");
					if (check2 == 0) {
						return "The election: " + x.titulo + "does not contain the list: " + list;
					}
				}
				check2 = 0;
				for (Mesas k : x.mesas_votos) {
					if (k.name.equals(table.name)) {
						check2 = 1;
					}
				}
				System.out.println("teste4");
				if (check2 == 0) {
					return "You cannot vote for the election: " + x.titulo + "in this table";
				}
				if (x.end.before(date)) {
					return "Election already closed!";
				}
				if (list.equals("blanc")) {
					x.votos_branco++;
				} else {
					for (HashMap.Entry<Lista_candidata, Integer> entry : x.listas.todas_listas.get(select_person.cargo)
							.entrySet()) {
						if (entry.getKey().nome_lista.equals(list)) {
							entry.setValue(entry.getValue() + 1);
						}
					}
				}
				x.votos_total++;
				select_person.elections_done.add(x);
				date = new Date();
				HashMap<Mesas, Date> temp = new HashMap<Mesas, Date>();
				temp.put(table, date);
				x.auditoria.put(select_person, temp);
				String mesas = "";
				int count = 0;
				for (Mesas k : x.mesas_votos) {
					count = 0;
					for (HashMap.Entry<Pessoa, HashMap<Mesas, Date>> entry : x.auditoria.entrySet()) {
						for (HashMap.Entry<Mesas, Date> entry2 : entry.getValue().entrySet()) {
							if (k.name.equals(entry2.getKey().name)) {
								count++;
							}
						}
					}
					mesas += "Mesa:" + k.name + "\nVotes:" + count + "\n";
				}
				notifyAdmins("The person:" + select_person.nome + " with the cc number of:" + cc
						+ " has votted on the table:" + table.name);
				notifyAdmins("Election:" + x.titulo + "\n" + mesas);
				write();
				return "vote registed";
			}
		}
		if (check == 0) {
			return "There is no such election";
		}
		return null;
	}

	public synchronized void callbackNadmin() {
		for (Admin_rmi_I x : admins) {
			x.notify();
		}
	}

	public synchronized Pessoa get_person(String cc) {
		for (Pessoa x : pessoas) {
			if (x.numero_cc == Integer.parseInt(cc)) {
				return x;
			}
		}
		return null;
	}

	public synchronized boolean login(String username, String password) {
		for (Pessoa x : pessoas) {
			if (x.nome.equals(username)) {
				if (x.password.equals(password)) {
					x.loggedIN = true;
					return true;
				}
			}
		}
		return false;
	}

	public synchronized void subscribe(Server_rmi mesa) {
		servers.add(mesa);
	}

	public synchronized void subscribeAdmin(Admin_rmi_I admin) {
		admins.add(admin);
	}

	public synchronized ArrayList<Mesas> getMesasVoto() {
		return mesas_votos_todas;
	}

	public synchronized String update_person(Pessoa update_person) {
		int check = 0;
		Pessoa update_pessoa = null;
		for (Pessoa x : pessoas) {
			if (update_person.numero_cc == x.numero_cc) {
				check = 1;
				update_pessoa = x;
			}
		}
		if (check == 1) {
			pessoas.remove(update_pessoa);
			pessoas.add(update_person);
			write();
			return "Person updated\n";
		} else {
			return "Critical failure ERROR 404 person not found\n";
		}
	}

	public synchronized boolean getATerminal(int cc) {
		for (Pessoa x : pessoas) {
			if (x.numero_cc == cc) {
				return true;

			}
		}
		return false;

	}

	public synchronized String update_table(Mesas update_table) {
		int check = 0;
		Mesas update_mesas = null;
		for (Mesas x : mesas_votos_todas) {
			if (x.name.equals(update_table.name)) {
				check = 1;
				update_mesas = x;
			}
		}
		if (check == 1) {
			mesas_votos_todas.remove(update_mesas);
			mesas_votos_todas.add(update_table);
			write();
			return "Table updated\n";
		} else {
			return "Critical failure ERROR 404 table not found\n";
		}
	}

	public synchronized String update_faculty(Faculty update_faculty) {
		int check = 0;
		Faculty update_faculdade = null;
		for (Faculty x : faculdades) {
			if (x.name.equals(update_faculty.name)) {
				check = 1;
				update_faculdade = x;
			}
		}
		if (check == 1) {
			faculdades.remove(update_faculdade);
			faculdades.add(update_faculty);
			write();
			return "Faculty updated\n";
		} else {
			return "Critical failure ERROR 404 faculty not found\n";
		}
	}

	public synchronized String update_election(Eleicoes update_election) {
		int check = 0;
		Eleicoes update_eleicao = null;
		for (Eleicoes x : eleitores) {
			if (x.titulo.equals(update_election.titulo)) {
				check = 1;
				update_eleicao = x;
			}
		}
		if (check == 1) {
			eleitores.remove(update_eleicao);
			eleitores.add(update_election);
			write();
			return "Election updated\n";
		} else {
			return "Critical failure ERROR 404 election not found\n";
		}
	}

	public synchronized String removeMesaVoto(String name) {
		int check = 0;
		Mesas delete_mesa = null;
		for (Mesas x : mesas_votos_todas) {
			if (name.equals(x.name)) {
				check = 1;
				delete_mesa = x;
			}
		}
		if (check == 1) {
			mesas_votos_todas.remove(delete_mesa);
			write();
			return "Table deleted\n";
		} else {
			return "Critical failure ERROR 404 table not found\n";
		}
	}

	public synchronized String remove_faculty(String nome) {
		int check = 0;
		Faculty delete_faculty = null;
		for (Faculty x : faculdades) {
			if (nome.equals(x.name)) {
				check = 1;
				delete_faculty = x;
			}
		}
		if (check == 1) {
			faculdades.remove(delete_faculty);
			write();
			return "Faculty deleted\n";
		} else {
			return "Critical failure ERROR 404 faculty not found\n";
		}
	}

	public synchronized String removePerson(int cc) {
		int check = 0;
		Pessoa delete_pessoa = null;
		for (Pessoa x : pessoas) {
			if (cc == x.numero_cc) {
				check = 1;
				delete_pessoa = x;
			}
		}
		if (check == 1) {
			pessoas.remove(delete_pessoa);
			write();
			return "Person deleted\n";
		} else {
			return "Critical failure ERROR 404 person not found\n";
		}
	}

	public synchronized String remove_election(String titulo) {
		int check = 0;
		Eleicoes delete_eleicoes = null;
		for (Eleicoes x : eleitores) {
			if (titulo.equals(x.titulo)) {
				check = 1;
				delete_eleicoes = x;
			}
		}
		if (check == 1) {
			eleitores.remove(delete_eleicoes);
			write();
			return "Election deleted\n";
		} else {
			return "Critical failure ERROR 404 election not found\n";
		}
	}

	public synchronized String removeDepartment(String name) {
		int check = 0;
		Department delete_departement = null;
		for (Department x : departments) {
			if (name.equals(x.name)) {
				check = 1;
				delete_departement = x;
			}
		}
		if (check == 1) {
			departments.remove(delete_departement);
			write();
			return "Departement deleted\n";
		} else {
			return "Critical failure ERROR 404 department not found\n";
		}
	}

	@Override
	public synchronized String print_departments() {
		String departments_string = "";
		int i = 1;
		for (Department x : departments) {
			departments_string += i + "-" + x.name + "\n";
			i++;
		}
		return departments_string;
	}

	@Override
	public synchronized void addNewElection(Eleicoes election) {
		eleitores.add(election);
		write();
	}

	@Override
	public synchronized String print_tables() {
		String print_tables = "";
		int i = 1;
		for (Mesas x : mesas_votos_todas) {
			print_tables += i + "-" + x.toString() + "\n";
			i++;
		}
		return print_tables;
	}

	@Override
	public synchronized ArrayList<Faculty> return_facultys() {
		return faculdades;
	}

	@Override
	public synchronized ArrayList<Eleicoes> return_elections() {
		return eleitores;
	}

	@Override
	public synchronized ArrayList<Pessoa> return_pessoa() {
		return pessoas;
	}

	@Override
	public synchronized void add_pessoas(String name, String cargo, String password, Department departamento,
			Faculty chosen_faculty, int telefone, int numero_cc, Date validade_cc) {
		Pessoa nova_pessoa = new Pessoa(name, cargo, password, departamento, chosen_faculty, telefone, numero_cc,
				validade_cc);
		pessoas.add(nova_pessoa);
		write();
	}

	public synchronized String print_facultys() {
		String facultys = "";
		int i = 1;
		for (Faculty x : faculdades) {
			facultys += i + "-" + x.toString() + "\n";
			i++;
		}
		return facultys;
	}

	@Override
	public synchronized String print_elections() {
		String elections = "";
		int i = 1;
		for (Eleicoes x : eleitores) {
			elections += i + "-" + x.toString() + "\n";
			i++;
		}
		return elections;
	}

	@Override
	public synchronized ArrayList<Mesas> return_mesas() {
		return mesas_votos_todas;
	}

	@Override
	public synchronized void create_faculty(String name) {
		Faculty new_faculty = new Faculty(name);
		faculdades.add(new_faculty);
		write();
	}

	@Override
	public synchronized boolean search_faculty(String name) {
		for (Faculty x : faculdades) {
			if (x.name.equals(name)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public synchronized void add_table(String name, Department department) {
		Mesas new_table = new Mesas(name, department);
		mesas_votos_todas.add(new_table);
		write();
	}

	@Override
	public synchronized void AddDepartments(String name) {
		Department new_department = new Department(name);
		departments.add(new_department);
		write();
	}

	@Override
	public synchronized ArrayList<Department> return_departments() {
		return departments;
	}

	@Override
	public synchronized boolean search_table(String name) {
		for (Mesas x : mesas_votos_todas) {
			if (x.name.equals(name)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public synchronized boolean search_department(String name) {
		for (Department x : departments) {
			if (x.name.equals(name)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public synchronized boolean search_election(String title) {
		for (Eleicoes x : eleitores) {
			if (x.titulo.equals(title)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public synchronized String imprime_pessoas() {
		String return_people = "";
		int i = 0;
		for (Pessoa x : pessoas) {
			return_people += ++i + "-" + x.toString() + "\n";
		}
		return return_people;
	}

	@Override
	public synchronized String print_election2() {
		String print_election_resume = "";
		int i = 0;
		for (Eleicoes x : eleitores) {
			++i;
			print_election_resume += i + "-" + "Title: " + x.titulo + "\tType of election: " + x.tipo + "\n";
		}
		return print_election_resume;
	}

	@Override
	public synchronized String print_department_faculty(Faculty departs_print) {
		int i = 1;
		String department = "";
		for (Department x : departs_print.departments) {
			department += i + "-" + x.toString() + "\n";
			i++;
		}
		return department;
	}

	@Override
	public synchronized Lista cria_listas(HashMap<String, ArrayList<Lista_candidata>> listas) {
		HashMap<String, HashMap<Lista_candidata, Integer>> temporaria = new HashMap<>();
		HashMap<Lista_candidata, Integer> temporaria1 = new HashMap<>();
		HashMap<Lista_candidata, Integer> temporaria2 = new HashMap<>();
		HashMap<Lista_candidata, Integer> temporaria3 = new HashMap<>();
		for (HashMap.Entry<String, ArrayList<Lista_candidata>> entry : listas.entrySet()) {
			if (entry.getKey().equals("student")) {
				for (Lista_candidata x : entry.getValue()) {
					temporaria1.put(x, 0);
				}
				temporaria.put(entry.getKey(), temporaria1);
			} else if (entry.getKey().equals("teacher")) {
				for (Lista_candidata x : entry.getValue()) {
					temporaria2.put(x, 0);
				}
				temporaria.put(entry.getKey(), temporaria2);
			} else if (entry.getKey().equals("staff")) {
				for (Lista_candidata x : entry.getValue()) {
					temporaria3.put(x, 0);
				}
				temporaria.put(entry.getKey(), temporaria3);
			}

		}
		Lista nova_lista = new Lista(temporaria);
		return nova_lista;
	}

	
	
	// =========================================================
	public static void main(String args[]) throws RemoteException {
		load_config();
		try {
			Registry createRMIRegistry = LocateRegistry.createRegistry(rmiRegistry);
			DataServer dataserver = new DataServer();
			createRMIRegistry.rebind("rmi", dataserver);
			System.out.println("Hello Server ready.");
		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
		pings(0);
		System.out.println("\n\nData Server is ready for business!!!");
		new Thread() {
			public void run() {
				while (true) {
					Date date = new Date();
					HashMap<Integer, Double> temp1;
					HashMap<Lista_candidata, HashMap<Integer, Double>> temp2;
					HashMap<String, HashMap<Lista_candidata, HashMap<Integer, Double>>> temp3;
					HashMap<Integer, Double> nulls;
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (Eleicoes x : eleitores) {
						temp3 = new HashMap<String, HashMap<Lista_candidata, HashMap<Integer, Double>>>();
						nulls = new HashMap<Integer, Double>();
						if (x.end.before(date) && x.active == 1) {
							if (x.votos_total != 0) {
								for (HashMap.Entry<String, HashMap<Lista_candidata, Integer>> entry : x.listas.todas_listas
										.entrySet()) {
									temp2 = new HashMap<Lista_candidata, HashMap<Integer, Double>>();
									for (HashMap.Entry<Lista_candidata, Integer> entry2 : entry.getValue().entrySet()) {
										temp1 = new HashMap<Integer, Double>();
										temp1.put(entry2.getValue(),
												(((double) entry2.getValue() / x.votos_total) * 100));
										temp2.put(entry2.getKey(), temp1);
									}
									temp3.put(entry.getKey(), temp2);
								}
								nulls.put(x.votos_branco, (((double) x.votos_branco / x.votos_total) * 100));
							} else {
								for (HashMap.Entry<String, HashMap<Lista_candidata, Integer>> entry : x.listas.todas_listas
										.entrySet()) {
									temp2 = new HashMap<Lista_candidata, HashMap<Integer, Double>>();
									for (HashMap.Entry<Lista_candidata, Integer> entry2 : entry.getValue().entrySet()) {
										temp1 = new HashMap<Integer, Double>();
										temp1.put(entry2.getValue(), (double) 0);
										temp2.put(entry2.getKey(), temp1);
									}
									temp3.put(entry.getKey(), temp2);
								}
								nulls.put(x.votos_branco, (double) 0);
							}
							Results resultados = new Results(temp3, nulls);
							x.resultados = resultados;
							x.active = 0;
							for (Admin_rmi_I k : admins) {
								try {
									k.imprimeNotificacao("Election:" + x.titulo + "has just ended");
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							}
							write();
						}
					}
					for (Eleicoes x : eleitores) {
						if (x.start.before(date) && x.start_var == 0) {
							x.start_var = 1;
							x.active = 1;
							for (Admin_rmi_I k : admins) {
								try {
									k.imprimeNotificacao("Election:" + x.titulo + " has just started");
								} catch (RemoteException e) {
									e.printStackTrace();
								}
							}
							write();
						}
					}
				}
			}
		}.start();
	}

	public void loginDone(String cc, Mesas mesa) {
		Pessoa pessoa = get_person(cc);
		notifyAdmins("The person:" + pessoa.nome + " has logged in the table:" + mesa.name);
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


	public static void write() {
		try {
			FileOutputStream os = new FileOutputStream("pessoas.dat");
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(pessoas);
			oos.close();
			os = new FileOutputStream("elections.dat");
			oos = new ObjectOutputStream(os);
			oos.writeObject(eleitores);
			oos.close();
			os = new FileOutputStream("tables.dat");
			oos = new ObjectOutputStream(os);
			oos.writeObject(mesas_votos_todas);
			oos.close();
			os = new FileOutputStream("facultys.dat");
			oos = new ObjectOutputStream(os);
			oos.writeObject(faculdades);
			oos.close();
			os = new FileOutputStream("departments.dat");
			oos = new ObjectOutputStream(os);
			oos.writeObject(departments);
			oos.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Error writing to file");
			ex.printStackTrace();
		} catch (IOException ex) {
			System.out.println("Error reading the file ");
			ex.printStackTrace();
		}

	}

	public static void read() {
		try {
			FileInputStream is = new FileInputStream("pessoas.dat");
			ObjectInputStream ois = new ObjectInputStream(is);
			pessoas = (ArrayList<Pessoa>) ois.readObject();
			ois.close();
			is = new FileInputStream("elections.dat");
			ois = new ObjectInputStream(is);
			eleitores = (ArrayList<Eleicoes>) ois.readObject();
			ois.close();
			is = new FileInputStream("tables.dat");
			ois = new ObjectInputStream(is);
			mesas_votos_todas = (ArrayList<Mesas>) ois.readObject();
			ois.close();
			is = new FileInputStream("facultys.dat");
			ois = new ObjectInputStream(is);
			faculdades = (ArrayList<Faculty>) ois.readObject();
			ois.close();
			is = new FileInputStream("departments.dat");
			ois = new ObjectInputStream(is);
			departments = (ArrayList<Department>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Error reading from file");
		} catch (IOException ex) {
			System.out.println("Error acessing file");
		} catch (ClassNotFoundException ex) {
			System.out.println("Error reading file");
		}
	}
}

class UDPConnectionSend extends Thread {
	DatagramSocket aSocket = null;
	public int port;
	public int portSec;
	public String host;

	public UDPConnectionSend(String host, int port, int portSec) {
		this.host = host;
		this.port = port;
		this.portSec = portSec;
		this.start();
	}

	public void run() {
		try {
			aSocket = new DatagramSocket(port);

			String texto = "";
			while (true) {
				byte[] m = texto.getBytes();
				InetAddress aHost = InetAddress.getByName("localhost"); 
				int serverPort = portSec;
				DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
				aSocket.send(request);
				Thread.sleep(5000);
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO: " + e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (aSocket != null) {
				aSocket.close();
			}
		}
	}
}

class UDPConnectionReceive extends Thread {
	DatagramSocket aSocket = null;
	public int port;
	public int portSec;
	public String host;

	public UDPConnectionReceive(String host, int port, int portSec) {
		this.host = host;
		this.port = port;
		this.portSec = portSec;
		this.start();
	}

	public void run() {
		int timeout = 2000;
		try {
			aSocket = new DatagramSocket(port);
			System.out.println("Socket Datagram à escuta no porto " + port);
			while (true) {
				byte[] buffer = new byte[1000];
				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				aSocket.setSoTimeout(timeout);
				aSocket.receive(request);
				timeout = 30000;
			}
		} catch (SocketException e) {
			System.out.println("Socket: " + e.getMessage());
		} catch (IOException e) {
			DataServer.pings(1);
			System.out.println("IO: " + e.getMessage());
		} finally {
			if (aSocket != null) {
				aSocket.close();
			}
		}
	}
}
