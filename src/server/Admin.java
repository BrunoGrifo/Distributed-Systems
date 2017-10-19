//package hello2;
package server;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
				int option, number_cc, phone_int;
				String name = "", phone = "", number_cc_string = "", write_date;
				SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
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
				break;
			case 5:
				rmi.sayHello("Admin", "Funciona");
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
			case 9:
				break;
			case 10:
				break;
			case 11:
				break;
			case 12:
				break;
			case 13:
				break;
			case 14:
				break;
			case 15:
				break;
			case 16:
				break;
			case 17:
				out.print(rmi.imprime_pessoas());
				break;
			case 18:
				// rmi.print_facultys();
				break;
			case 19:
				// rmi.print_departments();
				break;
			case 20:
				System.exit(0);
			default:
				out.print("\nOpçao invalida");
			}
		}

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
