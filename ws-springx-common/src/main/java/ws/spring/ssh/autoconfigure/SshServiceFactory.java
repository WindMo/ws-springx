package ws.spring.ssh.autoconfigure;

import ws.spring.ssh.SshService;

/**
 * @author WindShadow
 * @version 2024-03-28.
 */
public interface SshServiceFactory {

    SshService buildSshService(SshConfigProperties properties) throws Exception;
}
