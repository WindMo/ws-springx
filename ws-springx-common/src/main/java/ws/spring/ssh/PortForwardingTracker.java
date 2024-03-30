package ws.spring.ssh;

/**
 * @author WindShadow
 * @version 2024-03-26.
 */
public interface PortForwardingTracker extends AutoCloseable {

    int getLocalPort();

    boolean isClosed();
}
