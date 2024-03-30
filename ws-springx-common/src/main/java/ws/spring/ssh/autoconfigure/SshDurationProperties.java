package ws.spring.ssh.autoconfigure;

import ws.spring.ssh.SshDuration;

import java.time.Duration;

/**
 * @author WindShadow
 * @version 2024-03-02.
 */
public class SshDurationProperties implements SshDuration {

    private Duration connectTimeout = Duration.ofSeconds(30L);

    private Duration heartbeatTimeout = Duration.ofSeconds(30L);

    private Duration channelTimeout = Duration.ofSeconds(10L);

    @Override
    public Duration getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    @Override
    public Duration getHeartbeatTimeout() {
        return heartbeatTimeout;
    }

    @Override
    public Duration getChannelTimeout() {
        return channelTimeout;
    }

    public void setHeartbeatTimeout(Duration heartbeatTimeout) {
        this.heartbeatTimeout = heartbeatTimeout;
    }

    public void setChannelTimeout(Duration channelTimeout) {
        this.channelTimeout = channelTimeout;
    }
}
