package server;
// TCPServer2.java: Multithreaded server
import java.net.*;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.*;
import static java.lang.System.*;

public class TCPServer{
	
	public static String rmiHost;
	public static int rmiPort;
	public static String Server;
	public static int ServerPort;
	public static String SecundaryServer;
	public static int SecundaryServerPort;
    public static void main(String args[]){
        int numero=0;
        ArrayList<Connection> connections = new ArrayList<Connection>();
        
        try{
            int serverPort = 12345;
            System.out.println("A Escuta no Porto 12345");
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("LISTEN SOCKET="+listenSocket);
            while(true) {
                Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
                System.out.println("CLIENT_SOCKET (created at accept())="+clientSocket);
                numero ++;
                connections.add(new Connection(clientSocket,connections, numero));
            }
        }catch(IOException e)
        {System.out.println("Listen:" + e.getMessage());}
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
					serverAHost = tokenizer.nextToken();
				}else if(line.equals("Server A Port")){
					serverAPort = Integer.parseInt(tokenizer.nextToken());
				}else if(line.equals("RMI Host")){
					rmiHost = tokenizer.nextToken();
				}else if(line.equals("RMI Port")){
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				}else if(line.equals("Server B Host")){
					serverBHost = tokenizer.nextToken();
				}else if(line.equals("Server B Port")){
					serverBPort = Integer.parseInt(tokenizer.nextToken());
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
















//= Thread para tratar de cada canal de comunicação com um cliente
class Connection extends Thread {
	BufferedReader inStream = null;
    PrintWriter outStream;
    Socket clientSocket;
    int thread_number;
    ArrayList<Connection> lista;
    
    public Connection (Socket aClientSocket,ArrayList<Connection> connections, int numero) {
        thread_number = numero;
        lista=connections;
        clientSocket=aClientSocket;
        try{
	            inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	            outStream = new PrintWriter(clientSocket.getOutputStream(), true);
	            this.start();
        }catch(IOException e){System.out.println("Connection:" + e.getMessage());}
    }
    //=============================
    public void run(){
    	//Conecção ao rmi ----->falta
        try{
            while(true){

            	readComandLine(inStream.readLine());
            }
        }catch(EOFException e){System.out.println("EOF:" + e);
        }catch(IOException e){System.out.println("IO:" + e);}
    }
    
    public static void readComandLine(String s){
    	try{
        	HashMap<String, String> m = ProtocolParser.parse(s);
        	
        	switch(m.get("type")) {
        		case("login"):{
        			String username = m.get("username").equals(null)?null:m.get("username");
	    			String password = m.get("password").equals(null)?null:m.get("password");
	    			
        		}
        		case("satus"):{
        			String logged = m.get("logged").equals(null)?null:m.get("logged");
        			String msg = m.get("msg").equals(null)?null:m.get("msg");
        			
        		}
        		
        	}
        	
    	}catch(Exception e){ System.out.println("type: error_message, status: not enought arguments");};
    }
}