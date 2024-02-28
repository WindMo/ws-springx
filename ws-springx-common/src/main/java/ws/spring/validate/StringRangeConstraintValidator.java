package ws.spring.validate;

import ws.spring.validate.annotation.StringRange;

import javax.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 字符串元素范围约束检验器
 *
 * @author WindShadow
 * @version 2021-12-16.
 */

public class StringRangeConstraintValidator extends AbstractElementRangeConstraintValidator<StringRange, String> {

    private boolean trim;
    private boolean ignoreCase;

    @Override
    protected Set<String> getElements(StringRange stringRange) {

        this.trim = stringRange.trim();
        this.ignoreCase = stringRange.ignoreCase();
        return Stream.of(stringRange.value()).map(this::applyValue).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        return super.isValid(applyValue(value), context);
    }

    private String applyValue(String value) {

        String v = trim ? value.trim() : value;
        return ignoreCase ? v.toUpperCase() : v;
    }
}
