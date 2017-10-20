//package hello2;
package server;

import static java.lang.System.out;

import java.io.BufferedReader;
import java.io.*;
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
		String write_date, name = null;
		int option,check,j;
		SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy");
        Date write_date_format = new Date(0, 0, 0);

		int numero;
		while (true) {
            out.print("-------------MENU------------- " //criar mesas? criar listas?
                    + "\n1-Criar Pessoa "
                    + "\n2-Criar Faculdade \n"
                    + "3-Criar Departamento\n"
                    + "4-Criar Eleiçao\n"
                    + "5-Create tables\n"
                    + "6-Apagar Pessoa\n"
                    + "7-Apagar Faculdade\n"
                    + "8-Apagar departamento\n"
                    + "9-Apagar Eleiçao \n"
                    + "10-Delete tables\n"
                    + "11-alterar dados de uma pessoa\n"
                    + "12-Alterar dados de uma faculdade\n"
                    + "13-Alterar dados de um departamento\n"
                    + "14-Alterar dados de uma eleiçao\n"
                    + "15-Alterar dados de uma mesa\n"
                    + "16-Listar Eleiçoes\n"
                    + "17-Listar eleiçoes a decorrer\n"
                    + "18-Listar eleiçoes acabadas\n"
                    + "19-Listar Pessoas\n"
                    + "20-Listar faculdade\n"
                    + "21-Listar departamentos\n"
                    + "22-Listar mesas\n"
                    + "23-Exit\n"
                    + "Escolha:");
            numero = testOption(1, 23);
            switch (numero) {
                case 1:
                     int number_cc,
                     phone_int;
                     String phone = "",
                     number_cc_string = "";
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
                                    write_date_format = dt.parse(write_date);
                                    option = 0;
                                    Date date = new Date();
                                    if(date.after(write_date_format)){
                                        System.out.println("Date1 is after Date2");
                                        option=1;
                                    }
                                } catch (ParseException e) {
                                    out.println("Error in date format!\n");
                                }
                            }
                            rmi.add_pessoas(name, cargo, password, departamento, chosen_faculty, phone_int, number_cc, write_date_format);
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
                    do {
                        title = inputS.nextLine();
                        while (rmi.search_election(title) == false) {
                            out.print("Name already in use chose a new one:");
                            do {
                                name = inputS.nextLine();
                            } while (!checkStringWithNumbers(name));
                        }
                    } while (!checkStringWithNumbers(title));
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
                        lists.put("students", create_lists_election("students"));
                        if (type.equals("geral")) {
                            lists.put("teachers", create_lists_election("teachers"));
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
                                rmi.cria_listas(lists), election_tables, 0, 0);
                        rmi.addNewElection(new_election);
                        out.print("Election added\n");
                    }
                    break;
                case 5:
                    out.print("Name of the table:\n");
                    do {
                        name = inputS.nextLine();
                    } while (!checkStringWithNumbers(name));
                    while (rmi.search_table(name) == false) {
                        name = inputS.nextLine();
                    }
                    int i;
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
                                    if (k.departamento.equals(x)) { //esta a dar erro por causa do null ver tudo o que esta a ser comparado com possiveis null
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
                                }
                            }
                        }
                        if (j == 0 && i != 0) {
                            out.print("0-Exit");
                            option = testOption(0, i);
                        }
                        if (i == 0) {
                            j++;
                            out.print("No departments avaible to be atributed to table:\n1-Add table without department\n0-Exit");
                            option = testOption(0, 1);
                            if (option == 1) {
                                rmi.add_table(name, null);
                                out.print("Table was created without department\n");
                            }
                        }
                    }
                    break;
                case 6:
                	ArrayList<Pessoa> person = rmi.return_pessoa();
                    if (person.isEmpty()) {
                        out.print("There is no people to delete\n");
                    } else {
                        option = 1;
                        while (option != 0 && !person.isEmpty()) {
                            System.out.println(rmi.imprime_pessoas());
                            out.print("0-Exit");
                            option = testOption(0, person.size());
                            if (option != 0) {
                            	out.print(rmi.removePerson(person.get(option - 1).numero_cc));
                            }
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
                            for (i = 0; i < 2; i++) {
                                j = 0;
                                if (i == 0) {
                                    out.print("Facultys avaible to de be deleted:\n");
                                }
                                for (Faculty x : facultys) {
                                    check = 0;
                                    for (Pessoa k : rmi.return_pessoa()) { //aqui pode ra dar asneiras se a faculdade for null testar
                                        if (k.faculdade != null) {
                                            if (k.faculdade.equals(x)) {
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
                                rmi.remove_faculty(remove_fac.name);
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
                            for (i = 0; i < 2; i++) {
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
                                    for (Mesas d : rmi.return_mesas()) {
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
                                rmi.removeDepartment(remove_dep.name);
                                out.print("Department removed \n");
                            }
 
                        }
                    }
                    break;
                case 9:
                	ArrayList<Eleicoes> eleicoes = rmi.return_elections();
                    if (eleicoes.isEmpty()) {
                        out.print("Theres is no elections avaible\n");
                    } else {
                        out.print(rmi.print_elections());
                        out.print("0-Exit");
                        option = testOption(0, eleicoes.size());
                        if (option != 0) {
                            rmi.remove_election(eleicoes.get(option - 1).titulo);
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
                                rmi.removeMesaVoto(delete_table.name);
                                out.print("Table removed \n");
                            }
 
                        }
                    }
                    break;
                case 11:
                    if (rmi.return_pessoa().isEmpty()) {
                        out.print("Theres is no persons avaible\n");
                    } else {
                        rmi.imprime_pessoas();
                        out.print("0-Exit");
                        option = testOption(0, rmi.return_pessoa().size());
                        if (option != 0) {
                            Pessoa select_person = rmi.return_pessoa().get(option - 1);
                            while (option != 0) {
                                out.print("select the field you want to change:\n"
                                        + "1-Change name\n"
                                        + "2-Change job\n"
                                        + "3-Change faculty\n"
                                        + "4-Change department\n"
                                        + "5-Change password\n"
                                        + "6-Change phone number\n"
                                        + "7-Change cc number\n"
                                        + "8-Change expiration date of cc\n"
                                        + "0-Exit\n"
                                        + "Chose:");
                                option = testOption(0, 8);
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
                                        out.print("Name updated\n");
                                        break;
                                    case 2:
                                        out.print("Current position: " + select_person.cargo + "\nNew job:\n1-student\n2-teacher\n3-staff\n0-Exit");
                                        option = testOption(0, 3);
                                        if (option != 0) {
                                            if (option == 1) {
                                                select_person.cargo = "student";
                                            } else if (option == 2) {
                                                select_person.cargo = "teacher";
                                            } else if (option == 3) {
                                                select_person.cargo = "staff";
                                            }
                                            out.print("Job updated\n");
                                        }
                                        break;
                                    case 3:
                                        out.print("Current faculty: " + select_person.faculdade.name + "\nNew faculty(it will also afect the department):\n");
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
                                                select_person.departamento = new_faculty.departments.get(inputI.nextInt() - 1);
                                                out.print("Faculty and department updated:\n");
                                            }
                                        }
                                        break;
                                    case 4:
                                        out.print("Current department: " + select_person.departamento.toString() + "\nDepartments avaible:\n");
                                        out.print(rmi.print_department_faculty(select_person.faculdade));
                                        out.print("0-Exit");
                                        option = testOption(0, select_person.faculdade.departments.size());
                                        if (option != 0) {
                                            select_person.departamento = select_person.faculdade.departments.get(option - 1);
                                            out.print("Department updated\n");
                                        }
                                        break;
                                    case 5:
                                        out.print("Current password: " + select_person.password + "\nNew password:\n");
                                        select_person.password = inputS.nextLine();
                                        out.print("password updated\n");
                                        break;
                                    case 6:
                                        phone = "";
                                        do {
                                            out.print("Currente phone number: +" + select_person.telefone + "\nNew phone number with 9 digits:");
                                            try {
                                                phone = inputS.nextLine();
                                            } catch (InputMismatchException e) {
                                                out.println("invalid option");
                                                inputI.next();
                                            }
                                        } while (!checkNumbers(phone) || phone.length() != 9);
                                        phone_int = Integer.parseInt(phone);
                                        select_person.telefone = phone_int;
                                        out.print("phone number updated\n");
                                        break;
                                    case 7:
                                        number_cc_string = "";
                                        do {
                                            do {
                                                out.print("Current cc number: " + select_person.numero_cc + "\nNumber of cc(8 digits):");
                                                try {
                                                    number_cc_string = inputS.nextLine();
                                                } catch (InputMismatchException e) {
                                                    out.println("Invalid option");
                                                    inputI.next();
                                                }
                                            } while (!checkNumbers(number_cc_string) || number_cc_string.length() != 8);
                                            number_cc = Integer.parseInt(number_cc_string);
                                        } while (!checkCCnumber(number_cc, rmi.return_pessoa()) || number_cc != select_person.numero_cc);
                                        out.print("cc number updated\n");
                                        break;
                                    case 8:
                                        while (option != 0) {
                                            try {
                                                out.println("Current expiration date: " + select_person.validade_cc + "\nNew expiration date of cc(dd-mm-yyyy):");
                                                write_date = inputS.nextLine();
                                                write_date_format = dt.parse(write_date);
                                                option = 0;
                                            } catch (ParseException e) {
                                                out.println("Error in date format!\n");
                                            }
                                        }
                                        out.print("Expiration date updated\n");
                                        break;
                                    case 9:
                                        option = 0;
                                        break;
                                    default:
                                        out.print("\noption not avaible\n");
                                }
                            }
                        }
                    }
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
                        assing_departments_to_facultys(chosen_faculty);
                    } else if (opcao == 3) {
                        remove_departments_from_facultys(chosen_faculty);
                    }
                    break;
                case 13:
                    String new_name;
                    if (rmi.return_departments().isEmpty()) {
                        out.print("Theres is no departments avaible\n");
                    } else {
                        out.print("Chose department you want to change the name:\n");
                        out.print(rmi.print_departments());
                        out.print("0-Exit");
                        option = testOption(0, rmi.return_departments().size());
                        if (option != 0) {
                            out.print("New name of department:");
                            do {
                                new_name = inputS.nextLine();
                            } while (!checkStringWithNumbers(new_name));
                            rmi.return_departments().get(option - 1).name = new_name;
                        }
                    }
                    break;
                case 14:
                    int option2;
                    String write;
                    if (rmi.return_elections().isEmpty()) {
                        out.print("Theres is no elections avaible\n");
                    } else {
                        out.print("Elections:\n");
                        rmi.print_election2();
                        out.print("\n0-Exit\n");
                        out.print("Chose:");
                        option = testOption(0, rmi.return_elections().size());
                        if (option != 0) {
                            Eleicoes election = rmi.return_elections().get(option - 1);
                            while (option != 0) {
                                out.print("select the field you want to change:\n"
                                        + "1-Change type\n"
                                        + "2-Change starting date\n"
                                        + "3-Change ending date\n"
                                        + "4-Change title\n"
                                        + "5-Change resume\n"
                                        + "6-Change lists\n"
                                        + "7-Change voting tables\n"
                                        + "0-Exit");
                                option = testOption(0, 7);
                                switch (option) {
                                    case 0:
                                        option = 0;
                                        break;
                                    case 1:
                                        out.print("Current type: " + election.tipo + "\nChange to:\n1-nucleo\n2-geral\n0-Exit\n");
                                        option2 = testOption(0, 2);
                                        if (option2 == 1) {
                                            election.tipo = "nucleo";
                                        } else if (option2 == 2) {
                                            election.tipo = "geral";
                                        }
                                        out.print("Type updated\n");
                                        break;
                                    case 2:
                                        option2 = 1;
                                        while (option2 != 0) {
                                            try {
                                                out.print("Current starting date: " + election.start + "\nNew starting date(hh:mm:ss dd-mm-yyyy):\n");
                                                write = inputS.nextLine();
                                                write_date_format = dt.parse(write);
                                                if (election.end.after(write_date_format)) {
                                                    option2 = 0;
                                                } else {
                                                    out.print("End date can not be set to before ou equal to start date.");
                                                }
                                            } catch (ParseException e) {
                                                out.println("Erro no formato da data!\n");
                                            }
                                        }
                                        election.start = write_date_format;
                                        out.print("Starting date updated\n");
                                        break;
                                    case 3:
                                        option2 = 1;
                                        while (option2 != 0) {
                                            try {
                                                out.print("Current ending date: " + election.end + "\nNew ending date(hh:mm:ss dd-mm-yyyy):");
                                                write = inputS.nextLine();
                                                write_date_format = dt.parse(write);
                                                if (write_date_format.after(election.start)) {
                                                    option2 = 0;
                                                } else {
                                                    out.print("End date can not be set to before ou equal to start date.\n");
                                                }
                                            } catch (ParseException e) {
                                                out.println("Erro no formato da data!\n");
                                            }
                                        }
                                        election.end = write_date_format;
                                        out.print("Ending date updated\n");
                                        break;
                                    case 4:
                                        out.print("Current title of election: " + election.titulo + "\nNew title of election:");
                                        do {
                                            write = inputS.nextLine();
                                        } while (!checkStringWithNumbers(write)); //por protecao para o titulo nao ser igual a nenhum
                                        election.titulo = write;
                                        out.print("Title updated\n");
                                        break;
                                    case 5:
                                        out.print("Current resume of election: " + election.resumo + "\nNew resume:");
                                        election.resumo = inputS.nextLine();
                                        out.print("Resume updated\n");
                                        break;
                                    case 6:
                                        out.print("1-Add list\n2-Remove list\n0-Exit\n");//por am ostrar as listas //por a tirar listas tambem
                                        option2 = testOption(0, 2);
                                        if (option2 == 1) {
                                            add_lists_election("students", election.listas.todas_listas);
                                            if (election.tipo.equals("geral")) {
                                                add_lists_election("teachers", election.listas.todas_listas);
                                                add_lists_election("staff", election.listas.todas_listas);
                                            }
                                        } else if (option2 == 2) {
                                            for (HashMap.Entry<String, HashMap<Lista_candidata, Integer>> entry : election.listas.todas_listas.entrySet()) {
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
                                        break;
 
                                    case 7://mostrar as mesas//tirar mesas
                                        check = 0;
                                        j = 0;
                                        out.print("1-Remove table\n2-Add table\n0-Exit\n");
                                        option2 = testOption(0, 2);
                                        if (option2 == 1) {
                                            if (election.mesas_votos.isEmpty()) {
                                                out.print("No tables avaible to be removes\n");
                                            } else {
                                                {
                                                    while (option2 != 0) {
                                                        j = 0;
                                                        for (Mesas k : election.mesas_votos) {
                                                            ++j;
                                                            out.print(j + "-" + k.name);
                                                        }
                                                        if (j == 0) {
                                                            option2 = 0;
                                                            out.print("No more tables avaible to be removed\n");
                                                        } else {
                                                            out.print("0-Exit");
                                                            option2 = testOption(0, j);
                                                            if (option2 != 0) {
                                                                election.mesas_votos.remove(election.mesas_votos.get(option2 - 1));
                                                                out.print("Table removed\n");
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (option2 == 2) {
                                            if (rmi.return_mesas().isEmpty()) {
                                                out.print("No tables avaible to be added\n");
                                            } else {
                                                while (option2 != 0) {
                                                    for (i = 0; i < 2; i++) {
                                                        j = 0;
                                                        for (Mesas x : rmi.return_mesas()) {
                                                            check = 0;
                                                            if (x.departamento != null) {
                                                                if (election.mesas_votos.contains(x)) {
                                                                    check = 1;
                                                                }
                                                            }
                                                            if (check == 0 && i == 0) {
                                                                ++j;
                                                                out.print(j + "-" + x.name + "\n");
                                                            }
                                                            if (check == 0 && i == 1) {
                                                                ++j;
                                                                if (j == option2) {
                                                                    election.mesas_votos.add(x);
                                                                    out.print("Table added to election\n");
                                                                }
                                                            }
                                                        }
                                                        if (i == 0 && j != 0) {
                                                            out.print("0-Exit");
                                                            option2 = testOption(0, j);
                                                        }
                                                        if (j == 0) {
                                                            out.print("No more tables avaible to de added\n");
                                                            option2 = 0;
                                                            i++;
                                                        }
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
                    }
                    break;
                case 15:
                    if (rmi.return_mesas().isEmpty()) {
                        out.print("Theres is no tables avaible\n");
                    } else {
                        out.print("Chose table:\n");
                        out.print(rmi.print_tables());
                        out.print("0-Exit");
                        option = testOption(0, rmi.return_mesas().size());
                        if (option != 0) {
                            Mesas table_change = rmi.return_mesas().get(option - 1);
                            out.print("Change:\n1-Name of table\n2-Change department where is located\n0-Exit");
                            option = testOption(0, 2);
                            if (option != 0) {
                                if (option == 1) {
                                    do {
                                        out.print("New name:");
                                        name = inputS.nextLine();
                                    } while (!checkStringWithNumbers(name));
                                    table_change.name = name;
                                } else if (option == 2) {
                                    out.print("Chose new department:");
                                    out.print(rmi.print_departments());
                                    out.print("0-Exit");
                                    option = testOption(0, rmi.return_departments().size());
                                    if (option != 0) {
                                        table_change.departamento = rmi.return_departments().get(option - 1);
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 16:
                    if (rmi.return_elections().isEmpty()) {
                        out.println("Lista vazia");
                    } else {
                        System.out.println(rmi.print_elections());
                    }
                    break;
                case 17:
                    break;
                case 18:
                    break;
                case 19:
                    if (rmi.return_pessoa().isEmpty()) {
                        out.print("Theres is no persons avaible\n");
                    } else {
                        out.print(rmi.imprime_pessoas());
                    }
                    break;
                case 20:
                    if (rmi.return_facultys().isEmpty()) {
                        out.print("Theres is no facultys avaible\n");
                    } else {
                        out.print(rmi.print_facultys());
                    }
                    break;
                case 21:
                    if (rmi.return_departments().isEmpty()) {
                        out.print("Theres is no departments avaible\n");
                    } else {
                        out.print(rmi.print_departments());
                    }
                    break;
                case 22:
                    if (rmi.return_mesas().isEmpty()) {
                        out.print("Theres is no tables avaible\n");
                    } else {
                        out.print(rmi.print_tables());
                    }
                    break;
                case 23:
                    System.exit(0);
                default:
                    out.print(
                            "\nOpçao invalida\n");
            }
        }

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
            if ((int) array[0] == 32 || ((int) array[i] > 90 && (int) array[i] < 97) || (int) array[array.length - 1] == 32 || (((int) array[i] > 122 || (int) array[i] < 65) && (int) array[i] != 32)) {
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
            if ((int) array[0] == 32 || (int) array[array.length - 1] == 32 || ((int) array[i] > 90 && (int) array[i] < 97) || ((((int) array[i] > 122 || (int) array[i] < 65) && ((int) array[i] > 57 || (int) array[i] < 48)) && (int) array[i] != 32)) {
                out.println("Nao pode comecar com um espaco nem acabar com um espaco e só pode conter letras e numeros!");
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
 
    public static void add_lists_election(String type, HashMap<String, HashMap<Lista_candidata, Integer>> todas_listas) {
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
                        if (delete.equals(entry2.getKey().nome_lista)) {
                            delete_list = entry2.getKey();
                        }
                    }
                }
                if (j == 0 && i != 0) {
                    out.print("0-Exit");
                    out.print("Name of the list you want to delete(type done when finished):"); //por proteçao para se puseram um nome que nao pertence
                    delete = inputS.nextLine();
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
 
    public static ArrayList create_lists_election(String type) {
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
 
    public static void assing_departments_to_facultys(Faculty faculty_chosen) throws RemoteException {
        int i = 1, check = 0, option = 0;
        for (int j = 0; j < 2; j++) {
            i = 0;
            if (j == 0) {
                out.print("Departments avaible to be added:\n");
            }
            for (Department k : rmi.return_departments()) {
                check = 0;
                for (Faculty x : rmi.return_facultys()) {
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
            if (i == 0) {
                out.print("No more departments avaible to be added");
                j++;
            }
            if (j == 0 && i != 0) {
                out.print("0-Exit");
                option = testOption(0, i);
            }
        }
    }
 
    public static void remove_departments_from_facultys(Faculty faculty_chosen) throws RemoteException {
        int option = 1, i, check = 0;
        Department delete_department = null;
        while (option != 0) {
            i = 0;
            out.print("Departments avaible from this faculty to be removed:\n");
            for (int j = 0; j < 2; j++) {
                i = 0;
                for (Department x : faculty_chosen.departments) {
                    check = 0;
                    for (Pessoa k : rmi.return_pessoa()) {
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
                        if (i == option) {
                            delete_department = x;
                        }
                    }
 
                }
                if (i == 0) {
                    out.print("No more departments avaible to be added");
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
