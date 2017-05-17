package com.raynigon.lib.websrv.handlers;

import com.raynigon.lib.events.handling.EventManager;
import com.raynigon.lib.websrv.events.HttpRequestEvent;
import com.raynigon.lib.websrv.utils.HttpRequest;

public class HttpEventHandler implements HttpHandler {

    private EventManager em;
    
    @Override
    public void handleRequest(HttpRequest request){
        em.fireEvent(new HttpRequestEvent(request));
    }
}