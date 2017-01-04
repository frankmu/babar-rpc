package com.babar.rpc.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.babar.rpc.common.BabarRPCRequest;
import com.babar.rpc.common.BabarRPCResponse;

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
			babarClient.send(req);
			logger.info("Calling babar client to send request with requestId: " + req.getRequestId());
			BabarRPCResponse response = getResponse(req.getRequestId());
			if(response.getError() != null){
				return response.getError();
			}else{
				return response.getResult();
			}
		}
	}
	private BabarRPCResponse getResponse(String requestId){
		BabarRPCResponse res = null;
		babarRPCResponseMap.getResponseMap().putIfAbsent(requestId, new LinkedBlockingQueue<BabarRPCResponse>(1));
		try {
			res = babarRPCResponseMap.getResponseMap().get(requestId).take();
			if(res == null){
				res = babarRPCResponseMap.getResponseMap().get(requestId).poll(5, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return res;
	}
}
