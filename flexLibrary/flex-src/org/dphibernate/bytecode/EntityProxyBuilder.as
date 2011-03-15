/**
 * Copyright (c) 2011 Digital Primates
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author     Michael Labriola
 * @version
 **/
package org.dphibernate.bytecode
{
	import flash.display.LoaderInfo;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.IOErrorEvent;
	import flash.net.registerClassAlias;
	import flash.utils.Dictionary;
	import flash.utils.getQualifiedClassName;
	
	import org.as3commons.bytecode.emit.IAbcBuilder;
	import org.as3commons.bytecode.emit.IClassBuilder;
	import org.as3commons.bytecode.proxy.IClassProxyInfo;
	import org.as3commons.bytecode.proxy.IProxyFactory;
	import org.as3commons.bytecode.proxy.ProxyScope;
	import org.as3commons.bytecode.proxy.event.ProxyFactoryBuildEvent;
	import org.as3commons.bytecode.proxy.impl.ProxyFactory;
	import org.as3commons.bytecode.proxy.impl.ProxyInfo;
	import org.as3commons.bytecode.reflect.ByteCodeType;
	import org.as3commons.reflect.ITypeProvider;
	import org.as3commons.reflect.Metadata;
	import org.as3commons.reflect.MetadataArgument;
	import org.as3commons.reflect.Type;
	import org.dphibernate.bytecode.decorators.EntityDecorator;
	import org.dphibernate.core.BaseEntity;
	import org.dphibernate.core.IEntity;
	import org.dphibernate.core.Metadata;
	import org.dphibernate.entitymanager.IEntityManager;
	import org.dphibernate.loader.IPropertyInterceptor;
	import org.dphibernate.loader.LazyLoaderFactory;
	import org.dphibernate.loader.PropertyInterceptor;
	import org.dphibernate.loader.service.ILazyLoadServiceProvider;

	[Event(name="complete",type="flash.events.Event")]
	[Event(name="verifyError",type="flash.events.IOErrorEvent")]
	public class EntityProxyBuilder extends EventDispatcher
	{
		private var loaderInfo:LoaderInfo;
		private var proxyFactory:IProxyFactory;
		private var mapping:Dictionary;
		private var entityManager:IEntityManager;
		private var serviceProvider:ILazyLoadServiceProvider;
		
		private function initStructures():void
		{
			var provider:ITypeProvider = ByteCodeType.getTypeProvider();
			if (provider.getTypeCache().size() == 0)
				ByteCodeType.fromLoader(loaderInfo);
		}

		private function findEntityNames():Array
		{
			return ByteCodeType.getClassesWithMetadata(org.dphibernate.core.Metadata.ENTITY);
		}

		private function defineEntity(entityName:String, proxyFactory:IProxyFactory):void
		{
			var type:ByteCodeType;
			var classProxyInfo:IClassProxyInfo;

			type=ByteCodeType.forName(entityName);
			var loaderFactory:LazyLoaderFactory = new LazyLoaderFactory(serviceProvider,entityManager);
			var propertyInterceptor:IPropertyInterceptor = new PropertyInterceptor(loaderFactory);

			classProxyInfo=proxyFactory.defineProxy(type.clazz);
			classProxyInfo.proxyAccessorScopes=ProxyScope.PUBLIC;
			classProxyInfo.proxyMethodScopes=ProxyScope.NONE;
			classProxyInfo.introduce(EntityDecorator);
			classProxyInfo.interceptorFactory=new EntityInterceptorFactory(HibernateEntityInterceptor, propertyInterceptor);
		}

		private function registerProxyClassReplacements():void
		{
			var type:ByteCodeType;
			var definitionNames:Array=findEntityNames();
			var proxyClass:Class;
			var alias:String;

			for (var i:int=0; i < definitionNames.length; i++)
			{
				type=ByteCodeType.forName(definitionNames[i]);
				trace("Registering " + type.name);

				proxyClass=getEntityClass(type.clazz);
				alias=getRemoteAlias(type);

				mapping[proxyClass]=type.clazz;

				if (alias && proxyClass)
				{
					registerClassAlias(alias, proxyClass);
				}
			}
		}

		private function getRemoteAlias(type:ByteCodeType):String
		{
			var ar:Array=type.getMetadata(org.dphibernate.core.Metadata.REMOTE_CLASS);
			var remoteAlias:String;

			if (ar)
			{
				for (var j:int=0; j < ar.length; j++)
				{
					var metadata:org.as3commons.reflect.Metadata=ar[j] as org.as3commons.reflect.Metadata;
					var argument:MetadataArgument=metadata.getArgument(org.dphibernate.core.Metadata.ALIAS);
					if (argument)
					{
						remoteAlias=argument.value;
						break;
					}
				}
			}

			return remoteAlias;
		}

		private function implementInterface(event:ProxyFactoryBuildEvent):void
		{
			//just here for now for linking
			var builder:IClassBuilder=event.classBuilder;
			builder.implementInterfaces([getQualifiedClassName(IEntity)]);
		}

		public function createEntity(clazz:Class, args:Array=null):*
		{
			return proxyFactory.createProxy(clazz, args);
		}

		public function getEntityClass(clazz:Class):Class
		{
			var proxyInfo:ProxyInfo=proxyFactory.getProxyInfoForClass(clazz);
			var proxyClass:Class;

			if (proxyInfo)
			{
				proxyClass=proxyInfo.proxyClass;
			}

			return proxyClass;
		}

		public function getProxiedClass(instance:*):Class
		{
			var proxy:Class=instance.constructor;

			return mapping[proxy];
		}

		public function manageEntities():void
		{
			var definitionNames:Array;

			initStructures();

			definitionNames=findEntityNames();

			for (var i:int=0; i < definitionNames.length; i++)
			{
				defineEntity(definitionNames[i], proxyFactory);
			}

			var abcBuilder:IAbcBuilder=proxyFactory.generateProxyClasses();
			proxyFactory.addEventListener(Event.COMPLETE, handleLoaded);
			proxyFactory.addEventListener(IOErrorEvent.VERIFY_ERROR, handleVerifyError);

			proxyFactory.loadProxyClasses();
		}

		private function handleLoaded(event:Event):void
		{
			registerProxyClassReplacements();
			dispatchEvent(event.clone());
		}

		private function handleVerifyError(event:IOErrorEvent):void
		{
			//something went terribly wrong during class generation...
			dispatchEvent(event.clone());
		}
		
		public function EntityProxyBuilder(loaderInfo:LoaderInfo,entityManager:IEntityManager,serviceProvider:ILazyLoadServiceProvider)
		{
			this.loaderInfo=loaderInfo;
			this.entityManager = entityManager;
			this.serviceProvider = serviceProvider;
			proxyFactory=new ProxyFactory();
			proxyFactory.addEventListener(ProxyFactoryBuildEvent.AFTER_PROXY_BUILD, implementInterface);
			mapping=new Dictionary(true);
		}
	}
}