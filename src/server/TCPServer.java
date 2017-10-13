package server;
// TCPServer2.java: Multithreaded server
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
import static java.lang.System.*;


public class TCPServer{
	
	public static String rmiHost;
	public static int rmiPort;
	public static String ServerAHost;
	public static int ServerAPort;
	public static String ServerBHost;
	public static int ServerBPort;
	
	public static void main(String[] args){
		carregaServerConfig();
		//ServerA servidor = new ServerA(serverAHost,serverAPort,rmiHost,rmiPort);
		Scanner sc1 = new Scanner(System.in);
		System.out.print("1 - ServerA\n2 - ServerB\n: ");
		String opc = sc1.nextLine();
		if(opc.equals("1")){
			ServerA serverA = new ServerA(ServerAHost,ServerAPort,rmiHost,rmiPort,ServerBHost,ServerBPort);
		}else if(opc.equals("2")){
			ServerB serverB = new ServerB(ServerBHost,ServerBPort,rmiHost,rmiPort,ServerAHost,ServerAPort);
		}
	}
	
	
	public static void carregaServerConfig(){
		String file = "ServerAConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Server A configurations...");
		try{
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for(int i=0;i<6;i++){
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line,"=");
				line = tokenizer.nextToken();
				if(line.equals("Server A Host")){
					ServerAHost = tokenizer.nextToken();
				}else if(line.equals("Server A Port")){
					ServerAPort = Integer.parseInt(tokenizer.nextToken());
				}else if(line.equals("RMI Host")){
					rmiHost = tokenizer.nextToken();
				}else if(line.equals("RMI Port")){
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				}else if(line.equals("Server B Host")){
					ServerBHost = tokenizer.nextToken();
				}else if(line.equals("Server B Port")){
					ServerBPort = Integer.parseInt(tokenizer.nextToken());
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("File "+file+" not found");
			System.exit(0);
		}catch(IOException e){}
		catch(NullPointerException e){
			System.out.println("Erro a carregar os dados do ficheiro");
		}

	}
}
class ServerA extends Thread{
	public String myHost;
	public int myPort;
	public String targetHost;
	public int targetPort;
	public String rmiHost;
	public int rmiPort;
	public Receiver receiver;
	public MulticastSocket multiSocket;
	public String group;

	public ServerA(String myHost,int myPort,String rmiHost,int rmiPort,String targetHost, int targetPort){
		this.myHost=myHost;
		this.myPort=myPort;
		this.rmiHost=rmiHost;
		this.rmiPort=rmiPort;
		this.targetHost=targetHost;
		this.targetPort=targetPort;
		//this.receiver = new Receiver(myPort,rmiHost,rmiPort);
		this.start();
	}
	public void run(){
		/*
		 * Aqui será feita a comunicação com o servidor B através do protocolo UDP
		 */
	}
}

class ServerB extends Thread{
	public String myHost;
	public int myPort;
	public String targetHost;
	public int targetPort;
	public String rmiHost;
	public int rmiPort;
	public Receiver receiver;
	public MulticastSocket multiSocket;
	public String group;

	public ServerB(String myHost,int myPort,String rmiHost,int rmiPort,String targetHost, int targetPort){
		this.myHost=myHost;
		this.myPort=myPort;
		this.rmiHost=rmiHost;
		this.rmiPort=rmiPort;
		this.targetHost=targetHost;
		this.targetPort=targetPort;
		//this.receiver = new Receiver(myPort,rmiHost,rmiPort);
		this.start();
	}
	public void run(){
		/*
		 * Aqui será feita a comunicação com o servidor A através do protocolo UDP
		 */
	}
}
class Receiver extends Thread{
	public int myPort;
	public String rmiHost;
	public int rmiPort;
	public int numero = 0;
	
	public Receiver(int myPort,String rmiHost,int rmiPort){
		this.myPort=myPort;
		this.rmiHost=rmiHost;
		this.rmiPort=rmiPort;
		this.start();
	}
	public void run(){
		int thread_id=0;
		try{
			ServerSocket listenSocket = new ServerSocket(myPort);
			System.out.println("Listen Socket="+listenSocket);
			while(true){
				Socket clientSocket = listenSocket.accept();
				System.out.println("Client_socket accepted= "+clientSocket);
				listaSockets.add(clientSocket);
				new Connection(clientSocket,rmiHost,rmiPort);
			}
		}catch(IOException e){
			System.out.println("Listen: "+e.getMessage());
		}
	}

}
	