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

package net.digitalprimates.persistence.hibernate.rpc 
{
	import flash.utils.*;
	
	import mx.core.mx_internal;
	import mx.rpc.AsyncToken;
	import mx.rpc.remoting.mxml.RemoteObject;
	
	import net.digitalprimates.persistence.hibernate.HibernateManaged;
	import net.digitalprimates.persistence.hibernate.IHibernateProxy;
	import net.digitalprimates.persistence.hibernate.IHibernateRPC;

use namespace flash_proxy;
use namespace mx_internal;

	use namespace flash_proxy;

	dynamic public class HibernateRemoteObject extends RemoteObject implements IHibernateRPC
	{
		
		public function HibernateRemoteObject(destination:String = null)
		{
	        super(destination);
	    }
		
		public function loadProxy( proxyKey:Object, hibernateProxy:IHibernateProxy ):AsyncToken {
	    	return this.loadDPProxy( proxyKey, hibernateProxy );
		}

	    override flash_proxy function callProperty(name:*, ... args:Array):*
	    {
	    	var token:AsyncToken;

			HibernateManaged.disableServerCalls( this );
			token = getOperation(getLocalName(name)).send.apply(null, args);

	    	HibernateManaged.addHibernateResponder( this, token );
			HibernateManaged.enableServerCalls( this );

	        return token;
	    }
	}
}
