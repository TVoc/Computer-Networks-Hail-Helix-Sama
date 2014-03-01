package client;

import java.util.Scanner;

import client.implementation.ClientHTTP10;
import client.interfaces.ClientHTTP;

public class ClientCommandLine {

	public static void main(String[] args) {
		ClientHTTP client = new ClientHTTP10();
		Scanner in = new Scanner(System.in);
		
		System.out.println("Enter command. Expected syntax: <HTTP command> <URI> <port> <HTTP version>");
		

	}

}