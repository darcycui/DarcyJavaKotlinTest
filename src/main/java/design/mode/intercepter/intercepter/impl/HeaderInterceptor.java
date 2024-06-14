package design.mode.intercepter.intercepter.impl;

import design.mode.intercepter.bean.Request;
import design.mode.intercepter.bean.Response;
import design.mode.intercepter.intercepter.IInterceptor;

import java.util.Map;

public class HeaderInterceptor implements IInterceptor {
    @Override
    public Response onIntercept(Request request) {
        Map<String, String> headers = request.getHeaders();
        headers.put("header-key", "header-value");
        request.setHeaders(headers);
        return new Response(request, false, "header intercepted");
    }
}
