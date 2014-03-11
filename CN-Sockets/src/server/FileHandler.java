package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
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
		File file = new File(filePath);
		byte[] buffer = new byte[(int) file.length()];
		FileInputStream input = new FileInputStream(filePath);
		
		int read = 0;
		int offset = 0;
		
		while (offset < buffer.length)
		{
			read = input.read(buffer, offset, buffer.length - offset);
			offset += read;
		}
		input.close();
		return new FileReadResult(buffer);
	}

}
