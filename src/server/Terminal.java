package server;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Raul Barbosa
 * @author Alcides Fonseca The code to handle the configuration was the only
 *         code not made my the authors above
 */
class Terminal {
	public static String ServerAHost;
	public static int ServerAPort;
	public static String ServerBHost;
	public static int ServerBPort;

	public static void carregaTerminalConfig() {
		String file = "TerminalConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Client configurations...");
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

	public static void main(String[] args) {
		Socket socket;
		String host = ServerAHost;// just so its not null
		int port = ServerAPort; // just so its not 0
		PrintWriter outToServer;
		BufferedReader inFromServer = null;
		carregaTerminalConfig();

		try {
			// connect to the specified address:port (default is localhost:12345)
			Scanner serverinput = new Scanner(System.in);
			System.out.println("Choose the server to connect:\n1-ServerA\n2-ServerB");
			switch (serverinput.nextLine()) {
			case "1":
				host = ServerAHost;
				port = ServerAPort;
				break;
			case "2":
				host = ServerBHost;
				port = ServerBPort;
			default:
				System.out.println("Opção Invalida");
				System.exit(0);
			}
			socket = new Socket(host, port);

			// create streams for writing to and reading from the socket
			inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outToServer = new PrintWriter(socket.getOutputStream(), true);

			// create a thread for reading from the keyboard and writing to the server
			new Thread() {
				public void run() {
					Scanner keyboardScanner = new Scanner(System.in);
					while (!socket.isClosed()) {
						String readKeyboard = keyboardScanner.nextLine();
						outToServer.println(readKeyboard);
					}
				}
			}.start();

			// the main thread loops reading from the server and writing to System.out
			String messageFromServer;
			while ((messageFromServer = inFromServer.readLine()) != null)
				System.out.println(messageFromServer);
		} catch (IOException e) {
			if (inFromServer == null)
				System.out.println("\nUsage: java TCPClient <host> <port>\n");
			System.out.println(e.getMessage());
		} finally {
			try {
				inFromServer.close();
			} catch (Exception e) {
			}
		}
	}
}
