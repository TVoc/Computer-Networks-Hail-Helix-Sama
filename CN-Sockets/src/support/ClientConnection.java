package support;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class ClientConnection {
	
	private Socket socket;
	private BufferedReader in;
	private DataInputStream rawIn;
	private DataOutputStream out;
	private String charset;
	
	public ClientConnection(String host, int port, String charset) throws IOException
	{
		socket = new Socket(InetAddress.getByName(host), port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
		rawIn = new DataInputStream(socket.getInputStream());
		out = new DataOutputStream (socket.getOutputStream());
		this.charset = charset;
	}
	
	public void write(String message) throws IOException
	{
		out.writeBytes(message);
	}
	
	public String readLine() throws IOException
	{
		return in.readLine();
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
	
	public void readAndStoreImage() throws IOException
	{	
		// get image size from header
		int imageLength = 0;
		String lineFromServer = in.readLine();
		while (! lineFromServer.equals(""))
		{
			lineFromServer = lineFromServer.replaceAll(" ", "");
			if (lineFromServer.toLowerCase().contains("content-length"))
			{
				imageLength = Integer.parseInt(lineFromServer.split(":")[1]);
			}
			lineFromServer = in.readLine();
		}
		
		FileOutputStream fileOut = new FileOutputStream("clientdownloads/" + System.currentTimeMillis() + ".jpg");
		int bytesRead = 0;
		byte[] buffer = new byte[imageLength];
		while (bytesRead < imageLength)
		{
			int step = rawIn.read(buffer, bytesRead, buffer.length - bytesRead);
			bytesRead += step;
		}
		fileOut.write(buffer);
		fileOut.close();
	}
	
	public void readAndStoreImage(ResponseHeader header) throws IOException
	{
				
		FileOutputStream fileOut = new FileOutputStream("clientdownloads/" + System.currentTimeMillis() + ".jpg");
		int bytesRead = 0;
		byte[] buffer = new byte[header.getContentLength()];
		while (bytesRead < buffer.length)
		{
			int step = rawIn.read(buffer, bytesRead, buffer.length - bytesRead);
			bytesRead += step;
		}
		fileOut.write(buffer);
		fileOut.close();
	}
	
	public String process11Response(ResponseHeader header) throws IOException
	{
		byte[] buffer = new byte[header.getContentLength()];
		int offset = 0;
		int step = 0;
//		rawIn.readFully(buffer, 0, buffer.length);
		System.out.println("ping");
		while (step != -1) 
		{
			step = rawIn.read(buffer, offset, buffer.length - offset);
			System.out.println("Step: " + step);
			offset += step;
		}
//		while (offset < buffer.length)
//		{
//			step = rawIn.read(buffer, offset, buffer.length - offset);
//			if (step == -1)
//				break;
//			System.out.println(step);
//			offset += step;
//		}
		String result = new String(buffer, this.charset);
		System.out.println(result);
		return result;
	}
	
	public ResponseHeader readHeader() throws IOException
	{
		ResponseHeader header = new ResponseHeader();
		String lineFromServer = in.readLine();
		System.out.println(lineFromServer);
		if (lineFromServer.toLowerCase().contains("100 continue"))
		{
			lineFromServer = in.readLine(); // consume empty line
			lineFromServer = in.readLine(); // first line from relevant header
		}
		while (! lineFromServer.equals(""))
		{
			header.append(lineFromServer + "\n");
			lineFromServer = lineFromServer.toLowerCase().replaceAll(" ", "");
			if (lineFromServer.contains("connection"))
			{
				String keepAlive = lineFromServer.split(":")[1];
				if (keepAlive.toLowerCase().equals("keep-alive"))
				{
					header.setPersistentConnectionAccepted(true);
				}
			}
			if (lineFromServer.contains("content-length"))
			{
				header.setContentLength(Integer.parseInt(lineFromServer.split(":")[1]));
			}
			lineFromServer = in.readLine();
		}
		return header;
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
