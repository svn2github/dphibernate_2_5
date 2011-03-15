package org.dphibernate.loader.service
{
	import org.dphibernate.core.IEntity;

	public interface ILazyLoadServiceProvider
	{
		function getLoader( entity:IEntity ):ILazyLoadService;
	}
}