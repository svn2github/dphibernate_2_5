package org.dphibernate.entitymanager
{
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.persistence.state.IHibernateProxyDescriptor;
	import org.dphibernate.persistence.state.StateRepository;
	
	public class EntityManager implements IEntityManager
	{
		private var entityMap:Object = {};
		public function EntityManager()
		{
		}
		
		public function putEntity(proxy:IHibernateProxy):String
		{
			var key:String = StateRepository.getKey(proxy);
			entityMap[key] = proxy;
			return null;
		}
		
		public function containsEntity(proxy:IHibernateProxy):Boolean
		{
			var key:String = StateRepository.getKey(proxy);
			return entityMap[key] != null;
		}
		
		public function getEntity(proxy:IHibernateProxy):IHibernateProxy
		{
			var key:String = StateRepository.getKey(proxy);
			return entityMap[key];
		}
		
		public function evictEntity(proxy:IHibernateProxy):IHibernateProxy
		{
			var key:String = StateRepository.getKey(proxy);
			var entityToEvict:IHibernateProxy = entityMap[key] as IHibernateProxy;
			if (entityToEvict)
			{
				delete entityMap[key];
			}
			return entityToEvict;
		}
		
		public function findForDescriptor(descriptor:IHibernateProxyDescriptor):IHibernateProxy
		{
			var key:String = StateRepository.getKeyForDescriptor(descriptor);
			return findForKey(key);
		}
		public function containsForDescriptor(descriptor:IHibernateProxyDescriptor):Boolean
		{
			var key:String = StateRepository.getKeyForDescriptor(descriptor);
			return containsKey(key);
		}
		public function findForKey(key:String):IHibernateProxy
		{
			return entityMap[key];
		}
		public function containsKey(key:String):Boolean
		{
			return entityMap[key] != null;
		}
	}
}