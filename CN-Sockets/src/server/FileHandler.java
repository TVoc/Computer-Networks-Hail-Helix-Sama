package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
	
	public static final FileHandler INSTANCE = new FileHandler();
	
	private FileHandler() // singleton pattern
	{
		
	}
	
	public synchronized String read(String filePath) throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(ServerHTTP.ROOT_SERVER_FILES + filePath));
		StringBuilder output = new StringBuilder();
		String line = reader.readLine();
		while (line != null)
		{
			output.append(line);
			line = reader.readLine();
			if (line != null)
			{
				output.append("\n");
			}
		}
		reader.close();
		return output.toString();
	}
	
	public synchronized void write(String filePath, String input) throws FileNotFoundException, IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(ServerHTTP.ROOT_SERVER_FILES + filePath, true));
		writer.write(input);
		writer.close();
	}
	
	public synchronized String writeAndRead(String filePath, String input) throws FileNotFoundException, IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(ServerHTTP.ROOT_SERVER_FILES + filePath, true));
		writer.write(input);
		writer.close();
		return nonSyncRead(filePath);
	}
	
	private String nonSyncRead(String filePath) throws FileNotFoundException, IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		StringBuilder output = new StringBuilder();
		String line = reader.readLine();
		while (line != null)
		{
			output.append(line);
			line = reader.readLine();
			if (line != null)
			{
				output.append("\n");
			}
		}
		reader.close();
		return output.toString();
	}

}
