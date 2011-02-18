package org.dphibernate.util
{
	import flash.utils.getQualifiedClassName;
	
	import mx.logging.ILogger;
	import mx.logging.Log;
	import mx.logging.targets.TraceTarget;
	
	public class LogUtil
	{
		public static const DEFAULT_CATEGORY : String = "global";
		
		public static function addTraceTarget() : void 
		{
			var target : TraceTarget = new TraceTarget();
			target.includeCategory = true;
			target.includeLevel = true;
			Log.addTarget( target );
		}
		
		public static function getLogger( object : Object ) : ILogger  
		{
			try
			{
				var category : String = null;
				if( object is String )
				{ 
					category = object as String;
				}	
				else 
				{
					category = getQualifiedClassName( object ).replace("::",".").replace("$","-");
						
				}
				return Log.getLogger( category );
			}
			catch( error : Error )
			{
				throw new Error("Error getting Logger for :" + category + " message:" + error.message );
			}
			return Log.getLogger( DEFAULT_CATEGORY );
		}

	}
}