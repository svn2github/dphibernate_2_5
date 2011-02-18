package org.dphibernate.persistence.state
{
	import mx.utils.UIDUtil;

	[RemoteClass(alias="org.dphibernate.persistence.state.PropertyChangeMessage")]
	public class PropertyChangeMessage
	{
		public function PropertyChangeMessage( propertyName : String = null , oldValue : Object = null , newValue : Object = null) 
		{
			_propertyName = propertyName;
			_oldValue = oldValue;
			_newValue = newValue;
			_uid = UIDUtil.createUID();
		}
		private var _propertyName : String;
		private var _oldValue : Object;
		private var _newValue : Object;
		private var _uid:String;
		
		public function get uid():String
		{
			return _uid;
		}
		
		public function get propertyName() : String
		{
			return _propertyName;
		}
		public function set propertyName( value : String ) : void
		{
			this._propertyName = value;
		}
		public function get oldValue() : Object
		{
			return _oldValue;
		}
		public function set oldValue( value : Object ) : void
		{
			this._oldValue = value;
		}
		public function get newValue() : Object
		{
			return _newValue;
		}
		public function set newValue( value : Object ) : void
		{
			_newValue = value;
		}
		public function get oldAndNewValueMatch() : Boolean
		{
			return oldValue == newValue;
		}
		public function merge(valueToMerge:PropertyChangeMessage):PropertyChangeMessage
		{
			assertIsSameProperty(valueToMerge);
			return new PropertyChangeMessage(propertyName,oldValue,valueToMerge.newValue);
		}
		private function assertIsSameProperty(valueToMerge:PropertyChangeMessage):void
		{
			if (valueToMerge.propertyName != this.propertyName) throw new Error("Cannot merge properties with different property names");
		}
		public function mergeOldValue(valueToMerge:PropertyChangeMessage):PropertyChangeMessage
		{
			assertIsSameProperty(valueToMerge);
			return new PropertyChangeMessage(propertyName,newValue,valueToMerge.newValue);
		}
	}
}