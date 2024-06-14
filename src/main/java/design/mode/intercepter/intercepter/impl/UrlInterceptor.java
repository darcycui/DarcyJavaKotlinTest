package design.mode.intercepter.intercepter.impl;

import design.mode.intercepter.bean.Request;
import design.mode.intercepter.bean.Response;
import design.mode.intercepter.intercepter.IInterceptor;

import java.util.Map;

public class UrlInterceptor implements IInterceptor {
    @Override
    public Response onIntercept(Request request) {
        String url = request.getUrl();
        request.setUrl(url + "/urlInterceptor");
        return new Response(request, false, "url intercepted");
    }
}
