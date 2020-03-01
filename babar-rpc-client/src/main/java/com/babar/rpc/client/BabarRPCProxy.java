package com.babar.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.babar.rpc.common.BabarRPCRequest;

@Component
public class BabarRPCProxy {

	@Autowired
	private BabarRPCClient babarClient;

	@Autowired
	BabarRPCResponseMap babarRPCResponseMap;
	
	private static final Logger logger = LoggerFactory.getLogger(BabarRPCProxy.class);

	@SuppressWarnings("unchecked")
	public <T> T create(Class<?> clazz){
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new BabarInvocationHandler());
	}
	
	public class BabarInvocationHandler implements InvocationHandler{
		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			BabarRPCRequest req = new BabarRPCRequest();
			req.setRequestId(UUID.randomUUID().toString());
			req.setClassName(method.getDeclaringClass().getName());
			req.setMethodName(method.getName());
			req.setParameters(args);
			req.setParameterTypes(method.getParameterTypes());
			
			logger.info("Create 1 record in response map with requestId: " + req.getRequestId());
			babarRPCResponseMap.getResponseMap().putIfAbsent(req.getRequestId(), new CompletableFuture<Object>());
			
			babarClient.send(req);
			return babarRPCResponseMap.getResponseMap().get(req.getRequestId());
		}
	}
}
