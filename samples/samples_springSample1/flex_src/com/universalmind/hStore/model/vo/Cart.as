package com.universalmind.hStore.model.vo
{
	import mx.collections.ArrayCollection;
	
	import net.digitalprimates.persistence.hibernate.HibernateBean;
	
	[RemoteClass(alias="com.universalmind.hStore.model.vo.Cart")]
	[Managed]
	public class Cart extends HibernateBean
	{
		private var __id		:String = null;
		private var __cartDate	:Date = null;
		private var __orderID	:String = null;
		private var __items		:ArrayCollection = null;
		private var __total		:Number = 0;
		
		
		public function get id():String{
			return this.__id;
		}
		public function set id(id:String):void{
			this.__id = id;
		}
		
		public function get cartDate():Date{
			return this.__cartDate;
		}
		public function set cartDate(cartDate:Date):void{
			this.__cartDate = cartDate;
		}
		
		public function get orderID():String{
			return this.__orderID;
		}
		public function set orderID(orderID:String):void{
			this.__orderID = orderID;	
		}
		
		public function get items():ArrayCollection{
			return this.__items;
		}
		
		public function set items(items:ArrayCollection):void{
			this.__items = items;
		}
		
		public function get total():Number{
			return this.__total;
		}
		
		public function set total(total:Number):void{
			this.__total = total;
		}

	}
}