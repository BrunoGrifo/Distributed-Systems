package server;

import java.io.*;
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

public class DataServer extends UnicastRemoteObject implements RMI {
	public static int rmiRegistry;
	public Scanner inputI = new Scanner(System.in);
	public Scanner inputS = new Scanner(System.in);

	public ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
	public ArrayList<Eleicoes> eleitores;
	public ArrayList<Mesas> mesas_votos_todas;
	public HashMap<Eleicoes, HashMap<Pessoa, Mesas>> regista_pessoas;
	public HashMap<Eleicoes, HashMap<Lista, Results>> resultados;
	public ArrayList<Faculty> faculdades = new ArrayList<Faculty>();
	public ArrayList<Department> departments = new ArrayList<Department>();
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataServer() throws RemoteException {
		super();
	}

	public static void load_config() {
		String file = "DataServerConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding DataServer configurations...");
		try {
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for (int i = 0; i < 1; i++) {
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line, "=");
				line = tokenizer.nextToken();
				if (line.equals("RMI Registry")) {
					rmiRegistry = Integer.parseInt(tokenizer.nextToken());
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

	public synchronized ArrayList<Faculty> return_facultys() {
		return faculdades;
	}

	public synchronized ArrayList<Pessoa> return_pessoa() {
		return pessoas;
	}

	public synchronized void add_pessoas(String name, String cargo, String password, Department departamento,
			Faculty chosen_faculty, int telefone, int numero_cc, Date validade_cc) {
		Pessoa nova_pessoa = new Pessoa(name, cargo, password, departamento, chosen_faculty, telefone, numero_cc,
				validade_cc);
		pessoas.add(nova_pessoa);
	}

	public synchronized String print_department_faculty(Faculty departs_print) {
		int i = 1;
		String department = "";
		for (Department x : departs_print.departments) {
			department += i + "-" + x.toString() + "\n";
			i++;
		}
		return department;
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

	public synchronized String imprime_pessoas() {
		String return_people = "";
		int i = 0;
		for (Pessoa x : pessoas) {
			return_people = ++i + "-" + x.toString() + "\n";
		}
		return return_people;
	}

	public synchronized boolean search_department(String name) {
		for (Department x : departments) {
			if (x.name.equals(name)) {
				return false;
			}
		}
		return true;
	}

	public synchronized boolean search_faculty(String name) {
		for (Faculty x : faculdades) {
			if (x.name.equals(name)) {
				return false;
			}
		}
		return true;
	}

	public synchronized void AddDepartments(String name) {
		Department new_department = new Department(name);
		departments.add(new_department);
	}
	public synchronized void create_faculty(String name) {
        Faculty new_faculty = new Faculty(name);
        faculdades.add(new_faculty);
    }

	@Override
	public synchronized String sayHello(String username, String password) throws RemoteException {
		System.out.println(username + "\n" + password);

		return "Hello, World!";
	}

	// =========================================================
	public static void main(String args[]) {
		load_config();
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

}
