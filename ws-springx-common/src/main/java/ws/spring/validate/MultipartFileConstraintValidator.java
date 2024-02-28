package ws.spring.validate;

import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ws.spring.validate.annotation.MultipartFileConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author WindShadow
 * @version 2022-11-22.
 */

public class MultipartFileConstraintValidator implements ConstraintValidator<MultipartFileConstraint, MultipartFile> {

    private String[] contentTypes;

    private boolean allowNullContentType;

    private long maxSize;

    @Override
    public void initialize(MultipartFileConstraint constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
        this.contentTypes = constraintAnnotation.contentTypes();
        this.allowNullContentType = constraintAnnotation.allowNullContentType();
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {

        if (!ObjectUtils.isEmpty(contentTypes)) {

            String contentType = multipartFile.getContentType();
            if ((contentType == null && !allowNullContentType) || !ObjectUtils.containsElement(contentTypes, contentType)) {

                return false;
            }
        }
        if (maxSize != MultipartFileConstraint.UNLIMITED_SIZE) {

            long size = multipartFile.getSize();
            return size <= maxSize;
        }
        return true;
    }
}
