/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.testdemo.validate;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ws.spring.testdemo.SpringxAppTests;
import ws.spring.testdemo.validate.enums.Direction;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintViolationException;
import java.lang.annotation.ElementType;
import java.util.Locale;
import java.util.UUID;

/**
 * @author WindShadow
 * @version 2022-09-19.
 * @see ValidateServiceSupport
 */

@Slf4j
public class ValidateServiceSupportTests extends SpringxAppTests {

    @Autowired
    private ValidateServiceSupport service;

    // ~ EnumRange
    // =====================================================================================

    @Test
    public void validateEnumRange() {

        Assertions.assertDoesNotThrow(() -> service.validateEnumRange(Direction.UP));
        Assertions.assertDoesNotThrow(() -> service.validateEnumRange(Direction.DOWN));
        ConstraintViolationException e1 = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateEnumRange(Direction.LEFT));
        log.info("ConstraintViolationException: {}", e1.getMessage());
        ConstraintViolationException e2 = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateEnumRange(Direction.RIGHT));
        log.info("ConstraintViolationException: {}", e2.getMessage());
        ConstraintDeclarationException e3 = Assertions.assertThrows(ConstraintDeclarationException.class, () -> service.validateEnumRangeErrorEnumType(ElementType.METHOD));
        log.info("ConstraintViolationException: {}", e3.getMessage());
    }

    @Test
    public void validateEnumRangeErrorEnumName() {

        ConstraintDeclarationException e = Assertions.assertThrows(ConstraintDeclarationException.class, () -> service.validateEnumRangeErrorEnumName(Direction.LEFT));
        log.info("ConstraintViolationException: {}", e.getMessage());
    }

    @Test
    public void validateEnumRangeErrorWithoutRange() {

        ConstraintDeclarationException e = Assertions.assertThrows(ConstraintDeclarationException.class, () -> service.validateEnumRangeErrorWithoutRange(Direction.LEFT));
        log.info("ConstraintViolationException: {}", e.getMessage());
    }

    // ~ StringRange
    // =====================================================================================

    @Test
    public void validateStringRange() {

        LocaleContextHolder.setLocale(Locale.CHINA);

        Assertions.assertDoesNotThrow(() -> service.validateStringRange("aaa"));
        Assertions.assertDoesNotThrow(() -> service.validateStringRange("bbb"));
        ConstraintViolationException e = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateStringRange("ccc"));
        log.info("ConstraintViolationException: {}", e.getMessage());
    }

    @Test
    public void validateStringRangeErrorWithoutRange() {

        ConstraintDeclarationException e = Assertions.assertThrows(ConstraintDeclarationException.class, () -> service.validateStringRangeErrorWithoutRange("123"));
        log.info("ConstraintViolationException: {}", e.getMessage());
    }

    @Test
    public void validateStringRangeTrim() {

        LocaleContextHolder.setLocale(Locale.CHINA);

        Assertions.assertDoesNotThrow(() -> service.validateStringRangeTrim("aaa "));
        Assertions.assertDoesNotThrow(() -> service.validateStringRangeTrim(" bbb"));
        ConstraintViolationException e = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateStringRangeTrim("ccc "));
        log.info("ConstraintViolationException: {}", e.getMessage());
    }

    @Test
    public void validateStringRangeIgnoreCase() {

        LocaleContextHolder.setLocale(Locale.CHINA);

        Assertions.assertDoesNotThrow(() -> service.validateStringRangeIgnoreCase("AAA"));
        Assertions.assertDoesNotThrow(() -> service.validateStringRangeIgnoreCase("bBb"));
        ConstraintViolationException e = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateStringRangeIgnoreCase("ccc"));
        log.info("ConstraintViolationException: {}", e.getMessage());
    }

    // ~ UUID
    // =====================================================================================

    @Test
    void validateUuidTest() {

        Assertions.assertDoesNotThrow(() -> service.validateUuid(null));

        String uuid = UUID.randomUUID().toString();
        Assertions.assertDoesNotThrow(() -> service.validateUuid(uuid));

        ConstraintViolationException ex;
        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateUuid(""));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateUuid(" "));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateUuid("123"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateUuid(uuid.substring(1)));
        log.info("ConstraintViolationException: {}", ex.getMessage());
    }

    // ~ IPv4
    // =====================================================================================

    @Test
    void validateIPv4Test() {

        Assertions.assertDoesNotThrow(() -> service.validateIPv4(null));
        Assertions.assertDoesNotThrow(() -> service.validateIPv4("0.0.0.0"));
        Assertions.assertDoesNotThrow(() -> service.validateIPv4("255.255.255.255"));

        ConstraintViolationException ex;
        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4(""));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4(" "));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4("abc"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4("255.255.255.256"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4("-1.255.255.255"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4("0.0.0.0.0"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4("0.0.0.a"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateIPv4("a.a.a.a"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

    }

    // ~ MAC
    // =====================================================================================

    @Test
    void validateMACTest() {

        Assertions.assertDoesNotThrow(() -> service.validateMAC(null));
        Assertions.assertDoesNotThrow(() -> service.validateMAC("00-FF-54-40-1E-CD"));
        Assertions.assertDoesNotThrow(() -> service.validateMAC("00-00-00-00-00-00"));
        Assertions.assertDoesNotThrow(() -> service.validateMAC("FF-FF-FF-FF-FF-FF"));

        ConstraintViolationException ex;
        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateMAC(""));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateMAC(" "));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateMAC("abc"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateMAC("255.255.255.256"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateMAC("FF-FF-FF-FF-FF-FG"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateMAC("FF-FF-FF-FF-FF-FF-aa"));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class, () -> service.validateMAC("FF-FF-FF-FF-FF"));
        log.info("ConstraintViolationException: {}", ex.getMessage());
    }

    @Test
    void validateMultipartFileTest() {

        String originalFilename = "fake-data";
        Assertions.assertDoesNotThrow(() ->
                service.validateMultipartFile(new MockMultipartFile("fake-name", originalFilename, MediaType.MULTIPART_FORM_DATA_VALUE, new byte[1])));

        ConstraintViolationException ex;
        ex = Assertions.assertThrows(ConstraintViolationException.class,
                () -> service.validateMultipartFile(new MockMultipartFile("fake-name", originalFilename, MediaType.MULTIPART_FORM_DATA_VALUE, new byte[100])));
        log.info("ConstraintViolationException: {}", ex.getMessage());
        ex = Assertions.assertThrows(ConstraintViolationException.class,
                () -> service.validateMultipartFile(new MockMultipartFile("fake-name", originalFilename, MediaType.APPLICATION_JSON_VALUE, new byte[1])));
        log.info("ConstraintViolationException: {}", ex.getMessage());

        ex = Assertions.assertThrows(ConstraintViolationException.class,
                () -> service.validateMultipartFile(new MockMultipartFile("fake-name", originalFilename, null, new byte[1])));
        log.info("ConstraintViolationException: {}", ex.getMessage());
    }
}
