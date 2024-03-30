package ws.spring.ssh;

/**
 * @author WindShadow
 * @version 2024-02-26.
 */
public interface SshSource {

    String getHost();

    int getPort();

    String getUsername();
}
