package ws.spring.aop.exception;

/**
 * @author WindShadow
 * @version 2022-01-22.
 */

public class PeekMethodDeclarationException extends RuntimeException {

    public PeekMethodDeclarationException(String message) {
        super(message);
    }

    public PeekMethodDeclarationException(Throwable cause) {
        super(cause);
    }

    public PeekMethodDeclarationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PeekMethodDeclarationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
