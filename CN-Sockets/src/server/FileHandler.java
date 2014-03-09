package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileHandler {
	
	public static final FileHandler INSTANCE = new FileHandler();
	
	private FileHandler() // singleton pattern
	{
		
	}
	
	public synchronized FileReadResult read(String filePath) throws FileNotFoundException, IOException
	{
		return readHelper(filePath);
	}
	
	public synchronized void write(String filePath, String input) throws FileNotFoundException, IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
		writer.write(input);
		writer.close();
	}
	
	public synchronized FileReadResult writeAndRead(String filePath, String input) throws FileNotFoundException, IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
		writer.write(input);
		writer.close();
		return nonSyncRead(filePath);
	}
	
	private FileReadResult nonSyncRead(String filePath) throws FileNotFoundException, IOException
	{
		return readHelper(filePath);
	}
	
	private FileReadResult readHelper(String filePath) throws FileNotFoundException, IOException
	{
		FileInputStream input = new FileInputStream(filePath);
		int numBytes = 0;
		List<byte[]> bytes = new ArrayList<byte[]>();
		
		byte[] buffer = new byte[1024];
		int read = 0;
		
		while (read >= 0)
		{
			read = input.read(buffer);
			if (read < 0)
			{
				continue;
			}
			byte[] truncatedToRealLength = Arrays.copyOf(buffer, read);
			bytes.add(truncatedToRealLength);
			numBytes += read;
		}
		input.close();
		return new FileReadResult(numBytes, bytes);
	}

}
