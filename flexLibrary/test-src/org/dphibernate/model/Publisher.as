package org.dphibernate.model
{
	import org.dphibernate.core.BaseEntity;
	
	

	[RemoteClass(alias="net.digitalprimates.persistence.hibernate.testObjects.Publisher")]
	public class Publisher extends BaseEntity
	{
		public function Publisher()
		{
		}
		public var name : String;
		public var address : String;
	}
}