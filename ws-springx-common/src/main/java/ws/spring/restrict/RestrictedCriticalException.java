package ws.spring.restrict;

/**
 * @author WindShadow
 * @version 2023-10-18.
 */

public class RestrictedCriticalException extends RestrictionException {

    public RestrictedCriticalException(String restrictorName, String message) {
        super(assembleMessage(restrictorName, message));
    }

    public RestrictedCriticalException(String restrictorName, String message, Throwable cause) {
        super(assembleMessage(restrictorName, message), cause);
    }

    private static String assembleMessage(String restrictorName, String message) {
        return String.format("Restrictor[%s]: %s", restrictorName, message);
    }
}
