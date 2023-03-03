package pl.kalisz.ak.pup.todolist_mobile.rest.exceptions;

public class BadHttpRequestException extends IllegalArgumentException{
    public BadHttpRequestException() {
    }

    public BadHttpRequestException(String s) {
        super(s);
    }

    public BadHttpRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadHttpRequestException(Throwable cause) {
        super(cause);
    }
}
