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

	public class HibernateManagedEntry
	{
		public var ro:IHibernateRPC;
		public var parent:*;
		public var parentProperty:String;
		
		public function HibernateManagedEntry( ro:IHibernateRPC, parent:*, parentProperty:String ) 
		{
			this.ro = ro;
			this.parent = parent;
			this.parentProperty = parentProperty;
		}
	}
}