package ws.spring.ssh.support;

import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.io.input.NoCloseInputStream;
import org.apache.sshd.common.util.io.output.NoCloseOutputStream;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import ws.spring.ssh.PortForwardingTracker;
import ws.spring.ssh.SshException;
import ws.spring.ssh.SshIOException;
import ws.spring.ssh.SshSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2024-02-29.
 */
class MinaSshOperator extends AbstractSshOperator {

    private final SshSource source;
    private final ClientSession clientSession;
    private final long channelTimeout;

    MinaSshOperator(SshSource source, ClientSession clientSession, Duration channelTimeout) {
        this(source, clientSession, channelTimeout.toMillis());
    }

    MinaSshOperator(SshSource source, ClientSession clientSession, long channelTimeout) {
        this.source = Objects.requireNonNull(source);
        this.clientSession = Objects.requireNonNull(clientSession);
        this.channelTimeout = channelTimeout;
    }

    @Override
    protected boolean isSshOpen() {
        return clientSession.isOpen();
    }

    @Override
    protected void doExec(String command, OutputStream out) {

        try (ClientChannel channel = clientSession.createExecChannel(command)) {
            channel.setOut(new NoCloseOutputStream(out));
            channel.setRedirectErrorStream(true);
            channel.open().verify(channelTimeout);
            channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0);
        } catch (IOException e) {
            throw new SshIOException(source, "Open exec channel failed", e);
        }
    }

    @Override
    protected void doShell(InputStream in, OutputStream out) throws SshException {

        try (ClientChannel channel = clientSession.createShellChannel()) {
            channel.setIn(new NoCloseInputStream(in));
            channel.setOut(new NoCloseOutputStream(out));
            channel.setRedirectErrorStream(true);
            channel.open().verify(channelTimeout);
            channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0);
        } catch (IOException e) {
            throw new SshIOException(source, "Open shell channel failed", e);
        }
    }

    @Override
    protected void doShell(Consumer<OutputStream> inputConsumer, Consumer<InputStream> outputConsumer) throws SshException {

        try (ClientChannel channel = clientSession.createShellChannel()) {
            channel.setRedirectErrorStream(true);
            channel.open().verify(channelTimeout);
            inputConsumer.accept(channel.getInvertedIn());
            outputConsumer.accept(channel.getInvertedOut());
            channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0);
        } catch (IOException e) {
            throw new SshIOException(source, "Open shell channel failed", e);
        }
    }

    @Override
    protected void doShell(InputStream in, Consumer<InputStream> outputConsumer) throws SshException {

        try (ClientChannel channel = clientSession.createShellChannel()) {
            channel.setIn(new NoCloseInputStream(in));
            channel.setRedirectErrorStream(true);
            channel.open().verify(channelTimeout);
            outputConsumer.accept(channel.getInvertedOut());
            channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0);
        } catch (IOException e) {
            throw new SshIOException(source, "Open shell channel failed", e);
        }
    }

    @Override
    protected void doShell(Consumer<OutputStream> inputConsumer, OutputStream out) throws SshException {

        try (ClientChannel channel = clientSession.createShellChannel()) {
            channel.setOut(new NoCloseOutputStream(out));
            channel.setRedirectErrorStream(true);
            channel.open().verify(channelTimeout);
            inputConsumer.accept(channel.getInvertedIn());
            channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 0);
        } catch (IOException e) {
            throw new SshIOException(source, "Open shell channel failed", e);
        }
    }

    @Override
    protected PortForwardingTracker doForward(String localHost, int localPort, String remoteHost, int remotePort) throws SshException {

        try {
            org.apache.sshd.client.session.forward.PortForwardingTracker tracker = clientSession.createLocalPortForwardingTracker(
                    new SshdSocketAddress(localHost, localPort),
                    new SshdSocketAddress(remoteHost, remotePort));
            return new DefaultPortForwardingTracker<>(localHost, tracker.getBoundAddress().getPort(), remoteHost, remotePort, tracker, tk -> !tk.isOpen());
        } catch (IOException e) {
            throw new SshIOException(source, "Open forward tracker failed", e);
        }
    }

    @Override
    public void close() throws Exception {

        if (clientSession.isOpen()) {
            clientSession.close();
        }
    }
}
