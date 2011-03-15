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


package org.dphibernate.rpc
{
	import mx.rpc.AsyncToken;
	
	import org.dphibernate.core.IEntity;
	import org.dphibernate.loader.service.ILazyLoadService;
	
	public interface IEntityService extends ILazyLoadService
	{
		function get stateTrackingEnabled() : Boolean;
		function set stateTrackingEnabled( value : Boolean ) : void;
		
		function saveProxy( hibernateProxy : IEntity , objectChangeMessages : Array ) : AsyncToken;
		
		function get enabled():Boolean;
		function set enabled(value:Boolean):void;
	}
}