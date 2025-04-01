package ws.spring.jdbc.dynamic;

import ws.spring.jdbc.DelegateConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * @author WindShadow
 * @version 2024-12-30.
 */
class ConnectionProxy extends DelegateConnection {

    public ConnectionProxy(Connection delegate) {
        super(delegate);
    }

    @Override
    public void commit() throws SQLException {

    }

    @Override
    public void rollback() throws SQLException {

    }

    @Override
    public void close() throws SQLException {

    }
}
