package ws.spring.ssh.autoconfigure;

import ws.spring.ssh.AbstractSshSource;
import ws.spring.ssh.SshSource;
import ws.spring.ssh.support.SshForwardDeclaration;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author WindShadow
 * @version 2024-03-30.
 */

abstract class SshSourceProperties {

    @NotBlank
    private String host;
    @Min(0)
    private int port;

    @NotBlank
    private String username;

    private List<SshForwardDeclaration> forwards;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SshForwardDeclaration> getForwards() {
        return forwards;
    }

    public void setForwards(List<SshForwardDeclaration> forwards) {
        this.forwards = forwards;
    }

    public abstract AbstractSshSource createAbstractSshSource() throws Exception;

    public SshSource buildSshSource() throws Exception {

        AbstractSshSource source = createAbstractSshSource();
        source.setHost(getHost());
        source.setPort(getPort());
        source.setUsername(getUsername());
        return source;
    }
}
