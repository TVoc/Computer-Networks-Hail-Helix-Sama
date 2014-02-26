package client;

import java.util.Scanner;

public class ClientCommandLine {

	public static void main(String[] args) {
		ClientHTTP client = new ClientHTTP();
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter command. Expected syntax: <HTTP command> <URI> <port> <HTTP version>");
		

	}

}
