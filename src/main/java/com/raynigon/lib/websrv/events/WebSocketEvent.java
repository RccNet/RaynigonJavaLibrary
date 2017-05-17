package com.raynigon.lib.websrv.events;

public abstract class WebSocketEvent implements WebServerEvent {

	WebSocketContext context;

	public WebSocketContext getContext() {
		return this.context;
	}

	/**
	 * 
	 * @param context
	 */
	public WebSocketEvent(WebSocketContext context) {
		// TODO - implement WebSocketEvent.WebSocketEvent
		throw new UnsupportedOperationException();
	}
}