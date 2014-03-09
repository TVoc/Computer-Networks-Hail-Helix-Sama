package server;

import java.util.List;

public class FileReadResult {
	
	public FileReadResult(int numTotalBytes, List<byte[]> bytes)
	{
		this.numTotalBytes = numTotalBytes;
		this.bytes = bytes;
	}
	
	private final List<byte[]> bytes;
	
	public List<byte[]> getBytes()
	{
		return this.bytes;
	}
	
	public byte[] getBytesAt(int index)
	{
		byte[] ele = this.getBytes().get(index);
		return ele;
	}
	
	private final int numTotalBytes;
	
	public int getNumTotalBytes()
	{
		return this.numTotalBytes;
	}

}
