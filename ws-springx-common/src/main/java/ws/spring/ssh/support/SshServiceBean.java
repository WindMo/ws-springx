package ws.spring.ssh.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ws.spring.ssh.SshOperator;
import ws.spring.ssh.SshService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author WindShadow
 * @version 2024-03-03.
 */
public class SshServiceBean extends DelegateSshService implements ApplicationContextAware, InitializingBean, DisposableBean {

    private final Set<SshOperator> operators = new CopyOnWriteArraySet<>();
    private final List<SshSourceRegistrar> registrars = new CopyOnWriteArrayList<>();

    public SshServiceBean(SshService delegate) {
        super(delegate);
    }

    @Override
    public SshOperator buildSshOperator(String sourceName) {

        SshOperator operator = new SshOperatorBean(super.buildSshOperator(sourceName));
        operators.add(operator);
        return operator;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        addRegistrars(context.getBeansOfType(SshSourceRegistrar.class).values());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        for (SshSourceRegistrar registrar : registrars) {
            registrar.registerSshSources(this);
        }
    }

    @Override
    public void destroy() throws Exception {
        closeAllSshOperators();
    }

    public void addRegistrars(Collection<SshSourceRegistrar> registrars) {
        this.registrars.addAll(registrars);
    }

    public void addRegistrars(SshSourceRegistrar... registrars) {
        this.registrars.addAll(Arrays.asList(registrars));
    }

    protected void closeAllSshOperators() throws Exception {

        for (SshOperator operator : operators) {
            if (!operator.isClosed()) {
                operator.close();
            }
        }
    }
}
