package org.dphibernate.util
{
	import flash.utils.describeType;
	
	import mx.utils.DescribeTypeCache;
	import mx.utils.DescribeTypeCacheRecord;

	public class ClassUtils
	{
		public function ClassUtils()
		{
		}

		public static function getRemoteClassName(object:Object):String
		{
			var type:XML=describeType(object);
			var remoteName:String=type.@alias
			return remoteName;
		}

		public static function getAccessors(object:Object):XMLList
		{
			var cacheRecord:DescribeTypeCacheRecord=DescribeTypeCache.describeType(object);
			var entry:XML; 
			var accessors:XMLList
			entry=cacheRecord.typeDescription;
			accessors=entry.accessor;
			return accessors;
		}
		public static function isTransient( object : Object , propertyName : String ) : Boolean
		{
			var entry:XML; 
			var accessors:XMLList
			var cacheRecord:DescribeTypeCacheRecord=DescribeTypeCache.describeType(object);
			entry=cacheRecord.typeDescription;
			accessors=entry.accessor.(@name==propertyName).metadata.(@name=='Transient');
			if ( accessors.length() == 0 ) return false;
			return true;
		}
		public static function isImmutable( object : Object ) : Boolean
		{
			var entry:XML; 
			var immutableTag:XMLList
			var cacheRecord:DescribeTypeCacheRecord=DescribeTypeCache.describeType(object);
			entry=cacheRecord.typeDescription;
			immutableTag=entry.metadata.(@name=='Immutable');
			if ( immutableTag.length() == 0 ) return false;
			return true;
		}
		public static function isPropertyImmutable( object : Object , propertyName : String ) : Boolean
		{
			var entry:XML; 
			var immutableTag:XMLList
			var cacheRecord:DescribeTypeCacheRecord=DescribeTypeCache.describeType(object);
			entry=cacheRecord.typeDescription;
			immutableTag=entry.accessor.(@name==propertyName).metadata.(@name=='Immutable');
			if ( immutableTag.length() == 0 ) return false;
			return true;
		}
	}
}