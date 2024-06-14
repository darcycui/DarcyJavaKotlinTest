package design.mode.intercepter.bean;

public class Response {
    private Request request;
    private boolean isDone;
    private String message = "Default message.";

    public Response(Request request, boolean isDone, String message) {
        this.request = request;
        this.isDone = isDone;
        this.message = message;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isIntercepted) {
        this.isDone = isIntercepted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "request=" + request +
                ", isDone=" + isDone +
                ", message='" + message + '\'' +
                '}';
    }
}
