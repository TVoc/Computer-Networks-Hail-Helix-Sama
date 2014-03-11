package support;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import server.FileReadResult;

public class ServerConnection {
	
	private Socket socket;
	private BufferedReader in;
	private BufferedOutputStream out;
	private String charset;
	private StringByteCounter counter;
	
	public ServerConnection(Socket socket, String charset) throws IOException
	{
		this.socket = socket;
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
		this.out = new BufferedOutputStream(new DataOutputStream (socket.getOutputStream()));
		this.charset = charset;
		this.counter = new StringByteCounter();
	}
	
	public void write(String message) throws IOException
	{
		out.write(message.getBytes(this.charset));
	}
	
	public void write(FileReadResult fileContents) throws IOException
	{
		out.write(fileContents.getBytes());
		out.flush();
	}
	
	public String readLine() throws IOException
	{
		return in.readLine();
	}
	
	public String readLength(int bytes) throws IOException
	{
		StringBuilder toReturn = new StringBuilder();
		
		char[] charBuffer = new char[1024];
		int totalRead = 0;
		while (totalRead < bytes)
		{
			int singleStepRead = in.read(charBuffer, 0, charBuffer.length);
			char[] copy = Arrays.copyOf(charBuffer, singleStepRead);
			String part =  new String(copy);
			totalRead += counter.countBytes(part, this.charset);
			toReturn.append(part);
		}
		return toReturn.toString();
	}
	
	public boolean readyForRead() throws IOException
	{
		if (this.isClosed())
		{
			return false;
		}
		return in.ready();
	}
	
	public boolean isClosed()
	{
		return socket.isClosed();
	}
	
	public void close() throws IOException
	{
		if (! socket.isClosed())
			socket.close();
		in.close();
		out.close();
		in = null;
		out = null;
	}

}
