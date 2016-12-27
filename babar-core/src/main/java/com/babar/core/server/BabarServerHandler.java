package com.babar.core.server;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.babar.common.BabarRequest;
import com.babar.common.BabarResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BabarServerHandler extends SimpleChannelInboundHandler<BabarRequest> {
	
	private Map<String, Object> handlerMap;
	Log log = LogFactory.getLog(this.getClass());
	
	public BabarServerHandler(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BabarRequest req) throws Exception {
		log.info("Get request from client with requestId: " + req.getRequestId());
		BabarResponse response = new BabarResponse();
		response.setRequestId(req.getRequestId());
		try {
			Object result = processRequest(req);
			response.setResult(result);
		} catch (Exception e) {
			response.setError(e);
		}
		ctx.writeAndFlush(response);
		log.info("Send response back to client with responseId: " + req.getRequestId());
	}

	public Map<String, Object> getHandlerMap() {
		return handlerMap;
	}

	public void setHandlerMap(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}

	private Object processRequest(BabarRequest req) throws Exception{
		String className = req.getClassName();
		Object serviceImpl = handlerMap.get(className);
		String methodName = req.getMethodName();
		Object[] parameters = req.getParameters();
		Method method = serviceImpl.getClass().getMethod(methodName, req.getParameterTypes());
		log.info("Invode function call with class name: " + className + " method name: " + methodName);
		return method.invoke(serviceImpl, parameters);
	}
}
