package client;

import client.implementation.ClientHTTP10;
import client.interfaces.ClientHTTP;

public class ClientCommandLine {

	/** 
	 * Start a new HTTPClient with the specified arguments. 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 4)
			System.out.println("Invalid number of arguments. "
					+ "Expected syntax: <HTTP command> <URI> <port> "
					+ "<HTTP version>");
		
		if (!args[2].matches("[123456789]\\d*|0"))
			System.out.println("Invalid port number. Expected a value in "
					+ "[0,1..].");
		
		ClientHTTP client = new ClientHTTP10();		
	}

}
