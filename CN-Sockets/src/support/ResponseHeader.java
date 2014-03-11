package support;

public class ResponseHeader {
	
	private boolean persistentConnectionAccepted = false;
	private int contentLength = 0;
	private StringBuilder contents = new StringBuilder();
	
	public boolean persistentConnectionAccepted()
	{
		return this.persistentConnectionAccepted;
	}
	
	public void setPersistentConnectionAccepted(boolean accepted)
	{
		this.persistentConnectionAccepted = accepted;
	}
	
	public int getContentLength()
	{
		return this.contentLength;
	}
	
	public void setContentLength(int contentLength)
	{
		this.contentLength = contentLength;
	}
	
	public void append(String input)
	{
		this.contents.append(input);
	}
	
	public String getContents()
	{
		return this.contents.toString();
	}

}
