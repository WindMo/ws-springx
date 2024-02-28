package ws.spring.restrict.support;

import ws.spring.restrict.FrequencyRestrictor;

import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-02-24.
 */
public class DelegateFrequencyRestrictor implements FrequencyRestrictor {

    private final FrequencyRestrictor delegate;

    public DelegateFrequencyRestrictor(FrequencyRestrictor delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public boolean tryRestrict(String refer) {
        return this.delegate.tryRestrict(refer);
    }

    @Override
    public void resetRestrict(String refer) {
        this.delegate.resetRestrict(refer);
    }

    @Override
    public void resetRestrictor() {
        this.delegate.resetRestrictor();
    }
}
