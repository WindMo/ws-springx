/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict.support;

import ws.spring.restrict.FrequencyRestrictorDefinition;

import java.util.concurrent.TimeUnit;

/**
 * @author WindShadow
 * @version 2024-10-15.
 */
public abstract class GenericFrequencyRestrictor extends AbstractFrequencyRestrictor {

    protected final int frequency;
    protected final long durationMillis;

    public GenericFrequencyRestrictor(FrequencyRestrictorDefinition definition) {
        super(definition.getName());
        this.frequency = definition.getFrequency();
        this.durationMillis = TimeUnit.SECONDS.toMillis(definition.getDuration());
    }
}
