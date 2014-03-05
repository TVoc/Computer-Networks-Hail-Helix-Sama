package server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import support.ServerConnection;
import support.StringByteCounter;

public class SessionHandler implements Runnable {
	
	private ServerHTTP server;
	private ServerConnection connection;
	private StringByteCounter counter = new StringByteCounter();
	private boolean persistentConnection;
	private String charset;
	
	public SessionHandler(Socket socket, ServerHTTP server) throws IllegalArgumentException, IOException
	{
		if (socket == null)
		{
			throw new IllegalArgumentException("Cannot initialise a session with non-existent socket");
		}
		connection = new ServerConnection(socket, charset);
		this.server = server;
	}

	@Override
	public void run() {
		try {
			String requestMethod = connection.readLine();
			if (! isValidRequestMethod(requestMethod))
			{
				notifyInvalidRequest();
				connection.close();
				return;
			}
			String[] requestParts = requestMethod.split(" ");
			String method = requestParts[0];
			String filePath = requestParts[1];
			String protocol = requestParts[2].split("/")[1];
			if (! server.isAllowedMethod(method) || ! server.isAllowedProtocol(protocol))
			{
				notifyInvalidRequest();
				connection.close();
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isValidRequestMethod(String requestMethod)
	{
		String[] parts = requestMethod.split(" ");
		if (parts.length != 3)
		{
			return false;
		}
		String method = parts[0];
		if (! server.isAllowedMethod(method))
		{
			return false;
		}
		String version = parts[2];
		String[] protocolRequestParts = version.split("/");
		if (protocolRequestParts.length != 2 || ! protocolRequestParts[0].equals("HTTP"))
		{
			return false;
		}
		return true;
	}
	
	private void notifyInvalidRequest()
	{
		StringBuilder response = new StringBuilder();
		String body = null;
		int contentLength = 0;
		try {
			body = FileHandler.INSTANCE.read("badrequest.html");
			contentLength = counter.countBytes(body);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		response.append("HTTP/1.1 400 Bad Request\r\n");
		response.append("Content-Type: text/html\r\n");
		response.append("Content-Length: " + contentLength + "\r\n");
		response.append("\r\n");
		response.append(body);
		
	}

}
