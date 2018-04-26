package server;

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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//mudar os ciclos todos que estao mal que estao bue inificentes
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
							+ "3-Criar Departamento\n" + "4-Criar Eleiçao\n" + "5-Create tables\n" + "6-Apagar Pessoa\n"
							+ "7-Apagar Faculdade\n" + "8-Apagar departamento\n" + "9-Apagar Eleiçao \n"
							+ "10-Apagar mesa\n" + "11-alterar dados de uma pessoa\n"
							+ "12-Alterar dados de uma faculdade\n" + "13-Alterar dados de uma eleiçao\n"
							+ "14-Voto Antecipado\n" + "15-Listar Eleiçoes\n" + "16-Listar eleiçoes a decorrer\n"
							+ "17-Listar eleiçoes acabadas\n" + "18-Listar Pessoas\n" + "19-Listar faculdade\n"
							+ "20-Listar departamentos\n" + "21-Listar mesas\n" + "22-Auditoria\n" + "23-Exit\n"
							+ "(Any delete function will only be ables to delete data that isnt on store or beeing used\n):");
					numero = testOption(1, 23);
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
							String departamento = null;
							String chosen_faculty = null;
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
							ArrayList<String> list_facul = rmi.list_facultys();
							if (!list_facul.isEmpty()) {
								out.print("Chose faculty:\n");
								out.print(print_strings(list_facul));
								out.print("0-Exit");
								option = testOption(0, list_facul.size());
								if (option != 0) {
									chosen_faculty = list_facul.get(option - 1);
									ArrayList<String> dep_fac = rmi.dep_fac(chosen_faculty);
									if (!dep_fac.isEmpty()) {
										out.print("Department:\n");
										out.print(print_strings(dep_fac));
										out.print("0-Exit");
										option = testOption(0, dep_fac.size());
										if (option != 0) {
											departamento = dep_fac.get(option - 1);
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
								out.println("Morada:");
								String morada = inputS.nextLine();
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
								} while (!checkCCnumber(number_cc, rmi.get_pessoas()));
								rmi.add_pessoa(number_cc, name, password, phone_int, write_date_format, morada, cargo);
								if (!rmi.list_facultys().contains(chosen_faculty)) {
									out.println("Faculty not found! Faculty and Department set to none");
								} else {
									rmi.insert_faculty(number_cc, chosen_faculty);
									if (!rmi.dep_fac(chosen_faculty).contains(departamento)) {
										out.println("Department not found! Department set to none");
									} else {
										rmi.insert_department(number_cc, departamento);
									}
								}
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
						out.print("type of election:\n1-geral\n2-faculty\n3-Department\n4-nucleo\n0-Exit");
						option = testOption(0, 4);
						if (option != 0) {
							SimpleDateFormat dt = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
							Date data_inicial = new Date(0, 0, 0, 0, 0, 0);
							Date data_final = new Date(0, 0, 0, 0, 0, 0);
							HashMap<String, ArrayList<String>> lists = new HashMap<>();
							String type = "";
							String resume, faculdade = "", departamento = "";
							if (option == 1) {
								type = "geral";
							} else if (option == 2) {
								type = "faculty";
								ArrayList<String> list_facul = rmi.list_facultys();
								if (!list_facul.isEmpty()) {
									out.print("Chose faculty:\n");
									out.print(print_strings(list_facul));
									out.print("0-Exit");
									option = testOption(0, list_facul.size());
									if (option != 0) {
										faculdade = list_facul.get(option - 1);
									}
								} else {
									out.println("Option invalid because theres no facultys");
									option = 0;
								}
							} else if (option == 3) {
								type = "department";
								ArrayList<String> list_depart = rmi.departments_for_elections();
								if (!list_depart.isEmpty()) {
									out.print("Chose Department:\n");
									out.print(print_strings(list_depart));
									out.print("0-Exit");
									option = testOption(0, list_depart.size());
									if (option != 0) {
										departamento = list_depart.get(option - 1);
									}
								} else {
									out.println("Option invalid because theres no departments avaible to be added");
									option = 0;
								}
							} else if (option == 4) {
								type = "nucleo";
								ArrayList<String> list_depart = rmi.departments_for_elections();
								if (!list_depart.isEmpty()) {
									out.print("Chose Department:\n");
									out.print(print_strings(list_depart));
									out.print("0-Exit");
									option = testOption(0, list_depart.size());
									if (option != 0) {
										departamento = list_depart.get(option - 1);
									}
								} else {
									out.println("Option invalid because theres no departments avaible to be added");
									option = 0;
								}
							}
							if (option != 0) {
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
												out.print(
														"End date can not be set to before ou equal to start date.\n");
											}
										}
									} catch (ParseException e) {
										out.println("Erro no formato da data!\n");
									}
								}
								if (type.equals("faculty") || type.equals("department")) {
									lists.put("teacher", create_lists_election("teachers"));
								} else {
									lists.put("student", create_lists_election("students"));
									if (type.equals("geral")) {
										lists.put("teacher", create_lists_election("teachers"));
										lists.put("staff", create_lists_election("staff"));
									}
								}
								check = 0;
								int i = 1;
								option = 1;
								ArrayList<String> mesas_escolhidas = new ArrayList<String>();
								ArrayList<String> avaible_mesas = new ArrayList<String>();
								while (option != 0 && i != 0) {
									avaible_mesas = rmi.avaible_tables();
									for (String k : mesas_escolhidas) {
										if (avaible_mesas.contains(k)) {
											avaible_mesas.remove(k);
										}
									}
									for (j = 0; j < 2; j++) {
										i = 0;
										if (j == 0) {
											out.print("Table avaible do be added to the election:\n");
										}
										for (String k : avaible_mesas) {
											if (j == 0) {
												out.println(++i + "-" + k);
											}
											if (j == 1) {
												++i;
												if (i == option) {
													mesas_escolhidas.add(k);
												}
											}
										}
										if (j == 0 && i != 0) {
											out.print("0-Exit");
											option = testOption(0, i);
										}
									}
									if (option == 0) {
										j++;
									}
									if (i == 0) {
										out.print("No tables avaible to be added\n");
									}
								}
								String title;
								do {
									out.print("Name of election:");
									title = inputS.nextLine();
									while (search_election(title) == false) {
										out.print("Name already in use chose a new one:");
										do {
											title = inputS.nextLine();
										} while (!checkStringWithNumbers(title));
									}
								} while (!checkStringWithNumbers(title));
								int id = 1;
								ArrayList<Eleicoes> eleicoes = rmi.return_eleicoes();
								boolean cicle = true;
								if (!eleicoes.isEmpty()) {
									while (cicle == true) {
										for (Eleicoes x : eleicoes) {
											if (id == x.id || id < x.id) {
												id++;
											} else {
												cicle = false;
											}
										}
									}
								}
								option = 1;
								if (type.equals("department") || type.equals("nucleo")) {
									ArrayList<String> list_depart = rmi.departments_for_elections();
									if (!list_depart.contains(departamento)) {
										option = 0;
										out.println(
												"ERROR deleted must have been deleted or faculty removed and impossible to set. Election not created");
									}
								} else if (type.equals("faculty")) {
									ArrayList<String> list_facul = rmi.list_facultys();
									if (!list_facul.contains(faculdade)) {
										out.println(
												"ERROR faculty must have been deleted and impossible to set. Election not created");
										option = 0;
									}
								}
								if (option != 0) {
									Date date = new Date();
									if (date.after(data_inicial)) {
										System.out.println(
												"Error criating election on date, starting date before actual date");
									} else {
										rmi.add_eleicao(id, data_inicial, data_final, title, resume, type);
										out.print("Election added\n");
										if (type.equals("department") || type.equals("nucleo")) {
											rmi.insert_eleicao_dep(id, departamento);
										} else if (type.equals("faculty")) {
											rmi.insert_fac_eleicao(id, faculdade);
										}
										int id_lista = 1;
										ArrayList<Integer> id_listas = rmi.id_listas();
										for (HashMap.Entry<String, ArrayList<String>> entry : lists.entrySet()) {
											for (String x : entry.getValue()) {
												id_lista = 1;
												cicle = true;
												id_listas = rmi.id_listas();
												if (!id_listas.isEmpty()) {
													while (cicle == true) {
														for (Integer k : id_listas) {
															if (id_lista == k || id_lista < k) {
																id_lista++;
															} else {
																cicle = false;
															}
														}
													}
												}
												rmi.insert_listas(id, x, entry.getKey(), id_lista);
											}
										}
										out.println("lists added");
										ArrayList<Lista_candidata> lists_election = rmi.get_lists_election(id);
										ArrayList<Integer> ids_lists = new ArrayList<Integer>();
										for (Lista_candidata x : lists_election) {
											ids_lists.add(x.id);
										}
										avaible_mesas = rmi.avaible_tables();
										ArrayList<String> temp_add = new ArrayList<String>();
										for (String x : mesas_escolhidas) {
											if (avaible_mesas.contains(x)) {
												temp_add.add(x);
											} else {
												out.println(
														"Table " + x + "  was not longer avaible and was not added");
											}
										}
										for (String x : temp_add) {
											rmi.insert_mesa_election(x, id);
											for (Integer k : ids_lists) {
												rmi.insert_mesa_election_list_votes(x, k);
											}
										}
										out.println("tables added");
									}
								}
							}
						}
						break;
					case 5:
						do {
							out.print("Name of the table:\n");
							name = inputS.nextLine();
							while (search_table(name) == false) {
								out.print("Name of the table already exists\nChose another name:");
								name = inputS.nextLine();
							}
						} while (!checkStringWithNumbers(name));
						rmi.add_mesa(name);
						out.print("Table was created\n");
						break;
					case 6:
						int i = 0;
						Pessoa pessoa;
						ArrayList<Pessoa> pessoas = rmi.get_pessoas();
						out.println("Witch voter do you wish to delete?");
						for (Pessoa x : pessoas) {
							i++;
							out.println(i + "- " + x);
						}
						out.print("0-Exit");
						option = testOption(0, i);
						if (option != 0) {
							pessoa = pessoas.get(option - 1);
							if (rmi.searchVoterForDelete(pessoa.numero_cc)) {
								if (rmi.deleteVoter(pessoa.numero_cc))
									out.println(pessoa.nome + " deleted!!!");
							} else {
								out.println(pessoa.nome + " can't be deleted!!!");
								continue;
							}
						} else {
							continue;
						}
						break;

					case 7: // TODO case 7
						i = 0;
						String faculty;
						ArrayList<String> faculties = rmi.list_facultys();
						out.println("Witch faculty do you wish to delete?");
						for (String x : faculties) {
							i++;
							out.println(i + "- " + x);
						}
						out.print("0-Exit");
						option = testOption(0, i);
						if (option != 0) {
							faculty = faculties.get(option - 1);
							if (rmi.searchFacultyForDelete(faculty)) {
								if (rmi.deleteFaculty(faculty))
									out.println(faculty + " deleted!!!");
							} else {
								out.println(faculty + " can't be deleted!!!");
								continue;
							}
						} else {
							continue;
						}
						break;
					case 8: // TODO case 8
						i = 0;
						String dep;
						ArrayList<String> deps = rmi.listDepartments();
						out.println("Witch department do you wish to delete?");
						for (String x : deps) {
							i++;
							out.println(i + "- " + x);
						}
						out.print("0-Exit");
						option = testOption(0, i);
						if (option != 0) {
							dep = deps.get(option - 1);
							if (rmi.searchDepartmentForDelete(dep)) {
								if (rmi.deleteDepartment(dep))
									out.println(dep + " deleted!!!");
							} else {
								out.println(dep + " can't be deleted!!!");
								continue;
							}
						} else {
							continue;
						}
						break;
					case 9: // TODO Case 9
						i = 0;
						Eleicoes eleicao;
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						ArrayList<Eleicoes> list = rmi.listElectionToDelete(timestamp);
						out.println("Witch election do you wish to delete?");
						for (Eleicoes x : list) {
							i++;
							out.println(i + "- " + x);
						}
						out.print("0-Exit");
						option = testOption(0, i);
						if (option != 0) {
							eleicao = list.get(option - 1);
							if (rmi.deleteElection(eleicao.id)) {
								out.println(eleicao.id + ": " + eleicao.titulo + "-> deleted!!!");
							} else {
								out.println(eleicao.id + ": " + eleicao.titulo + "-> can't be deleted!!!");
								continue;
							}
						} else {
							continue;
						}
						break;
					case 10: // TODO case 10
						i = 0;
						String nomeMesa;
						ArrayList<String> mesas = rmi.listTables();
						out.println("Witch table do you wish to delete?");
						for (String x : mesas) {
							i++;
							out.println(i + "- " + x);
						}
						out.print("0-Exit");
						option = testOption(0, i);
						if (option != 0) {
							nomeMesa = mesas.get(option - 1);
							if (rmi.searchMesaVBNA(nomeMesa)) {
								if (rmi.deleteTable(nomeMesa))
									out.println(nomeMesa + " deleted!!!");
							} else {
								out.println(nomeMesa + " can't be deleted!!!");
								continue;
							}
						} else {
							continue;
						}
						break;
					case 11:
						ArrayList<Pessoa> list_people = rmi.get_pessoas();
						if (list_people.isEmpty()) {
							out.print("Theres is no persons avaible\n");
						} else {
							out.print(print_pessoas(list_people));
							out.print("0-Exit");
							option = testOption(0, list_people.size());
							if (option != 0) {
								Pessoa select_person = list_people.get(option - 1);
								while (option != 0) {
									out.print("select the field you want to change:\n" + "1-Change morada\n"
											+ "2-Change job\n" + "3-Change faculty\n" + "4-Change department\n"
											+ "5-Change password\n" + "6-Change phone number\n"
											+ "7-Change expiration date of cc\n" + "0-Exit\n" + "Chose:");
									option = testOption(0, 7);
									switch (option) {
									case 0:
										option = 0;
										break;
									case 1:
										String morada;
										out.print("Currente adress: " + select_person.morada + "\nNew adress:\n");
										morada = inputS.nextLine();
										select_person.morada = morada;
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
										if (rmi.list_facultys().isEmpty()) {
											out.print("Theres is no facultys avaible\n");
										} else {
											String fac_pessoa = rmi.fac_pessoa(select_person.numero_cc);
											ArrayList<String> faculdades = rmi.list_facultys();
											out.print("Current faculty: " + fac_pessoa
													+ "\nNew faculty(it will also afect the department):\n");

											out.print(print_strings(faculdades));
											out.print("0-Exit");
											option = testOption(0, faculdades.size());
											if (option != 0) {
												String new_faculty = faculdades.get(option - 1);
												ArrayList<String> dep_fac = rmi.dep_fac(new_faculty);
												if (!dep_fac.isEmpty()) {
													String dep_pessoa = rmi.dep_pessoa(select_person.numero_cc);
													out.print("Current Department: " + dep_pessoa
															+ "\nNew Department:\n");
													out.print(print_strings(dep_fac));
													out.print("0-Exit");
													option = testOption(0, dep_fac.size());
													if (option != 0) {
														String departamento = dep_fac.get(option - 1);
														ArrayList<String> current_facultys = rmi.list_facultys();
														output = rmi.update_person(select_person);
														out.print(output);
														if (!output.equals("Person updated\n")) {
															option = 0;
														} else {
															if (!current_facultys.contains(new_faculty)) {
																if (!fac_pessoa.equals("None")) {
																	if (!current_facultys.contains(fac_pessoa)) {
																		out.println(
																				"New faculty not found!Tried reset to old faculty but also not found! Faculty and Department set to none");
																	} else {
																		if (!dep_pessoa.equals("None")) {
																			if (!rmi.dep_fac(fac_pessoa)
																					.contains(dep_pessoa)) {
																				out.println(
																						"New faculty not found!Reseted to old faculty but old department not found! department set to none");
																			} else {
																				out.println(
																						"New faculty not found. Reseted to old faculty and old department");
																			}
																		} else {
																			out.println(
																					"New faculty not found! Reseted to old faculty and department");
																		}
																	}
																} else {
																	out.println(
																			"New faculty not found! Reseted to previus state!");
																}
															} else {
																if (fac_pessoa.equals("None")) {
																	rmi.insert_faculty(select_person.numero_cc,
																			new_faculty);
																} else {
																	rmi.update_faculty(new_faculty,
																			select_person.numero_cc);
																}
																if (!rmi.dep_fac(new_faculty).contains(departamento)) {
																	out.println(
																			"Department not found!Faculty updated but Department set to none");
																	rmi.del_dep_person(select_person.numero_cc);
																} else {
																	if (dep_pessoa.equals("None")) {
																		rmi.insert_department(select_person.numero_cc,
																				departamento);
																	} else {
																		rmi.update_department(departamento,
																				select_person.numero_cc);
																	}
																}
															}
														}
													}
												} else {
													output = rmi.update_person(select_person);
													out.print(output);
													if (!output.equals("Person updated\n")) {
														option = 0;
													} else {
														out.println(
																"This faculty has no departments. Faculty updated but department set to None");
														if (fac_pessoa.equals("None")) {
															rmi.insert_faculty(select_person.numero_cc, new_faculty);
														} else {
															rmi.update_faculty(new_faculty, select_person.numero_cc);
														}
														rmi.del_dep_person(select_person.numero_cc);
													}
												}
											}
										}
										break;

									case 4:
										String fac_pessoa = rmi.fac_pessoa(select_person.numero_cc);
										String dep_pessoa = rmi.dep_pessoa(select_person.numero_cc);
										if (!fac_pessoa.equals("None")) {
											out.print("Current department: " + dep_pessoa + "\nDepartments avaible:\n");
											ArrayList<String> dep_fac = rmi.dep_fac(fac_pessoa);
											if (!dep_fac.isEmpty()) {
												out.print(print_strings(dep_fac));
												out.print("0-Exit");
												option = testOption(0, dep_fac.size());
												if (option != 0) {
													String departamento = dep_fac.get(option - 1);
													output = rmi.update_person(select_person);
													out.print(output);
													if (!output.equals("Person updated\n")) {
														option = 0;
													} else {
														if (dep_pessoa.equals("None")) {
															rmi.insert_department(select_person.numero_cc,
																	departamento);
														} else {
															rmi.update_department(departamento,
																	select_person.numero_cc);
														}
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
											out.print("Currente phone number: " + select_person.telefone
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
						ArrayList<String> list_facultys = rmi.list_facultys();
						if (list_facultys.isEmpty()) {
							out.print("Theres is no facultys avaible\n");
						} else {
							out.print("Chose faculty:\n");
							System.out.print(print_strings(list_facultys));
							out.print("0-Exit");
							option = testOption(0, list_facultys.size());
							if (option != 0) {
								String chosen_faculty = list_facultys.get(option - 1);
								out.print(
										"What do you want to change:\n1-Add departments to faculty\n2-Remove departments from faculty\n0-Exit");
								option = testOption(0, 2);
								out.println("segurança para nao ser 0");
								if (option == 1) {
									assing_departments_to_facultys(chosen_faculty);
								} else if (option == 2) {
									remove_departments_from_facultys(chosen_faculty);
								}
							}

						}
						break;
					case 13:
						int option2;
						i = 0;
						String write;
						ArrayList<Eleicoes> list_elections = rmi.return_eleicoes();
						if (list_elections.isEmpty()) {
							out.print("Theres is no elections avaible\n");
						} else {
							out.print("Elections:\n");
							Date date = new Date();
							for (Eleicoes x : list_elections) {
								if (x.start.after(date)) {
									out.println(++i + "-" + "Title: " + x.titulo + "\tType of election: " + x.tipo);
								}
							}
							if (i != 0) {
								out.println("0-Exit");
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
										out.print("Select the field you want to change:\n"
												+ "1-Change type(also the place of election)\n"
												+ "2-Change starting date\n" + "3-Change ending date\n"
												+ "4-Change resume\n" + "5-Change lists\n"
												+ "6-Edit tables(adicionar as 3 pessoas com proteçao para nao estarem em dois locais ao mesmo tempo e poderem votar)\n"
												+ "0-Exit");
										option = testOption(0, 6);
										switch (option) {
										case 0:
											option = 0;
											break;
										case 1:
											out.print("Current type: " + election.tipo
													+ "\nChange to:\n1-geral\n2-faculty\n3-department\n4-nucleo\n"
													+ "(By changing the type of election some lists may also be deleted)\n0-Exit");
											option2 = testOption(0, 6);
											ArrayList<Lista_candidata> lists = rmi.get_lists_election(election.id);
											String tipo = "";
											String previous_type = "";
											String escolha = "";
											if (option2 == 1) {
												previous_type = election.tipo;
												election.tipo = "geral";
											} else if (option2 == 2) {
												previous_type = election.tipo;
												ArrayList<String> list_facul = rmi.list_facultys();
												if (!list_facul.isEmpty()) {
													out.print("Chose faculty:\n");
													out.print(print_strings(list_facul));
													out.print("0-Exit");
													option2 = testOption(0, list_facul.size());
													if (option2 != 0) {
														escolha = list_facul.get(option2 - 1);
														election.tipo = "faculty";
														tipo = "teacher";
													}
												} else {
													out.println("Option invalid because theres no facultys");
												}

											} else if (option2 == 3) {
												previous_type = election.tipo;
												ArrayList<String> list_dep = rmi.departments_for_elections();
												if (!list_dep.isEmpty()) {
													out.print("Chose department:\n");
													out.print(print_strings(list_dep));
													out.print("0-Exit");
													option2 = testOption(0, list_dep.size());
													if (option2 != 0) {
														escolha = list_dep.get(option2 - 1);
														election.tipo = "department";
														tipo = "teacher";
													}
												} else {
													out.println("Option invalid because theres no department avabiles");
												}
											} else if (option2 == 4) {
												previous_type = election.tipo;
												ArrayList<String> list_dep = rmi.departments_for_elections();
												if (!list_dep.isEmpty()) {
													out.print("Chose department:\n");
													out.print(print_strings(list_dep));
													out.print("0-Exit");
													option2 = testOption(0, list_dep.size());
													if (option2 != 0) {
														escolha = list_dep.get(option2 - 1);
														election.tipo = "department";
														tipo = "teacher";
													}
												} else {
													out.println("Option invalid because theres no department avabiles");
												}

											}
											if (option2 != 0) {
												Eleicoes ninguem_mexe = null;
												list_elections = rmi.return_eleicoes();
												int check3 = 0;
												for (Eleicoes x : list_elections) {
													if (x.id == election.id) {
														check3 = 1;
														ninguem_mexe = x;
													}
												}
												if (check3 == 1) {
													date = new Date();
													if (election.start.after(date)) {
														if (ninguem_mexe.tipo.equals(previous_type)) {
															output = rmi.update_election(election);
															out.print(output);
															if (output.equals("Election updated\n")) {
																if (election.tipo != "geral") {
																	apaga_listas(tipo, lists);
																}
																if (election.tipo.equals("geral")) {
																	if (previous_type.equals("faculty")) {
																		rmi.del_local_election_fac(election.id);
																	} else if (previous_type.equals("department")
																			|| previous_type.equals("nucleo")) {
																		rmi.del_local_election_dep(election.id);
																	}
																} else if ((previous_type.equals(election.tipo)
																		&& (election.tipo.equals("department")
																				|| election.tipo.equals("nucleo")))
																		|| (previous_type.equals("nucleo")
																				&& election.tipo.equals("department"))
																		|| previous_type.equals("department")
																				&& election.tipo.equals("nucleo")) {
																	rmi.update_department_eleicao(election.id, escolha);
																} else if (previous_type.equals(election.tipo)
																		&& election.tipo.equals("faculty")) {
																	rmi.update_faculty_eleicao(election.id, escolha);
																} else if ((previous_type.equals("department")
																		|| previous_type.equals("nucleo"))
																		&& election.tipo.equals("faculty")) {
																	rmi.del_local_election_dep(election.id);
																	rmi.add_election_fac(escolha, election.id);
																} else if (previous_type.equals("faculty")
																		&& (election.tipo.equals("department")
																				|| election.tipo.equals("nucleo"))) {
																	rmi.del_local_election_fac(election.id);
																	rmi.add_election_dep(escolha, election.id);
																} else if (previous_type.equals("geral")
																		&& (election.tipo.equals("department")
																				|| election.tipo.equals("nucleo"))) {
																	rmi.add_election_dep(escolha, election.id);
																} else if (previous_type.equals("geral")
																		&& election.tipo.equals("faculty")) {
																	rmi.add_election_fac(escolha, election.id);
																}
															}
														} else {
															out.println(
																	"Changes not registered because another admin changed the type of election meanwhile");
														}
													} else {
														out.println(
																"Election not updated because it started meanwhile");
														option = 0;
													}
												} else {
													out.println("Election not found");
												}
											}
											break;
										case 2:
											int check3 = 0;
											Date inicial_date = election.start;
											option2 = 1;
											while (option2 != 0) {
												try {
													SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss dd-MM-yyyy");
													out.print("Current starting date: " + election.start
															+ "\nNew starting date(hh:mm:ss dd-mm-yyyy):\n");
													write = inputS.nextLine();
													write_date_format = dt.parse(write);
													date = new Date();
													if (date.after(write_date_format)) {
														System.out
																.println("Starting date is before today date invalide");
													} else {
														if (election.end.after(write_date_format)) {
															option2 = 0;
														} else {
															out.print(
																	"End date can not be set to before ou equal to start date.");
														}
													}
												} catch (ParseException e) {
													out.println("Erro no formato da data!\n");
												}
											}
											list_elections = rmi.return_eleicoes();
											check3 = 0;
											for (Eleicoes x : list_elections) {
												if (x.id == election.id) {
													check3 = 1;
												}
											}
											if (check3 == 1) {
												date = new Date();
												if (inicial_date.after(date)) {
													election.start = write_date_format;
													output = rmi.update_election(election);
													out.print(output);
												} else {
													out.println("Election not updated because it started meanwhile");
													option = 0;
												}
											} else {
												out.println("Election not found");
												option = 0;
											}
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
											list_elections = rmi.return_eleicoes();
											check3 = 0;
											for (Eleicoes x : list_elections) {
												if (x.id == election.id) {
													check3 = 1;
												}
											}
											if (check3 == 1) {
												date = new Date();
												if (election.start.after(date)) {
													election.end = write_date_format;
													output = rmi.update_election(election);
													out.print(output);
												} else {
													out.println("Election not updated because it started meanwhile");
													option = 0;
												}
											} else {
												out.println("Election not found");
												option = 0;
											}
											break;
										case 4:
											String new_resume;
											out.print(
													"Current resume of election: " + election.resumo + "\nNew resume:");
											new_resume = inputS.nextLine();
											list_elections = rmi.return_eleicoes();
											check3 = 0;
											for (Eleicoes x : list_elections) {
												if (x.id == election.id) {
													check3 = 1;
												}
											}
											if (check3 == 1) {
												date = new Date();
												if (election.start.after(date)) {
													election.resumo = new_resume;
													output = rmi.update_election(election);
													out.print(output);
												} else {
													out.println("Election not updated because it started meanwhile");
													option = 0;
												}
											} else {
												out.println("Election not found");
												option = 0;
											}
											break;
										case 5:
											out.print("1-Add list\n2-Remove list\n0-Exit");
											option2 = testOption(0, 2);
											if (option2 == 1) {
												if (election.tipo.equals("nucleo")) {
													add_lists_election(election.id, "student", election);
													output = rmi.update_election(election);
													if (!output.equals("Election updated\n")) {
														option = 0;
													}
													date = new Date();
													if (election.start.before(date)) {
														option = 0;
													}
												} else if (election.tipo.equals("faculty")
														|| election.tipo.equals("department")) {
													add_lists_election(election.id, "teacher", election);
													output = rmi.update_election(election);
													if (!output.equals("Election updated\n")) {
														option = 0;
													}
													date = new Date();
													if (election.start.before(date)) {
														option = 0;
													}
												} else if (election.tipo.equals("geral")) {
													add_lists_election(election.id, "student", election);
													output = rmi.update_election(election);
													if (!output.equals("Election updated\n")) {
														option = 0;
													} else {
														add_lists_election(election.id, "teacher", election);
														output = rmi.update_election(election);
														if (!output.equals("Election updated\n")) {
															option = 0;
														} else {
															add_lists_election(election.id, "staff", election);
															output = rmi.update_election(election);
															if (!output.equals("Election updated\n")) {
																option = 0;
															}
														}
													}
													date = new Date();
													if (election.start.before(date)) {
														option = 0;
													}
												}

											} else if (option2 == 2) {
												delete_lists(election.id, election);
												output = rmi.update_election(election);
												if (!output.equals("Election updated\n")) {
													option = 0;
												}
											}
											break;
										case 6:
											check = 0;
											j = 0;
											out.print(
													"1-Remove table\n2-Add table\n3-Change location of table\n4-Edit people to table\n0-Exit");
											option2 = testOption(0, 4);
											if (option2 == 1) {
												ArrayList<String> mesas_remove = rmi.mesas_eleicao(election.id);
												if (mesas_remove.isEmpty()) {
													out.print("No tables avaible to be removed\n");
												} else {
													j = 1;
													option2 = 1;
													while (option2 != 0 && j != 0) {
														out.println("Tables avaible to be removed:");
														mesas_remove = rmi.mesas_eleicao(election.id);
														for (i = 0; i < 2; i++) {
															j = 0;
															for (String k : mesas_remove) {
																if (i == 0) {
																	out.print(++j + "-" + k + "\n");
																}
																if (i == 1) {
																	++j;
																	if (j == option2) {
																		output = rmi.update_election(election);

																		if (output.equals("Election updated\n")) {
																			date = new Date();
																			if (election.start.after(date)) {
																				mesas_remove = rmi
																						.mesas_eleicao(election.id);
																				if (mesas_remove.contains(k)) {
																					out.println("table deleted");
																					rmi.delete_table_from_election(k);
																				} else {
																					out.println(
																							"table not found. removed by another admin");
																				}
																			} else {
																				out.println(
																						"Table not deleted because election started meanwhile");
																				option2 = 0;
																				option = 0;
																			}
																		} else {
																			option2 = 0;
																			option = 0;
																			out.println("Election not found!");
																		}
																	}
																}
															}
															if (i == 0 && j != 0) {
																out.print("0-Exit");
																option2 = testOption(0, j);
															}
															if (j == 0) {
																out.println("No more tables avaible");
																i++;
															}
															if (option2 == 0) {
																i++;
															}
														}
													}
												}
											} else if (option2 == 2) {
												mesas = rmi.avaible_tables();
												if (mesas.isEmpty()) {
													out.print("No tables avaible to be added\n");
												} else {
													i = 1;
													option2 = 1;
													while (option2 != 0 && i != 0) {
														mesas = rmi.avaible_tables();
														for (j = 0; j < 2; j++) {
															i = 0;
															if (j == 0) {
																out.print(
																		"Table avaible do be added to the election:\n");
															}
															for (String k : mesas) {
																if (j == 0) {
																	++i;
																	out.print(i + "-" + k + "\n");
																}
																if (j == 1) {
																	++i;
																	if (i == option2) {
																		output = rmi.update_election(election);
																		if (output.equals("Election updated\n")) {
																			date = new Date();
																			if (election.start.after(date)) {
																				mesas = rmi.avaible_tables();
																				if (mesas.contains(k)) {
																					rmi.insert_mesa_election(k,
																							election.id);
																					ArrayList<Lista_candidata> listas = rmi
																							.get_lists_election(
																									election.id);
																					for (Lista_candidata x : listas) {
																						rmi.insert_mesa_election_list_votes(
																								k, x.id);
																					}
																					out.println("table added");
																				} else {
																					out.println(
																							"table not found. removed by another admin");
																				}
																			} else {
																				out.println(
																						"Table not deleted because election started meanwhile");
																				option2 = 0;
																				option = 0;
																			}
																		} else {
																			option2 = 0;
																			option = 0;
																			out.println("Election not found!");
																		}
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
											} else if (option2 == 3) {
												mesas = rmi.mesas_eleicao(election.id);
												String mesa_escolhida;
												if (!mesas.isEmpty()) {
													out.println("Table avaible:");
													j = 0;
													for (String x : mesas) {
														out.println(++j + "-Table:" + x + " Location:"
																+ rmi.location_table(x));
													}
													out.print("0-Exit");
													option2 = testOption(0, j);
													if (option2 != 0) {
														mesas = rmi.mesas_eleicao(election.id);
														mesa_escolhida = mesas.get(option2 - 1);
														String location;
														if (mesas.contains(mesa_escolhida)) {
															location = rmi.location_table(mesa_escolhida);
															out.print(
																	"Change to:\n1-Faculty\n2-department\n3-set to none\n0-Exit");
															option2 = testOption(0, 3);
															int option3 = 0;
															ArrayList<String> sem_repetidas = new ArrayList<String>();
															ArrayList<String> locations = new ArrayList<String>();
															for (String x : mesas) {
																locations.add(rmi.location_table(x));
															}
															if (option2 == 1) {
																String faculdade_escolhida;
																ArrayList<String> faculdades = rmi.list_facultys();
																for (String x : faculdades) {
																	if (!locations.contains(x)) {
																		sem_repetidas.add(x);
																	}
																}
																if (!faculdades.isEmpty()) {
																	j = 0;
																	for (String x : sem_repetidas) {
																		out.println(++j + "-" + x);

																	}
																	out.print("0-Exit");
																	option3 = testOption(0, j);
																	if (option3 != 0) {
																		faculdade_escolhida = sem_repetidas
																				.get(option3 - 1);
																		output = rmi.update_election(election);
																		if (output.equals("Election updated\n")) {
																			date = new Date();
																			if (election.start.after(date)) {
																				mesas = rmi.mesas_eleicao(election.id);
																				if (mesas.contains(mesa_escolhida)) {
																					faculdades = rmi.list_facultys();
																					if (faculdades.contains(
																							faculdade_escolhida)) {
																						if (location.equals("None")) {
																							rmi.insert_mesa_faculdade(
																									mesa_escolhida,
																									faculdade_escolhida);
																						} else {
																							if (rmi.list_facultys()
																									.contains(
																											location)) {
																								rmi.update_tab_location_fac(
																										mesa_escolhida,
																										faculdade_escolhida);

																							} else if (rmi
																									.departments_for_elections()
																									.contains(
																											location)) {
																								rmi.insert_mesa_faculdade(
																										mesa_escolhida,
																										faculdade_escolhida);
																								rmi.delete_table_from_department(
																										mesa_escolhida);
																							}
																						}

																					} else {
																						out.println(
																								"Impossible action faculty not found probably deleted by another admin");
																					}
																				} else {
																					out.println(
																							"table not found. removed by another admin");
																				}
																			} else {
																				out.println(
																						"Table location not changed because election started meanwhile");
																				option = 0;
																			}
																		} else {
																			option = 0;
																			out.println("Election not found!");
																		}

																	}
																} else {
																	out.println("No facultys avaible");
																}

															} else if (option2 == 2) {
																String departamento_escolhido;
																ArrayList<String> departamentos = rmi
																		.departments_for_elections();
																for (String x : departamentos) {
																	if (!locations.contains(x)) {
																		sem_repetidas.add(x);
																	}
																}
																if (!departamentos.isEmpty()) {
																	j = 0;
																	for (String x : sem_repetidas) {

																		out.println(++j + "-" + x);

																	}
																	out.print("0-Exit");
																	option3 = testOption(0, j);
																	if (option3 != 0) {
																		departamento_escolhido = sem_repetidas
																				.get(option3 - 1);
																		output = rmi.update_election(election);
																		if (output.equals("Election updated\n")) {
																			date = new Date();
																			if (election.start.after(date)) {
																				mesas = rmi.mesas_eleicao(election.id);
																				if (mesas.contains(mesa_escolhida)) {
																					departamentos = rmi
																							.departments_for_elections();
																					if (departamentos.contains(
																							departamento_escolhido)) {
																						if (location.equals("None")) {
																							rmi.insert_mesa_department(
																									mesa_escolhida,
																									departamento_escolhido);
																						} else {
																							if (rmi.list_facultys()
																									.contains(
																											location)) {
																								rmi.insert_mesa_department(
																										mesa_escolhida,
																										departamento_escolhido);
																								rmi.delete_table_from_faculty(
																										mesa_escolhida);

																							} else if (rmi
																									.departments_for_elections()
																									.contains(
																											location)) {
																								rmi.update_tab_location_dep(
																										mesa_escolhida,
																										departamento_escolhido);
																							}
																						}

																					} else {
																						out.println(
																								"Impossible action department not found probably deleted by another admin");
																					}
																				} else {
																					out.println(
																							"table not found. removed by another admin");
																				}
																			} else {
																				out.println(
																						"Table location not changed because election started meanwhile");
																				option = 0;
																			}
																		} else {
																			option = 0;
																			out.println("Election not found!");
																		}

																	}
																} else {
																	out.println("No departments avaible");
																}

															} else if (option2 == 3) {
																output = rmi.update_election(election);
																if (output.equals("Election updated\n")) {
																	date = new Date();
																	if (election.start.after(date)) {
																		mesas = rmi.mesas_eleicao(election.id);
																		if (mesas.contains(mesa_escolhida)) {
																			if (!location.equals("None")) {
																				if (rmi.list_facultys()
																						.contains(location)) {
																					rmi.delete_table_from_faculty(
																							mesa_escolhida);

																				} else if (rmi
																						.departments_for_elections()
																						.contains(location)) {
																					rmi.delete_table_from_department(
																							mesa_escolhida);

																				}
																			}
																		} else {
																			out.println(
																					"table not found. removed by another admin");
																		}
																	} else {
																		out.println(
																				"Table location not changed because election started meanwhile");
																		option = 0;
																	}
																} else {
																	option = 0;
																	out.println("Election not found!");
																}
															}
														} else {
															out.println("table not found");
														}
													}
												} else {
													out.print("No tables avaible");
												}
											} else if (option2 == 4) {
												out.print("1-Add person to table\n2-Remove person from table\n0-Exit");
												option2 = testOption(0, 2);
												if (option2 != 0) {
													if (option2 == 1) {
														mesas = rmi.mesas_eleicao(election.id);
														ArrayList<String> mesas_not_full = new ArrayList<String>();
														if (!mesas.isEmpty()) {
															out.println("Table avaible:");
															j = 0;

															for (String x : mesas) {
																if (rmi.count_number_of_persom_table(x) != 3) {
																	mesas_not_full.add(x);
																}
															}
															for (String x : mesas_not_full) {
																out.println(++j + "-Table:" + x);
															}
															if (j != 0) {
																out.print("0-Exit");
																option2 = testOption(0, j);
																if (option2 != 0) {
																	String mesa = mesas_not_full.get(option2 - 1);
																	pessoas = rmi.get_pessoas();
																	HashMap<Integer, String> people_tables = rmi
																			.get_pessoas_mesas();
																	HashMap<Integer, Integer> pessoa_eleicao = new HashMap<Integer, Integer>();
																	for (HashMap.Entry<Integer, String> entry : people_tables
																			.entrySet()) {
																		pessoa_eleicao.put(entry.getKey(),
																				rmi.get_election_of_table(
																						entry.getValue()));
																	}
																	Pessoa remove = null;
																	int check4;
																	for (HashMap.Entry<Integer, Integer> entry : pessoa_eleicao
																			.entrySet()) {
																		check4 = 0;
																		if (testaPessoaInElection(election,
																				entry.getValue()) == 1) {
																			for (Pessoa x : pessoas) {
																				if (x.numero_cc == entry.getKey()) {
																					check4 = 1;
																					remove = x;
																				}
																			}
																			if (check4 == 1) {
																				pessoas.remove(remove);
																			}
																		}
																	}
																	j = 0;
																	for (Pessoa x : pessoas) {
																		out.println(++j + "-" + x.nome + " cc:"
																				+ x.numero_cc);
																	}
																	if (j != 0) {
																		out.print("0-Exit");
																		option2 = testOption(0, j);
																		if (option2 != 0) {
																			output = rmi.update_election(election);
																			if (output.equals("Election updated\n")) {
																				date = new Date();
																				if (election.start.after(date)) {
																					rmi.insert_person_table(
																							pessoas.get(option2
																									- 1).numero_cc,
																							mesa);

																				} else {
																					out.println(
																							"election not updated because it started meanwhile");
																				}
																			} else {
																				out.println("Election not found");
																			}
																		}
																	} else {
																		out.println("No persons avaible to be added");
																	}
																}
															} else {
																out.println("No tables avaible");
															}
														} else {
															out.println("No tables avaible");
														}

													} else if (option2 == 2) {
														out.println("ENTRA");
														mesas = rmi.mesas_eleicao(election.id);
														ArrayList<String> mesas_not_empty = new ArrayList<String>();
														out.println("Tables avaible:");
														j = 0;

														for (String x : mesas) {
															if (rmi.count_number_of_persom_table(x) != 0) {
																mesas_not_empty.add(x);
															}
														}
														for (String x : mesas_not_empty) {
															out.println(++j + "-Table:" + x);
														}
														if (j != 0) {
															out.print("0-Exit");
															option2 = testOption(0, j);
															if (option2 != 0) {
																String mesa = mesas_not_empty.get(option2 - 1);
																pessoas = rmi.get_pessoas();
																HashMap<Integer, String> people_tables = rmi
																		.get_pessoas_mesas();
																ArrayList<Pessoa> ccs_table = new ArrayList<Pessoa>();
																for (HashMap.Entry<Integer, String> entry : people_tables
																		.entrySet()) {
																	for (Pessoa x : pessoas) {
																		if (x.numero_cc == entry.getKey()
																				&& mesa.equals(entry.getValue())) {
																			ccs_table.add(x);
																		}
																	}
																}
																j = 0;
																for (Pessoa x : ccs_table) {
																	out.println(++j + "-" + x.nome + " number cc:"
																			+ x.numero_cc);
																}
																out.print("0-Exit");
																option2 = testOption(0, j);
																if (option2 != 0) {
																	output = rmi.update_election(election);
																	if (output.equals("Election updated\n")) {
																		date = new Date();
																		if (election.start.after(date)) {
																			rmi.delete_person_from_table(mesa, ccs_table
																					.get(option2 - 1).numero_cc);
																		} else {
																			out.println(
																					"election not updated because it started meanwhile");
																		}
																	} else {
																		out.println("Election not found");
																	}
																}
															}
														} else {
															out.print("No tables avaible");
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
						boolean cicle = true;
						int check5 = 0;
						while (cicle == true) {
							out.println("username(type cancel to exit):");
							String username = inputS.nextLine();
							if (!username.equals("cancel")) {
								Pessoa pessoa_escolhida = null;
								for (Pessoa x : rmi.get_pessoas()) {
									if (x.nome.equals(username)) {
										pessoa_escolhida = x;
										check5 = 1;
									}
								}
								if (check5 == 1) {
									boolean cicle2 = true;
									while (cicle2 == true) {
										out.println("password(type cancel to exit):");
										String password = inputS.nextLine();
										if (!password.equals("cancel")) {
											if (password.equals(pessoa_escolhida.password)) {
												ArrayList<Eleicoes> eleicoes = rmi.return_eleicoes();
												Date date = new Date();
												ArrayList<Eleicoes> eleicoes_ant = new ArrayList<Eleicoes>();
												for (Eleicoes x : eleicoes) {
													if(date.before(x.start)) {
														eleicoes_ant.add(x);
													}
												}
												i = 0;
												if (!eleicoes_ant.isEmpty()) {
													for (Eleicoes x : eleicoes_ant) {
														out.println(++i + "-" + x.titulo + "\ttype:" + x.tipo);
													}
													out.print("0-Exit");
													option2 = testOption(0, i);
													if (option2 != 0) {
														out.println(
																"Your logs will be registed in a table of the elction");
														Eleicoes eleicao_escolhida = eleicoes_ant.get(option2 - 1);
														mesas = rmi.mesas_eleicao(eleicao_escolhida.id);// mais uma
														if (!mesas.isEmpty()) {
															String mesa = mesas.get(0);
															ArrayList<Lista_candidata> lists = rmi
																	.print_lists_for_person(pessoa_escolhida.numero_cc,
																			mesa);
															if (lists.isEmpty()) {
																out.println(
																		"No lists avaible for you to vote on this election");
																cicle2=false;
																cicle=false;
															} else {
																for (Lista_candidata x : lists) {
																	out.println(x.toString());
																}
																out.println(
																		"Type the name the list you want to vote(type blanc for vote in blanc and cancel to exit)");
																String list_chosen = inputS.nextLine();
																if (!list_chosen.equals("cancel")) {
																	rmi.register_vote(pessoa_escolhida.numero_cc,
																			list_chosen, mesa);
																} else {
																	cicle2 = false;
																	cicle = false;
																}
															}
														} else {
															out.println("Impossible to vote in this election because there is no tables");
															cicle2 = false;
															cicle = false;
														}
													} else {
														cicle2 = false;
														cicle = false;
													}
												} else {
													out.println("No elections avaible");
													cicle2 = false;
													cicle = false;
												}
											} else {
												out.println("password incorrect");
											}
										} else {
											cicle2 = false;
											cicle = false;
										}
									}
								} else {
									out.println("Username not found");
								}
							} else {
								cicle = false;
							}
						}
						break;

					case 15:
						ArrayList<Eleicoes> eleicoes = rmi.return_eleicoes();
						if (rmi.return_eleicoes().isEmpty()) {
							out.println("Lista vazia");
						} else {
							for (Eleicoes x : eleicoes) {
								System.out.println(x.toString());
							}
						}
						break;
					case 16:
						if (rmi.return_eleicoes().isEmpty()) {
							out.println("Lista vazia");
						} else {
							Date date = new Date();
							i = 0;
							for (Eleicoes x : rmi.return_eleicoes()) {
								if (x.start.before(date) && x.end.after(date)) {
									out.print(++i + "-" + x.toString() + "\n");
								}
							}
							if (i == 0) {
								out.print("No elections on at the moment");
							}
						}
						break;
					case 17: // TODO case 17

						if (rmi.return_eleicoes().isEmpty()) {
							out.println("Lista vazia");
						} else {
							Date date = new Date();
							i = 0;
							int votos_total;
							int votos_brancos;
							int votos_null;
							for (Eleicoes x : rmi.return_eleicoes()) {
								votos_total = 0;
								if (x.end.before(date)) {
									out.println(++i + "-" + x.titulo);
									ArrayList<Lista_candidata> listas = rmi.get_lists_election(x.id);
									for (Lista_candidata k : listas) {
										votos_total += k.num_votos;
									}
									votos_brancos = rmi.votos_null(x.id);
									votos_total += votos_brancos;
									votos_null = rmi.votos_brancos(x.id);
									votos_total += votos_null;
									float percentagem = 0;
									out.println("null votes:" + votos_null);
									out.println("blanc votes:" + votos_brancos);
									if (x.tipo.equals("geral")) {
										out.println("Students lists:");
										for (Lista_candidata k : listas) {
											if (k.classe.equals("student")) {
												if (votos_total != 0) {
													percentagem = (k.num_votos / votos_total) * 100;
												} else {
													percentagem = 0;
												}
												out.println("List:" + k.nome_lista + "\tnumber of votes:" + k.num_votos
														+ "\tpercentage of votes:" + percentagem);
											}
										}
										out.println("teacher lists:");
										for (Lista_candidata k : listas) {
											if (k.classe.equals("teacher")) {
												if (votos_total != 0) {
													percentagem = (k.num_votos / votos_total) * 100;
												} else {
													percentagem = 0;
												}
												out.println("List:" + k.nome_lista + "\tnumber of votes:" + k.num_votos
														+ "\tpercentage of votes:" + percentagem);
											}
										}
										out.println("staff lists:");
										for (Lista_candidata k : listas) {
											if (k.classe.equals("staff")) {
												if (votos_total != 0) {
													percentagem = (k.num_votos / votos_total) * 100;
												} else {
													percentagem = 0;
												}
												out.println("List:" + k.nome_lista + "\tnumber of votes:" + k.num_votos
														+ "\tpercentage of votes:" + percentagem);
											}
										}
									} else if (x.tipo.equals("department") || x.tipo.equals("faculty")) {
										out.println("teacher lists:");
										for (Lista_candidata k : listas) {
											if (k.classe.equals("teacher")) {
												if (votos_total != 0) {
													percentagem = (k.num_votos / votos_total) * 100;
												} else {
													percentagem = 0;
												}
												out.println("List:" + k.nome_lista + "\tnumber of votes:" + k.num_votos
														+ "\tpercentage of votes:" + percentagem);
											}
										}
									} else if (x.tipo.equals("nucleo")) {
										out.println("Students lists:");
										for (Lista_candidata k : listas) {
											if (k.classe.equals("student")) {
												if (votos_total != 0) {
													percentagem = (k.num_votos / votos_total) * 100;
												} else {
													percentagem = 0;
												}
												out.println("List:" + k.nome_lista + "\tnumber of votes:" + k.num_votos
														+ "\tpercentage of votes:" + percentagem);
											}
										}
									}
								}
							}
							if (i == 0) {
								out.print("No elections done at the moment");
							}
						}
						break;

					case 18:
						ArrayList<Pessoa> lista_pessoas = rmi.get_pessoas();
						if (lista_pessoas.isEmpty()) {
							out.print("Theres is no persons avaible\n");
						} else {
							for (Pessoa x : lista_pessoas) {
								out.print(x.toString());
							}
						}
						break;

					case 19:
						ArrayList<String> facultys = rmi.list_facultys();
						if (facultys.isEmpty()) {
							out.print("Theres is no facultys avaible\n");
						} else {
							print_strings(facultys);
						}
						break;

					case 20:
						ArrayList<String> departments = rmi.listDepartments();
						if (departments.isEmpty()) {
							out.print("Theres is no departments avaible\n");
						} else {
							print_strings(departments);
						}
						break;

					case 21:
						ArrayList<String> tables = rmi.listTables();
						if (tables.isEmpty()) {
							out.print("Theres is no departments avaible\n");
						} else {
							print_strings(tables);
						}
						break;

					case 22:
						HashMap<Integer, HashMap<String, Timestamp>> auditoria = rmi.auditoria();
						if (!auditoria.isEmpty()) {
							for (HashMap.Entry<Integer, HashMap<String, Timestamp>> entry : auditoria.entrySet()) {
								for (HashMap.Entry<String, Timestamp> entry2 : entry.getValue().entrySet()) {
									out.println("The person with the cc:"+entry.getKey()+"\tvoted on the table:"+entry2.getKey()+"\tat the time:"+entry2.getValue());
								}
							}
						}else {
							out.println("No results to search");
						}
						break;
            
					case 23: // TODO case 23
						System.exit(0);
					default:
						out.print("\nOpçao invalida\n");
					}
				}

			} catch (Exception e) {
				out.println("erro2");
			}
		}

	}

	public void imprimeNotificacao(String notification) {
		System.out.println("\n" + notification);
	}

	public int testaPessoaInElection(Eleicoes eleicao, int id) throws RemoteException {
		ArrayList<Eleicoes> eleicoes = rmi.return_eleicoes();
		for (Eleicoes k : eleicoes) {
			if (k.id == id && (((eleicao.end.before(k.end) && eleicao.end.after(k.start))
					|| (eleicao.start.before(k.end) && eleicao.start.after(k.start)) || eleicao.start.equals(k.start))
					|| eleicao.end.equals(k.end) || (eleicao.start.before(k.start) && eleicao.end.after(k.end)))) {
				return 1;
			}
		}
		return 0;
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
						"Nao pode comecar com um espaco nem acabar com um espaco e só pode conter letras e numeros!");
				return false;
			}
			i++;
		}
		if (p.length() == 0) {
			out.println("Nao pode comecar com um espaco nem acabar com um espaco e só pode conter letras e numeros!");
			return false;
		}
		return true;
	}

	public static void add_lists_election(int id, String type, Eleicoes election) throws RemoteException {
		String output;
		int check = 0;
		int id_lista;
		String name_list = "";
		while (!name_list.equals("done")) {
			out.print("Name of the list to add for " + type + " do vote(type done when finished):\n");
			do {
				name_list = inputS.nextLine();
			} while (!checkStringWithNumbers(name_list));
			if (!name_list.equals("done")) {
				check = 0;
				ArrayList<Lista_candidata> lista_existentes = null;
				try {
					lista_existentes = rmi.get_lists_election(id);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				for (Lista_candidata x : lista_existentes) {
					if (x.classe.equals(type) && x.nome_lista.equals(name_list)) {
						out.println("There is a list already with that name on the election");
						check = 1;
					}
				}
				if (check == 0) {
					output = rmi.update_election(election);
					if (output.equals("Election updates\n")) {
						id_lista = id_lista();
						Date date = new Date();
						if (election.start.after(date)) {
							rmi.insert_listas(id, name_list, type, id_lista);
							ArrayList<String> mesas = rmi.mesas_eleicao(id);
							for (String x : mesas) {
								rmi.insert_mesa_election_list_votes(x, id_lista);
							}
							out.print("List added\n");
						} else {
							out.println("List not added because election started meanwhile");
							name_list = "done";
						}
					} else {
						out.print(output);
						name_list = "done";
					}

				}
			}
		}
	}

	public static int id_lista() throws RemoteException {
		boolean cicle;
		int id_lista = 1;
		cicle = true;
		ArrayList<Integer> id_listas = rmi.id_listas();
		if (!id_listas.isEmpty()) {
			while (cicle == true) {
				for (Integer k : id_listas) {
					if (id_lista == k || id_lista < k) {
						id_lista++;
					} else {
						cicle = false;
						return id_lista;
					}
				}
			}
		} else {
			return id_lista;
		}
		return 1;
	}

	public static void delete_lists(int id, Eleicoes election) throws RemoteException {
		ArrayList<Lista_candidata> listas_apagar = rmi.get_lists_election(id);
		String output;
		int option = 0;
		int i = 1;
		while (option != 0 && i != 0) {
			i = 0;
			out.print("lists avaible to be deleted:\n");
			listas_apagar = rmi.get_lists_election(id);
			for (int j = 0; j < 2; j++) {
				i = 0;
				for (Lista_candidata x : listas_apagar) {
					if (j == 0) {
						out.print(++i + "-" + "classe:" + x.classe + " Nome:" + x.nome_lista + "\n");
					}
					if (j == 1) {
						++i;
						if (option == i) {
							output = rmi.update_election(election);
							if (output.equals("Election updates\n")) {
								Date date = new Date();
								if (election.start.after(date)) {
									listas_apagar = rmi.get_lists_election(id);
									for (Lista_candidata k : listas_apagar) {
										if (x.id == k.id) {
											rmi.delete_list(x.id);
										}
									}
								} else {
									out.println("List not deleted because election started meanwhile");
								}
							} else {
								out.print(output);
								i = 0;
								option = 0;
							}
						}
					}
					if (j == 0 && i != 0) {
						out.print("0-Exit");
						option = testOption(0, i);
					}
				}
				if (i == 0) {
					out.print("No more lists avaible to be deleted \n");
					j++;
				}
				if (option == 0) {
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

	/*
	 * public String print_facultys(ArrayList<String> faculdades) { String nome_fac
	 * = ""; int i = 0; for (String x : faculdades) { nome_fac += ++i + "-" + x +
	 * "\n"; } return nome_fac; }
	 * 
	 * public static String print_dep_fac(ArrayList<String> dep_fac) { int i = 0;
	 * String output = ""; for (String x : dep_fac) { output += ++i + "-" + x +
	 * "\n"; } return output; }
	 */

	public static String print_strings(ArrayList<String> strings) {
		int i = 0;
		String output = "";
		for (String x : strings) {
			output += ++i + "-" + x + "\n";
		}
		return output;
	}

	public static ArrayList<String> create_lists_election(String type) {
		ArrayList<String> lists_running = new ArrayList<String>();
		int check = 0;
		String name_list = "";
		while (!name_list.equals("done")) {
			out.print("Name of the list to add for " + type + " do vote(type done when finished):\n");
			name_list = inputS.nextLine();
			if (!name_list.equals("done")) {
				if (lists_running.contains(name_list)) {
					out.println("Already exists a list with that name!");
				} else {
					lists_running.add(name_list);
					out.print("List added\n");
				}
			}
		}
		return lists_running;
	}

	public static void assing_departments_to_facultys(String fac) {
		int i = 1, option = 0;
		String escolha = "";
		try {
			ArrayList<String> depart = rmi.avaible_departments();
			for (int j = 0; j < 2; j++) {
				i = 0;
				if (j == 0) {
					out.print("Departments avaible to be added:\n");
				}
				for (String k : depart) {
					if (j == 0) {
						out.print(++i + "-" + k + "\n");
					}
					if (j == 1) {
						++i;
						if (i == option) {
							escolha = depart.get(option - 1);
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
			depart = rmi.avaible_departments();
			if (option != 0) {
				if (depart.contains(escolha)) {
					rmi.update_dep_fac(escolha, fac);
					out.println("Department added");
				} else {
					out.println("Department was already atributed to another faculty or deleted.");
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static void remove_departments_from_facultys(String fac) {
		int option = 1, i;
		String dep_remove = "";
		ArrayList<String> dep_fac = null;
		while (option != 0) {
			try {
				dep_fac = rmi.dep_fac(fac);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			i = 0;
			out.println(
					"Departments avaible from this faculty to be removed(This action will also remove the department of everyone/tables with the same one):\n");
			for (int j = 0; j < 2; j++) {
				i = 0;
				for (String x : dep_fac) {
					if (j == 0) {
						++i;
						out.print(i + "-" + x.toString() + "\n");
					}
					if (j == 1) {
						++i;
						if (i == option) {
							dep_remove = x;
						}
					}
				}
				if (i == 0) {
					out.print("No more departments avaible to be deleted\n");
					option = 0;
					j++;
				}
				if (j == 0 && i != 0) {
					out.print("0-Exit");
					option = testOption(0, i);
				}
			}
			if (option != 0) {
				try {
					// tirar as mesas associadas e pessoas?
					if (rmi.dep_fac(fac).contains(dep_remove)) {
						rmi.del_person_dep(dep_remove);
						rmi.del_dep_fac(dep_remove);
						out.println("Department deleted from faculty");
					} else {
						out.println("Department is no longer in this faculty");
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String print_pessoas(ArrayList<Pessoa> print_pessoa) {
		int i = 0;
		String output = "";
		for (Pessoa x : print_pessoa) {
			output += ++i + "-" + x + "\n";
		}
		return output;
	}

	public static void apaga_listas(String tipo, ArrayList<Lista_candidata> lists) {
		for (Lista_candidata x : lists) {
			if (!x.classe.equals(tipo)) {
				try {
					rmi.delete_list(x.id);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				out.println("List:" + x.nome_lista + " from class:" + x.classe + " deleted from election");
			}
		}
	}

	public static boolean search_table(String name) {
		try {
			ArrayList<String> tabs = rmi.tables();
			for (String x : tabs) {
				if (x.equals(name)) {
					return false;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static boolean search_election(String name) {
		try {
			ArrayList<Eleicoes> elections = rmi.return_eleicoes();
			for (Eleicoes x : elections) {
				if (x.titulo.equals(name)) {
					return false;
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}
}
