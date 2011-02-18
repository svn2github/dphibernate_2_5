package org.dphibernate.util
{
	import mx.binding.utils.ChangeWatcher;

	public class PropertyHelper
	{
		private var _func : Function;
		private var _funcParams : Array;
		private var _host : Object;
		private var _propertiesToWatch : Array;
		
		private var changeWatchers : Array = []; // of ChangeWatcher
		public function invokeWhenAllPopulated( func : Function , funcParams : Array , host : Object , propertiesToWatch : Array ) : void
		{
			_func = func;
			_funcParams = funcParams;
			_host = host;
			_propertiesToWatch = propertiesToWatch;
			setup();
		}
		
		private function check(...ignored) : void
		{
			for each ( var propName : String in _propertiesToWatch )
			{
				if ( _host[propName] == null )
				{
					return;
				}
			}
			cleanUp();
			invoke();
		}
		
		private function cleanUp() : void
		{
			for each ( var c : ChangeWatcher in changeWatchers )
			{
				c.unwatch();
			}
			changeWatchers = null;
		}
		
		private function invoke() : void
		{
			_func.call( this ); //.apply( _funcParams );
		}
		private function setup() : void
		{
			for each ( var propName : String in _propertiesToWatch )
			{
				changeWatchers.push( ChangeWatcher.watch( _host , propName , check ) );
			}
		}
	}
}