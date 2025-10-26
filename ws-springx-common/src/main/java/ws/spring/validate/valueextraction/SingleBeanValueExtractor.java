/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.validate.valueextraction;

import ws.spring.beans.SingleBean;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

/**
 * @author WindShadow
 * @version 2023-07-20.
 */

public class SingleBeanValueExtractor implements ValueExtractor<SingleBean<@ExtractedValue ?>> {

    static final String NODE_NAME = "<value>";

    @Override
    public void extractValues(SingleBean<?> singleBean, ValueReceiver receiver) {

        Object value = singleBean.getValue();
        receiver.value(NODE_NAME, value);
    }
}
