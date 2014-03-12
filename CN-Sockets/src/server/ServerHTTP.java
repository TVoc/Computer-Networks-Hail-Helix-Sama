package server;

import java.io.*; 
import java.net.*; 
import java.util.HashSet;
import java.util.Set;

/**
 * The ServerHTTP listens on the port with which it is initialised, it passes 
 * any connections to SessionHandlers. 
 * 
 * It provides methods for listening, as well as checking incoming messages. 
 */
public class ServerHTTP { 
	/** The server socket of this ServerHTTP. */
	private ServerSocket welcomeSocket;
	/** The allowed methodscalls */
	private Set<String> allowedMethods;
	/** The allowed protocols. */
	private Set<String> allowedProtocols;

	private String charset;
	private long timeout_millis = 100000;
	
	public static final String ROOT_SERVER_FILES = "/serverfiles";
	
	/**
	 * Construct a new ServerHTTP with the given charset that listens to the 
	 * given port. 
	 * 
	 * @param charset
	 * 		The charset that this new ServerHTTP will use.
	 * @param port
	 * 		The port to which this new ServerHTTP will listen.
	 *
	 * @precondition | port >= 0
	 * 
	 * @throws IOException
	 */
	public ServerHTTP(String charset, int port) throws IOException
	{
		initialiseAllowedMethods();
		initialiseAllowedProtocols();
		welcomeSocket = new ServerSocket(port);
		this.charset = charset;
	}
	
	/**
	 * Start the server for an infinite amount of time. The server will listen
	 * to any signal on its port, and will start a SessionHandler 
	 * in a seperate thread to deal with the request.
	 *  
	 * @throws IOException 
	 */
	public void listenForConnection() throws IOException
	{
		while (true)
		{
			Socket clientSocket = welcomeSocket.accept();
			if (clientSocket != null)
			{
				System.out.println("Accepted connection from " + clientSocket.getInetAddress());
				SessionHandler handler = new SessionHandler(clientSocket, this);
				Thread thread = new Thread(handler);
				thread.start();
			}
		}
	}
	
	/** 
	 * Check if the given method is a valid methods. 
	 * 
	 * @param method
	 * 		A method call
	 * @return
	 * 		True if valid method, False otherwise. 
	 */
	public boolean isAllowedMethod(String method)
	{
		return allowedMethods.contains(method);
	}
	
	/**
	 * Check if the given protocol is a valid protocol. 
	 * 
	 * @param protocol
	 * 		the version of http being used. 
	 * @return
	 * 		True if valid protocol, False otherwise.
	 */
	public boolean isAllowedProtocol(String protocol)
	{
		return allowedProtocols.contains(protocol);
	}
	
	/**
	 * Check if the given protocol is persistent.
	 * 
	 * @param protocol
	 * 		The version of the http protocol. 
	 * @return
	 * 		True if persitent protocol, False otherwise.
	 */
	public boolean isPersistentProtocol(String protocol)
	{
		return protocol.equals("1.1");
	}
	
	/**
	 * Get the time out of this ServerHTTP.
	 * @return the time out of this ServerHTTP
	 */
	public long getTimeoutMillis()
	{
		return this.timeout_millis;
	}
	
	/**
	 * Get the char set of this ServerHTTP.
	 * @return the char set of this ServerHTTP
	 */
	public String getCharSet()
	{
		return this.charset;
	}
	
	/**
	 * Add the valid methods to the allowedMethods variable.
	 * should only be called in the constructor. 
	 */
	private void initialiseAllowedMethods()
	{
		allowedMethods = new HashSet<String>();
		allowedMethods.add("GET");
		allowedMethods.add("HEAD");
		allowedMethods.add("POST");
		allowedMethods.add("PUT");
	}

	/** 
	 * Add the valid protocols to the allowedProtocols variable. 
	 * should only be called in the constructor. 
	 */
	private void initialiseAllowedProtocols()
	{
		allowedProtocols = new HashSet<String>();
		allowedProtocols.add("1.0");
		allowedProtocols.add("1.1");
	}
} 
