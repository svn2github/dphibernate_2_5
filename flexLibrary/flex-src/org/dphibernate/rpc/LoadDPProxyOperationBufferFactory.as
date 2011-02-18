package org.dphibernate.rpc
{
	public class LoadDPProxyOperationBufferFactory extends OperationBufferFactory
	{
		public function LoadDPProxyOperationBufferFactory(initialDelay:int=50, maxDelay:int=350)
		{
			super(["loadDPProxy"], initialDelay, maxDelay);
		}
	}
}