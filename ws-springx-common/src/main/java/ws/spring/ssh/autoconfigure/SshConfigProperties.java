/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Validated
@ConfigurationProperties("spring.ext.ssh")
public class SshConfigProperties extends SshDurationProperties {

    private boolean enabled;

    private Map<String, @Valid SshAccountProperties> accounts = Collections.emptyMap();
    private Map<String, @Valid SshKeyPairProperties> keyPairs = Collections.emptyMap();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<String, SshAccountProperties> getAccounts() {
        return accounts;
    }

    public void setAccounts(Map<String, SshAccountProperties> accounts) {
        this.accounts = accounts;
    }

    public Map<String, SshKeyPairProperties> getKeyPairs() {
        return keyPairs;
    }

    public void setKeyPairs(Map<String, SshKeyPairProperties> keyPairs) {
        this.keyPairs = keyPairs;
    }
}
