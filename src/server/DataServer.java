package server;
import java.io.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class DataServer extends UnicastRemoteObject implements RMI {
	public static int rmiRegistry;
    public static ArrayList<Pessoa> pessoas;
    public static ArrayList<Eleicoes> eleitores;
    public static ArrayList<Mesas> mesas_votos_todas;

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

    public synchronized void regista_pessoa(String nome, String cargo, String password, String departamento, String faculdade, int telefone, int numero_cc, String validade_cc) throws RemoteException{
        Pessoa nova_pessoa = new Pessoa(nome, cargo, password, departamento, faculdade, telefone, numero_cc, validade_cc);
        pessoas.add(nova_pessoa);

    }

    //ponto 2
    public synchronized void insercao() throws RemoteException{
        //mesma coisa que modificaçao???
    }

    public synchronized void mudificacao(int num, String facul, String depart) throws RemoteException{
        for (Pessoa x : pessoas) {
            if (x.numero_cc == num) {
                x.faculdade = facul;
                x.departamento = depart;
            }
        }
    }

    public synchronized void remocao(int num, String facul, String depart) throws RemoteException{
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

    public synchronized Lista cria_listas(HashMap<String, ArrayList> listas) throws RemoteException{ //hashmap
        if (listas.get(listas.keySet().toArray()[0]).equals("nucleo")) {
            Lista nova_lista = new Lista(listas.get("nucleo"), null, null);//modificar depois para hashmap
            return nova_lista;
        } else if (listas.get(listas.keySet().toArray()[0]).equals("estudante") || listas.get(listas.keySet().toArray()[0]).equals("funcionario") || listas.get(listas.keySet().toArray()[0]).equals("docente")) {
            Lista nova_lista = new Lista(listas.get("estudante"), listas.get("funcionario"), listas.get("docente"));
            return nova_lista;
        } else {
            System.out.print("nao da");
        }
        return null;
    }

    public synchronized void cria_eleicao(String tipo, String inicio, String fim, String titulo, String resumo, HashMap<String, ArrayList> listas) throws RemoteException{
        Eleicoes nova_eleicao = new Eleicoes(tipo, inicio, fim, titulo, resumo, cria_listas(listas), null);
        eleitores.add(nova_eleicao);

    }

    //4
   /* 
     public void adiciona_remove(String eleicao_titulo, String tipo, HashMap<String, ArrayList> muda) throws RemoteException{
        for (Eleicoes x : eleitores) {
            if (x.titulo.equals(eleicao_titulo)) {
                for (HashMap.Entry<String, ArrayList> entry : muda.entrySet()) { //segurança para nao adicionar a outro que nao sejas do mesmo tipo
                    if (tipo.equals("adiciona")) {
                        if (entry.getValue() != null) {
                            if (entry.getKey().equals("estudantes")) {
                                x.listas.estudantes.add(entry.getValue());
                            }
                            if (x.tipo.equals("geral")) {
                                if (entry.getKey().equals("docentes")) {
                                    x.listas.docentes.add(entry.getValue()); //ver se nao existe ja a mesma eleicao
                                } else if (entry.getKey().equals("funcionarios")) {
                                    x.listas.funcionarios.add(entry.getValue());
                                } else {
                                    System.out.print("off");
                                }
                            }
                        }
                    } else if (tipo.equals("remove")) {
                        if (entry.getValue() != null) {
                            if (entry.getKey().equals("estudantes")) {
                                x.listas.estudantes.remove(entry.getValue());
                            }
                            if (x.tipo.equals("geral")) {
                                if (entry.getKey().equals("docentes")) {
                                    x.listas.docentes.remove(entry.getValue()); //ver se nao existe ja a mesma eleicao
                                } else if (entry.getKey().equals("funcionarios")) {
                                    x.listas.funcionarios.remove(entry.getValue());
                                } else {
                                    System.out.print("off");
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
    //funcao adicional ir buscar eleiçao

    public synchronized Eleicoes procura_eleicao(String titulo_eleicao) throws RemoteException {
        for (Eleicoes x : eleitores) {
            if (x.titulo.equals(titulo_eleicao)) {
                return x;
            }
        }
        return null;
    }

    public synchronized Mesas procura_mesas(String departamento) throws RemoteException{
        for (Mesas t : mesas_votos_todas) {
            if (t.departamento.equals(departamento)) {
                return t;
            }
        }
        return null;

    }

    //ponto5
    public synchronized void adiciona_mesas(String titulo_eleicao, String departamento) throws RemoteException{ //falta seguranças á força toda
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
    public synchronized void altera_eleicao(String titulo_eleicao, String novo_titulo, String novo_inicio, String novo_fim, String novo_tipo, String novo_resumo) throws RemoteException{
        Eleicoes x = procura_eleicao(titulo_eleicao);
        x.inicio = novo_inicio;
        x.fim = novo_fim;
        x.titulo = novo_titulo;
        x.tipo = novo_tipo;
        x.resumo = novo_resumo;
        
    }

    public synchronized void remove_mesa(String titulo_eleicao, Mesas mesa_remove)  throws RemoteException{
        Eleicoes x = procura_eleicao(titulo_eleicao);
        x.mesas_votos.remove(mesa_remove);
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
