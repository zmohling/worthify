package hW1;
import java.io.*;
import java.util.Scanner;
import java.net.*;

public class Client {
	public static void main(String[] args) throws IOException {
		
		if (args.length != 0) {
			System.err.println(
					"Usage: java EchoClient <host name> <port number>");
			System.exit(1);
		}
		
		String hostName = "127.0.0.1";
		int portNumber = 1025;
		Scanner input = new Scanner(System.in);
		String name;
		System.out.print("Enter your name: ");
		name = input.nextLine();
		try (
			Socket echoSocket = new Socket(hostName, portNumber);
			PrintWriter out = 
					new PrintWriter(echoSocket.getOutputStream(), true);
			Scanner in = 
					new Scanner(
						new InputStreamReader(echoSocket.getInputStream()));
			BufferedReader stdIn =
				new BufferedReader(
					new InputStreamReader(System.in));
		) {
			System.out.println("You are now connected");
			String userInput;
			while((userInput = stdIn.readLine()) != null) {
				out.println(name + ": " + userInput);
				if(in.hasNext()) System.out.println(in.nextLine());
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " + 
				hostName);
			System.exit(1);
		}
	}
}
