package org.dphibernate.events
{
	import flash.events.Event;

	public class LazyLoadEvent extends Event
	{
		public static const pending:String = "LazyLoadPending";
		public static const complete:String = "LazyLoadComplete";
		public static const failed:String = "LazyLoadFailed";
		//public var proxy:IHibernateProxy;
		
		public function LazyLoadEvent( type:String, propertyName : String , parent : Object , bubbles:Boolean=false, cancelable:Boolean=false )
		{
			//this.proxy = proxy;
			super(type, bubbles, cancelable);
			this._propertyName = propertyName;
			this._parent = parent;
		}
		
		private var _propertyName : String;
		public function get propertyName() : String
		{
			return _propertyName;
		}
		private var _parent : Object
		public function get parent() : Object
		{
			return _parent;
		}		
		override public function clone():Event {
			return new LazyLoadEvent( type, propertyName, parent, bubbles, cancelable );
		}
	}
}