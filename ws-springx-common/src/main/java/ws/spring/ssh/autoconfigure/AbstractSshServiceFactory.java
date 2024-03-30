package ws.spring.ssh.autoconfigure;

import org.springframework.util.CollectionUtils;
import ws.spring.ssh.SshDuration;
import ws.spring.ssh.SshService;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WindShadow
 * @version 2024-03-28.
 */
public abstract class AbstractSshServiceFactory implements SshServiceFactory {

    @Override
    public SshService buildSshService(SshConfigProperties properties) throws Exception {

        SshService service = newInstance(properties);
        Map<String, SshAccountProperties> sshAccounts = properties.getAccounts();
        Map<String, SshKeyPairProperties> sshKeyPairs = properties.getKeyPairs();
        Map<String, SshSourceProperties> authIdentities = new HashMap<>();
        authIdentities.putAll(sshAccounts);
        authIdentities.putAll(sshKeyPairs);
        if (CollectionUtils.isEmpty(authIdentities)) {
            throw new IllegalArgumentException("Configure at least one ssh source");
        }
        for (Map.Entry<String, SshSourceProperties> entry : authIdentities.entrySet()) {

            String name = entry.getKey();
            SshSourceProperties identity = entry.getValue();
            service.registerSshSource(name, identity.buildSshSource());
        }
        return service;
    }

    protected abstract SshService newInstance(SshDuration sshDuration);
}
