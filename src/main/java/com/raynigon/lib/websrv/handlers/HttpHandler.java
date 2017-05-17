package com.raynigon.lib.websrv.handlers;

import com.raynigon.lib.websrv.utils.HttpRequest;

public interface HttpHandler extends WebServerHandler {

	/**
	 * 
	 * @param request
	 */
	void handleRequest(HttpRequest request);

}