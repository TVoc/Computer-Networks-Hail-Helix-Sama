package client.implementation;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class ClientConnection {
	
	private Socket socket;
	private BufferedReader inFromServer;
	private DataOutputStream outToServer;
	
	public ClientConnection(String host, int port) throws IOException
	{
		socket = new Socket(InetAddress.getByName(host), port);
		inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		outToServer = new DataOutputStream (socket.getOutputStream());
	}
	
	public void write(String message) throws IOException
	{
		outToServer.writeBytes(message);
	}
	
	public String read() throws IOException
	{
		StringBuilder response = new StringBuilder();
		String line = inFromServer.readLine();
		while (line != null)
		{
			if (! (response.toString() == ""))
			{
				response.append("\n");
			}
			response.append(line);
			line = inFromServer.readLine();
		}
		return response.toString();
	}
	
	public void close() throws IOException
	{
		if (! socket.isClosed())
			socket.close();
		inFromServer.close();
		outToServer.close();
		inFromServer = null;
		outToServer = null;
	}

}
