package hW1;
// Java implementation of  Server side 
// It contains two classes : Server and ClientHandler 
// Save file as Server.java 
  
import java.io.*; 
import java.net.*;
import java.util.Scanner;
import java.util.Vector;
  
// Server class 
public class ThreadedServer { 
	static ClientHandler[] ar = new ClientHandler[10];
	public static void main(String[] args) throws IOException {
		int clientNum = 0;
		int portNumber = 1025;
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(1025);
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
			System.exit(-1);
		}
		
		while(true) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				ar[clientNum] = new ClientHandler(clientSocket, clientNum, out);
				Thread t = new Thread(ar[clientNum]);
				clientNum++;
				t.start();
			} catch (IOException e) {
				System.out.println("Accept failed: 4444");
				System.exit(-1);
			}
		}
	}
}

class ClientHandler implements Runnable {
	Socket s;
	int num;
	PrintWriter out;
	
	ClientHandler(Socket s, int n, PrintWriter out) {
		this.s = s;
		num = n;
		this.out = out;
	}
	
	public void run() {
		
		try (
			Scanner in = 
				new Scanner(
					new InputStreamReader(s.getInputStream()));
			BufferedReader stdIn = 
				new BufferedReader(
						new InputStreamReader(System.in));
		) {
			String inputLine = "";
			while(true) {
				if(in.hasNext()) {
					inputLine = in.nextLine();
					int i = 0;
					String inLine = "";
					if(!inLine.equals(inputLine))
					{
						inLine = inputLine;
						while(ThreadedServer.ar[i] != null)
						{
							if(!ThreadedServer.ar[i].equals(this)) {
								out = new PrintWriter(ThreadedServer.ar[i].s.getOutputStream(), true);
								out.print(inputLine);
								out.println();
							}
							else {
								System.out.println(inputLine);
							}
							i++;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
