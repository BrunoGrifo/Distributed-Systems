package server;


import java.io.*;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
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

public class DataServerB extends UnicastRemoteObject implements RMI {
	public static int rmiRegistry;
	public static int rmiPort;
	public static int rmiPortSec;
	public static int rmiRegistryS;
	public static String rmiHost;
	public static String rmiHostSec;
	public Scanner inputI = new Scanner(System.in);
	public Scanner inputS = new Scanner(System.in);

	public ArrayList<Pessoa> pessoas = new ArrayList<Pessoa>();
	public ArrayList<Eleicoes> eleitores = new ArrayList<Eleicoes>();
	public ArrayList<Mesas> mesas_votos_todas = new ArrayList<Mesas>();
	public HashMap<Eleicoes, HashMap<Pessoa, Mesas>> regista_pessoas;
	public HashMap<Eleicoes, HashMap<Lista, Results>> resultados;
	public ArrayList<Faculty> faculdades = new ArrayList<Faculty>();
	public ArrayList<Department> departments = new ArrayList<Department>();
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public DataServerB() throws RemoteException {
		super();
		new UDPConnection(rmiHost,rmiPort,rmiPortSec);
	}
	
	public synchronized void sayHello() {
		out.println("Server");
	}

	public static void load_config() {
		String file = "DataServerBConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding DataServerB configurations...");
		try {
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for (int i = 0; i < 5; i++) {
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line, "=");
				line = tokenizer.nextToken();
				if (line.equals("RMI Registry")) {
					rmiRegistry = Integer.parseInt(tokenizer.nextToken());
				}else if (line.equals("RMI UDP Port connection")) {
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				}else if (line.equals("RMI UDP Host connection")) {
					rmiHost = tokenizer.nextToken();
				}else if (line.equals("RMI UDP sec Port connection")) {
					rmiPortSec = Integer.parseInt(tokenizer.nextToken());
				}else if (line.equals("RMI UDP sec Host connection")) {
					rmiHostSec = tokenizer.nextToken();
				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File " + file + " not found");
			System.exit(0);
		} catch (IOException e) {
		}
		System.out.println("DataServerBConfig.txt successfully uploaded.");
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
            return "Person updated\n";
        } else {
            return "Critical failure ERROR 404 person not found\n";
        }
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
            return "Election updated\n";
        } else {
            return "Critical failure ERROR 404 election not found\n";
        }
    }
 
    /*public String update_department(Department update_department){
     int check = 0;
     Department update_departamento = null;
     for (Department x : departments) {
     if (x.name.equals(update_department.name)) {
     check = 1;
     update_departamento = x;
     }
     }
     if (check == 1) {
     departments.remove(update_departamento);
     departments.add(update_department);
     return "Faculty updated\n";
     } else {
     return "Critical failure ERROR 404 faculty not found\n";
     }
     }*/
   
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
    public synchronized void add_pessoas(String name, String cargo, String password, Department departamento, Faculty chosen_faculty, int telefone, int numero_cc, Date validade_cc) {
        Pessoa nova_pessoa = new Pessoa(name, cargo, password, departamento, chosen_faculty, telefone, numero_cc, validade_cc);
        pessoas.add(nova_pessoa);
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
    }
 
    @Override
    public synchronized void AddDepartments(String name) {
        Department new_department = new Department(name);
        departments.add(new_department);
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
    public synchronized boolean getATerminal(int cc) {
		for(Pessoa x: pessoas) {
			if(x.numero_cc==cc) {
				return true;
			}
		}
		return false;
		
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
            if (entry.getKey().equals("students")) {
                for (Lista_candidata x : entry.getValue()) {
                    temporaria1.put(x, 0);
                }
                temporaria.put(entry.getKey(), temporaria1);
            } else if (entry.getKey().equals("teachers")) {
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
	public static void main(String args[]) {
		load_config();
		try {
			//Registry createRMIRegistry = LocateRegistry.createRegistry(rmiRegistry);
			DataServerB DataServerB = new DataServerB();
			//createRMIRegistry.rebind("rmi", DataServerB);
			System.out.println("Hello Server ready: "+rmiRegistry);
			Naming.rebind("rmi://" + "localhost" + ":" + rmiRegistry + "/rmi", DataServerB);
			out.println(rmiRegistry);
			
		} catch (RemoteException re) {
			try {
				Registry createRMIRegistry = LocateRegistry.createRegistry(rmiRegistry);
				DataServerB DataServerB = new DataServerB();
				createRMIRegistry.rebind("rmi", DataServerB);
				System.out.println("Hello Server ready1: "+rmiRegistry);
				
			}catch (RemoteException res) {
				out.println("Error trying to create rmi registry");
			}			
		}
		catch (MalformedURLException e) {
			System.out.println("MalformedURLException in HelloImpl.main: " + e);
		}
		System.out.println("\n\nData Server is ready for business!!!");
	}

}

