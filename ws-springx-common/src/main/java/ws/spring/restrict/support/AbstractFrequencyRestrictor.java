package ws.spring.restrict.support;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import ws.spring.restrict.FrequencyRestrictor;

/**
 * @author WindShadow
 * @version 2024-02-23.
 */
public abstract class AbstractFrequencyRestrictor implements FrequencyRestrictor {

    private final String name;
    private final boolean acceptNullRefer;

    protected AbstractFrequencyRestrictor(String name, boolean acceptNullRefer) {
        this.name = name;
        this.acceptNullRefer = acceptNullRefer;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean tryRestrict(String refer) {

        checkRefer(refer);
        return doTryRestrict(refer);
    }

    @Override
    public void resetRestrict(String refer) {

        checkRefer(refer);
        doResetRestrict(refer);
    }

    protected void checkRefer(@Nullable String refer) {

        if (!acceptNullRefer) {
            Assert.notNull(refer, "The refer must not be null");
        }
    }

    protected abstract boolean doTryRestrict(@Nullable String refer);

    protected abstract void doResetRestrict(@Nullable String refer);
}
