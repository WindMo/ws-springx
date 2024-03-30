package ws.spring.ssh.autoconfigure;

import ws.spring.ssh.AbstractSshSource;
import ws.spring.ssh.SshAccount;

import javax.validation.constraints.NotBlank;

/**
 * @author WindShadow
 * @version 2024-03-28.
 */
public class SshAccountProperties extends SshSourceProperties {

    @NotBlank
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public AbstractSshSource createAbstractSshSource() throws Exception {

        SshAccount account = new SshAccount();
        account.setPassword(getPassword());
        return account;
    }
}
