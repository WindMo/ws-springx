package ws.spring.validate;

import ws.spring.validate.annotation.EnumRange;

import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintValidatorContext;
import java.util.EnumSet;
import java.util.Set;

/**
 * 枚举元素约束校验器
 *
 * @author WindShadow
 * @version 2021-12-16.
 */

@SuppressWarnings({"rawtypes"})
public class EnumRangeRangeConstraintValidator extends AbstractElementRangeConstraintValidator<EnumRange, Enum> {

    private Class<? extends Enum> type;

    @Override
    public boolean isValid(Enum value, ConstraintValidatorContext context) {

        /*
            美中不足的是，类型不符合时抛出异常，
            虽然阻止了校验过程，以提示开发者预期限定的枚举和被校验目标的枚举不是同一个类型，即校验注解作用的目标类型错误
            但比真正的校验目标类型逻辑稍稍晚些，因为注解中不能使用泛型统配所有的注解
         */
        checkType(value);
        return super.isValid(value, context);
    }

    /**
     * 根据指定的枚举类型{@link EnumRange#enumType()}和枚举名称{@link EnumRange#enums()}
     * 调用{@link Enum#valueOf(Class, String)}返回对应的枚举实例集合
     *
     * @param enumRange
     * @return
     */
    @SuppressWarnings({"unchecked"})
    @Override
    protected Set<Enum> getElements(EnumRange enumRange) {

        this.type = enumRange.enumType();
        String[] elementNames = enumRange.enums();
        EnumSet enumSet = EnumSet.noneOf(type);
        for (String elName : elementNames) {

            try {
                Enum e = Enum.valueOf(type, elName);
                enumSet.add(e);
            } catch (IllegalArgumentException e) {
                throw new ConstraintDeclarationException(e);
            }

        }
        return enumSet;
    }

    private void checkType(Enum value) throws ConstraintDeclarationException {

        if (!type.isInstance(value)) {

            throw new ConstraintDeclarationException("This enumeration type <" + value.getClass().getName() + "> does not match the expected enumeration type <" + type.getName() + ">");
        }
    }
}
