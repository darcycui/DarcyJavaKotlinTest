package design.mode.intercepter.list;

import design.mode.intercepter.bean.Request;
import design.mode.intercepter.bean.Response;
import design.mode.intercepter.intercepter.IInterceptor;
import design.mode.intercepter.intercepter.impl.DoneInterceptor;
import design.mode.intercepter.intercepter.impl.HeaderInterceptor;
import design.mode.intercepter.intercepter.impl.MethodInterceptor;
import design.mode.intercepter.intercepter.impl.UrlInterceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 责任链: 将请求的发起者和接收者分离
 * use list to collect interceptors
 */
public class TestListInterceptor {
    public static void main(String[] args) {
        Map<String, String> headers = new HashMap<>();
        headers.put("a","aaa");
        headers.put("b","bbb");
        // create original request
        Request request = new Request("www.baidu.com", "GET", headers);
        System.out.println("request-->" + request);
        // create interceptors and add to list
        List<IInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderInterceptor());
        interceptors.add(new MethodInterceptor());
        interceptors.add(new DoneInterceptor()); // done interceptor

        interceptors.add(new UrlInterceptor());
        Response response = null;
        // iterate list, do intercept actions
        for (IInterceptor interceptor : interceptors) {
            response = interceptor.onIntercept(request);
            System.out.println("response-->" + response);
            // todo if response is done, break
            if (response.isDone()) {
                break;
            }
        }
    }
}
