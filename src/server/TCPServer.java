package server;

import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
import static java.lang.System.*;

public class TCPServer {

	public static String rmiHost;
	public static int rmiPort;
	public static String ServerAHost;
	public static int ServerAPort;
	public static String ServerBHost;
	public static int ServerBPort;

	public static void main(String[] args) {
		carregaServerConfig();
		// ServerA servidor = new ServerA(serverAHost,serverAPort,rmiHost,rmiPort);
		Scanner sc1 = new Scanner(System.in);
		System.out.print("1 - ServerA\n2 - ServerB\n: ");
		String opc = sc1.nextLine();
		if (opc.equals("1")) {
			new Server(ServerAHost, ServerAPort, rmiHost, rmiPort);
		} else if (opc.equals("2")) {
			new Server(ServerBHost, ServerBPort, rmiHost, rmiPort);
		}
	}

	public static void carregaServerConfig() {
		String file = "TCPServerConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Server configurations...");
		try {
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for (int i = 0; i < 6; i++) {
				line = buffer.readLine();
				tokenizer = new StringTokenizer(line, "=");
				line = tokenizer.nextToken();
				if (line.equals("Server A Host")) {
					ServerAHost = tokenizer.nextToken();
				} else if (line.equals("Server A Port")) {
					ServerAPort = Integer.parseInt(tokenizer.nextToken());
				} else if (line.equals("RMI Host")) {
					rmiHost = tokenizer.nextToken();
				} else if (line.equals("RMI Port")) {
					rmiPort = Integer.parseInt(tokenizer.nextToken());
				} else if (line.equals("Server B Host")) {
					ServerBHost = tokenizer.nextToken();
				} else if (line.equals("Server B Port")) {
					ServerBPort = Integer.parseInt(tokenizer.nextToken());
				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			System.out.println("File " + file + " not found");
			System.exit(0);
		} catch (IOException e) {
		} catch (NullPointerException e) {
			System.out.println("Error updating the data from the files.");
		}

	}
}

class Server extends Thread {
	public String myHost;
	public int myPort;
	public String rmiHost;
	public int rmiPort;
	public Receiver receiver;
	public String group;

	public Server(String myHost, int myPort, String rmiHost, int rmiPort) {
		this.myHost = myHost;
		this.myPort = myPort;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;

		this.receiver = new Receiver(myPort, rmiHost, rmiPort);
		this.start();
	}

	public void run() {
		/*
		 * Aqui será feita a comunicação com o servidor B através do protocolo UDP
		 */
	}
}

class Receiver extends Thread {
	public int myPort;
	public String rmiHost;
	public int rmiPort;
	public int thread_id = 0;
	public ArrayList<Connection> connections = new ArrayList<Connection>();

	public Receiver(int myPort, String rmiHost, int rmiPort) {
		this.myPort = myPort;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		this.start();
	}

	public void run() {
		try {
			ServerSocket listenSocket = new ServerSocket(myPort);
			System.out.println("LISTEN SOCKET=" + listenSocket);
			while (true) {
				Socket clientSocket = listenSocket.accept(); // BLOQUEANTE
				System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
				thread_id++;
				connections.add(new Connection(clientSocket, connections, rmiHost, rmiPort, thread_id));
			}
		} catch (IOException e) {
			System.out.println("Listen:" + e.getMessage());
		}
	}

}

class Connection extends Thread {
	public BufferedReader inStream = null;
	public static PrintWriter outStream;
	public Socket clientSocket;
	public String rmiHost;
	public int rmiPort;
	public int thread_id;
	public RMI rmi = null;
	public ArrayList<Connection> lista;

	public Connection(Socket aClientSocket, ArrayList<Connection> connections, String rmiHost, int rmiPort,
			int numero) {
		this.thread_id = numero;
		this.lista = connections;
		this.clientSocket = aClientSocket;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		try {
			inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outStream = new PrintWriter(clientSocket.getOutputStream(), true);
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	// =============================
	public void run() {
		try {
			rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
			while (true) {

				readComandLine(inStream.readLine(), rmi);
			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e);
		} catch (IOException e) {
			System.out.println("IO:" + e);
		} catch (NotBoundException e) {
			System.out.println("Error no lookup method");
			System.exit(0);
		}
	}

	public static void readComandLine(String s, RMI rmi) {
		try {
			HashMap<String, String> m = ProtocolParser.parse(s);

			switch (m.get("type")) {
			case ("login"): {
				String username = m.get("username").equals(null) ? null : m.get("username");
				String password = m.get("password").equals(null) ? null : m.get("password");
				break;
			}
			/*
			 * case("satus"):{ String logged =
			 * m.get("logged").equals(null)?null:m.get("logged"); String msg =
			 * m.get("msg").equals(null)?null:m.get("msg"); break; }
			 */

			}

		} catch (Exception e) {
			System.out.println("type: error_message, status: not enought arguments");
		}
		;
	}
}
