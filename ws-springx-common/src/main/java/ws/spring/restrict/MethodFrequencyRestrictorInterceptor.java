package ws.spring.restrict;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import ws.spring.restrict.annotation.FrequencyRestrict;
import ws.spring.restrict.support.DelegateFrequencyRestrictor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WindShadow
 * @version 2024-01-26.
 */

public class MethodFrequencyRestrictorInterceptor implements MethodInterceptor {

    private final FrequencyRestrictRegistrar registrar;
    private final BeanResolver beanResolver;
    private final ParameterNameDiscoverer parameterNameDiscoverer;
    private final ExpressionParser parser;
    private final Map<Method, FrequencyRestrictorHolder> restrictorHolders = new ConcurrentHashMap<>(16);

    public MethodFrequencyRestrictorInterceptor(FrequencyRestrictRegistrar registrar, BeanFactory beanFactory, ParameterNameDiscoverer parameterNameDiscoverer, ExpressionParser parser) {
        this.registrar = registrar;
        this.beanResolver = new BeanFactoryResolver(beanFactory);
        this.parameterNameDiscoverer = parameterNameDiscoverer;
        this.parser = parser;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object root = invocation.getThis();
        if (root != null) {
            Method method = invocation.getMethod();
            FrequencyRestrictorHolder holder = findHolder(method);
            if (holder != null) {
                String refer;
                if (holder.isDynamicRefer()) {
                    Object[] arguments = invocation.getArguments();
                    MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(root, method, arguments, parameterNameDiscoverer);
                    context.setBeanResolver(beanResolver);
                    refer = holder.getReferExp().getValue(context, String.class);
                } else {
                    refer = holder.getDefaultRefer();
                }
                if (holder.tryRestrict(refer)) {

                    String message = holder.isDynamicRefer() ?
                            String.format("The refer[%s] accesses too frequently and access is denied", refer) : "Access is too frequent, access is denied";
                    throw new RestrictedCriticalException(holder.getName(), message);
                }
            }
        }
        return invocation.proceed();
    }

    @Nullable
    private FrequencyRestrictorHolder findHolder(Method method) {

        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(method, FrequencyRestrict.class, false, true);
        if (attributes == null) return null;
        return restrictorHolders.computeIfAbsent(method, md -> {

            FrequencyRestrictorDefinition definition = new FrequencyRestrictorDefinition();
            String name = attributes.getString("name");
            if (name.isEmpty()) {
                definition.setName(method.toGenericString());
            } else {
                definition.setName(name);
            }
            definition.setFrequency(attributes.getNumber("frequency"));
            definition.setDuration(attributes.getNumber("duration"));
            FrequencyRestrictor restrictor = registrar.registerRestrictor(definition);

            String refer = attributes.getString("refer");
            Expression referExp = null;
            String defaultRefer = null;
            if (refer.isEmpty()) {
                defaultRefer = method.toGenericString();
            } else {
                referExp = parser.parseExpression(refer);
            }
            return new FrequencyRestrictorHolder(referExp, defaultRefer, restrictor);
        });
    }

    private static class FrequencyRestrictorHolder extends DelegateFrequencyRestrictor {

        @Nullable
        private final Expression referExp;

        @Nullable
        private final String defaultRefer;
        private final boolean dynamicRefer;

        private FrequencyRestrictorHolder(Expression referExp, String defaultRefer, FrequencyRestrictor restrictor) {
            super(restrictor);
            Assert.isTrue((referExp == null && defaultRefer != null) || (referExp != null && defaultRefer == null),
                    "Only one of refer expression and default refer can be null");
            this.referExp = referExp;
            this.defaultRefer = defaultRefer;
            this.dynamicRefer = defaultRefer == null;
        }

        public Expression getReferExp() {
            return referExp;
        }

        public String getDefaultRefer() {
            return defaultRefer;
        }

        public boolean isDynamicRefer() {
            return dynamicRefer;
        }
    }
}
