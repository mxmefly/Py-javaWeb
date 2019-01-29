package cn.mxmefly.app.Common;

public class Results {
    private boolean success;
    private String message;
    private Object data;
    public Results(){

    }
    public Results(boolean success, String message) {
        this(success, message, "");
    }
    public Results(boolean success) {
        this(success, null, "");
    }
    public Results(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
