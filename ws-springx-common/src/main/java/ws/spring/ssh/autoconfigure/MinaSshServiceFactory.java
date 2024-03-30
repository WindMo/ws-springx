package ws.spring.ssh.autoconfigure;

import org.apache.sshd.client.session.ClientSessionCreator;
import ws.spring.ssh.SshDuration;
import ws.spring.ssh.SshService;
import ws.spring.ssh.support.MinaSshService;

/**
 * @author WindShadow
 * @version 2024-03-28.
 */
class MinaSshServiceFactory implements SshServiceFactory {

    private final ClientSessionCreator client;

    public MinaSshServiceFactory(ClientSessionCreator client) {
        this.client = client;
    }

    @Override
    public SshService buildSshService(SshDuration sshDuration) {
        return new MinaSshService(client, sshDuration);
    }
}
