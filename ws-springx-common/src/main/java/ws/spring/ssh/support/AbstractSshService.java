package ws.spring.ssh.support;

import org.springframework.util.Assert;
import ws.spring.ssh.*;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WindShadow
 * @version 2024-02-27.
 */
public abstract class AbstractSshService implements SshService {

    protected final SshDuration sshDuration;

    private final Map<String, SshSource> sshSources = new ConcurrentHashMap<>(16);
    private final Map<String, SshOperator> sshOperators = new ConcurrentHashMap<>(16);

    protected AbstractSshService(SshDuration sshDuration) {
        this.sshDuration = Objects.requireNonNull(sshDuration);
    }

    @Override
    public SshOperator buildSshOperator(String sourceName) {

        Assert.hasText(sourceName, "The sourceName must not be empty/null");
        return sshOperators.computeIfAbsent(sourceName, name -> {

            SshSource sshSource = sshSources.get(name);
            Assert.notNull(sshSource, "SshSource: " + name + " does not exist");
            return doBuildSshOperator(sshSource);
        });
    }

    @Override
    public void registerSshSource(String sourceName, SshSource sshSource) {

        Assert.hasText(sourceName, "The sourceName must not be empty/null");
        Assert.notNull(sshSource, "The sshSource must not be null");
        sshSources.merge(sourceName, sshSource, (s1, s2) -> {
            throw new IllegalStateException("Duplicate registration SshSource: " + sourceName);
        });
    }

    protected abstract SshOperator doBuildSshOperator(SshSource sshSource) throws SshException;
}
