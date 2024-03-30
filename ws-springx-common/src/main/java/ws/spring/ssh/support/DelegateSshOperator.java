package ws.spring.ssh.support;

import ws.spring.ssh.PortForwardingTracker;
import ws.spring.ssh.SshOperator;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2024-03-26.
 */
public class DelegateSshOperator implements SshOperator {


    private final SshOperator delegate;

    public DelegateSshOperator(SshOperator delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public String exec(String command) {
        return delegate.exec(command);
    }

    @Override
    public String exec(String command, Charset charset) {
        return delegate.exec(command, charset);
    }

    @Override
    public void exec(String command, OutputStream out) {
        delegate.exec(command, out);
    }

    @Override
    public void shell(InputStream in, OutputStream out) {
        delegate.shell(in, out);
    }

    @Override
    public void shell(Consumer<OutputStream> inputConsumer, Consumer<InputStream> outputConsumer) {
        delegate.shell(inputConsumer, outputConsumer);
    }

    @Override
    public void shell(Consumer<OutputStream> inputConsumer, OutputStream out) {
        delegate.shell(inputConsumer, out);
    }

    @Override
    public void shell(InputStream in, Consumer<InputStream> outputConsumer) {
        delegate.shell(in, outputConsumer);
    }

    @Override
    public PortForwardingTracker forward(String remoteHost, int remotePort) {
        return delegate.forward(remoteHost, remotePort);
    }

    @Override
    public PortForwardingTracker forward(String localHost, String remoteHost, int remotePort) {
        return delegate.forward(localHost, remoteHost, remotePort);
    }

    @Override
    public PortForwardingTracker forward(int localPort, String remoteHost, int remotePort) {
        return delegate.forward(localPort, remoteHost, remotePort);
    }

    @Override
    public PortForwardingTracker forward(String localHost, int localPort, String remoteHost, int remotePort) {
        return delegate.forward(localHost, localPort, remoteHost, remotePort);
    }

    @Override
    public boolean isClosed() {
        return delegate.isClosed();
    }

    @Override
    public void close() throws Exception {
        delegate.close();
    }
}
