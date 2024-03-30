package ws.spring.ssh;

import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-03-01.
 */

public abstract class SshException extends RuntimeException {

    SshException(SshSource source, String message) {
        super(combineMessage(source, message));
    }

    SshException(SshSource source, String message, Throwable cause) {
        super(combineMessage(source, message), cause);
    }

    private static String combineMessage(SshSource source, String message) {

        Objects.requireNonNull(source);
        return String.format("[%s@%s:%d]: %s", source.getUsername(), source.getHost(), source.getPort(), message);
    }
}
