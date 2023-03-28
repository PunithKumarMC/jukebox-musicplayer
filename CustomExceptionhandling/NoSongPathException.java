package CustomExceptionhandling;

public class NoSongPathException extends Exception {

    String msg;

    public NoSongPathException(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return msg;
    }

}
