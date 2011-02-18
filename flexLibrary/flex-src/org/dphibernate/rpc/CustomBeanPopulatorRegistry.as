package org.dphibernate.rpc
{
	import flash.utils.Dictionary;
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	import flash.utils.getQualifiedSuperclassName;

	public class CustomBeanPopulatorRegistry
	{
		public function CustomBeanPopulatorRegistry()
		{
		}
		
		private static var registry : Dictionary = new Dictionary();
		private static var populatorCache : Object = new Object();
		
		public static function registerPopulator( targetClass : Class , populator : IBeanPopulator ) : void
		{
			registry[ targetClass ] = populator;
			// Reset cache
			populatorCache = new Object();
		}
		public static function containsPopulatorForClass( targetClass : Class ) : Boolean
		{
			return getPopulator( targetClass ) != null;
		}
		public static function getPopulator( targetClass : Class ) : IBeanPopulator
		{
			var populator : IBeanPopulator;
			var keyClass : Class = targetClass;
			var targetClassName : String = getQualifiedClassName( targetClass );
			if ( populatorCache.hasOwnProperty( targetClassName ) )
			{
				return populatorCache[ targetClassName ];
			}
			populator = registry[ keyClass ];
			while ( populator == null )
			{
				var parentClassName : String = getQualifiedSuperclassName( keyClass );
				if ( !parentClassName )
				{
					break;
				}
				keyClass = getDefinitionByName( parentClassName ) as Class;
				populator = registry[ keyClass ];
			}
			populatorCache[targetClassName] = populator;
			return populator;
		}
	}
}