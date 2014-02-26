package client;

import java.io.Console;
import java.util.Arrays;
import java.io.IOException;

import java.io.*; 
import java.net.*; 

public class ClientHTTP {
	
	public String[] doGet10(String filePath, String host, int port)
	{
		StringBuilder HTTPCommand = new StringBuilder("GET ");
		HTTPCommand.append(filePath + " HTTP/1.0 \n\n");
		
		Socket socket = new Socket(host, port);
	}
	
	public static void main(String[] args) throws Exception {
		
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); 
		
		Socket clientSocket = new Socket("localhost", 6789); 
		DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream()); 	
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
		
		String sentence = inFromUser.readLine(); 
		outToServer.writeBytes(sentence + '\n'); 
		String modifiedSentence = inFromServer.readLine(); 
		System.out.println("FROM SERVER: " + modifiedSentence); 
		clientSocket.close(); 
	}
}