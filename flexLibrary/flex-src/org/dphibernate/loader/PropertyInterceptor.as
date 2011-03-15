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
 * @author     Marty Pitt
 * @version
 **/
package org.dphibernate.loader
{
	import flash.events.IEventDispatcher;
	
	import org.dphibernate.core.EntityStatus;
	import org.dphibernate.core.IEntity;

	public class PropertyInterceptor implements IPropertyInterceptor
	{
		private var factory:ILazyLoaderFactory;

		public function PropertyInterceptor(lazyLoaderFactory:ILazyLoaderFactory)
		{
			this.factory=lazyLoaderFactory;
		}

		public function getProperty(entity:IEntity, property:String):*
		{

			var entityValue:* = entity[property];
			if (entityValue)
				return entityValue;
			
			if (!entityValue && entity.entityInitialized)
				return null;
			
			if (entity.entityStatus & EntityStatus.LOADING)
				return null;
			
			entity.entityStatus |= EntityStatus.SERIALIZING;

			var lazyLoader:ILazyLoader=factory.newInstance(entity,property);
			lazyLoader.load();

			entity.entityStatus&=(~EntityStatus.SERIALIZING);

			return null;
		}

		public function setProperty(obj:IEntity, property:String, oldValue:*, newValue:*):void
		{
			//Perhaps we need to throw an error if we are asked to set a property that is not yet loaded... might make things easier to debug?
			//Marty, thoughts?
		}
	}
}