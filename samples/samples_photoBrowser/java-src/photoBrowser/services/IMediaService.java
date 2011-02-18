package photoBrowser.services;

import java.util.Collection;

public interface IMediaService
{
	public Object save(Object obj);
	public Collection getAllMediaSets();
	public void initializeDatabase(String photoDir);
}
