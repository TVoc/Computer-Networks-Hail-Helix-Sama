package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import client.implementation.ClientHTTP10;
import client.implementation.ClientHTTP11;
import client.interfaces.ClientHTTP;

public class HTTP11ClientTest {

	ClientHTTP client = new ClientHTTP11();
	String host = "www.gianitp.com";
	int port = 80;
	
	@Test
	public void embeddedGetTest404() throws IOException {
		String response = client.doGet("/this-cannot-exist.html", host, port);
		System.out.println(response);
	}
	
	@Test
	public void embeddedGetTest() throws IOException
	{
		String response = client.doGet("/index.html", host, port);
		System.out.println(response);
	}
	
	@Test
	public void headTest() throws IOException {
		String response = client.doHead("/", host, port);
		System.out.println(response);
	}
	
	@Test
	public void postTest() throws IOException {
		String response = client.doPost("/clientplayground.txt", "Brevity is the soul of wit.", host, port);
		System.out.println(response);
	}
	
	@Test
	public void putTest() throws IOException {
		String response = client.doPut("/puttest.txt", "Brevity is the soul of wit.", host, port);
		System.out.println(response);
	}
}
