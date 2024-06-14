package design.mode.intercepter.bean;

import java.util.Map;

public class Request {
    private String url;
    private String method;
    private Map<String, String> headers;

    public Request(String url, String method, Map<String, String> headers) {
        this.url = url;
        this.method = method;
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "Request{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                '}';
    }
}
