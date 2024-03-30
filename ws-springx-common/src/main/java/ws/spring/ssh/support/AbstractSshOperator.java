package ws.spring.ssh.support;

import org.springframework.util.Assert;
import ws.spring.ssh.PortForwardingTracker;
import ws.spring.ssh.SshException;
import ws.spring.ssh.SshOperator;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2024-02-29.
 */

public abstract class AbstractSshOperator implements SshOperator {

    @Override
    public void exec(String command, OutputStream out) {

        Objects.requireNonNull(command);
        Objects.requireNonNull(out);
        checkSshState();
        try {
            doExec(command, out);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void shell(InputStream in, OutputStream out) {

        Objects.requireNonNull(in);
        Objects.requireNonNull(out);
        checkSshState();
        doShell(in, out);
    }

    @Override
    public void shell(Consumer<OutputStream> inputConsumer, Consumer<InputStream> outputConsumer) {

        Objects.requireNonNull(inputConsumer);
        Objects.requireNonNull(outputConsumer);
        checkSshState();
        doShell(inputConsumer, outputConsumer);
    }

    @Override
    public void shell(InputStream in, Consumer<InputStream> outputConsumer) {

        Objects.requireNonNull(in);
        Objects.requireNonNull(outputConsumer);
        checkSshState();
        doShell(in, outputConsumer);
    }

    @Override
    public void shell(Consumer<OutputStream> inputConsumer, OutputStream out) {

        Objects.requireNonNull(inputConsumer);
        Objects.requireNonNull(out);
        checkSshState();
        doShell(inputConsumer, out);
    }

    @Override
    public PortForwardingTracker forward(String localHost, int localPort, String remoteHost, int remotePort) {

        checkSshState();
        return doForward(localHost, localPort, remoteHost, remotePort);
    }

    @Override
    public boolean isClosed() {
        return !isSshOpen();
    }

    protected abstract boolean isSshOpen();

    protected abstract void doExec(String command, OutputStream out) throws SshException;

    protected abstract void doShell(InputStream in, OutputStream out) throws SshException;

    protected abstract void doShell(Consumer<OutputStream> inputConsumer, Consumer<InputStream> outputConsumer) throws SshException;

    protected abstract void doShell(InputStream in, Consumer<InputStream> outputConsumer) throws SshException;

    protected abstract void doShell(Consumer<OutputStream> inputConsumer, OutputStream out) throws SshException;

    protected abstract PortForwardingTracker doForward(String localHost, int localPort, String remoteHost, int remotePort) throws SshException;

    protected final void checkSshState() {
        Assert.state(isSshOpen(), "The ssh connect is closed");
    }
}
