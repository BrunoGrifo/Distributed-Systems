package server;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This class establishes a TCP connection to a specified server, and loops
 * sending/receiving strings to/from the server.
 * <p>
 * The main() method receives two arguments specifying the server address and
 * the listening port.
 * <p>
 * The usage is similar to the 'telnet <address> <port>' command found in most
 * operating systems, to the 'netcat <host> <port>' command found in Linux,
 * and to the 'nc <hostname> <port>' found in macOS.
 *
 * @author Raul Barbosa
 * @author Alcides Fonseca
 * @version 1.1
 */
class Terminal {
	public static void carregaServerConfig(){
		String file = "TCPClientConfig.txt";
		String line;
		StringTokenizer tokenizer;
		System.out.println("Uploding Client configurations...");
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
			buffer.close();
		}catch(FileNotFoundException e){
			System.out.println("File "+file+" not found");
			System.exit(0);
		}catch(IOException e){}
		catch(NullPointerException e){
			System.out.println("Erro a carregar os dados do ficheiro");
		}

}
  public static void main(String[] args) {
    Socket socket;
    PrintWriter outToServer;
    BufferedReader inFromServer = null;
    try {
      // connect to the specified address:port (default is localhost:12345)
      if(args.length == 2)
        socket = new Socket(args[0], Integer.parseInt(args[1]));
      else
        socket = new Socket("localhost", 7000);
        
      // create streams for writing to and reading from the socket
      inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      outToServer = new PrintWriter(socket.getOutputStream(), true);

      // create a thread for reading from the keyboard and writing to the server
      new Thread() {
        public void run() {
          Scanner keyboardScanner = new Scanner(System.in);
          while(!socket.isClosed()) {
            String readKeyboard = keyboardScanner.nextLine();
            outToServer.println(readKeyboard);
          }
        }
      }.start();

      // the main thread loops reading from the server and writing to System.out
      String messageFromServer;
      while((messageFromServer = inFromServer.readLine()) != null)
        System.out.println(messageFromServer);
    } catch (IOException e) {
      if(inFromServer == null)
        System.out.println("\nUsage: java TCPClient <host> <port>\n");
      System.out.println(e.getMessage());
    } finally {
      try { inFromServer.close(); } catch (Exception e) {}
    }
  }
}
