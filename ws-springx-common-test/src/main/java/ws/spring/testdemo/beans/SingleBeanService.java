/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.beans;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ws.spring.beans.SingleBean;
import ws.spring.testdemo.pojo.Person;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

/**
 * @author WindShadow
 * @version 2024-10-21.
 */
@Validated
@Service
public class SingleBeanService {

    public void consumeStringSingleBean(SingleBean<@NotBlank String> bean) {}

    public void consumeIntegerSingleBean(SingleBean<@Min(100) Integer> bean) {}

    public void consumeCollectionSingleBean(SingleBean<@NotEmpty Collection<Object>> bean) {}

    public void consumePojoSingleBean(SingleBean<@Valid Person> bean) {}
}
