package server;

import java.io.*;
import java.lang.reflect.Array;

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
	
	public synchronized Eleicoes procura_eleicao(String titulo_eleicao) {
        for (Eleicoes x : eleitores) {
            if (x.titulo.equals(titulo_eleicao)) {
                return x;
            }
        }
        return null;
    }
	public synchronized Lista cria_listas(HashMap<String, ArrayList<Lista_candidata>> listas) { //hashmap DAR ERRO AQUI
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
	public synchronized ArrayList<Mesas> getMesas_votos_todas(){
		return mesas_votos_todas;
	}
	public synchronized void addNewElection(Eleicoes new_e){
		eleitores.add(new_e);
	}
	public synchronized void removePerson(Pessoa pessoa) {
		pessoas.remove(pessoa);
	}
	public synchronized ArrayList<Department> return_departments() {
        return departments;
    }
	public synchronized void removeDepartment(Department dep) {
		departments.remove(dep);
	}
	public synchronized void removeMesaVoto(Mesas mesa) {
		mesas_votos_todas.remove(mesa);
	}
	public synchronized void assing_departments_to_facultys(Faculty faculty_chosen) {
        int i = 1, check = 0, option = 0;
        for (int j = 0; j < 2; j++) {
            i = 0;
            if (j == 0) {
                out.print("Departments avaible to be added:\n");
            }
            for (Department k : departments) {
                check = 0;
                for (Faculty x : faculdades) {
                    if (x.departments.contains(k)) {
                        check = 1;
                    }
                }
                if (check == 0 && j == 0) {
                    ++i;
                    out.print(i + "-" + k.name + "\n");
                }
                if (check == 0 && j == 1) {
                    ++i;
                    if (i == option) {
                        faculty_chosen.departments.add(k);
                    }
                }

            }
            if (j == 0) {
                option = inputI.nextInt();
            }
        }
    }
	public synchronized void remove_departments_from_facultys(Faculty faculty_chosen) {
        int opcao = 1, i, check = 0;
        Department delete_department = null;
        while (opcao != 0) {
            i = 0;
            out.print("Departments avaible from this faculty to be removed:\n");
            for (int j = 0; j < 2; j++) {
                i = 0;
                for (Department x : faculty_chosen.departments) {
                    check = 0;
                    for (Pessoa k : pessoas) {
                        if (k.departamento.equals(x)) {
                            check = 1;
                        }
                    }
                    if (check == 0 && j == 0) {
                        ++i;
                        out.print(i + "-" + x.toString() + "\n");
                    }
                    if (check == 0 && j == 1) {
                        ++i;
                        if (i == opcao) {
                            delete_department = x;
                        }
                    }

                }
                out.print("0-Exit\n");
                opcao = inputI.nextInt();
            }
            faculty_chosen.departments.remove(delete_department);

        }
    }
	public synchronized String print_tables(){
        String print_tables="";
        int i = 1;
        for (Mesas x : mesas_votos_todas) {
            print_tables += i + "-" + x.toString() + "\n";
            i++;
        }
        return print_tables;
    }
	public synchronized String print_elections() {
        String elections = "";
        int i = 1;
        for (Faculty x : faculdades) {
            elections += i + "-" + x.toString() + "\n";
            i++;
        }
        return elections;
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
