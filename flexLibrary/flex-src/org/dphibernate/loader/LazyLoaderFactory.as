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
package org.dphibernate.loader
{
	import org.dphibernate.core.IEntity;
	import org.dphibernate.entitymanager.IEntityManager;
	import org.dphibernate.loader.service.ILazyLoadService;
	import org.dphibernate.loader.service.ILazyLoadServiceProvider;

	public class LazyLoaderFactory implements ILazyLoaderFactory
	{

		private var _entityResolver:IEntityManager;

		private var _serviceProvider:ILazyLoadServiceProvider;

		public function LazyLoaderFactory(serviceProvider:ILazyLoadServiceProvider, entityResolver:IEntityManager)
		{
			this._serviceProvider = serviceProvider;
			this._entityResolver=entityResolver;
		}

		public function get serviceProvider():ILazyLoadServiceProvider
		{
			return _serviceProvider;
		}

		public function set serviceProvider(value:ILazyLoadServiceProvider):void
		{
			_serviceProvider = value;
		}

		public function get entityResolver():IEntityManager
		{
			return _entityResolver;
		}

		public function set entityResolver(value:IEntityManager):void
		{
			_entityResolver=value;
		}


		/**
		 * Returns a new LazyLoader.
		 * Note - this implementation ignores the property value, as the entire
		 * entity is loaded.  However, the property is provided should
		 * others wish to provide a different implementation.
		 * */
		public function newInstance(entity:IEntity, property:String):ILazyLoader
		{
			var service:ILazyLoadService = serviceProvider.getLoader(entity);
			return new LazyLoader(entity, service, entityResolver);
		}
	}
}