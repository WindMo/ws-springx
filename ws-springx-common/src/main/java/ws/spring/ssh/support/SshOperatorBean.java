package ws.spring.ssh.support;

import ws.spring.ssh.PortForwardingTracker;
import ws.spring.ssh.SshOperator;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author WindShadow
 * @version 2024-03-26.
 */
public class SshOperatorBean extends DelegateSshOperator {

    private final Set<PortForwardingTracker> channelHolders = new CopyOnWriteArraySet<>();

    public SshOperatorBean(SshOperator delegate) {
        super(delegate);
    }

    @Override
    public PortForwardingTracker forward(String remoteHost, int remotePort) {
        return saveChannelHolder(super.forward(remoteHost, remotePort));
    }

    @Override
    public PortForwardingTracker forward(String localHost, String remoteHost, int remotePort) {
        return saveChannelHolder(super.forward(localHost, remoteHost, remotePort));
    }

    @Override
    public PortForwardingTracker forward(int localPort, String remoteHost, int remotePort) {
        return saveChannelHolder(super.forward(localPort, remoteHost, remotePort));
    }

    @Override
    public PortForwardingTracker forward(String localHost, int localPort, String remoteHost, int remotePort) {
        return saveChannelHolder(super.forward(localHost, localPort, remoteHost, remotePort));
    }

    private PortForwardingTracker saveChannelHolder(PortForwardingTracker holder) {

        channelHolders.add(holder);
        return holder;
    }

    @Override
    public void close() throws Exception {

        closeAllPortForwardingChannel();
        super.close();
    }

    private void closeAllPortForwardingChannel() throws Exception {

        for (PortForwardingTracker holder : channelHolders) {
            if (!holder.isClosed()) {
                holder.close();
            }
        }
    }
}
