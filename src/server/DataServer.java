package server;
import java.io.*;
import static java.lang.System.out;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
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
    
    public static void load_config(){
		String file = "DataServerConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding DataServer configurations...");
		try{
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for(int i=0;i<1;i++){
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line,"=");
				line = tokenizer.nextToken();
				if(line.equals("RMI Registry")){
					rmiRegistry = Integer.parseInt(tokenizer.nextToken());
				}
			}
			buffer.close();
		}catch(FileNotFoundException e){
			System.out.println("File "+file+" not found");
			System.exit(0);
		}catch(IOException e){}
		System.out.println("DataServerConfig.txt successfully uploaded.");
	}

    public synchronized void regista_pessoa_teste_inputs() {
        out.print("Nome:");
        String nome = inputS.nextLine();
        out.print("Cargo:");
        String cargo = inputS.nextLine();
        out.print("Password:");
        String password = inputS.nextLine();
        out.print("Departamento:");
        String departamento = inputS.nextLine();
        out.print("Faculdade:");
        String faculdade = inputS.nextLine();
        out.print("telefone:");
        int telefone = inputI.nextInt();
        out.print("numero cartao unico:");
        int numero_cc = inputI.nextInt();
        out.print("validade cartao:");
        String validade_cc = inputS.nextLine();
        Pessoa nova_pessoa = new Pessoa(nome, cargo, password, departamento, faculdade, telefone, numero_cc, validade_cc);
        pessoas.add(nova_pessoa);
    }
    public synchronized boolean search_faculty(String name) {
        for (Faculty x : faculdades) {
            if (x.name.equals(name)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void create_faculty() {
        out.print("Name of new faculty:");
        String name = inputS.nextLine();
        while (search_faculty(name) == false) {
            out.print("Name already in use chose a new one:");
            name = inputS.nextLine();
        }
        Faculty new_faculty = new Faculty(name);
        faculdades.add(new_faculty);
    }

    public synchronized void print_facultys() {
        int i = 1;
        for (Faculty x : faculdades) {
            out.print(i + "-" + x.toString());
            i++;
        }
    }

    public synchronized boolean search_department(String name) {
        for (Department x : departments) {
            if (x.name.equals(name)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void assing_departments_to_facultys() {
        int i = 1, check = 0;
        out.print("chose the faculty that you want to add departments:\n");
        print_facultys();
        int option = inputI.nextInt();
        Faculty faculty_chosen = faculdades.get(option - 1);
        for (int j=0; j <2; j++) {
            i=0;
            if(j==0){
                out.print("Departments avaible to be added:\n");
            }
            for (Department k : departments) {
                check=0;
                for (Faculty x : faculdades) {
                    if (x.departments.contains(k)) {
                        check = 1;
                    }
                }
                if (check == 0 && j==0) {
                    ++i;
                    out.print(i +"-" + k.name+ "\n");
                }
                if(check==0 && j==1){
                    ++i;
                    if(i==option){
                        faculty_chosen.departments.add(k);
                    }
                }

            }
            if(j==0){
                option= inputI.nextInt();
            }
        }
    }

    public synchronized void print_departments() {
        int i = 1;
        for (Department x : departments) {
            out.print(i + "-" + x.name+ "\n");
            i++;
        }
    }

    public synchronized void create_department() {
        out.print("Name of new department:");
        String name = inputS.nextLine();
        while (search_department(name) == false) {
            out.print("Name already in use chose a new one:");
            name = inputS.nextLine();
        }
        Department new_department = new Department(name);
        departments.add(new_department);
    }

    public synchronized void imprime_pessoas() {
        for (Pessoa x : pessoas) {
            System.out.print(x.toString() + "\n");
        }
    }

    //ponto 2
    public synchronized void insercao() {
        //mesma coisa que modificaçao???
    }

    public synchronized void mudificacao(int num, String facul, String depart) {
        for (Pessoa x : pessoas) {
            if (x.numero_cc == num) {
                x.faculdade = facul;
                x.departamento = depart;
            }
        }
    }

    public synchronized void remocao(int num, String facul, String depart) {
        for (Pessoa x : pessoas) {
            if (x.numero_cc == num) {
                if (facul.equals("remove")) {
                    x.faculdade = null;
                }
                if (depart.equals("remove")) {
                    x.departamento = null;
                }
            }
        }
    }

    public synchronized Lista cria_listas(String tipo, HashMap<String, ArrayList<Lista_candidata>> listas) { //hashmap
        HashMap<String, HashMap<String, Integer>> temporaria = null;
        HashMap<String, Integer> temporaria1 = null;
        if (tipo.equals("nucleo")) {
            /* Lista nova_lista = new Lista(listas.get("nucleo"), null, null);//modificar depois para hashmap muddar tudo para hashmap por causa dos votos
             return nova_lista;*/
        } else if (tipo.equals("geral")) {
            for (HashMap.Entry<String, ArrayList<Lista_candidata>> entry : listas.entrySet()) {
                for (Lista_candidata x : entry.getValue()) {
                    temporaria1.put(entry.getKey(), 0);
                }
                temporaria.put(entry.getKey(), temporaria1);
                temporaria1.clear();

            }
            Lista nova_lista = new Lista(temporaria);
            return nova_lista;
        } else {
            System.out.print("nao da");
        }
        return null;
    }

    public synchronized void cria_eleicao(String tipo, String inicio, String fim, String titulo, String resumo, HashMap<String, ArrayList<Lista_candidata>> listas) {
        Eleicoes nova_eleicao = new Eleicoes(tipo, inicio, fim, titulo, resumo, cria_listas(tipo, listas), null, 0, 0);
        eleitores.add(nova_eleicao);

    }

    //4
    public synchronized void adiciona_remove(String eleicao_titulo, String tipo, HashMap<String, ArrayList<Lista_candidata>> muda) {
        for (Eleicoes x : eleitores) {
            if (x.titulo.equals(eleicao_titulo)) {
                for (HashMap.Entry<String, ArrayList<Lista_candidata>> entry : muda.entrySet()) { //segurança para nao adicionar a outro que nao sejas do mesmo tipo
                    if (tipo.equals("adiciona")) {
                        for (Lista_candidata k : entry.getValue()) {
                            if (entry.getValue() != null) {
                                if (entry.getKey().equals("estudantes")) {
                                    x.listas.todas_listas.get("estudantes").put(k.nome_lista, 0);//por segurança para nao por lista erradas como no caso em baixo
                                }
                            }
                            if (x.tipo.equals("geral")) {
                                if (entry.getKey().equals("docentes")) {
                                    x.listas.todas_listas.get("docentes").put(k.nome_lista, 0); //ver se nao existe ja a mesma eleicao
                                } else if (entry.getKey().equals("funcionarios")) {
                                    x.listas.todas_listas.get("funcionarios").put(k.nome_lista, 0);
                                } else {
                                    System.out.print("off");
                                }

                            } else if (tipo.equals("remove")) {
                                if (entry.getValue() != null) {
                                    if (entry.getKey().equals("estudantes")) {
                                        x.listas.todas_listas.get("estudantes").remove(k.nome_lista, 0);
                                    }
                                    if (x.tipo.equals("geral")) {
                                        if (entry.getKey().equals("docentes")) {
                                            x.listas.todas_listas.get("docentes").remove(k.nome_lista, 0); //ver se nao existe ja a mesma eleicao
                                        } else if (entry.getKey().equals("funcionarios")) {
                                            x.listas.todas_listas.get("funcionarios").remove(k.nome_lista, 0);
                                        } else {
                                            System.out.print("off");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    //funcao adicional ir buscar eleiçao

    public synchronized Eleicoes procura_eleicao(String titulo_eleicao) {
        for (Eleicoes x : eleitores) {
            if (x.titulo.equals(titulo_eleicao)) {
                return x;
            }
        }
        return null;
    }

    public synchronized Mesas procura_mesas(String departamento) {
        for (Mesas t : mesas_votos_todas) {
            if (t.departamento.equals(departamento)) {
                return t;
            }
        }
        return null;

    }

    //ponto5
    public synchronized void adiciona_mesas(String titulo_eleicao, String departamento) { //falta seguranças á força toda
        Eleicoes x = procura_eleicao(titulo_eleicao);
        if (x == null) {
            System.out.print("falhou");
        }
        Mesas check_mesa = procura_mesas(departamento);
        if (check_mesa != null) {
            if (!x.mesas_votos.contains(check_mesa)) {
                x.mesas_votos.add(check_mesa);
            }
        } else {
            Mesas nova_mesa = new Mesas(departamento);
            mesas_votos_todas.add(nova_mesa);
            Mesas check_nova = procura_mesas(departamento);
            x.mesas_votos.add(check_nova);
        }
    }

    //ponto9 //depois alterar para a ediçao so poder ser feita antes do inicio falta as listas e as mesas
    public synchronized void altera_eleicao(String titulo_eleicao, String novo_titulo, String novo_inicio, String novo_fim, String novo_tipo, String novo_resumo) {
        Eleicoes x = procura_eleicao(titulo_eleicao);
        x.inicio = novo_inicio;
        x.fim = novo_fim;
        x.titulo = novo_titulo;
        x.tipo = novo_tipo;
        x.resumo = novo_resumo;

    }

    public synchronized Pessoa procura_pessoa(int numero) {
        for (Pessoa x : pessoas) {
            if (x.numero_cc == numero) {
                return x;
            }
        }
        return null;
    }

    public synchronized void regista_eleitores_local(String titulo_eleicao, int numero_cc, Mesas mesa_voto) { //por a adicionar as eleicoes á lista dos votadores
        Eleicoes x = procura_eleicao(titulo_eleicao);
        Pessoa pessoa_vota = procura_pessoa(numero_cc);
        regista_pessoas.get(x).put(pessoa_vota, mesa_voto);
    }

    //12
    public synchronized void mostra_numero(String titulo_eleicao) {
        Eleicoes x = procura_eleicao(titulo_eleicao);
        int contador = 0;
        for (int i = 0; i < x.mesas_votos.size(); i++) {
            contador = 0;
            for (HashMap.Entry<Pessoa, Mesas> entry : regista_pessoas.get(x).entrySet()) {
                if (entry.getValue().equals(x.mesas_votos.get(i))) {
                    contador += 1;
                }
            }
            System.out.print("Na mesa:" + x.mesas_votos.get(i) + " votaram" + contador + " pessoa/s");
        }

    }

    public synchronized void remove_mesa(String titulo_eleicao, Mesas mesa_remove) {
        Eleicoes x = procura_eleicao(titulo_eleicao);
        x.mesas_votos.remove(mesa_remove);
    }

    //13
    public synchronized void termina_eleicao(Eleicoes eleicao) {

    }
    
    //criar uma cena que automatiza o encontro da eleicao
    @Override
    public synchronized String sayHello(String username, String password) throws RemoteException {
        System.out.println(username+"\n"+password);

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
