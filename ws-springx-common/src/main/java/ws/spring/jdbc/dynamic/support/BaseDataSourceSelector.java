package ws.spring.jdbc.dynamic.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import ws.spring.jdbc.dynamic.SwitchableDataSourceSelector;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * @author WindShadow
 * @version 2024-03-31.
 */
public abstract class BaseDataSourceSelector implements SwitchableDataSourceSelector {

    protected static final DataSourceName DEFAULT_DATA_SOURCE_NAME = NullDataSourceName.INSTANCE;

    protected final Log log = LogFactory.getLog(getClass());

    @Override
    public final String currentDataSource() {
        return determineCurrentName().getValue();
    }

    @Override
    public final void useDefaultDataSource() {
        saveCurrentName(DEFAULT_DATA_SOURCE_NAME);
    }

    @Override
    public final void selectedDataSource(@NonNull String name) {
        saveCurrentName(new DecidedDataSourceName(name));
    }

    @Override
    public final void resetSelected() {
        removeCurrentName();
    }

    @Override
    public final void runWithDefault(Runnable runnable) {
        runWith(DEFAULT_DATA_SOURCE_NAME, runnable);
    }

    @Override
    public final void runWith(String name, Runnable runnable) {
        runWith(new DecidedDataSourceName(name), runnable);
    }

    @Override
    public final <T> T runWithDefault(Callable<T> callable) throws Exception {
        return runWith(DEFAULT_DATA_SOURCE_NAME, callable);
    }

    @Override
    public final <T> T runWith(String name, Callable<T> callable) throws Exception {
        return runWith(new DecidedDataSourceName(name), callable);
    }

    // ~ internal method
    // ==================================

    protected final DataSourceName determineCurrentName() {

        DataSourceName currentName = getCurrentName();
        if (currentName == null) {
            if (log.isDebugEnabled()) {
                log.debug("No data source is selected. The default data source is used");
            }
            currentName = NullDataSourceName.INSTANCE;
        }
        return currentName;
    }

    @NotNull
    protected abstract BaseDataSourceSelector.DataSourceName getCurrentName();

    protected abstract void saveCurrentName(DataSourceName name);
    protected abstract void removeCurrentName();

    protected final void runWith(DataSourceName name, Runnable runnable) {

        try {
            runWith(name, () -> {
                runnable.run();
                return null;
            });
        } catch (RuntimeException e) {
            throw e;
        }  catch (Exception e) {
            // doesn't usually happen
            throw new IllegalStateException(e);
        }
    }

    protected final <T> T runWith(DataSourceName name, Callable<T> callable) throws Exception {

        Objects.requireNonNull(callable);
        DataSourceName lastName = determineCurrentName();
        saveCurrentName(name);
        try {
            return callable.call();
        } finally {
            saveCurrentName(lastName);
        }
    }

    protected interface DataSourceName {

        @Nullable
        String getValue();

        static DataSourceName of(@Nullable String name) {
            return name == null ? NullDataSourceName.INSTANCE : new DecidedDataSourceName(name);
        }
    }

    private static class DecidedDataSourceName implements DataSourceName {

        @NonNull
        private final String name;

        private DecidedDataSourceName(String name) {

            Assert.hasText(name, "The datasource name must not be empty/null");
            this.name = name;
        }

        @Override
        public String getValue() {
            return name;
        }

        @Override
        public String toString() {
            return "DataSourceName: " + name;
        }
    }

    private static class NullDataSourceName implements DataSourceName {

        public static final NullDataSourceName INSTANCE = new NullDataSourceName();

        private NullDataSourceName() {
        }

        @Override
        public String getValue() {
            return null;
        }

        @Override
        public String toString() {
            return "DataSourceName: null";
        }
    }
}
