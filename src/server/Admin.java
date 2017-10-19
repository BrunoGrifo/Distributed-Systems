//package hello2;
package server;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Admin {
	public static RMI rmi = null;
	public static String rmiHost;
	public static int rmiPort;
	public static Scanner inputI = new Scanner(System.in);
	public static Scanner inputS = new Scanner(System.in);

	public static void carregaServerConfig() {
		String file = "AdminConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Server configurations...");
		try {
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for (int i = 0; i < 2; i++) {
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line, "=");
				line = tokenizer.nextToken();
				if (line.equals("RMI Host")) {
					rmiHost = tokenizer.nextToken();
				} else if (line.equals("RMI Port")) {
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File " + file + " not found");
			System.exit(0);
		} catch (IOException e) {
		} catch (NullPointerException e) {
			System.out.println("Erro a carregar os dados do ficheiro");
		}

	}

	public static void main(String args[]) throws RemoteException {
		carregaServerConfig();
		System.out.println(rmiHost);
		System.out.println(rmiPort);

		try {
			rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
		} catch (IOException e) {
			System.out.println("IO:" + e);
		} catch (NotBoundException e) {
			System.out.println("Error no lookup method");
			System.exit(0);
		}

		Scanner inputI = new Scanner(System.in);
		SimpleDateFormat dt;
		String write_date, name = null;
		int option,check,j;

		int numero;
		while (true) {
			out.print("-------------MENU------------- " + "\n1-Criar Pessoa " + "\n2-Criar Faculdade \n"
					+ "3-Criar Departamento\n" + "4-Criar Eleiçao\n" + "5-Atribuir departamentos a uma faculdade\n"
					+ "6-Apagar Pessoa\n" + "7-Apagar Faculdade\n" + "8-Apagar departamento\n" + "9-Apagar Eleiçao \n"
					+ "10-alterar dados de uma pessoa\n" + "11-Alterar dados de uma faculdade\n"
					+ "12-Alterar dados de um departamento\n" + "13-Alterar dados de uma eleiçao\n"
					+ "14-Listar Eleiçoes\n" + "15-Listar eleiçoes a decorrer\n" + "16-Listar eleiçoes acabadas\n"
					+ "17-Listar Pessoas\n" + "18-Listar faculdade\n" + "19-Listar departamentos\n" + "20-Exit\n"
					+ "Escolha:");
			numero = inputI.nextInt();

			switch (numero) {
			case 1:
				int number_cc, phone_int;
				String phone = "", number_cc_string = "";
				dt = new SimpleDateFormat("dd-MM-yyyy");
				Date expiration_date = new Date(0, 0, 0);
				do {
					out.print("Name:");
					name = inputS.nextLine();
				} while (!testString(name));
				out.print("Cargo:\n1-student\n2-teacher\n3-staff\n0-Exit");
				option = testOption(0, 3);
				if (option != 0) {
					Department departamento = null;
					Faculty chosen_faculty = null;
					String cargo = "";
					if (option == 1) {
						cargo = "student";
					} else if (option == 2) {
						cargo = "teacher";
					} else if (option == 3) {
						cargo = "staff";
					}
					out.print("Password:");
					String password = inputS.nextLine();
					if (!rmi.return_facultys().isEmpty()) {
						out.print("Chose faculty:\n");
						out.print(rmi.print_facultys());
						out.print("0-Exit\n");
						option = testOption(0, rmi.return_facultys().size());
						if (option != 0) {
							chosen_faculty = rmi.return_facultys().get(option - 1);
							if (!chosen_faculty.departments.isEmpty()) {
								out.print("Department:\n");
								rmi.print_department_faculty(chosen_faculty);
								out.print("0-Exit\n");
								option = testOption(0, chosen_faculty.departments.size());
								if (option != 0) {
									departamento = chosen_faculty.departments.get(option - 1);
								}
							} else {
								out.print("No departments avaible at this faculty\n");
							}
						}
					} else {
						out.print("No facultys avaible\n");
					}
					if (option != 0) {
						out.print("telefone:");
						do {
							out.print("phone number with 9 digits:");
							try {
								phone = inputS.nextLine();
							} catch (InputMismatchException e) {
								out.println("invalid option");
								inputI.next();
							}
						} while (!checkNumbers(phone) || phone.length() != 9);
						phone_int = Integer.parseInt(phone);
						do {
							do {
								out.print("Number of cc(8 digits):");
								try {
									number_cc_string = inputS.nextLine();
								} catch (InputMismatchException e) {
									out.println("Invalid option");
									inputI.next();
								}
							} while (!checkNumbers(number_cc_string) || number_cc_string.length() != 8);
							number_cc = Integer.parseInt(number_cc_string);
						} while (!checkCCnumber(number_cc, rmi.return_pessoa()));
						while (option != 0) {
							try {
								out.println("expiration date of cc(dd-mm-yyyy):");
								write_date = inputS.nextLine();
								expiration_date = dt.parse(write_date);
								option = 0;
							} catch (ParseException e) {
								out.println("Erro no formato da data!\n");
							}
						}
						rmi.add_pessoas(name, cargo, password, departamento, chosen_faculty, phone_int, number_cc,
								expiration_date);
						out.print("Person added to users\n");
					}
				}

				break;
			case 2:
				out.print("Name of new faculty(type cancel to exit):");
				do {
					name = inputS.nextLine();
				} while (!checkStringWithNumbers(name));
				if (!name.equals("cancel")) {
					while (rmi.search_faculty(name) == false) {
						do {
							out.print("Name already in use chose a new one(type cancel to exit):");
							name = inputS.nextLine();
						} while (!checkStringWithNumbers(name));
					}
					if (!name.equals("cancel")) {
						rmi.create_faculty(name);
						out.print("Faculty added\n");
					}
				}
				break;
			case 3:
				out.print("Name of new department(type cancel to exit):");
				do {
					name = inputS.nextLine();
				} while (!checkStringWithNumbers(name));
				if (!name.equals("cancel")) {
					while (rmi.search_department(name) == false) {
						out.print("Name already in use chose a new one(type cancel to exit):");
						do {
							name = inputS.nextLine();
						} while (!checkStringWithNumbers(name));
					}
					if (!name.equals("cancel")) {
						rmi.AddDepartments(name);
						out.print("Department added\n");
					}
				}
				break;
			case 4:
				String title;
				out.print("Name of election:");
				title = inputS.nextLine();
				out.print("type of election:\n1-geral\n2-nucleo\n0-Exit");
				option = testOption(0, 3);
				if (option != 0) {
					dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
					Date data_inicial = new Date(0, 0, 0, 0, 0, 0);
					Date data_final = new Date(0, 0, 0, 0, 0, 0);
					ArrayList<Mesas> election_tables = new ArrayList<>();
					HashMap<String, ArrayList<Lista_candidata>> lists = new HashMap<>();
					String type = "";
					String resume;
					if (option == 1) {
						type = "geral";
					} else if (option == 2) {
						type = "nucleo";
					}
					out.print("short resume of the election:");
					resume = inputS.nextLine();
					if (rmi.procura_eleicao(title) == (null)) {
						while (option != 0) {
							try {
								out.println("Start date(hh:mm:ss dd-mm-yyyy):");
								write_date = inputS.nextLine();
								data_inicial = dt.parse(write_date);
								out.println("End date(hh:mm:ss dd-mm-yyyy):");
								write_date = inputS.nextLine();
								data_final = dt.parse(write_date);
								if (data_final.after(data_inicial)) {
									option = 0;
								} else {
									out.print("End date can not be set to before ou equal to start date.\n");
								}
							} catch (ParseException e) {
								out.println("Erro no formato da data!\n");
							}
						}

					} else {
						out.print("theres already one election with the same name\n");
					}
					// if type.equals(nucleo) ??????
					lists.put("students", create_lists_election("students"));
					if (type.equals("geral")) {
						lists.put("teachers", create_lists_election("teachers"));
						lists.put("staff", create_lists_election("staff"));
					}
					int i = 1;
					option = 1;
					while (option != 0 && i != 0) {
						for (j = 0; j < 2; j++) {
							i = 0;
							if (j == 0) {
								out.print("Table avaible do be added to the election:\n");
							}
							for (Mesas k : rmi.getMesas_votos_todas()) {
								check = 0;
								if (election_tables.contains(k)) {
									check = 1;
								}
								if (k.departamento == null) {
									check = 1;
								}
								if (check == 0 && j == 0) {
									++i;
									out.print(i + "-" + k.name + "\n");
								}
								if (check == 0 && j == 1) {
									++i;
									if (i == option) {
										election_tables.add(k);
									}
								}

							}
							if (j == 0 && i != 0) {
								out.print("0-Exit");
								option = testOption(0, i);
							}
						}
						if (i == 0) {
							out.print("No tables avaible to be added\n");
						}
					}
					Eleicoes new_election = new Eleicoes(type, data_inicial, data_final, title, resume,
							cria_listas(lists), election_tables, 0, 0);
					rmi.addNewElection(new_election);
					out.print("Election added\n");
				}
				break;
			case 5:
				break;
			case 6:
				ArrayList<Pessoa> pessoas = rmi.return_pessoa();
				if (pessoas.isEmpty()) {
					out.print("There is no people to delete\n");
				} else {
					option = 1;
					while (option != 0 && !pessoas.isEmpty()) {
						System.out.println(rmi.imprime_pessoas());
						out.print("0-Exit");
						option = testOption(0, pessoas.size());
						if (option != 0) {
							rmi.removePerson(pessoas.get(option - 1));
							out.print("Person deleted\n");
						}
					}
				}
				break;
			case 7:
				break;
			case 8:
				Department remove_dep = null;
				check = 0;
				j = 1;
				option = 1;
				ArrayList<Department> deps = rmi.return_departments();
				if (deps.isEmpty()) {
					out.print("There is no departments to delete\n");
				} else {
					while (option != 0 && j != 0) {
						for (int i = 0; i < 2; i++) {
							j = 0;
							if (i == 0) {
								out.print("Departments avaible to de be deleted:\n");
							}
							for (Department x : deps) {
								check = 0;
								for (Faculty k : rmi.return_facultys()) {
									if (k.departments.contains(x)) {
										check = 1;
									}
								}
								for (Mesas d : rmi.getMesas_votos_todas()) {
									if (d.departamento.equals(x)) {
										check = 1;
									}
								}
								if (check == 0 && i == 0) {
									j++;
									out.print(j + "-" + x.toString() + "\n");
								}
								if (check == 0 && i == 1) {
									j++;
									if (j == option) {
										remove_dep = x;
									}
								}
							}

							if (i == 0 && j != 0) {
								out.print("0-Exit");
								option = testOption(0, j);
							}
						}
						if (j == 0) {
							out.print("No more departments avaible to be removed!\n");
						}
						if (option != 0 && j != 0) {
							rmi.removeDepartment(remove_dep);
							out.print("Department removed \n");
						}

					}
				}
				break;
			case 9:
				break;
			case 10:
				Mesas delete_table = null;
				check = 0;
				j = 1;
				option=1;
				ArrayList<Mesas> mesas = rmi.getMesas_votos_todas();
				if (mesas.isEmpty()) {
					out.print("There is no tables to delete\n");
				} else {
					while (option != 0 && j != 0) {
						for (int i = 0; i < 2; i++) {
							j = 0;
							if (i == 0) {
								out.print("Table avaible to de be deleted:\n");
							}
							for (Mesas x : mesas) {
								check = 0;
								if (x.departamento != null) {
									check = 1;
								}

								if (check == 0 && i == 0) {
									j++;
									out.print(j + "-" + x.toString() + "\n");
								}
								if (check == 0 && i == 1) {
									j++;
									if (j == option) {
										delete_table = x;
									}
								}
							}

							if (i == 0 && j != 0) {
								out.print("0-Exit");
								option = testOption(0, j);
							}
						}
						if (j == 0) {
							out.print("No more tables avaible to be removed!\n");
						}
						if (option != 0 && j != 0) {
							rmi.removeMesaVoto(delete_table);
							out.print("Table removed \n");
						}

					}
				}
				break;
			case 11:
				break;
			case 12:
				int opcao;
		        out.print("Chose faculty:\n");
		        System.out.println(rmi.print_facultys());
		        Faculty chosen_faculty = rmi.return_facultys().get(inputI.nextInt() - 1);
		        out.print("What do you want to change:\n1-Name of faculty\n2-Add departments to faculty\n3-Remove departments from faculty\n0-Exit\nChose:");
		        opcao = inputI.nextInt();
		        if (opcao == 1) {
		            out.print("New name:");
		            chosen_faculty.name = inputS.nextLine();
		            out.print("Name updated\n");
		        } else if (opcao == 2) {
		            rmi.assing_departments_to_facultys(chosen_faculty);
		        } else if (opcao == 3) {
		            rmi.remove_departments_from_facultys(chosen_faculty);
		        }
				break;
			case 13:
				break;
			case 14:
				
				break;
			case 15:
				break;
			case 16:
				String elections=rmi.print_elections();
				if(elections.equals("")) {
					out.println("Lista vazia");
				}else {
					System.out.println(elections);
				}
				break;
			case 17:
				break;
			case 18:
				break;
			case 19:
				break;
			case 20:
				break;
			case 21:
				break;
			case 22:
				System.out.println(rmi.print_tables());
				break;
			case 23:
				System.exit(0);
			default:
				out.print("\nOpçao invalida");
			}
		}

	}

	public static Lista cria_listas(HashMap<String, ArrayList<Lista_candidata>> listas) { // hashmap DAR ERRO AQUI
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

	public static ArrayList<Lista_candidata> create_lists_election(String type) {
		ArrayList<Lista_candidata> lists_running = new ArrayList<>();
		String name_list = "";
		while (!name_list.equals("done")) {
			out.print("Name of the list to add for " + type + " do vote(type done when finished):\n");
			name_list = inputS.nextLine();
			if (!name_list.equals("done")) {
				Lista_candidata new_list = new Lista_candidata(name_list);
				lists_running.add(new_list);
				out.print("List added\n");
			}
		}
		return lists_running;
	}

	public static boolean checkStringWithNumbers(String p) {
		int i = 0;
		char[] array = p.toCharArray();
		while (p.length() != i) {
			if ((int) array[0] == 32 || (int) array[array.length - 1] == 32
					|| ((int) array[i] > 90 && (int) array[i] < 97)
					|| ((((int) array[i] > 122 || (int) array[i] < 65) && ((int) array[i] > 57 || (int) array[i] < 48))
							&& (int) array[i] != 32)) {
				out.println(
						"Nao pode comecar com um espaco nem acabar com um espaco e s� pode conter letras e numeros!");
				return false;
			}
			i++;
		}
		if (p.length() == 0) {
			out.println("Nao pode comecar com um espaco nem acabar com um espaco e s� pode conter letras e numeros!");
			return false;
		}
		return true;
	}

	public static int testOption(int minimo, int maximo) {
		int opcao = 0, test;
		String numero;
		while (true) {
			test = 0;
			try {
				while (test != 1) {
					try {
						out.print("\n[" + minimo + "-" + maximo + "]Chose:");
						numero = inputS.nextLine();
						opcao = Integer.parseInt(numero);
						test = 1;
					} catch (Exception e) {
						out.print("Escolha invalida!");
					}
				}
				if (opcao < minimo || opcao > maximo) {
					out.print("Escolha invalida!");
				} else {
					return opcao;
				}
			} catch (InputMismatchException exception) {
				out.print("Escolha invalida!");
				inputI.next();
			}
		}
	}

	public static boolean testString(String p) {
		int i = 0;
		char[] array = p.toCharArray();
		while (p.length() != i) {
			if ((int) array[0] == 32 || ((int) array[i] > 90 && (int) array[i] < 97)
					|| (int) array[array.length - 1] == 32
					|| (((int) array[i] > 122 || (int) array[i] < 65) && (int) array[i] != 32)) {
				out.println("Only letters are aceptable and canot start or end with a space");
				return false;
			}
			i++;
		}
		if (p.length() == 0) {
			out.println("Only letters are aceptable and canot start or end with a space");
			return false;
		}
		return true;
	}

	public static boolean checkNumbers(String num) {
		int i = 0;
		char[] array = num.toCharArray();
		while (num.length() != i) {
			if ((int) array[i] > 57 || (int) array[i] < 48) {
				out.println("Can not start or end with a space and can only ccontain numbers!");
				return false;
			}
			i++;
		}
		if (num.length() == 0) {
			out.println("Can not start or end with a space and can only ccontain numbers!");
			return false;
		}
		return true;
	}

	public static boolean checkCCnumber(int number_cc, ArrayList<Pessoa> list_person) {
		for (Pessoa x : list_person) {
			if (x.numero_cc == number_cc) {
				out.println("Number of cc already exist!");
				return false;
			}
		}
		return true;
	}

}
