package photoBrowser.beans;

public interface IMediaChild
{
	public String getId();
	public void setId(String id);
	
	public MediaSet getParent();
	public void setParent(MediaSet parent);
}
