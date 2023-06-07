package ws.spring.testdemo.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import ws.spring.testdemo.validate.enums.Direction;
import ws.spring.validate.annotation.*;

import java.lang.annotation.ElementType;

/**
 * @author WindShadow
 * @version 2022-09-19.
 */

@Slf4j
@Validated
@Service
public class ValidateServiceSupport {

    // ~ EnumRange
    // =====================================================================================

    public void validateEnumRange(@EnumRange(enumType = Direction.class, enums = {"UP", "DOWN"}) Direction direction) {

        log.info("direction: {}", direction);
    }

    public void validateEnumRangeErrorWithoutRange(@EnumRange(enumType = Direction.class, enums = {}) Direction direction) {

        log.info("direction: {}", direction);
    }

    public void validateEnumRangeErrorEnumName(@EnumRange(enumType = Direction.class, enums = {"abc"}) Direction direction) {

        log.info("direction: {}", direction);
    }

    public void validateEnumRangeErrorEnumType(@EnumRange(enumType = Direction.class, enums = {"DOWN"}) ElementType elementType) {

        log.info("elementType: {}", elementType);
    }

    // ~ StringRange
    // =====================================================================================

    public void validateStringRange(@StringRange({"aaa", "bbb"}) String str) {

        log.info("str: {}", str);
    }

    public void validateStringRangeErrorWithoutRange(@StringRange({}) String str) {

        log.info("str: {}", str);
    }

    public void validateStringRangeTrim(@StringRange(value = {"aaa", "bbb"}, trim = true) String str) {

        log.info("str: {}", str);
    }

    public void validateStringRangeIgnoreCase(@StringRange(value = {"aaa", "bbb"}, ignoreCase = true) String str) {

        log.info("str: {}", str);
    }

    // ~ UUID
    // =====================================================================================

    public void validateUuid(@UUID CharSequence uuid) {

        log.info("uuid:{}", uuid);
    }

    // ~ IPv4
    // =====================================================================================

    public void validateIPv4(@IPv4 CharSequence ipv4) {

        log.info("ipv4:{}", ipv4);
    }

    // ~ MAC
    // =====================================================================================

    public void validateMAC(@MAC CharSequence mac) {

        log.info("mac:{}", mac);
    }

    // ~ MultipartFile
    // =====================================================================================

    public void validateMultipartFile(@MultipartFileConstraint(contentTypes = {MediaType.MULTIPART_FORM_DATA_VALUE}, maxSize = 10L) MultipartFile multipartFile) {

        log.info("multipartFile: {}", multipartFile.getName());
    }
}
