package photoBrowser.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;

import net.digitalprimates.persistence.hibernate.utils.services.HibernateService;

import photoBrowser.beans.MediaSet;
import photoBrowser.beans.Photo;
import photoBrowser.utils.DirectoryParser;

public class MediaService extends HibernateService implements IMediaService
{

	public Object load(Class clazz, Serializable id)
	{
		Object obj = super.load(clazz, id);
		return obj;
	}

	
	public Object save( Object obj )
	{
		return super.merge(obj);
	}

	
	public Collection getAllMediaSets()
	{
		// get parent properties
		DetachedCriteria criteria = DetachedCriteria.forClass(MediaSet.class);
		//criteria.add( Expression.isNotNull("parent") );
		
		//Collection photoResults = super.list(Photo.class);
		
		Collection results = super.list(MediaSet.class, criteria);
		return results;
	}

	
	public void initializeDatabase(String photoDir)
	{
		//todo: add TRUNCATE TABLE SQL
		
		List results = new DirectoryParser().parsePhotoDirectory(photoDir);
		
		java.util.Iterator itr = results.iterator();
		while( itr.hasNext() )
		{
			super.save(itr.next());
		}
	}
	
}
