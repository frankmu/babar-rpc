package com.babar.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.babar.common.BabarRequest;
import com.babar.common.BabarResponse;

@Component
public class BabarProxy {

	@Autowired
	private BabarClient babarClient;

	@Autowired
	BabarResponseMap babarResponseMap;

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
			BabarResponse response = getResponse(req.getRequestId());
			if(response.getError() != null){
				return response.getError();
			}else{
				return response.getResult();
			}
		}
	}
	private BabarResponse getResponse(String requestId){
		BabarResponse res = null;
		babarResponseMap.getResponseMap().putIfAbsent(requestId, new LinkedBlockingQueue<BabarResponse>(1));
		try {
			res = babarResponseMap.getResponseMap().get(requestId).take();
			if(res == null){
				res = babarResponseMap.getResponseMap().get(requestId).poll(5, TimeUnit.SECONDS);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return res;
	}
}
