package org.dphibernate.loader
{
	import mx.rpc.AsyncToken;

	public interface ILazyLoader
	{
		function load():AsyncToken; //??  Should we return a token here?
	}
}