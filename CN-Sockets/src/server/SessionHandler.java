package server;

import java.io.File;
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
		this.charset = server.getCharSet();
	}

	@Override
	public void run() {
		while (! connection.isClosed()) {
			try {
				// in case of a persistent connection, check if it doesn't time out
				if (connectionTimeout())
				{
					connection.close();
					continue;
				}
				// read the request and analyse it
				String requestMethod = connection.readLine();
				if (!isValidRequestMethod(requestMethod)) {
					notifyInvalidRequest();
					continue;
				}
				String[] requestParts = requestMethod.split(" ");
				String method = requestParts[0];
				String filePath = requestParts[1];
				String protocol = requestParts[2].split("/")[1];
				if (!server.isAllowedMethod(method)
						|| !server.isAllowedProtocol(protocol)) {
					notifyInvalidRequest();
					connection.close();
					return;
				}
				// HTTP 1.1 requires sending back a continue header
				if (protocol.equals("1.1"))
				{
					sendContinue();
				}
				// client will most likely send a header along with the request: read it
				SingleRequestHeader header = readHeader();
				// if protocol is 1.1 and client requested persistent connection, make it so
				if (protocol.equals("1.1") && header.connectionKeepAlive())
				{
					persistentConnection = true;
				}
				// handle the requested method
				try 
				{
					if (method.equals("GET"))
					{
						handleGet(filePath);
					}
					if (method.equals("HEAD"))
					{
						handleHead(filePath);
					}
					if (method.equals("POST"))
					{
						if (! header.containsContentLength())
						{
							notifyInvalidRequest();
						}
						handlePost(filePath, header);
					}
					if (method.equals("PUT"))
					{
						if (! header.containsContentLength())
						{
							notifyInvalidRequest();
						}
						handlePut(filePath, header);
					}
				} 
				catch (FileNotFoundException e) 
				{
					notifyNotFound();
				}
				catch (IOException e)
				{
					notifyServerError();
				}
				if (! persistentConnection)
				{
					connection.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private boolean connectionTimeout()
	{
		boolean clientReady = false;
		long startTime = System.currentTimeMillis();
		long timeoutTime = startTime + server.getTimeoutMillis();
		while ((System.currentTimeMillis() < timeoutTime) && ! clientReady)
		{
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				clientReady = connection.readyForRead();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return clientReady;
	}
	
	private SingleRequestHeader readHeader() throws IOException
	{
		SingleRequestHeader toReturn = new SingleRequestHeader();
		String newLine = connection.readLine();
		while (! newLine.equals(" "))
		{
			newLine = newLine.trim();
			newLine = newLine.toLowerCase();
			if (newLine.contains("host"))
			{
				toReturn.setContainsHost(true);
			}
			if (newLine.contains("connection"))
			{
				String[] connectionString = newLine.split(":");
				if (connectionString.length == 2)
				{
					if (connectionString[1].equals("keep-alive"))
					{
						toReturn.setConnectionKeepAlive(true);
					}
				}
			}
			if (newLine.contains("content-length"))
			{
				toReturn.setContainsContentLength(true);
				try
				{
					int contentLength = Integer.parseInt(newLine.split(":")[1]);
					toReturn.setContentLength(contentLength);
				}
				catch (NumberFormatException | IndexOutOfBoundsException e)
				{
					notifyInvalidRequest();
					connection.close();
				}
				toReturn.setContentLength(Integer.parseInt(newLine.split(":")[1]));
			}
		}
		return toReturn;
	}
	
	private void sendContinue() throws IOException
	{
		connection.write("HTTP/1.1 100 Continue\r\n\r\n");
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
	
	private void notifyInvalidRequest() throws IOException
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
		connection.write(response.toString());
	}
	
	private void notifyNotFound()
	{
		try {
			connection.write("HTTP/1.1 404 File not found\r\n");
			connection.write("HTTP/1.1 File not found\r\n\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void notifyServerError()
	{
		try {
			connection.write("HTTP/1.1 500 Server Error\r\n");
			connection.write("<p>Server error</p>\r\n\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void handleGet(String filePath) throws FileNotFoundException, IOException
	{
		String fileContents;
		if (filePath.equals("/"))
		{
			fileContents = FileHandler.INSTANCE.read("/index.html");
		}
		fileContents = FileHandler.INSTANCE.read(filePath);
		int fileLength = counter.countBytes(fileContents, charset);
		connection.write("HTTP/1.1 200 OK\r\n");
		connection.write("Content-Length: " + fileLength + "\r\n\r\n");
		connection.write(fileContents);
	}
	
	private void handleHead(String filePath) throws FileNotFoundException, IOException
	{
		String fileContents = FileHandler.INSTANCE.read(filePath);
		int fileLength = counter.countBytes(fileContents, charset);
		connection.write("HTTP/1.1 200 OK\r\n");
		connection.write("Content-Length: " + fileLength + "\r\n\r\n");
	}
	
	private void handlePost(String filePath, SingleRequestHeader header) throws FileNotFoundException, IOException
	{
		File file = new File(filePath);
		if (! file.exists() || file.isDirectory())
		{
			throw new FileNotFoundException();
		}
		String body = connection.readLength(header.getContentLength());
		String fileContents = FileHandler.INSTANCE.writeAndRead(filePath, body);
		int fileLength = counter.countBytes(fileContents, charset);
		connection.write("HTTP/1.1 200 OK\r\n");
		connection.write("Content-Length: " + fileLength + "\r\n\r\n");
		connection.write(fileContents);
	}
	
	private void handlePut(String filePath, SingleRequestHeader header) throws IOException
	{
		String body = connection.readLength(header.getContentLength());
		FileHandler.INSTANCE.write(filePath, body);
		connection.write("HTTP/1.1 200 OK\r\n\r\n");
	}

}
