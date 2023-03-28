package CustomExceptionhandling;

public class UserException extends Exception {
    String msg;

    public UserException(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return msg;
    }
}
