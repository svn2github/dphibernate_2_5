package org.dphibernate.context.builders.mxml
{
	import flash.display.LoaderInfo;
	
	import mx.core.Application;
	import mx.core.IMXMLObject;
	import mx.messaging.ChannelSet;
	
	import org.dphibernate.context.Context;
	import org.dphibernate.entitymanager.EntityManager;
	import org.dphibernate.entitymanager.IEntityManager;

	public class ContextBuilder implements IMXMLObject
	{
		private var built:Boolean;
		private var _context:Context;
		public function get context():Context
		{
			return _context;
		}
		
		public function initialized(document:Object, id:String):void
		{
			if (autoBuild)
				build();
		}

		public function build():void
		{
			if (built)
			{
				// Should they be single use?  Any harm in building more than one?
				throw new Error("This context has already been built.  Builders are single-use")
			}

			if (!loaderInfo)
			{
				loaderInfo = Application.application.loaderInfo;
			}
			_context = new MXMLContext(this);
			built = true;
		}
		/**
		 * Indicates if the context is built as soon as the MXML document is initialized.
		 * If false, you need to call build() manually to build the context
		 * */
		public var autoBuild:Boolean = true;
		/**
		 * The loaderInfo to use for bytecode manipulation.
		 * Defaults to the loaderInfo of the current application. */
		public var loaderInfo:LoaderInfo;
		
		public var entityManager:IEntityManager = new EntityManager();
		
		/**
		 * A destination to be used for dpHibernate services (lazy loading, entity persistence, etc)
		 * */
		public var destination:String;
		/**
		 * A channelset to be used for dpHibernate services (lazy loading, entity persistence, etc)
		 * */
		public var channelSet:ChannelSet;
	}
}
import org.dphibernate.context.Context;
import org.dphibernate.context.builders.mxml.ContextBuilder;
import org.dphibernate.entitymanager.IEntityManager;
import org.dphibernate.loader.service.ILazyLoadServiceProvider;
import org.dphibernate.loader.service.SimpleLazyLoadServiceProvider;
import org.dphibernate.manager.HibernateManager;

class MXMLContext implements Context
{
		private var _entityManager:IEntityManager;
		private var _serviceProvider:ILazyLoadServiceProvider;
		private var enviroment:HibernateManager;
		
		public function MXMLContext(builder:ContextBuilder)
		{
			this._entityManager = builder.entityManager;
			this._serviceProvider = new SimpleLazyLoadServiceProvider(builder.destination,builder.channelSet);
			this.enviroment = new HibernateManager(this);
			enviroment.initialize(builder.loaderInfo);
		}

		public function get entityManager():IEntityManager
		{
			return _entityManager
		}

		public function get lazyLoaderServiceProvider():ILazyLoadServiceProvider
		{
			return _serviceProvider;
		}


		public function createEntity( clazz:Class, args:Array = null ):* 
		{
			return enviroment.createEntity(clazz,args);
		}
}