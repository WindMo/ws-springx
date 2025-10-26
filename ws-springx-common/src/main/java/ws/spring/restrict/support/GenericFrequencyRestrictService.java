/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict.support;

import ws.spring.restrict.FrequencyRestrictor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WindShadow
 * @version 2024-10-15.
 */
public abstract class GenericFrequencyRestrictService extends AbstractFrequencyRestrictService {

    private final Map<String, FrequencyRestrictor> restrictors = new ConcurrentHashMap<>(16);

    @Override
    public FrequencyRestrictor getRestrictor(String restrictorName) {
        return restrictors.get(restrictorName);
    }

    @Override
    protected boolean hasRestrictor(String restrictorName) {
        return restrictors.containsKey(restrictorName);
    }

    @Override
    protected void doAddRestrictor(FrequencyRestrictor restrictor) {
        restrictors.putIfAbsent(restrictor.getName(), restrictor);
    }

    @Override
    public void clearAllRestrictors() {

        restrictors.forEach((name, restrictor) -> restrictor.resetRestrictor());
        restrictors.clear();
    }
}
