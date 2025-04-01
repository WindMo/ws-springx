package ws.spring.jdbc.dynamic.tx;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

/**
 * @author WindShadow
 * @version 2025-01-01.
 */
public abstract class DynamicPlatformTransactionManager implements PlatformTransactionManager {




    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {
        return determineTargetTransactionManager().getTransaction(definition);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        determineTargetTransactionManager().commit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        determineTargetTransactionManager().rollback(status);
    }

    protected abstract PlatformTransactionManager determineTargetTransactionManager();
}
