package ws.spring.util.concurrent;

import org.springframework.util.Assert;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author WindShadow
 * @version 2024-11-24.
 */
public final class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger threadNumber = new AtomicInteger(0);
    private final ThreadGroup group;
    private final String name;

    public NamedThreadFactory(String name) {

        Assert.hasText(name, "The name of ThreadFactory must not be empty/null");
        this.name = name;
        SecurityManager sm = System.getSecurityManager();
        this.group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    public String getName() {
        return name;
    }

    public int getCurrentCreatedThreadSize() {
        return threadNumber.get();
    }

    @Override
    public Thread newThread(Runnable r) {

        Thread thread = new Thread(group, r, nextThreadName());
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        return thread;
    }

    private String nextThreadName() {
        return String.format("[%s::thread-%d]", name, threadNumber.incrementAndGet());
    }
}
