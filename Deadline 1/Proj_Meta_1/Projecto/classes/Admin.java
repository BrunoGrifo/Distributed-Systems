
import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Admin extends UnicastRemoteObject implements Admin_rmi_I {

	private static final long serialVersionUID = 1L;
	public static RMI rmi = null;
	public static String rmiHost;
	public static int rmiPort;
	public static int rmiPortSec;
	public static Scanner inputI = new Scanner(System.in);
	public static Scanner inputS = new Scanner(System.in);

	public Admin() throws RemoteException {
		super();
	}

	public void changeRmi() {
		Admin administrador = null;
		try {
			administrador = new Admin();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		administrador.menu(rmiHost, rmiPort, rmiPortSec);
	}

	public static void carregaServerConfig() {
		String file = "AdminConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Server configurations...");
		try {
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for (int i = 0; i < 3; i++) {
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line, "=");
				line = tokenizer.nextToken();
				if (line.equals("RMI Host")) {
					rmiHost = tokenizer.nextToken();
				} else if (line.equals("RMI Port")) {
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				} else if (line.equals("RMI PortSec")) {
					rmiPortSec = Integer.parseInt(tokenizer.nextToken());
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
		Admin administrador;
		administrador = new Admin();
		int check = 0;
		while (check != 1) {
			try {
				rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
				check = 1;
			} catch (Exception e) {
				try {
					rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPortSec + "/rmi");
					check = 1;
				} catch (MalformedURLException | NotBoundException e1) {
					System.out.println("Servers rmi univaible");
				}

			}
		}
		rmi.subscribeAdmin((Admin_rmi_I) administrador);
		administrador.menu(rmiHost, rmiPort, rmiPortSec);
	}

	public void menu(String rmiHost, int rmiPort, int rmiPortSec) {
		while (true) {
			try {
				int check = 0;
				while (check != 1) {
					try {
						rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
						check = 1;
					} catch (MalformedURLException e) {
						try {
							rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPortSec + "/rmi");
							check = 1;
						} catch (MalformedURLException | NotBoundException e1) {
							System.out.println("Servers rmi univaible");
							e1.printStackTrace();
						}
					} catch (NotBoundException e) {
						rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPortSec + "/rmi");
						check = 1;
					} catch (Exception e) {
						rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPortSec + "/rmi");
						check = 1;
					}
				}

				Scanner inputI = new Scanner(System.in);
				String write_date, name = null;
				int option, j;
				Date write_date_format = new Date(0, 0, 0);

				int numero;
				String output;
				while (true) {
					out.print("-------------MENU------------- " + "\n1-Criar Pessoa " + "\n2-Criar Faculdade \n"
							+ "3-Criar Departamento\n" + "4-Criar Elei�ao\n" + "5-Create tables\n" + "6-Apagar Pessoa\n"
							+ "7-Apagar Faculdade\n" + "8-Apagar departamento\n" + "9-Apagar Elei�ao \n"
							+ "10-Delete tables\n" + "11-alterar dados de uma pessoa\n"
							+ "12-Alterar dados de uma faculdade\n" + "13-Alterar dados de uma elei�ao\n"
							+ "14-Alterar dados de uma mesa\n" + "15-Listar Elei�oes\n"
							+ "16-Listar elei�oes a decorrer\n" + "17-Listar elei�oes acabadas\n"
							+ "18-Listar Pessoas\n" + "19-Listar faculdade\n" + "20-Listar departamentos\n"
							+ "21-Listar mesas\n" + "22-Auditoria\n" + "23-Exit\n" + "Chose:");
					numero = testOption(1, 22);
					switch (numero) {
					case 1:
						int number_cc, phone_int;
						name = "";
						String phone = "", number_cc_string = "";
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
								out.print("0-Exit");
								option = testOption(0, rmi.return_facultys().size());
								if (option != 0) {
									chosen_faculty = rmi.return_facultys().get(option - 1);
									if (!chosen_faculty.departments.isEmpty()) {
										out.print("Department:\n");
										out.print(rmi.print_department_faculty(chosen_faculty));
										out.print("0-Exit");
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
										SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
										out.println("expiration date of cc(dd-mm-yyyy):");
										write_date = inputS.nextLine();
										write_date_format = dt.parse(write_date);
										option = 0;
										Date date = new Date();
										if (date.after(write_date_format)) {
											System.out.println("today date is after expiration date, format invalid");
											option = 1;
										}
									} catch (ParseException e) {
										out.println("Error in date format!\n");
									}
								}
								rmi.add_pessoas(name, cargo, password, departamento, chosen_faculty, phone_int,
										number_cc, write_date_format);
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
								out.print("Name already in use chose a new one(type cancel to exit):");
								do {
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
						do {
							title = inputS.nextLine();
							while (rmi.search_election(title) == false) {
								out.print("Name already in use chose a new one:");
								do {
									title = inputS.nextLine();
								} while (!checkStringWithNumbers(title));
							}
						} while (!checkStringWithNumbers(title));
						out.print("type of election:\n1-geral\n2-nucleo\n0-Exit");
						option = testOption(0, 3);
						if (option != 0) {
							SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
							Date data_inicial = new Date(0, 0, 0, 0, 0, 0);
							Date data_final = new Date(0, 0, 0, 0, 0, 0);
							ArrayList<Mesas> election_tables = new ArrayList<Mesas>();
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
							while (option != 0) {
								try {
									out.println("Start date(hh:mm:ss dd-mm-yyyy):");
									write_date = inputS.nextLine();
									data_inicial = dt.parse(write_date);
									Date date = new Date();
									if (date.after(data_inicial)) {
										System.out.println("Starting date is before today date invalide");
										option = 1;
									} else {
										out.println("End date(hh:mm:ss dd-mm-yyyy):");
										write_date = inputS.nextLine();
										data_final = dt.parse(write_date);
										if (data_final.after(data_inicial)) {
											option = 0;
										} else {
											out.print("End date can not be set to before ou equal to start date.\n");
										}
									}
								} catch (ParseException e) {
									out.println("Erro no formato da data!\n");
								}
							}
							lists.put("student", create_lists_election("students"));
							if (type.equals("geral")) {
								lists.put("teacher", create_lists_election("teachers"));
								lists.put("staff", create_lists_election("staff"));
							}
							check = 0;
							int i = 1;
							option = 1;
							while (option != 0 && i != 0) {
								for (j = 0; j < 2; j++) {
									i = 0;
									if (j == 0) {
										out.print("Table avaible do be added to the election:\n");
									}
									for (Mesas k : rmi.return_mesas()) {
										check = 0;
										for (Mesas x : election_tables) {
											if (x.name.equals(k.name)) {
												check = 1;
											}
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
												out.print("table added to election\n");
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
									rmi.cria_listas(lists), election_tables, 0, 0);
							rmi.addNewElection(new_election);
							out.print("Election added\n");
						}
						break;
					case 5:
						do {
							out.print("Name of the table:\n");
							name = inputS.nextLine();
							while (rmi.search_table(name) == false) {
								out.print("Name of the table already exists\n");
								name = inputS.nextLine();
							}
						} while (!checkStringWithNumbers(name));
						int i;
						check = 0;
						option = 0;
						for (j = 0; j < 2; j++) {
							i = 0;
							if (j == 0) {
								out.print("Departments avaible to be added:\n");
							}
							for (Department x : rmi.return_departments()) { // mudar isto para nao ser tao recursivo?
								check = 0;
								for (Mesas k : rmi.return_mesas()) {
									if (k.departamento != null) {
										if (k.departamento.name.equals(x.name)) {
											check = 1;
										}
									}
								}
								if (check == 0 && j == 0) {
									++i;
									out.print(i + "-" + x.name + "\n");
								}
								if (check == 0 && j == 1) {
									++i;
									if (i == option) {
										rmi.add_table(name, x);
										out.print("Table added\n");
									}
								}
							}
							if (j == 0 && i != 0) {
								out.print("0-Exit");
								option = testOption(0, i);
							}
							if (i == 0) {
								j++;
								out.print(
										"No departments avaible to be atributed to table:\n1-Add table without department\n0-Exit");
								option = testOption(0, 1);
								if (option == 1) {
									rmi.add_table(name, null);
									out.print("Table was created without department\n");
								}
							}
						}
						break;
					case 6:
						ArrayList<Pessoa> list_people = rmi.return_pessoa();
						if (list_people.isEmpty()) {
							out.print("There is no people to delete\n");
						} else {
							option = 1;
							while (option != 0 && !list_people.isEmpty()) {
								list_people = rmi.return_pessoa();
								System.out.println(rmi.imprime_pessoas());
								out.print("0-Exit");
								option = testOption(0, list_people.size());
								if (option != 0) {
									output = rmi.removePerson(list_people.get(option - 1).numero_cc);
									out.print(output);
								}
								list_people = rmi.return_pessoa();
							}
						}
						break;
					case 7:
						Faculty remove_fac = null;
						ArrayList<Faculty> facultys = rmi.return_facultys();
						j = 1;
						option = 1;
						if (facultys.isEmpty()) {
							out.print("There is no facultys to delete\n");
						} else {
							while (option != 0 && j != 0) {
								facultys = rmi.return_facultys();
								for (i = 0; i < 2; i++) {
									j = 0;
									if (i == 0) {
										out.print("Facultys avaible to de be deleted:\n");
									}
									for (Faculty x : facultys) {
										check = 0;
										for (Pessoa k : rmi.return_pessoa()) {
											if (k.faculdade != null) {
												if (k.faculdade.name.equals(x.name)) {
													check = 1;
												}
											}
										}
										if (!x.departments.isEmpty()) {
											check = 1;
										}
										if (check == 0 && i == 0) {
											j++;
											out.print(j + "-" + x.toString() + "\n");
										}
										if (check == 0 && i == 1) {
											j++;
											if (j == option) {
												remove_fac = x;
											}
										}
									}
									if (i == 0 && j != 0) {
										out.print("0-Exit");
										option = testOption(0, j);
									}
								}
								if (j == 0) {
									out.print("No more facultys avaible to be removed!\n");
								}
								if (option != 0 && j != 0) {
									output = rmi.remove_faculty(remove_fac.name);
									out.print(output);
								}
							}
						}
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
								deps = rmi.return_departments();
								for (i = 0; i < 2; i++) {
									j = 0;
									if (i == 0) {
										out.print("Departments avaible to de be deleted:\n");
									}
									for (Department x : deps) {
										check = 0;
										for (Faculty k : rmi.return_facultys()) {
											for (Department l : k.departments) {
												if (l.name.equals(x.name)) {
													check = 1;
												}
											}
										}
										for (Mesas d : rmi.return_mesas()) {
											if (d.departamento.name.equals(x.name)) {
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
									output = rmi.removeDepartment(remove_dep.name);
									out.print(output);
								}
							}
						}
						break;
					case 9:
						option = 1;
						ArrayList<Eleicoes> eleicoes = rmi.return_elections();
						if (eleicoes.isEmpty()) {
							out.print("Theres is no elections avaible\n");
						} else {
							while (option != 0 && eleicoes.size() != 0) {
								i = 0;
								eleicoes = rmi.return_elections();
								Date date = new Date();
								for (Eleicoes x : eleicoes) {
									if (x.start.after(date)) {
										out.println(++i + "-" + "Title: " + x.titulo + "\tType of election: " + x.tipo
												+ "\n");
									}
								}
								if (i != 0) {
									out.print("0-Exit");
									option = testOption(0, i);
									if (option != 0) {
										Eleicoes election = null;
										i = 0;
										for (Eleicoes x : eleicoes) {
											if (x.start.after(date)) {
												++i;
												if (i == option) {
													election = x;
												}
											}
										}
										output = rmi.remove_election(election.titulo);
										out.print(output);
									}
									eleicoes = rmi.return_elections();
								} else {
									option = 0;
									System.out.println("No elections avaible");
								}
							}
						}
						break;
					case 10:
						Mesas delete_table = null;
						check = 0;
						j = 1;
						option = 1;
						ArrayList<Mesas> mesas = rmi.return_mesas();
						if (mesas.isEmpty()) {
							out.print("There is no tables to delete\n");
						} else {
							while (option != 0 && j != 0) {
								mesas = rmi.return_mesas();
								for (i = 0; i < 2; i++) {
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
									output = rmi.removeMesaVoto(delete_table.name);
									out.print(output);
								}
							}
						}
						break;
					case 11:
						list_people = rmi.return_pessoa();
						if (list_people.isEmpty()) {
							out.print("Theres is no persons avaible\n");
						} else {
							out.print(rmi.imprime_pessoas());
							out.print("0-Exit");
							option = testOption(0, list_people.size());
							if (option != 0) {
								Pessoa select_person = list_people.get(option - 1);
								while (option != 0) {
									out.print("select the field you want to change:\n" + "1-Change name\n"
											+ "2-Change job\n" + "3-Change faculty\n" + "4-Change department\n"
											+ "5-Change password\n" + "6-Change phone number\n"
											+ "7-Change expiration date of cc\n" + "0-Exit\n" + "Chose:");
									option = testOption(0, 7);
									switch (option) {
									case 0:
										option = 0;
										break;
									case 1:
										out.print("Currente name: " + select_person.nome + "\nNew name:\n");
										do {
											name = inputS.nextLine();
										} while (!testString(name));
										select_person.nome = name;
										output = rmi.update_person(select_person);
										out.print(output);
										if (!output.equals("Person updated\n")) {
											option = 0;
										}
										break;
									case 2:
										out.print("Current position: " + select_person.cargo
												+ "\nNew job:\n1-student\n2-teacher\n3-staff\n0-Exit");
										option = testOption(0, 3);
										if (option != 0) {
											if (option == 1) {
												select_person.cargo = "student";
											} else if (option == 2) {
												select_person.cargo = "teacher";
											} else if (option == 3) {
												select_person.cargo = "staff";
											}
											output = rmi.update_person(select_person);
											out.print(output);
											if (!output.equals("Person updated\n")) {
												option = 0;
											}
										}
										break;
									case 3:
										if (rmi.return_facultys().isEmpty()) {
											out.print("Theres is no facultys avaible\n");
										} else {
											if (select_person.faculdade != null) {
												out.print("Current faculty: " + select_person.faculdade.toString()
														+ "\nNew faculty(it will also afect the department):\n");
											} else {
												out.print("\nNew faculty(it will also afect the department):\n");
											}
											out.print(rmi.print_facultys());
											out.print("0-Exit");
											option = testOption(0, rmi.return_facultys().size());
											if (option == 0) {
												Faculty new_faculty = rmi.return_facultys().get(option - 1);
												out.print(rmi.print_department_faculty(new_faculty));
												out.print("0-Exit");
												option = testOption(0, new_faculty.departments.size());
												if (option == 0) {
													select_person.faculdade = new_faculty;
													select_person.departamento = new_faculty.departments
															.get(option - 1);
													output = rmi.update_person(select_person);
													out.print(output);
													if (!output.equals("Person updated\n")) {
														option = 0;
													}
												}
											}
										}
										break;
									case 4:
										if (select_person.faculdade != null) {
											if (select_person.departamento != null) {
												out.print("Current department: " + select_person.departamento.toString()
														+ "\nDepartments avaible:\n");
											} else {
												out.print("\nDepartments avaible:\n");
											}
											if (!select_person.faculdade.departments.isEmpty()) {
												out.print(rmi.print_department_faculty(select_person.faculdade));
												out.print("0-Exit");
												option = testOption(0, select_person.faculdade.departments.size());
												if (option != 0) {
													select_person.departamento = select_person.faculdade.departments
															.get(option - 1);
													output = rmi.update_person(select_person);
													out.print(output);
													if (!output.equals("Person updated\n")) {
														option = 0;
													}
												}
											} else {
												out.print("No departments avaible\n");
											}
										} else {
											out.print(
													"Impossible action beacuase the person does not have a faculty\n");
										}
										break;
									case 5:
										out.print("Current password: " + select_person.password + "\nNew password:\n");
										select_person.password = inputS.nextLine();
										output = rmi.update_person(select_person);
										out.print(output);
										if (!output.equals("Person updated\n")) {
											option = 0;
										}
										break;
									case 6:
										phone = "";
										do {
											out.print("Currente phone number: +" + select_person.telefone
													+ "\nNew phone number with 9 digits:");
											try {
												phone = inputS.nextLine();
											} catch (InputMismatchException e) {
												out.println("invalid option");
												inputI.next();
											}
										} while (!checkNumbers(phone) || phone.length() != 9);
										phone_int = Integer.parseInt(phone);
										select_person.telefone = phone_int;
										output = rmi.update_person(select_person);
										out.print(output);
										if (!output.equals("Person updated\n")) {
											option = 0;
										}
										break;
									case 7:
										while (option != 0) {
											try {
												SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
												out.println("Current expiration date: " + select_person.validade_cc
														+ "\nNew expiration date of cc(dd-mm-yyyy):");
												write_date = inputS.nextLine();
												write_date_format = dt.parse(write_date);
												option = 0;
											} catch (ParseException e) {
												out.println("Error in date format!\n");
											}
										}
										select_person.validade_cc = write_date_format;
										output = rmi.update_person(select_person);
										out.print(output);
										if (!output.equals("Person updated\n")) {
											option = 0;
										}
										break;
									default:
										out.print("\noption not avaible\n");
									}
								}
							}
						}
						break;
					case 12:
						ArrayList<Faculty> list_facultys = rmi.return_facultys();
						if (list_facultys.isEmpty()) {
							out.print("Theres is no facultys avaible\n");
						} else {
							out.print("Chose faculty:\n");
							System.out.println(rmi.print_facultys());
							out.print("0-Exit");
							option = testOption(0, list_facultys.size());
							Faculty chosen_faculty = list_facultys.get(option - 1);
							out.print(
									"What do you want to change:\n1-Add departments to faculty\n2-Remove departments from faculty\n0-Exit");
							option = testOption(0, 2);
							if (option == 1) {
								assing_departments_to_facultys(chosen_faculty);
								out.print(rmi.update_faculty(chosen_faculty));
							} else if (option == 2) {
								remove_departments_from_facultys(chosen_faculty);
								out.print(rmi.update_faculty(chosen_faculty));
							}
						}
						break;
					case 13:
						int option2;
						i = 0;
						String write;
						ArrayList<Eleicoes> list_elections = rmi.return_elections();
						if (list_elections.isEmpty()) {
							out.print("Theres is no elections avaible\n");
						} else {
							out.print("Elections:\n");
							Date date = new Date();
							for (Eleicoes x : list_elections) {
								if (x.start.after(date)) {
									out.println(
											++i + "-" + "Title: " + x.titulo + "\tType of election: " + x.tipo + "\n");
								}
							}
							if (i != 0) {
								out.print("0-Exit\n");
								out.print("Chose:");
								option = testOption(0, i);
								if (option != 0) {
									Eleicoes election = null;
									i = 0;
									for (Eleicoes x : list_elections) {
										if (x.start.after(date)) {
											++i;
											if (i == option) {
												election = x;
											}
										}
									}
									while (option != 0) {
										System.out.println(election.tipo);
										out.print("Select the field you want to change:\n" + "1-Change type\n"
												+ "2-Change starting date\n" + "3-Change ending date\n"
												+ "4-Change resume\n" + "5-Change lists\n" + "6-Change voting tables\n"
												+ "0-Exit");
										option = testOption(0, 6);
										switch (option) {
										case 0:
											option = 0;
											break;
										case 1:
											out.print("Current type: " + election.tipo
													+ "\nChange to:\n1-nucleo\n2-geral\n0-Exit\n");
											option2 = testOption(0, 2);
											if (option2 == 1) {
												election.tipo = "nucleo";
											} else if (option2 == 2) {
												election.tipo = "geral";
											}
											out.print(rmi.update_election(election));
											break;
										case 2:
											option2 = 1;
											while (option2 != 0) {
												try {
													SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
													out.print("Current starting date: " + election.start
															+ "\nNew starting date(hh:mm:ss dd-mm-yyyy):\n");
													write = inputS.nextLine();
													write_date_format = dt.parse(write);
													if (election.end.after(write_date_format)) {
														option2 = 0;
													} else {
														out.print(
																"End date can not be set to before ou equal to start date.");
													}
												} catch (ParseException e) {
													out.println("Erro no formato da data!\n");
												}
											}
											election.start = write_date_format;
											out.print(rmi.update_election(election));
											break;
										case 3:
											option2 = 1;
											while (option2 != 0) {
												try {
													SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
													out.print("Current ending date: " + election.end
															+ "\nNew ending date(hh:mm:ss dd-mm-yyyy):");
													write = inputS.nextLine();
													write_date_format = dt.parse(write);
													if (write_date_format.after(election.start)) {
														option2 = 0;
													} else {
														out.print(
																"End date can not be set to before ou equal to start date.\n");
													}
												} catch (ParseException e) {
													out.println("Erro no formato da data!\n");
												}
											}
											election.end = write_date_format;
											out.print(rmi.update_election(election));
											break;
										case 4:
											out.print(
													"Current resume of election: " + election.resumo + "\nNew resume:");
											election.resumo = inputS.nextLine();
											out.print(rmi.update_election(election));
											break;
										case 5:
											out.print("1-Add list\n2-Remove list\n0-Exit");
											option2 = testOption(0, 2);
											if (option2 == 1) {
												add_lists_election("students", election.listas.todas_listas);
												if (election.tipo.equals("geral")) {
													add_lists_election("teachers", election.listas.todas_listas);
													add_lists_election("staff", election.listas.todas_listas);
												}
											} else if (option2 == 2) {
												for (HashMap.Entry<String, HashMap<Lista_candidata, Integer>> entry : election.listas.todas_listas
														.entrySet()) {
													if (entry.getKey().equals("students")) {
														delete_lists("Students", entry.getValue());
													}
													if (election.tipo.equals("geral")) {
														if (entry.getKey().equals("teachers")) {
															delete_lists("Teachers", entry.getValue());
														} else if (entry.getKey().equals("staff")) {
															delete_lists("Staff", entry.getValue());
														}
													}
												}
											}
											out.print(rmi.update_election(election));
											break;
										case 6:
											check = 0;
											j = 0;
											out.print("1-Remove table\n2-Add table\n0-Exit");
											option2 = testOption(0, 2);
											if (option2 == 1) {
												if (election.mesas_votos.isEmpty()) {
													out.print("No tables avaible to be removed\n");
												} else {
													j = 1;
													option2 = 1;
													while (option2 != 0 && j != 0) {
														j = 0;
														for (Mesas k : election.mesas_votos) {
															++j;
															out.print(j + "-" + k.name + "\n");
														}
														if (j == 0) {
															option2 = 0;
															out.print("No more tables avaible to be removed\n");
														} else {
															out.print("0-Exit");
															option2 = testOption(0, j);
															if (option2 != 0) {
																election.mesas_votos
																		.remove(election.mesas_votos.get(option2 - 1));
																out.print("Table added");
																output = rmi.update_election(election);
																out.print(output);
																if (!output.equals("Election updated\n")) {
																	option2 = 0;
																}
															}
														}
													}
												}
											} else if (option2 == 2) {
												if (rmi.return_mesas().isEmpty()) {
													out.print("No tables avaible to be added\n");
												} else {
													i = 1;
													option2 = 1;
													while (option2 != 0 && i != 0) {
														for (j = 0; j < 2; j++) {
															i = 0;
															if (j == 0) {
																out.print(
																		"Table avaible do be added to the election:\n");
															}
															for (Mesas k : rmi.return_mesas()) {
																check = 0;
																for (Mesas x : election.mesas_votos) {
																	if (x.name.equals(k.name)) {
																		check = 1;
																	}
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
																	if (i == option2) {
																		election.mesas_votos.add(k);
																		out.print("Table added to election\n");
																		output = rmi.update_election(election);
																		out.print(output);
																		if (!output.equals("Election updated\n")) {
																			option2 = 0;
																		}
																		out.print("table added to election\n");
																	}
																}
															}
															if (j == 0 && i != 0) {
																out.print("0-Exit");
																option2 = testOption(0, i);
															}
														}
														if (i == 0) {
															out.print("No tables avaible to be added\n");
														}
													}
												}
											}
											break;
										default:
											out.print("\noption not avaible\n");
										}
									}
								}
							} else {
								out.print("No elections avaiblel\n");
							}
						}
						break;
					case 14:
						ArrayList<Mesas> list_tables = rmi.return_mesas();
						if (list_tables.isEmpty()) {
							out.print("Theres is no tables avaible\n");
						} else {
							out.print("Chose table:\n");
							out.print(rmi.print_tables());
							out.print("0-Exit");
							option = testOption(0, list_tables.size());
							if (option != 0) {
								Mesas table_change = list_tables.get(option - 1);
								out.print("Change:\n1-Change department where is located\n0-Exit");
								option = testOption(0, 1);
								if (option != 0) {
									if (option == 1) {
										check = 0;
										option = 0;
										for (j = 0; j < 2; j++) {
											i = 0;
											if (j == 0) {
												out.print("Departments avaible to be added:\n");
											}
											for (Department x : rmi.return_departments()) {
												check = 0;
												for (Mesas k : rmi.return_mesas()) {
													if (k.departamento != null) {
														if (k.departamento.name.equals(x.name)) {
															check = 1;
														}
													}
												}
												if (check == 0 && j == 0) {
													++i;
													out.print(i + "-" + x.name + "\n");
												}
												if (check == 0 && j == 1) {
													++i;
													if (i == option) {
														table_change.departamento = x;
														output = rmi.update_table(table_change);
														out.print(output);
													}
												}
											}
											if (j == 0 && i != 0) {
												out.print("0-Exit");
												option = testOption(0, i);
											}
											if (i == 0) {
												j++;
												out.print(
														"No more departments avaible to be atributed to table:\n1-update table without department\n0-Exit");
												option = testOption(0, 1);
												if (option == 1) {
													table_change.departamento = null;
													output = rmi.update_table(table_change);
													out.print(output);
												}
											}
										}
									}
								}
							}
						}
						break;
					case 15:
						if (rmi.return_elections().isEmpty()) {
							out.println("Lista vazia");
						} else {
							System.out.println(rmi.print_elections());
						}
						break;
					case 16:
						if (rmi.return_elections().isEmpty()) {
							out.println("Lista vazia");
						} else {
							Date date = new Date();
							i = 0;
							for (Eleicoes x : rmi.return_elections()) {
								if (x.start.before(date) && x.end.after(date)) {
									out.print(++i + "-" + x.toString() + "\n");
								}
							}
							if (i == 0) {
								out.print("No elections on at the moment");
							}
						}
						break;
					case 17:
						if (rmi.return_elections().isEmpty()) {
							out.println("Lista vazia");
						} else {
							Date date = new Date();
							i = 0;
							for (Eleicoes x : rmi.return_elections()) {
								if (x.end.before(date)) {
									out.println(++i + "-" + x.toStringResults());
								}
							}
							if (i == 0) {
								out.print("No elections done at the moment");
							}
						}
						break;
					case 18:
						if (rmi.return_pessoa().isEmpty()) {
							out.print("Theres is no persons avaible\n");
						} else {
							out.print(rmi.imprime_pessoas());
						}
						break;
					case 19:
						if (rmi.return_facultys().isEmpty()) {
							out.print("Theres is no facultys avaible\n");
						} else {
							out.print(rmi.print_facultys());
						}
						break;
					case 20:
						if (rmi.return_departments().isEmpty()) {
							out.print("Theres is no departments avaible\n");
						} else {
							out.print(rmi.print_departments());
						}
						break;
					case 21:
						if (rmi.return_mesas().isEmpty()) {
							out.print("Theres is no tables avaible\n");
						} else {
							out.print(rmi.print_tables());
						}
						break;
					case 22:
						i = 0;
						Date date = new Date();
						for (Eleicoes x : rmi.return_elections()) {
							if (x.end.before(date)) {
								out.print(++i + "-" + x.titulo + "\n");
							}
						}
						if (i != 0) {
							out.print("0-Exit");
							option = testOption(0, i);
							if (option != 0) {
								i = 0;
								for (Eleicoes x : rmi.return_elections()) {
									if (x.end.before(date)) {
										++i;
										if (i == option) {
											out.print(rmi.auditoria(x));
										}
									}
								}
							}
						} else {
							out.println("No elections avaible");
						}
						break;
					case 23:
						System.exit(0);
					default:
						out.print("\nOp�ao invalida\n");
					}
				}

			} catch (Exception e) {
				out.println("erro");
			}
		}
	}

	public void imprimeNotificacao(String notification) {
		System.out.println("\n" + notification);
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

	public static void add_lists_election(String type,
			HashMap<String, HashMap<Lista_candidata, Integer>> todas_listas) {
		String name_list = "";
		for (HashMap.Entry<String, HashMap<Lista_candidata, Integer>> entry : todas_listas.entrySet()) {
			if (entry.getKey().equals(type)) {
				while (!name_list.equals("done")) {
					out.print("Name of the list to add for " + type + " do vote(type done when finished):\n");
					do {
						name_list = inputS.nextLine();
					} while (!checkStringWithNumbers(name_list));
					if (!name_list.equals("done")) {
						Lista_candidata new_list = new Lista_candidata(name_list);
						entry.getValue().put(new_list, 0);
						out.print("List added\n");
					}
				}
			}
		}
	}

	public static void delete_lists(String type, HashMap<Lista_candidata, Integer> list_lists) {
		String delete = "";
		Lista_candidata delete_list = null;
		int i = 1;
		while (!delete.equals("done") && i != 0) {
			i = 0;
			out.print(type + " lists avaible to be deleted:\n");
			for (int j = 0; j < 2; j++) {
				i = 0;
				for (HashMap.Entry<Lista_candidata, Integer> entry2 : list_lists.entrySet()) {
					if (j == 0) {
						out.print(++i + "-" + entry2.getKey().toString() + "\n");
					}
					if (j == 1) {
						++i;
						if (delete.equals(entry2.getKey().nome_lista)) {
							delete_list = entry2.getKey();
						}
					}
				}
				if (j == 0 && i != 0) {
					out.print("Name of the list you want to delete(type done when finished):");
					delete = inputS.nextLine();
					if (checkListName(delete, list_lists) && !delete.equals("done")) {
						out.print("Theres no list with " + delete + " as a name\n");
						++j;
					}
				}
				if (j == 1 && i != 0) {
					if (!delete.equals("done")) {
						list_lists.remove(delete_list);
					} else {
						j++;
					}
				}
				if (i == 0) {
					out.print("No more list avaible to be deleted from " + type + "\n");
					j++;
				}
			}
		}
	}

	public static boolean checkListName(String name, HashMap<Lista_candidata, Integer> list_lists) {
		int check = 0;
		for (HashMap.Entry<Lista_candidata, Integer> entry : list_lists.entrySet()) {
			if (entry.getKey().nome_lista.equals(name)) {
				check = 1;
			}
		}
		if (check == 1) {
			return false;
		} else {
			return true;
		}
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

	public static void assing_departments_to_facultys(Faculty faculty_chosen) {
		int i = 1, check = 0, option = 0;
		for (int j = 0; j < 2; j++) {
			i = 0;
			if (j == 0) {
				out.print("Departments avaible to be added:\n");
			}
			try {
				for (Department k : rmi.return_departments()) {
					check = 0;
					for (Faculty x : rmi.return_facultys()) {
						for (Department l : x.departments) {
							if (l.name.equals(k.name)) {
								check = 1;
							}
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
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			if (i == 0) {
				out.print("No more departments avaible to be added\n");
				j++;
			}
			if (j == 0 && i != 0) {
				out.print("0-Exit");
				option = testOption(0, i);
			}
		}
	}

	public static void remove_departments_from_facultys(Faculty faculty_chosen) {
		int option = 1, i, check = 0;
		Department delete_department = null;
		while (option != 0) {
			i = 0;
			out.print("Departments avaible from this faculty to be removed:\n");
			for (int j = 0; j < 2; j++) {
				i = 0;
				for (Department x : faculty_chosen.departments) {
					check = 0;
					try {
						for (Pessoa k : rmi.return_pessoa()) {
							if (k.departamento != null) {
								if (k.departamento.name.equals(x.name)) {
									check = 1;
								}
							}
						}
					} catch (RemoteException e) {
						e.printStackTrace();
					}
					if (check == 0 && j == 0) {
						++i;
						out.print(i + "-" + x.toString() + "\n");
					}
					if (check == 0 && j == 1) {
						++i;
						if (i == option) {
							delete_department = x;
						}
					}
				}
				if (i == 0) {
					out.print("No more departments avaible to be added\n");
					option = 0;
					j++;
				}
				if (j == 0 && i != 0) {
					out.print("0-Exit");
					option = testOption(0, i);
				}
			}
			if (option != 0) {
				faculty_chosen.departments.remove(delete_department);
			}
		}
	}

}
