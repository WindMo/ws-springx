package ws.spring.ssh.autoconfigure;

import org.springframework.util.Assert;
import ws.spring.ssh.SshDuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WindShadow
 * @version 2025-07-06.
 */
class SshConfig {

    private final SshDuration sshDuration;
    private final Map<String, SshSourceProperties> authIdentities;

    SshConfig(SshConfigProperties properties) {

        Map<String, SshAccountProperties> sshAccounts = properties.getAccounts();
        Map<String, SshKeyPairProperties> sshKeyPairs = properties.getKeyPairs();
        Map<String, SshSourceProperties> authIdentities = new HashMap<>();
        authIdentities.putAll(sshAccounts);
        authIdentities.putAll(sshKeyPairs);
        Assert.notEmpty(authIdentities, "Configure at least one ssh source");
        this.sshDuration = properties;
        this.authIdentities = authIdentities;
    }

    public SshDuration getSshDuration() {
        return sshDuration;
    }

    public Map<String, SshSourceProperties> getAuthIdentities() {
        return authIdentities;
    }
}
