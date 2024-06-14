package design.mode.intercepter.intercepter;

import design.mode.intercepter.bean.Request;
import design.mode.intercepter.bean.Response;

public interface IInterceptor {
    Response onIntercept(Request request);
}
