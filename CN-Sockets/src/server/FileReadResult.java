package server;

import java.util.List;

public class FileReadResult {
	
	public FileReadResult(byte[] bytes)
	{
		this.bytes = bytes;
	}
	
	private final byte[] bytes;
	
	public byte[] getBytes()
	{
		return this.bytes;
	}
	
	public int getNumTotalBytes()
	{
		return this.getBytes().length;
	}

}
