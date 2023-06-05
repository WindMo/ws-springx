package ws.spring.validate;

import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 元素范围约束检验器抽象实现
 *
 * @author WindShadow
 * @version 2021-12-16.
 */

public abstract class AbstractElementRangeConstraintValidator<A extends Annotation, T> implements ConstraintValidator<A, T> {

    private Set<T> elements;

    @Override
    public void initialize(A constraintAnnotation) {

        Set<T> elements = getElements(constraintAnnotation);
        if (CollectionUtils.isEmpty(elements)) {
            throw new ConstraintDeclarationException("The elements in the <" + constraintAnnotation.getClass().getName() + "> annotation must contain at least one element");
        }
        this.elements = elements;
    }

    @Override
    public boolean isValid(T value, ConstraintValidatorContext context) {

        if (!elements.contains(value)) {

            invalid(value, context);
            return false;
        }
        return true;
    }

    /**
     * 从注解中获取元素的Set集合
     *
     * @param constraintAnnotation constraintAnnotation
     * @return {@link Set#isEmpty()}必须为false
     */

    @NonNull
    protected abstract Set<T> getElements(A constraintAnnotation);

    /**
     * 校验不通过时调用
     *
     * @param value   value
     * @param context ConstraintValidatorContext
     */
    protected void invalid(T value, ConstraintValidatorContext context) {

    }
}
