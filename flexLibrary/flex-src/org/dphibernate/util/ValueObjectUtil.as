/**
	Copyright (c) 2008. Digital Primates IT Consulting Group
	http://www.digitalprimates.net
	All rights reserved.
	
	This library is free software; you can redistribute it and/or modify it under the 
	terms of the GNU Lesser General Public License as published by the Free Software 
	Foundation; either version 2.1 of the License.

	This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
	without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
	See the GNU Lesser General Public License for more details.

	
	@author: malabriola
	@ignore
**/


package org.dphibernate.util
{
	import flash.utils.Dictionary;
	import flash.utils.describeType;
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ICollectionView;
	import mx.utils.ObjectUtil;
	

	public class ValueObjectUtil
	{
		protected static var accesssorTypeMap:Dictionary = new Dictionary();

		public static function populateVO( genericObj:Object, classDefinition:Class, existingValueObject:Object=null, dictionary:Dictionary=null ):Object {			
			var valueObj:Object;
			var classInfo:XML;
			var accessors:XMLList;

			if ( !genericObj ) {
				return null;
			}

			if ( dictionary[ genericObj ] == true ) {
				return genericObj;
			}

			dictionary[ genericObj ] = true;

			if ( accesssorTypeMap[ classDefinition ] == null ) {
				classInfo = describeType( classDefinition );
				accesssorTypeMap[ classDefinition ] = classInfo.factory.accessor;
			}

			accessors = accesssorTypeMap[ classDefinition ] as XMLList;

			var property:String;
			var type:String;
			var access:String;
			
			if ( existingValueObject != null ) {
				valueObj = existingValueObject;
			} else {
				
				var name:String = getQualifiedClassName( classDefinition );
				
				if ( name.indexOf( "::I" ) == -1 ) {
					valueObj = new classDefinition();
				} else {
					return genericObj;
				}
			}

			for ( var i:int=0; i<accessors.length(); i++ ) {					
				property = accessors[i].@name;
				type = accessors[i].@type;
				access = accessors[i].@access;
				
				if ( genericObj.hasOwnProperty( property ) && ( access != "readonly" ) ) {
					if ( type == 'Date' ) {
						if ( genericObj[ property ] is Date ) {
							valueObj[ property ] = new Date( ( genericObj[ property ] as Date ).getTime() );									
						} else {
							valueObj[ property ] = new Date( genericObj[ property ] );
						}
					} else {
						var voHelper:VOHelper = getVoInformation( accessors[i] );
						
						if ( voHelper.isSimple ) {
							valueObj[ property ] = genericObj[ property ];
						} else if ( voHelper.isArray ) {
							valueObj[ property ] = new Array();

							for ( var j:int=0; j<genericObj[ property ].length; j++ ) {
								valueObj[ property ].push( populateVO( genericObj[ property ][ j ], voHelper.elementClass, null, dictionary ) );
							}
						} else if ( voHelper.elementClass ) {
							if ( voHelper.elementClass is ICollectionView ) {
								trace('here');
							}
							if ( property != 'list' ) {
								valueObj[ property ] = populateVO( genericObj[ property ], voHelper.elementClass, null, dictionary );
							} else {
								//trace("break here");
							}
						} else {								
							valueObj[ property ] = ObjectUtil.copy( genericObj[ property ] );
						}
					}
				}
			}

			return valueObj;
		}

		protected static function getVoInformation( accessor:XML ):VOHelper {
			var type:String;
			var simpleArrayCopy:Boolean;
			var elementType:String;
			var elementClass:Class;
			var helper:VOHelper = new VOHelper();

			type = accessor.@type;

			if ( type == 'Array' ) {
				helper.isArray = true;
				var elementTypes:XMLList = accessor.metadata.(@name=="ArrayElementType");

				simpleArrayCopy = true;

				if ( elementTypes.length() > 0 ) {
					elementType = elementTypes[0].arg.@value;
					elementClass = getVoDefinitionByName( elementType );

					if ( elementClass != null ) {
						simpleArrayCopy = isSimpleType( elementType );
					}
				}

				if ( simpleArrayCopy ) {
					helper.isSimple = true;
					elementClass = null;
				}					
			} else if ( isSimpleType( type ) ) {
				helper.isSimple = true;
				helper.isArray = false;
			} else {
				//some type of object, try to call recursively and see what we can do
				elementClass = getVoDefinitionByName( type );
				helper.isSimple = false;
				helper.isArray = false;
				
				if ( type == 'Object' ) {						
					elementClass = null
				}
			}

			helper.elementClass = elementClass;
			return helper;
		}

		protected static var simpleTypes:Array = ['String','Number','uint','int','Boolean','Date', 'Array'];
		protected static function isSimpleType( type:String ):Boolean {
			for ( var i:int = 0; i<simpleTypes.length; i++ ) {
				if ( type == simpleTypes[ i ] ) {
					return true;
				}
			}
			
			return false;
		}
		
		protected static function getVoDefinitionByName( name:String ):Class {
			var voClass:Class;
			
			try {
				voClass = getDefinitionByName( name ) as Class;
			}
			
			catch (error:Error ) {
				trace("Can not get definition of valueObject " + name );
				voClass = null; //getDefinitionByName( "Object" ) as Class;
			}
			
			return voClass;
		}
	}
}

class VOHelper
{
	//simple, arrayvo, vo
	public var isSimple:Boolean = false;
	public var isArray:Boolean = false;
	public var elementClass:Class;
}
