package client;

import java.io.Console;
import java.util.Arrays;
import java.io.IOException;

import java.io.*; 
import java.net.*; 

public class ClientHTTP {	
	
	public static boolean isValidCommand(String cmd) {
		return false;
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
