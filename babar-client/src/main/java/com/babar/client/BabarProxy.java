package com.babar.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.babar.common.BabarRequest;

@Component
public class BabarProxy {
	
	@Autowired
	private BabarClient babarClient;
	
	@SuppressWarnings("unchecked")
	public <T> T create(Class<?> clazz){
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new BabarInvocationHandler());
	}
	
	public class BabarInvocationHandler implements InvocationHandler{
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			BabarRequest req = new BabarRequest();
			req.setRequestId(UUID.randomUUID().toString());
			req.setClassName(method.getDeclaringClass().getName());
			req.setMethodName(method.getName());
			req.setParameters(args);
			req.setParameterTypes(method.getParameterTypes());
			babarClient.send(req);
			return null;
		}
		
	}
}
