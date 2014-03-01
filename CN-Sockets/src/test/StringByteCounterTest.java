package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import support.StringByteCounter;

public class StringByteCounterTest {
	
	StringByteCounter counter = new StringByteCounter();

	@Test
	public void simpleStringTestUTF8() throws IOException {
		String testString = "The die has been cast.";
		assertEquals(counter.countBytes(testString, "UTF-8"), 22);
	}
	
	@Test
	public void multipleByteCharacterTestUTF8() throws IOException {
		String testString = "´´";
		assertEquals(counter.countBytes(testString, "UTF-8"), 4);
	}

}
