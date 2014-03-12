package client;

import java.io.IOException;
import java.util.Scanner;

import client.implementation.ClientHTTP10;
import client.implementation.ClientHTTP11;
import client.interfaces.ClientHTTP;

public class ClientCommandLine {

	/** 
	 * Start a new HTTPClient with the specified arguments. 
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 4)
			System.out.println("Invalid number of arguments. "
					+ "Expected syntax: <HTTP command> <URI> <port> "
					+ "<HTTP version>");
		
		String method = args[0];
		
		if (! method.matches("(GET)|(HEAD)|(POST)|(PUT)"))
			System.out.println("Invalid command. Expected: GET, HEAD,"
					+ " POST or PUT");
		
		String URI = args[1];
		String host;
		String filePath;
		int slashIndex = URI.indexOf('/');
		
		if (slashIndex < 0)
		{
			host = URI;
			filePath = "/";
		}
		
		else
		{
			host = URI.substring(0, slashIndex);
			filePath = URI.substring(slashIndex, URI.length());
		}
		
		String port = args[2];
		
		if (!port.matches("[123456789]\\d*|0"))
			System.out.println("Invalid port number. Expected a value in "
					+ "[0,1..].");
		
		int portNum = Integer.parseInt(port);
		
		String protocol = args[3];
		
		if (!protocol.matches("HTTP/1.[01]"))
			System.out.println("Invalid protocol. Expected: HTTP/1.0 or HTTP/1.1");
		
		ClientHTTP client;
		
		if (protocol.equals("HTTP/1.0"))
		{
			client = new ClientHTTP10();
		}
		else
		{
			client = new ClientHTTP11();
		}
		
		if (method.equals("GET"))
		{
			System.out.println(client.doGet(filePath, host, portNum));
		}
		else if (method.equals("HEAD"))
		{
			System.out.println(client.doHead(filePath, host, portNum));
		}
		else if (method.equals("POST"))
		{
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter a line.");
			String body = scanner.nextLine();
			scanner.close();
			System.out.println(client.doPost(filePath, body, host, portNum));
		}
		else // method.equals("PUT"))
		{
			Scanner scanner = new Scanner(System.in);
			System.out.println("Enter a line.");
			String body = scanner.nextLine();
			scanner.close();
			System.out.println(client.doPut(filePath, body, host, portNum));
		}
	}

}
