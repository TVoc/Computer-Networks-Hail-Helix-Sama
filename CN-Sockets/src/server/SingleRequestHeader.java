package server;

public class SingleRequestHeader {
	
	private boolean containsHost = false;
	
	private boolean containsContentLength = false;
	private int contentLength = 0;
	
	private boolean connectionKeepAlive = false;
	
	public SingleRequestHeader(boolean containsHost, boolean connectionKeepAlive, boolean containsContentLength, int contentLength)
	{
		this.containsHost = containsHost;
		this.connectionKeepAlive = connectionKeepAlive;
		this.containsContentLength = containsContentLength;
		this.contentLength = contentLength;
	}
	
	public SingleRequestHeader(boolean containsHost)
	{
		this(containsHost, false, false, 0);
	}
	
	public SingleRequestHeader()
	{
		this(false, false, false, 0);
	}
	
	public boolean containsHost()
	{
		return this.containsHost;
	}
	
	public void setContainsHost(boolean containsHost)
	{
		this.containsHost = containsHost;
	}
	
	public boolean connectionKeepAlive()
	{
		return this.connectionKeepAlive;
	}
	
	public void setConnectionKeepAlive(boolean connectionKeepAlive)
	{
		this.connectionKeepAlive = connectionKeepAlive;
	}
	
	public boolean containsContentLength()
	{
		return this.containsContentLength;
	}
	
	public void setContainsContentLength(boolean containsContentLength)
	{
		this.containsContentLength = containsContentLength;
	}
	
	public int getContentLength()
	{
		return this.contentLength;
	}
	
	public void setContentLength(int contentLength)
	{
		this.contentLength = contentLength;
	}

}
