package support;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class StringByteCounter {
	
	public int countBytes(String input) throws IOException
	{
		return countBytes(input, "UTF-8");
	}
	
	public int countBytes(String input, String encoding) throws IOException
	{
		CountingOutputStream cos = new CountingOutputStream();
		Writer writer = new OutputStreamWriter(cos, encoding);
		writer.write(input);
		writer.close();
		return cos.total;
	}

	private class CountingOutputStream extends OutputStream {
	    int total;

	    @Override
	    public void write(int i) {
	        throw new RuntimeException("don't use");
	    }
	    @Override
	    public void write(byte[] b) {
	        total += b.length;
	    }

	    @Override 
	    public void write(byte[] b, int offset, int len) {
	        total += len;
	    }
	}
}