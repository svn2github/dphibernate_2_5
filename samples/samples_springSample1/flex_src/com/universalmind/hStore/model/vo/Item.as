package com.universalmind.hStore.model.vo
{
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="com.universalmind.hStore.model.vo.Item")]
	[Managed]
	public class Item extends HibernateBean
	{
		public var  id			:String = "";
		public var  sku			:Number = 0;
		public var  name		:String = null;
		public var  description	:String = null;
		public var  price		:Number = 0;
		public var  added		:Date = new Date();
		public var  expires		:Date = new Date();
		
		public function Item(){}
	}
}