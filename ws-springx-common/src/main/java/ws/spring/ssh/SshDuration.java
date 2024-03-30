package ws.spring.ssh;

import java.time.Duration;

/**
 * @author WindShadow
 * @version 2024-03-01.
 */
public interface SshDuration {

    Duration getConnectTimeout();

    Duration getHeartbeatTimeout();

    Duration getChannelTimeout();
}
