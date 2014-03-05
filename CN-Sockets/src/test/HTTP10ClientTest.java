package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import client.implementation.ClientHTTP10;
import client.interfaces.ClientHTTP;

public class HTTP10ClientTest {

	ClientHTTP client = new ClientHTTP10();
	String host = "www.wolframalpha.com";
	int port = 80;
	
	@Test
	public void singleGetTest() throws IOException {
		String response = client.doSingleGet("/", host, port);
		assertNotEquals(response, null);
	}
	
	@Test
	public void singleGetTest404() throws IOException {
		String response = client.doSingleGet("/this-cannot-exist.html", host, port);
		assertTrue(response.contains("404"));
	}
	
	@Test
	public void embeddedGetTest404() throws IOException {
		List<String> response = client.doGet("/this-cannot-exist.html", host, port);
		assertEquals(response.size(), 1);
	}
	
	@Test
	public void embeddedGetTest() throws IOException
	{
		List<String> response = client.doGet("/", host, port);
		assertEquals(response.size(), 4); // examination of source code reveals three embedded images, so number of strings is initial response + number of images.
		for (String oneResponse : response)
		{
			System.out.println(oneResponse);
		}
	}
	
	@Test
	public void headTest() throws IOException {
		String response = client.doHead("/", host, port);
		assertNotEquals(response, null);
		System.out.println(response);
	}
	
	@Test
	public void postTest() throws IOException {
		String response = client.doPost("/", "Brevity is the soul of wit.", host, port);
		assertNotEquals(response, null);
		System.out.println(response);
	}
	
	@Test
	public void putTest() throws IOException {
		String response = client.doPost("/", "Brevity is the soul of wit.", host, port);
		assertNotEquals(response, null);
		System.out.println(response);
	}

}
