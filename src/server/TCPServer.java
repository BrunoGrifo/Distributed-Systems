package server;

import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
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
		final ArrayList<Connection> connections = new ArrayList<Connection>();
		if (opc.equals("1")) {
			new Server(ServerAHost, ServerAPort, rmiHost, rmiPort,connections);
		} else if (opc.equals("2")) {
			new Server(ServerBHost, ServerBPort, rmiHost, rmiPort,connections);
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
	public RMI rmi = null;
	public ArrayList<Connection> connections;

	public Server(String myHost, int myPort, String rmiHost, int rmiPort,ArrayList<Connection> connections) {
		this.myHost = myHost;
		this.myPort = myPort;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		this.connections=connections;
		this.receiver = new Receiver(myPort, rmiHost, rmiPort,connections,rmi);
		this.start();
	}

	public void run() {
		String number_cc_string="";
		Scanner inputS = new Scanner(System.in);
		try {
			rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
			while (true) {
				do {
					out.print("ID:");
					try {
						number_cc_string = inputS.nextLine();
					} catch (InputMismatchException e) {
						out.println("Invalid option");
						inputS.next();
					}
				} while (!checkNumbers(number_cc_string) || number_cc_string.length() != 8);
				if(rmi.getATerminal(Integer.parseInt(number_cc_string))) {
					out.println("ID aceite!");
					for(Connection x:connections) {
						if(x.available.isAvailable()) {
							out.println("Terminal "+x.thread_id+" livre.");
							out.println("Diriga-se ao terminal de voto "+x.thread_id);
							x.available.setAvailable(false);;
							break;
						}else {
							out.println("Terminal "+x.thread_id+" ocupado.");
						}
					}
					for (Connection x: connections) {
						System.out.println("Terminal "+x.thread_id+" "+x.available.available);
					}
				}else {
					out.println("Informamos o senhor que o cc inserido não existe ou voce é refugiado e está a tentar ter opinião num pais ao qual não percente, obrigado!");
				}
			}
		}catch (IOException e) {
			System.out.println("IO:" + e);
		}catch (NotBoundException e) {
			System.out.println("Error no lookup method");
			System.exit(0);
		}
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
}

class Receiver extends Thread {
	public int myPort;
	public String rmiHost;
	public int rmiPort;
	public int thread_id = 0;
	public RMI rmi = null;
	public ArrayList<Connection> connections;
	public Receiver(int myPort, String rmiHost, int rmiPort,ArrayList<Connection> connections,RMI rmi) {
		this.myPort = myPort;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		this.connections=connections;
		this.rmi=rmi;
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
				connections.add(new Connection(clientSocket, connections, rmiHost, rmiPort, thread_id,rmi));
			}
		}catch (EOFException e) {
			System.out.println("EOF:" + e);
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
	Available available;
	ArrayList<Connection> lista;

	public Connection(Socket aClientSocket, ArrayList<Connection> connections, String rmiHost, int rmiPort,int numero, RMI rmi) {
		this.thread_id = numero;
		this.lista = connections;
		this.clientSocket = aClientSocket;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		this.rmi=rmi;
		available=new Available();
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
		while(true) {
			//if (available.isAvailable()){
				try {
					clientSocket.setSoTimeout(20000);
					while(true) {
						readComandLine(inStream.readLine(), rmi,available);
					}
				}catch (SocketTimeoutException ste) {
					   System.out.println("### Timed out after 10 seconds");
					   for (Connection x: lista) {
							System.out.println("Terminal "+x.thread_id+" "+x.available);
						}
					   available.setAvailable(true);
				} catch (EOFException e) {
					System.out.println("EOF:" + e);
				} catch (IOException e) {
					System.out.println("IO:" + e);
				}
			//}
		}
	}

	public static void readComandLine(String s, RMI rmi, Available available) {
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
			System.out.println(available.available);
		}
	}
	
	
}
class Available{
	boolean available;
	public Available() {
		available=true;
	}
	public boolean isAvailable() {
		return available;
	}
	public void setAvailable(boolean available) {
		this.available = available;
	}
	
}