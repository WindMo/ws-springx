package ws.spring.aop.support;

import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import java.lang.annotation.Annotation;

/**
 * @author WindShadow
 * @version 2024-04-06.
 */
public class FlexAnnotationMethodMatcher extends FlexMethodMatcher {

    public FlexAnnotationMethodMatcher(Class<? extends Annotation> annotation, boolean checkInherited) {
       super(new AnnotationClassFilter(annotation, checkInherited), new AnnotationMethodMatcher(annotation, checkInherited));
    }
}
