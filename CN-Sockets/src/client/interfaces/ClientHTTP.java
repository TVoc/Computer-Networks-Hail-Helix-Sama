package client.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import support.StringByteCounter;

public abstract class ClientHTTP {
	
	private StringByteCounter counter;
	
	public ClientHTTP()
	{
		counter = new StringByteCounter();
	}

	public abstract String doGet(String filePath, String host, int port)
			throws IOException;

	public abstract String doSingleGet(String filePath, String host, int port)
			throws IOException;
	
	public abstract String doHead(String filePath, String host, int port)
			throws IOException;
	
	public abstract String doPost(String filePath, String body, String host, int port)
			throws IOException;
	
	public abstract String doPut(String filePath, String body, String host, int port)
			throws IOException;

	protected List<String> findEmbeddedObjects(String response)
	{

		List<String> toReturn = new ArrayList<String>();
		
		int index = response.toLowerCase().indexOf("<img");
		while (index != -1)
		{
			index += 4;
			response = response.substring(index, response.length());
			int srcIndex = response.indexOf("src=") + 4;
			response = response.substring(srcIndex, response.length());
			
			int URICursor = 0;
			char URIChar = response.charAt(URICursor);
			
			StringBuilder URI = new StringBuilder();
			while (URIChar != ' ' && URICursor < response.length())
			{
				URI.append(URIChar);
				URICursor++;
				URIChar = response.charAt(URICursor);
			}
			toReturn.add(URI.toString().replaceAll("\"",""));
			response = response.substring(srcIndex + URI.length());
			index = response.toLowerCase().indexOf("<img");
		}
		return toReturn;
	}
	
	protected int countBytes(String input) throws IOException
	{
		return counter.countBytes(input);
	}

}