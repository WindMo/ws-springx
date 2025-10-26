/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict.support;

import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import ws.spring.restrict.FrequencyRestrictor;

/**
 * @author WindShadow
 * @version 2024-02-23.
 */
public abstract class AbstractFrequencyRestrictor implements FrequencyRestrictor {

    protected final String name;

    protected AbstractFrequencyRestrictor(String name) {
        this.name = name;
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

    protected void checkRefer(@NonNull String refer) {
        Assert.notNull(refer, "The refer must not be null");
    }

    protected abstract boolean doTryRestrict(@NonNull String refer);

    protected abstract void doResetRestrict(@NonNull String refer);
}
