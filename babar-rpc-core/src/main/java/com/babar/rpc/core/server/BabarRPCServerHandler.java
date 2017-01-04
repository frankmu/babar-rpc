package com.babar.rpc.core.server;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.babar.rpc.common.BabarRPCRequest;
import com.babar.rpc.common.BabarRPCResponse;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class BabarRPCServerHandler extends SimpleChannelInboundHandler<BabarRPCRequest> {
	
	private Map<String, Object> handlerMap;
	Log log = LogFactory.getLog(this.getClass());
	
	public BabarRPCServerHandler(Map<String, Object> handlerMap) {
		this.handlerMap = handlerMap;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, BabarRPCRequest req) throws Exception {
		log.info("[" + Thread.currentThread().getName() + "] Get request from client with requestId: " + req.getRequestId());
		BabarRPCResponse response = new BabarRPCResponse();
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

	private Object processRequest(BabarRPCRequest req) throws Exception{
		String className = req.getClassName();
		Object serviceImpl = handlerMap.get(className);
		String methodName = req.getMethodName();
		Object[] parameters = req.getParameters();
		Method method = serviceImpl.getClass().getMethod(methodName, req.getParameterTypes());
		log.info("Invode function call with class name: " + className + " method name: " + methodName);
		return method.invoke(serviceImpl, parameters);
	}
}
