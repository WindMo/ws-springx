package ws.spring.ssh;

/**
 * @author WindShadow
 * @version 2024-02-27.
 */
public interface SshService extends SshSourceRegistry {

    SshOperator buildSshOperator(String sourceName);
}
