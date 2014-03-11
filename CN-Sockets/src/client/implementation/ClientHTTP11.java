package client.implementation;

import java.io.IOException;
import java.util.List;

import support.ClientConnection;
import support.ResponseHeader;
import client.interfaces.ClientHTTP;

public class ClientHTTP11 extends ClientHTTP {
	
	private ClientConnection connection;

	@Override
	public String doGet(String filePath, String host, int port)
			throws IOException {
		StringBuilder HTTPCommand = new StringBuilder();
		
		HTTPCommand.append("GET ");
		HTTPCommand.append(filePath + " ");
		HTTPCommand.append("HTTP/1.1");
		HTTPCommand.append("\r\nHost: " + host);
		HTTPCommand.append("\r\nConnection: Keep-Alive");
		HTTPCommand.append("\r\n\r\n");
		
		String commandString = HTTPCommand.toString();
		
		this.connection = new ClientConnection(host, port, "UTF-8");
		connection.write(commandString);
		ResponseHeader header = connection.readHeader();
		String response = connection.process11Response(header);
		
		List<String> embeddedObjectNames = super.findEmbeddedObjects(response);
		if (! header.persistentConnectionAccepted())
		{
			connection.close();
			connection = new ClientConnection(host, port, "UTF-8");
		}
		
		this.receiveImages(embeddedObjectNames, host, port);
		
		connection.close();
		return header.getContents() + response;
	}

	@Override
	public String doSingleGet(String filePath, String host, int port)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String doHead(String filePath, String host, int port)
			throws IOException {
		StringBuilder HTTPCommand = new StringBuilder();
		
		HTTPCommand.append("HEAD ");
		HTTPCommand.append(filePath + " ");
		HTTPCommand.append("HTTP/1.1");
		HTTPCommand.append("\r\nHost: " + host);
		HTTPCommand.append("\r\nConnection: Keep-Alive");
		HTTPCommand.append("\r\n\r\n");
		
		this.connection = new ClientConnection(host, port, "UTF-8");
		connection.write(HTTPCommand.toString());
		ResponseHeader header = connection.readHeader();
		connection.close();
		return header.getContents();
	}

	@Override
	public String doPost(String filePath, String body, String host, int port)
			throws IOException {
		StringBuilder HTTPCommand = new StringBuilder();
		int bytes = super.countBytes(body);
		
		HTTPCommand.append("POST ");
		HTTPCommand.append(filePath + " ");
		HTTPCommand.append("HTTP/1.1");
		HTTPCommand.append("\r\nHost: " + host);
		HTTPCommand.append("\r\nConnection: Keep-Alive");
		HTTPCommand.append("\r\nContent-Length: " + bytes);
		HTTPCommand.append("\r\n\r\n");
		HTTPCommand.append(body);
		
		this.connection = new ClientConnection(host, port, "UTF-8");
		this.connection.write(HTTPCommand.toString());
		
		ResponseHeader header = connection.readHeader();
		String response = connection.process11Response(header);
		
		connection.close();
		return header.getContents() + response;
	}

	@Override
	public String doPut(String filePath, String body, String host, int port)
			throws IOException {
		StringBuilder HTTPCommand = new StringBuilder();
		int bytes = super.countBytes(body);
		
		HTTPCommand.append("PUT ");
		HTTPCommand.append(filePath + " ");
		HTTPCommand.append("HTTP/1.1");
		HTTPCommand.append("\r\nHost: " + host);
		HTTPCommand.append("\r\nConnection: Keep-Alive");
		HTTPCommand.append("\r\nContent-Length: " + bytes);
		HTTPCommand.append("\r\n\r\n");
		HTTPCommand.append(body);
		
		String commandString = HTTPCommand.toString();
		
		this.connection = new ClientConnection(host, port, "UTF-8");
		this.connection.write(commandString);
		
		ResponseHeader header = connection.readHeader();
		
		connection.close();
		return header.getContents();
	}
	
	private void receiveImages(List<String> embeddedObjectNames, String host, int port) throws IOException
	{
		for (int i = 0; i < embeddedObjectNames.size(); i++) {
			StringBuilder HTTPCommand = new StringBuilder();
			HTTPCommand.append("GET ");
			HTTPCommand.append(embeddedObjectNames.get(i) + " ");
			HTTPCommand.append("HTTP/1.1");
			HTTPCommand.append("\r\nHost: " + host);
			if (i == embeddedObjectNames.size() - 1)
			{
				HTTPCommand.append("\r\nConnection: close");
			}
			else
			{
				HTTPCommand.append("\r\nConnection: Keep-Alive");
			}
			HTTPCommand.append("\r\n\r\n");
			connection.write(HTTPCommand.toString());
		}
		for (int i = 0; i < embeddedObjectNames.size(); i++) {
			ResponseHeader header = connection.readHeader();
			connection.readAndStoreImage(header);
			if (! header.persistentConnectionAccepted())
			{
				connection = new ClientConnection(host, port, "UTF-8");
			}
		}
		System.out.println("Saved " + embeddedObjectNames.size() + " images");
	}

}
