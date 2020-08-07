package top.kingworker.blog.entity;

/**
 * 用来转换为Json对象
 */
public class Message {
    private int code;
    private String message;
    public Message() {
    }

    public Message(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
