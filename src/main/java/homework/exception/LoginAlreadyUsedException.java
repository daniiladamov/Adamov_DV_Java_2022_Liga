package homework.exception;

public class LoginAlreadyUsedException extends RuntimeException{
    public LoginAlreadyUsedException(String message) {
        super(message);
    }
}
