package org.dphibernate
{
	import flash.utils.setTimeout;
	
	import mx.core.Application;
	
	import org.as3commons.bytecode.reflect.ByteCodeType;
	import org.as3commons.reflect.ITypeProvider;
	import org.flexunit.internals.runners.statements.IAsyncStatement;
	import org.flexunit.internals.runners.statements.MethodRuleBase;
	import org.flexunit.rules.IMethodRule;
	import org.flexunit.runners.model.FrameworkMethod;
	import org.flexunit.token.AsyncTestToken;
	
	public class ByteCodeRule extends MethodRuleBase implements IMethodRule
	{
		override public function evaluate(parentToken:AsyncTestToken):void
		{
			super.evaluate(parentToken);
			var provider:ITypeProvider = ByteCodeType.getTypeProvider();
			if (provider.getTypeCache().size() == 0)
				ByteCodeType.fromLoader(Application.application.loaderInfo);
			proceedToNextStatement();
		}
	}
}