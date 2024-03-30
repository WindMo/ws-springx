package ws.spring.ssh.autoconfigure;

import org.springframework.lang.Nullable;
import ws.spring.ssh.AbstractSshSource;
import ws.spring.ssh.SshKeyPair;
import ws.spring.util.SecurityUtils;

import javax.validation.constraints.NotBlank;

/**
 * @author WindShadow
 * @version 2024-03-28.
 */
public class SshKeyPairProperties extends SshSourceProperties {

    @NotBlank
    private String privateKey;

    @Nullable
    private char[] keyPassword;

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public char[] getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(char[] keyPassword) {
        this.keyPassword = keyPassword;
    }

    @Override
    public AbstractSshSource createAbstractSshSource() throws Exception {

        SshKeyPair kp = new SshKeyPair();
        kp.setKeyPair(SecurityUtils.readKeyPair(getPrivateKey(), getKeyPassword()));
        return kp;
    }
}