package ws.spring.jdbc;

import lombok.experimental.Delegate;

import java.sql.Connection;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-12-30.
 */
public class DelegateConnection implements Connection {

    @Delegate
    private final Connection delegate;

    public DelegateConnection(Connection delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }
}
