package client.implementation;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import support.ClientConnection;
import client.interfaces.ClientHTTP;

public class ClientHTTP10 extends ClientHTTP {
	
	private static String charset = "UTF-8";
	
	@Override
	public List<String> doGet(String filePath, String host, int port) throws IOException
	{
		List<String> toReturn = new ArrayList<String>();
		String initialResponse = doSingleGet(filePath, host, port);
		toReturn.add(initialResponse);
		List<String> embeddedObjects = findEmbeddedObjects(initialResponse);
		for (String embeddedObject : embeddedObjects)
		{
			toReturn.add(doSingleGet(embeddedObject, host, port));
		}
		return toReturn;
	}
	
	@Override
	public String doSingleGet(String filePath, String host, int port) throws IOException
	{
		StringBuilder HTTPCommand = new StringBuilder("GET ");
		HTTPCommand.append(filePath + " HTTP/1.0\r\n\r\n");

		ClientConnection connection = new ClientConnection(host, port, charset);
		connection.write(HTTPCommand.toString());
		String response = connection.readUntilEmpty();
		return response;
	}

	@Override
	public String doHead(String filePath, String host, int port)
			throws IOException {
		StringBuilder HTTPCommand = new StringBuilder("HEAD ");
		HTTPCommand.append(filePath + " HTTP/1.0\r\n\r\n");
		
		ClientConnection connection = new ClientConnection(host, port, charset);
		connection.write(HTTPCommand.toString());
		String response = connection.readUntilEmpty();
		return response;
	}

	@Override
	public String doPost(String filePath, String body, String host, int port)
			throws IOException {
		StringBuilder HTTPCommand = new StringBuilder("POST ");
		HTTPCommand.append(filePath + " HTTP/1.0\r\n");
		int numOfBytes = countBytes(body);
		HTTPCommand.append("Content-Length: " + numOfBytes);
		HTTPCommand.append("\r\n\r\n");
		HTTPCommand.append(body);
		
		ClientConnection connection = new ClientConnection(host, port, charset);
		connection.write(HTTPCommand.toString());
		String response = connection.readUntilEmpty();
		return response;
	}

	@Override
	public String doPut(String filePath, String body, String host, int port)
			throws IOException {
		StringBuilder HTTPCommand = new StringBuilder("PUT ");
		HTTPCommand.append(filePath + " HTTP/1.0\r\n");
		int numOfBytes = countBytes(body);
		HTTPCommand.append("Content-Length: " + numOfBytes);
		HTTPCommand.append("\r\n\r\n");
		HTTPCommand.append(body);
		
		ClientConnection connection = new ClientConnection(host, port, charset);
		connection.write(HTTPCommand.toString());
		String response = connection.readUntilEmpty();
		return response;
	}
	
}
