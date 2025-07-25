package biz.churen.ee.rag.sdk;

import java.time.LocalDateTime;

@SuppressWarnings({"LombokGetterMayBeUsed", "LombokSetterMayBeUsed"})
public class Message<T> {
    // 状态码
    private int status;
    // 状态描述
    private String message;
    // 实际返回的数据
    private T data;
    // host
    private String host;
    // port
    private int port;
    // 当前时间
    private LocalDateTime timestamp = LocalDateTime.now();

    public Message(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public static <T> Message<T> ok() {
        return new Message<>(MessageCode.SUCCESS.getCode(), null, null);
    }

    public static <T> Message<T> ok(T data) {
        return new Message<>(MessageCode.SUCCESS.getCode(), null, data);
    }

    public static <T> Message<T> error(MessageCode result) {
        return new Message<>(result.getCode(), null, null);
    }

    public static <T> Message<T> error(MessageCode result, String message) {
        return new Message<>(result.getCode(), message, null);
    }

    public static <T> Message<T> error(MessageCode result, T data) {
        return new Message<>(result.getCode(), null, data);
    }

    public static <T> Message<T> error(MessageCode result, String message, T data) {
        return new Message<>(result.getCode(), message, data);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
