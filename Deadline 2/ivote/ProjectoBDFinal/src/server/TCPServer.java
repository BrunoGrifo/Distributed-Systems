package server;

import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
	public static int rmiPortBackUp;
	public static String ServerAHost;
	public static int ServerAPort;
	public static String ServerBHost;
	public static int ServerBPort;
	public static RMI rmiMain;

	public static void main(String[] args) throws RemoteException {
		carregaServerConfig();
		Scanner sc1 = new Scanner(System.in);
		System.out.print("1 - ServerA\n2 - ServerB\n: ");
		String opc = sc1.nextLine();
		final ArrayList<Connection> connections = new ArrayList<Connection>();
		Server server;
		try {
			rmiMain = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
		if (opc.equals("1")) {
			server = new Server(rmiPortBackUp, ServerAHost, ServerAPort, rmiHost, rmiPort, connections);
		} else {
			server = new Server(rmiPortBackUp, ServerBHost, ServerBPort, rmiHost, rmiPort, connections);
		}
		try {
			rmiMain.subscribe((Server_rmi) server);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		server.mesaVoto();

	}

	public static void carregaServerConfig() {
		String file = "TCPServerConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Server configurations...");
		try {
			FileReader inputFile = new FileReader(file);
			BufferedReader buffer = new BufferedReader(inputFile);
			for (int i = 0; i < 7; i++) {
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
				} else if (line.equals("RMI PortBackUp")) {
					rmiPortBackUp = Integer.parseInt(tokenizer.nextToken());
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

class Server extends UnicastRemoteObject implements Server_rmi {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public String myHost;
	public int myPort;
	public String rmiHost;
	public int rmiPort;
	public int rmiPortBackUp;
	public Receiver receiver;
	public RMI rmi = null;
	ArrayList<Connection> connections;
	ArrayList<Connection> connectionsDeleteCache;
	String mesaVoto;
	ArrayList<String> buffer;

	public Server(int rmiPortBackUp, String myHost, int myPort, String rmiHost, int rmiPort,
			ArrayList<Connection> connections) throws RemoteException {
		this.rmiPortBackUp = rmiPortBackUp;
		this.myHost = myHost;
		this.myPort = myPort;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		this.connections = connections;
		buffer = new ArrayList<String>();
		this.connectionsDeleteCache = new ArrayList<Connection>();

		try {
			rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public String getMesaVoto() {
		return mesaVoto;
	}

	public void mesaVoto() {

		String number_cc_string = "";
		Scanner inputS = new Scanner(System.in);
		boolean mesa = false;
		ArrayList<String> mesasVoto;
		int option = 0;
		try {

			while (mesa == false) {
				try {
					out.println("Choose the representative table!");
					mesasVoto = rmi.mesas_prontas_abrir();
					for (String x : mesasVoto) {
						System.out.println(++option + " - " + x.toString());
					}
					try {
						if (mesasVoto.size() != 0) {
							option = testOption(0, mesasVoto.size());
							if (option != 0) {
								mesaVoto = mesasVoto.get(option - 1);
								rmi.notifyAdmins("The table:" + mesaVoto + " was activaded");
							} else {
								System.exit(0);
							}
						} else {
							out.println("Não existem mesas criadas!");
							System.exit(0);
						}
					} catch (IndexOutOfBoundsException e) {
						System.out.println("indexoutofboundsexception");
					}
					receiver = new Receiver(rmiPortBackUp, myPort, rmiHost, rmiPort, connections, mesaVoto, rmi);
					mesa = true;
				} catch (RemoteException e) {
					int check = 0;
					while (check == 0) {
						try {
							rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
							check = 1;
						} catch (NotBoundException e1) {
							check = 0;
							e1.printStackTrace();
						}
					}

					e.printStackTrace();
				}
			}
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
				addBuffer(number_cc_string);
				try {
					for (String pedido : buffer) {
						if (rmi.getATerminal(Integer.parseInt(pedido))) {
							out.println("ID aceite!");
							for (Connection x : connections) {
								if (x.isLive()) {
									if (x.available == true) {
										out.println("Terminal " + x.thread_id + " is free.");
										out.println("Go to terminal " + x.thread_id);
										x.outStream.println("Terminal unblocked of CC: " + pedido
												+ "\n Press enter to continue your request.");
										x.cc = pedido;
										x.available = false;
										break;
									} else {
										out.println("Terminal " + x.thread_id + " occupied.");
									}
								} else {
									connectionsDeleteCache.add(x);
								}
							}
							for (Connection x : connectionsDeleteCache) {
								connections.remove(x);
							}
							connectionsDeleteCache.clear();
						} else {
							out.println("Your cc does not exist");
						}
					}
					buffer.clear();
				} catch (RemoteException e) {
					int check = 0;
					while (check == 0) {
						do {
							out.print("ID:");
							try {
								number_cc_string = inputS.nextLine();
							} catch (InputMismatchException e1) {
								out.println("Invalid option");
								inputS.next();
							}
						} while (!checkNumbers(number_cc_string) || number_cc_string.length() != 8);
						addBuffer(number_cc_string);
						try {
							rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
							for (String pedido : buffer) {
								if (rmi.getATerminal(Integer.parseInt(pedido))) {
									out.println("ID aceite!");
									for (Connection x : connections) {
										if (x.isLive()) {
											if (x.available == true) {
												out.println("Terminal " + x.thread_id + " is free.");
												out.println("Go to terminal " + x.thread_id);
												x.outStream.println("Terminal unblocked of CC: " + pedido
														+ "\n Press enter to continue your request.");
												x.cc = pedido;
												x.available = false;
												break;
											} else {
												out.println("Terminal " + x.thread_id + " occupied.");
											}
										} else {
											connectionsDeleteCache.add(x);
										}
									}
									for (Connection x : connectionsDeleteCache) {
										connections.remove(x);
									}
									connectionsDeleteCache.clear();
								} else {
									out.println("Your cc does not exist");
								}
							}
							buffer.clear();

							check = 1;
						} catch (NotBoundException e1) {
							check = 0;
							e1.printStackTrace();
						} catch (RemoteException e2) {
							check = 0;
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("IO:" + e);
		}

	}

	public void addBuffer(String pedido) {
		int check = 0;
		for (String x : buffer) {
			if (x.equals(pedido)) {
				check = 1;
			}
		}
		if (check == 0) {
			buffer.add(pedido);
		} else {
			System.out.println("Pedido descartado(duplicado)");
		}

	}

	public static int testOption(int minimo, int maximo) {
		Scanner inputS = new Scanner(System.in);
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
				inputS.next();
			}
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
	public int rmiPortBackUp;
	public int thread_id = 0;
	ArrayList<Connection> connections;
	String mesaVoto;
	RMI rmi;

	public Receiver(int rmiPortBackUp, int myPort, String rmiHost, int rmiPort, ArrayList<Connection> connections,
			String mesaVoto, RMI rmi) {
		this.rmiPortBackUp = rmiPortBackUp;
		this.myPort = myPort;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		this.connections = connections;
		this.mesaVoto = mesaVoto;
		this.rmi = rmi;
		this.start();
	}

	public void run() {
		try {
			ServerSocket listenSocket = new ServerSocket(myPort);
			System.out.println("LISTEN SOCKET=" + listenSocket);
			while (true) {
				Socket clientSocket = listenSocket.accept();
				System.out.println("CLIENT_SOCKET (created at accept())=" + clientSocket);
				thread_id++;
				connections.add(new Connection(rmiPortBackUp, clientSocket, connections, rmiHost, rmiPort, thread_id,
						mesaVoto, rmi));
			}
		} catch (EOFException e) {
			System.out.println("EOF:" + e);
		} catch (IOException e) {
			System.out.println("Listen:" + e.getMessage());
		}

	}
}

class Connection extends Thread {
	public BufferedReader inStream = null;
	public PrintWriter outStream;
	public Socket clientSocket;
	public String rmiHost;
	public int rmiPort;
	public int rmiPortBackUp;
	public int thread_id;
	public RMI rmi;
	public String cc;
	public boolean available;
	public boolean loggedIN;
	ArrayList<Connection> lista;
	String mesaVoto;
	ArrayList<String> buffer;

	public Connection(int rmiPortBackUp, Socket aClientSocket, ArrayList<Connection> connections, String rmiHost,
			int rmiPort, int numero, String mesaVoto, RMI rmi) {
		this.rmiPortBackUp = rmiPortBackUp;
		this.thread_id = numero;
		this.lista = connections;
		this.clientSocket = aClientSocket;
		this.rmiHost = rmiHost;
		this.rmiPort = rmiPort;
		this.mesaVoto = mesaVoto;
		this.buffer = new ArrayList<String>();
		cc = null;
		available = true;
		loggedIN = false;
		try {
			this.rmi = rmi;
			inStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outStream = new PrintWriter(clientSocket.getOutputStream(), true);
			this.start();
		} catch (IOException e) {
			System.out.println("Connection:" + e.getMessage());
		}
	}

	public boolean isLive() {
		try {
			clientSocket.sendUrgentData(1);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	// =============================

	public void descodificador(String pedido) throws RemoteException {
		HashMap<String, String> m = ProtocolParser.parse(pedido);
		switch (m.get("type")) {
		case ("login"): {
			String username = m.get("username").equals(null) ? null : m.get("username");
			String password = m.get("password").equals(null) ? null : m.get("password");
			try {
				if (rmi.login(username, password)) {
					System.out.println("Login with sucess!");
					loggedIN = true;
					rmi.loginDone(cc, mesaVoto);
				} else {
					System.out.println("Username and Password do not correspond");
				}
			} catch (RemoteException e) {
				System.out.println("no rmi connection");
			}
			break;
		}
		case ("status"): {
			boolean status = loggedIN;
			if (status) {
				outStream.println("Looged on");
			} else {
				outStream.println("Looged off");
			}
			break;
		}
		case ("lists"): {
			boolean status = loggedIN;
			if (status) {
				try {
					ArrayList<Lista_candidata> lists =rmi.print_lists_for_person(Integer.parseInt(cc), mesaVoto);
					if(lists.isEmpty()) {
						outStream.println("No lists avaible for you to vote on this election");
					}else {
						for(Lista_candidata x:lists) {
							outStream.println(x.toString());
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				outStream.println("Looged off!");
			}
			break;
		}
		case ("vote"): {
			boolean status = loggedIN;//por segurança que pode votas naquela lista alterar a cena para os votos brancos e null
			if (status) {
				String list = m.get("list").equals(null) ? null : m.get("list");
				try {
					outStream.println(rmi.register_vote(Integer.parseInt(cc), list, mesaVoto));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			} else {
				outStream.println("Looged off");
			}
			break;

		}
		case ("loggoff"):{
			loggedIN = false;
			available = true;
			break;
		}
		default: {
			System.out.println("type: error_message, status: not enought arguments1");
			System.out.println("default");
			break;
		}
		}
	}
	public void loggedOff() {
		this.loggedIN = false;
	}
	public void run() {
		ArrayList<String> cache = new ArrayList<String>();
		String pedido = "";
		int check = 0;
		boolean exception = true;
		while (exception) {
			if (available == false) {
				try {
					clientSocket.setSoTimeout(120000);
					while (true) {
						pedido = inStream.readLine();
						try {
							rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
							descodificador(pedido);
						} catch (RemoteException er) {
							long start_time = System.currentTimeMillis();
							long wait_time = 30000;
							long end_time = start_time + wait_time;
							check = 0;
							while (System.currentTimeMillis() < end_time) {
								try {
									rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
									check = 1;
									break;
								} catch (Exception e) {
									check = 0;
									HashMap<String, String> m = ProtocolParser.parse(pedido);
									for (String x : cache) {
										HashMap<String, String> temp = ProtocolParser.parse(x);
										if (m.get("type").equals(temp.get("type"))) {
											check = 1;
										}
									}
									if (check != 1) {
										cache.add(pedido);
										outStream.println("next request");
										pedido = inStream.readLine();
									}
								}

							}
							if (check == 0) {
								System.out.println(rmiPort + "-" + rmiPortBackUp);
								int change = rmiPort;
								rmiPort = rmiPortBackUp;
								rmiPortBackUp = change;
								System.out.println(rmiPort + "-" + rmiPortBackUp);
							}

							try {
								rmi = (RMI) Naming.lookup("rmi://" + rmiHost + ":" + rmiPort + "/rmi");
								descodificador(pedido);
								for(int i=0;i<cache.size();i++) {
									descodificador(cache.get(i));
								}
								cache.clear();
							} catch (NotBoundException e) {
								e.printStackTrace();
							}
						} catch (Exception e) {
							System.out.println("both rmis down");
						}
					}

				} catch (SocketTimeoutException ste) {
					System.out.println("Terminal" + thread_id + " blocked. More then 120 sec without request");
					available = true;
					loggedOff();
					buffer.clear();
				} catch (EOFException e) {
					System.out.println("EOF:" + e);
					exception = false;
				} catch (IOException e) {
					exception = false;
					System.out.println("IO:" + e);
					try {
						clientSocket.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			} else {
				try {
					inStream.readLine();
				} catch (IOException e) {
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}

}