package ws.spring.ssh.support;

import ws.spring.ssh.PortForwardingTracker;

import java.util.function.Predicate;

/**
 * @author WindShadow
 * @version 2024-03-26.
 */
public class DefaultPortForwardingTracker<T extends AutoCloseable> implements PortForwardingTracker {

    private final int port;
    private final T closeable;
    private final Predicate<T> predicate;

    public DefaultPortForwardingTracker(int port, T closeable, Predicate<T> predicate) {
        this.port = port;
        this.closeable = closeable;
        this.predicate = predicate;
    }

    @Override
    public int getLocalPort() {
        return port;
    }

    @Override
    public boolean isClosed() {
        return predicate.test(closeable);
    }

    @Override
    public void close() throws Exception {
        closeable.close();
    }
}
