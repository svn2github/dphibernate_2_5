package org.dphibernate.util
{
	import org.dphibernate.core.IEntity;
	import org.dphibernate.persistence.state.IEntityDescriptor;

	public class KeyGenerator
	{
		public static function getKeyForEntity(entity:IEntity):String
		{
			return ClassUtils.getRemoteClassName(entity) + "." + entity.entityKey;
		}
		public static function getKeyForDescriptor(descriptor:IEntityDescriptor):String
		{
			return descriptor.remoteClassName + "." + descriptor.proxyId;
		}
	}
}