package support;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class ServerConnection {
	
	private Socket socket;
	private BufferedReader in;
	private DataOutputStream out;
	
	public ServerConnection(Socket socket, String charset) throws IOException
	{
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
		out = new DataOutputStream (socket.getOutputStream());
	}
	
	public void write(String message) throws IOException
	{
		out.writeBytes(message);
	}
	
	public String readLine() throws IOException
	{
		return in.readLine();
	}
	
	public String readLength(int bytes) throws IOException
	{
		StringBuilder toReturn = new StringBuilder();
		
		byte[] byteBuffer = new byte[1024];
		int read = 0;
		InputStream input = socket.getInputStream();
		while (read <= bytes)
		{
			read += input.read(byteBuffer, 0, byteBuffer.length);
			toReturn.append(new String(byteBuffer));
		}
		return toReturn.toString();
	}
	
	public String readUntilEmpty() throws IOException
	{
		StringBuilder response = new StringBuilder();
		String line = in.readLine();
		while (line != null)
		{
			if (! (response.toString() == ""))
			{
				response.append("\n");
			}
			response.append(line);
			line = in.readLine();
		}
		return response.toString();
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
