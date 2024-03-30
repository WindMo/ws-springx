package ws.spring.ssh.autoconfigure;

import org.apache.sshd.client.session.ClientSessionCreator;
import ws.spring.ssh.SshDuration;
import ws.spring.ssh.SshService;
import ws.spring.ssh.support.MinaSshService;

/**
 * @author WindShadow
 * @version 2024-03-28.
 */
public class MinaSshServiceFactory extends AbstractSshServiceFactory {

    private final ClientSessionCreator client;

    public MinaSshServiceFactory(ClientSessionCreator client) {
        this.client = client;
    }

    @Override
    protected SshService newInstance(SshDuration sshDuration) {
        return new MinaSshService(client, sshDuration);
    }
}
