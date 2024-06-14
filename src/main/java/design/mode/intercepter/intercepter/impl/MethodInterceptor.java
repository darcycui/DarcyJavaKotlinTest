package design.mode.intercepter.intercepter.impl;

import design.mode.intercepter.bean.Request;
import design.mode.intercepter.bean.Response;
import design.mode.intercepter.intercepter.IInterceptor;

public class MethodInterceptor implements IInterceptor {
    @Override
    public Response onIntercept(Request request) {
        String method = request.getMethod();
        request.setMethod(method + "-Post");
        return new Response(request, false, "method intercepted");
    }
}
