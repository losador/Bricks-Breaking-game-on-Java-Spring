package sk.tuke.gamestudio.service;

public class AccountException extends RuntimeException{
    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
