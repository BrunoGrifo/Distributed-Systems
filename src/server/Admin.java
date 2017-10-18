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
import java.util.Scanner;
import java.util.StringTokenizer;

public class Admin {
	public static RMI rmi=null;
	public static String rmiHost;
	public static int rmiPort;
	
	public static void carregaServerConfig(){
		String file = "AdminConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Server configurations...");
		try{
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for(int i=0;i<2;i++){
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line,"=");
				line = tokenizer.nextToken();
				if(line.equals("RMI Host")){
					rmiHost = tokenizer.nextToken();
				}else if(line.equals("RMI Port")){
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				}
			}
			buffer.close();
		}catch(FileNotFoundException e){
			System.out.println("File "+file+" not found");
			System.exit(0);
		}catch(IOException e){}
		catch(NullPointerException e){
			System.out.println("Erro a carregar os dados do ficheiro");
		}

	}
    public static void main(String args[]) throws RemoteException {
        carregaServerConfig();
        System.out.println(rmiHost);
        System.out.println(rmiPort);
        
        try{
        	rmi = (RMI) Naming.lookup("rmi://"+rmiHost+":"+rmiPort+"/rmi");
        }catch(IOException e){System.out.println("IO:" + e);
        }catch(NotBoundException e){
        	System.out.println("Error no lookup method");
        	System.exit(0);
        }

        Scanner inputI = new Scanner(System.in);

        int numero;
        while (true) {
            out.print("-------------MENU------------- "
                    + "\n1-Criar Pessoa "
                    + "\n2-Criar Faculdade \n"
                    + "3-Criar Departamento\n"
                    + "4-Criar Eleiçao\n"
                    + "5-Atribuir departamentos a uma faculdade\n"
                    + "6-Apagar Pessoa\n"
                    + "7-Apagar Faculdade\n"
                    + "8-Apagar departamento\n"
                    + "9-Apagar Eleiçao \n"
                    + "10-alterar dados de uma pessoa\n"
                    + "11-Alterar dados de uma faculdade\n"
                    + "12-Alterar dados de um departamento\n"
                    + "13-Alterar dados de uma eleiçao\n"
                    + "14-Listar Eleiçoes\n"
                    + "15-Listar eleiçoes a decorrer\n"
                    + "16-Listar eleiçoes acabadas\n"
                    + "17-Listar Pessoas\n"
                    + "18-Listar faculdade\n"
                    + "19-Listar departamentos\n"
                    + "20-Exit\n"
                    + "Escolha:");
            numero = inputI.nextInt();

            switch (numero) {
                case 1:
                    rmi.regista_pessoa_teste_inputs();
                    break;
                case 2:
                    rmi.create_faculty();
                    break;
                case 3:
                    rmi.create_department();
                    break;
                case 4:
                    break;
                case 5:
                	rmi.sayHello("Admin","Funciona");
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
                    //rmi.imprime_pessoas();
                    break;
                case 18:
                    //rmi.print_facultys();
                    break;
                case 19:
                    //rmi.print_departments();
                    break;
                case 20:
                    System.exit(0);
                default:
                    out.print("\nOpçao invalida");
            }
        }

    }

}
