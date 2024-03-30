package ws.spring.ssh;

/**
 * @author WindShadow
 * @version 2024-02-27.
 */
public interface SshOperator extends SshOperations, AutoCloseable {

    boolean isClosed();
}
