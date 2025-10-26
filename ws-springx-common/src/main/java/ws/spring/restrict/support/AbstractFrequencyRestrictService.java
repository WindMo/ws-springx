/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import ws.spring.restrict.FrequencyRestrictService;
import ws.spring.restrict.FrequencyRestrictor;
import ws.spring.restrict.FrequencyRestrictorDefinition;
import ws.spring.restrict.RestrictorDeclarationException;

/**
 * @author WindShadow
 * @version 2024-01-26.
 */

public abstract class AbstractFrequencyRestrictService implements FrequencyRestrictService, DisposableBean {

    @Override
    public FrequencyRestrictor registerRestrictor(FrequencyRestrictorDefinition definition) throws RestrictorDeclarationException {

        Assert.notNull(definition, "The restrictor must not be null");
        validateRestrictorDefinition(definition);
        FrequencyRestrictor restrictor = createRestrictor(definition);
        addRestrictor(restrictor);
        return restrictor;
    }

    @Override
    public void addRestrictor(FrequencyRestrictor restrictor) {

        String restrictorName = restrictor.getName();
        if (hasRestrictor(restrictorName)) {

            throw new RestrictorDeclarationException(String.format("The restrictor: %s is registered", restrictorName));
        } else {

            synchronized (this) {
                if (!hasRestrictor(restrictorName)) {
                    doAddRestrictor(restrictor);
                }
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        clearAllRestrictors();
    }

    protected void validateRestrictorDefinition(FrequencyRestrictorDefinition definition) throws RestrictorDeclarationException {

        String restrictorName = definition.getName();
        if (!StringUtils.hasText(restrictorName)) {
            throw new RestrictorDeclarationException("The restrictor must not be empty/null");
        }
        int frequency = definition.getFrequency();
        if (frequency < 1) {
            throw new RestrictorDeclarationException(String.format("The frequency: %d of restrictor is invalid", frequency));
        }
        long duration = definition.getDuration();
        if (duration < 1) {
            throw new RestrictorDeclarationException(String.format("The duration: %d of restrictor is invalid", duration));
        }
    }

    protected abstract FrequencyRestrictor createRestrictor(FrequencyRestrictorDefinition definition) throws RestrictorDeclarationException;

    protected abstract boolean hasRestrictor(String restrictorName);

    protected abstract void doAddRestrictor(FrequencyRestrictor restrictor);
}
