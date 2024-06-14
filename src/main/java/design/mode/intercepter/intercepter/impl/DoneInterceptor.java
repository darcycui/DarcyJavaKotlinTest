package design.mode.intercepter.intercepter.impl;

import design.mode.intercepter.bean.Request;
import design.mode.intercepter.bean.Response;
import design.mode.intercepter.intercepter.IInterceptor;

public class DoneInterceptor implements IInterceptor {
    @Override
    public Response onIntercept(Request request) {
        // done intercept
        return new Response(request, true, "done intercepted");
    }
}
