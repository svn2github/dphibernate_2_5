package org.dphibernate.persistence.state
{
	import org.dphibernate.util.ClassUtils;
	
	[RemoteClass(alias="org.dphibernate.persistence.state.ObjectChangeResult")]
	public class ObjectChangeResult
	{
		public function ObjectChangeResult()
		{
		}
		
		public var oldId : Object;
		public var newId : Object;
		public var remoteClassName : String;
		
		public static function create( objectClass : Class , oldId : Object , newId : Object ) : ObjectChangeResult
		{
			var className : String = ClassUtils.getRemoteClassName( objectClass );
			var result : ObjectChangeResult = new ObjectChangeResult();
			result.remoteClassName = className;
			result.oldId = oldId;
			result.newId = newId;
			return result;
		}
	}
}