/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.aop;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ws.spring.aop.ReturnValuePeeper;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.service.NormalService;
import ws.spring.testdemo.service.NormalServiceAdvice;

import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2022-01-22.
 * @see NormalService
 */

@Slf4j
public class NormalServiceTests extends SpringxAppTests {

    @Autowired
    private NormalService normalService;

    @SpyBean
    private NormalServiceAdvice normalServiceAdvice;

    private ReturnValuePeeper<?> returnValuePeeper;

    private void callMethodReturnValuePeeper(Consumer<NormalServiceAdvice> consumer) {

        NormalServiceAdvice t = Mockito.doAnswer(invocation -> {
            invocation.callRealMethod();
            return returnValuePeeper;
        }).when(normalServiceAdvice);
        consumer.accept(t);
    }

    @BeforeEach
    void initMock() {

        returnValuePeeper = mockReturnValuePeeper();
    }

    @Test
    void returnStringTest() {

        callMethodReturnValuePeeper(NormalServiceAdvice::returnString);
        Assertions.assertDoesNotThrow(() -> normalService.returnString());
        Mockito.verify(normalServiceAdvice, Mockito.times(1)).returnString();
        Mockito.verify(returnValuePeeper, Mockito.times(1)).peekReturnValue(Mockito.any(), Mockito.any());
    }

    @Test
    void returnStringAsyncTest() {

        Mockito.doReturn(returnValuePeeper).when(normalServiceAdvice).returnStringAsyn();
        Assertions.assertDoesNotThrow(() -> normalService.returnStringAsyn());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Mockito.verify(normalServiceAdvice, Mockito.times(1)).returnStringAsyn();
        Mockito.verify(returnValuePeeper, Mockito.times(1)).peekReturnValue(Mockito.any(), Mockito.any());
    }

    @Test
    void ioutTest() {

        Mockito.doReturn(returnValuePeeper).when(normalServiceAdvice).iout();
        Assertions.assertDoesNotThrow(() -> normalService.iout());
        Mockito.verify(normalServiceAdvice, Mockito.times(1)).iout();
        Mockito.verify(returnValuePeeper, Mockito.times(1)).peekReturnValue(Mockito.any(), Mockito.any());
    }

    @Test
    void consumerStringTest() {

        callMethodReturnValuePeeper(NormalServiceAdvice::consumerString);
        callMethodReturnValuePeeper(s -> s.consumerString(Mockito.anyString()));
        callMethodReturnValuePeeper(NormalServiceAdvice::consumerStringVoid);
        callMethodReturnValuePeeper(s -> s.consumerStringVoid(Mockito.anyString()));

        Assertions.assertDoesNotThrow(() -> normalService.consumerString("abc"));
        Mockito.verify(normalServiceAdvice, Mockito.times(1)).consumerString();
        Mockito.verify(normalServiceAdvice, Mockito.times(1)).consumerString(Mockito.anyString());
        Mockito.verify(normalServiceAdvice, Mockito.times(1)).consumerStringVoid();
        Mockito.verify(normalServiceAdvice, Mockito.times(1)).consumerStringVoid(Mockito.anyString());
        Mockito.verify(returnValuePeeper, Mockito.times(2)).peekReturnValue(Mockito.any(), Mockito.any());
    }

    private <T, E extends Exception> ReturnValuePeeper<T> mockReturnValuePeeper() {

        return Mockito.mock(ReturnValuePeeper.class);
    }
}
