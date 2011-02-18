package org.dphibernate.util
{
	import com.hexagonstar.util.debug.StopWatch;
	
	import flash.utils.Dictionary;
	import flash.utils.getDefinitionByName;
	import flash.utils.getQualifiedClassName;
	
	import mx.collections.ICollectionView;
	import mx.utils.DescribeTypeCache;
	import mx.utils.ObjectUtil;
	
	import org.dphibernate.core.HibernateBean;
	import org.dphibernate.core.IHibernateProxy;
	import org.dphibernate.rpc.CustomBeanPopulatorRegistry;
	import org.dphibernate.rpc.HibernateManaged;
	import org.dphibernate.rpc.IBeanPopulator;
	import org.dphibernate.rpc.IHibernateRPC;
	

	public class BeanUtil
	{
		protected static var accesssorTypeMap:Dictionary = new Dictionary();
		
		private static function populateBeanPropertiesFromMap( map : Object , bean : Object , parent : Object ) : void
		{
			if ( bean is IHibernateProxy ) {
				HibernateManaged.manageHibernateObject( bean as IHibernateProxy, parent );				
			}
			for ( var propertyName:String in map )
			{
				try {
					bean[propertyName] = map[ propertyName ];
				} catch ( error : Error )
				{
					trace ("Could not set property " + propertyName + " on " + getQualifiedClassName(bean));
				}
			}
		}

		public static function populateBean( source:Object, 
												classDefinition:Class, 
												existingBean:Object=null, 
												recursionDict:Dictionary=null,
												parent:Object=null
												):void {
			var bean:Object;
			var classInfo:XML;
			var accessors:XMLList;
//			populateBeanPropertiesFromMap( genericObj , existingBean , parent );
//			return;
			
			if ( !source ) {
				return;
			}

			if ( !recursionDict ) {
				recursionDict = new Dictionary( true );
			}

			if ( recursionDict[ source ] == true ) {
				return;
			}

			recursionDict[ source ] = true;
			
			if ( CustomBeanPopulatorRegistry.containsPopulatorForClass( classDefinition ) )
			{
				var populator : IBeanPopulator = CustomBeanPopulatorRegistry.getPopulator( classDefinition );
				populator.populateBean( source , classDefinition , existingBean , recursionDict , parent );
				return;
			}
			if ( accesssorTypeMap[ classDefinition ] == null ) {
				classInfo = DescribeTypeCache.describeType( classDefinition ).typeDescription;
				accesssorTypeMap[ classDefinition ] = classInfo..accessor;
			}

			accessors = accesssorTypeMap[ classDefinition ] as XMLList;

			var property:String;
			var type:String;
			var access:String;
			
			if ( existingBean != null ) {
				bean = existingBean;
			} else {
				
				var name:String = getQualifiedClassName( classDefinition );
				
				if ( name.indexOf( "::I" ) == -1 ) {
					bean = new classDefinition();
				} else {
					return;
				}
			}

			if ( bean is IHibernateProxy ) {
				HibernateManaged.manageHibernateObject( bean as IHibernateProxy, parent );
				HibernateManaged.manageChildTree( bean );
			}

			if ( ( source is IHibernateProxy ) && ( bean is IHibernateProxy ) ) {
				IHibernateProxy( bean ).proxyInitialized = IHibernateProxy( source ).proxyInitialized;
				IHibernateProxy( bean ).proxyKey = IHibernateProxy( source ).proxyKey;				
			} 

			if ( source is IHibernateProxy ) {
				if ( !IHibernateProxy( source ).proxyInitialized ) {
					//if we are not initialized, do not dive any deeper, you will cause Hibernate to lazy load by any of these
					//actions
					return;
				}
			} 
				
			for ( var i:int=0; i<accessors.length(); i++ ) {			
						
				property = accessors[i].@name;
				type = accessors[i].@type;
				access = accessors[i].@access;
				
				if ( source.hasOwnProperty( property ) && ( access != "readonly" ) ) {
					if ( type == 'Date' ) {
						if ( source[ property ] is Date ) {
							bean[ property ] = new Date( ( source[ property ] as Date ).getTime() );									
						} else {
							bean[ property ] = new Date( source[ property ] );
						}
					} else {
						var beanHelper:BeanHelper = getBeanInformation( accessors[i] );
						
						if ( beanHelper.isSimple ) {
							bean[ property ] = source[ property ];
						} else if ( beanHelper.isArray ) {
							bean[ property ] = new Array();

							for ( var j:int=0; j<source[ property ].length; j++ ) {
								bean[ property ].push( populateBean( source[ property ][ j ], beanHelper.elementClass, null, recursionDict ) );
							}
						} else if ( beanHelper.elementClass ) {
							if ( beanHelper.elementClass is ICollectionView ) {
								trace('here');
							}
							if ( property != 'list' ) {
								bean[ property ] = source[ property ] //populateBean( source[ property ], beanHelper.elementClass, null, recursionDict, bean );
							} else {
								//trace("break here");
							}
						} else {								
							bean[ property ] = source[ property ] //ObjectUtil.copy( source[ property ] );
						}
					}
				}
			}

			if ( bean is IHibernateProxy && !IHibernateProxy( bean ).proxyKey ) {
				//trace("STOP!");
			}

		}

		protected static function getBeanInformation( accessor:XML ):BeanHelper {
			var type:String;
			var simpleArrayCopy:Boolean;
			var elementType:String;
			var elementClass:Class;
			var helper:BeanHelper = new BeanHelper();

			type = accessor.@type;

			if ( type == 'Array' ) {
				helper.isArray = true;
				var elementTypes:XMLList = accessor.metadata.(@name=="ArrayElementType");

				simpleArrayCopy = true;

				if ( elementTypes.length() > 0 ) {
					elementType = elementTypes[0].arg.@value;
					elementClass = getBeanDefinitionByName( elementType );

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
				elementClass = getBeanDefinitionByName( type );
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
		
		protected static function getBeanDefinitionByName( name:String ):Class {
			var beanClass:Class;
			
			try {
				beanClass = getDefinitionByName( name ) as Class;
			}
			
			catch (error:Error ) {
				trace("Can not get definition of bean " + name );
				beanClass = null; //getDefinitionByName( "Object" ) as Class;
			}
			
			return beanClass;
		}
	}
}

class BeanHelper
{
	//simple, arrayvo, vo
	public var isSimple:Boolean = false;
	public var isArray:Boolean = false;
	public var elementClass:Class;
}
