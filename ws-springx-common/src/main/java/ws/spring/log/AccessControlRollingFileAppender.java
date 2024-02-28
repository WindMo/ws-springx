package ws.spring.log;

import ch.qos.logback.core.rolling.RollingFileAppender;
import ws.spring.constant.LinuxConstants;

import java.io.IOException;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * @author WindShadow
 * @version 2022-09-28.
 */

public class AccessControlRollingFileAppender<E> extends RollingFileAppender<E> {

    public static final int DEFAULT_FILE_MODE = 600;
    protected Set<PosixFilePermission> logFilePermissions = LinuxConstants.transformPermissions(DEFAULT_FILE_MODE);

    public AccessControlRollingFileAppender() {
    }

    public void setFilePermissions(String filePermissions) {
        this.logFilePermissions = LinuxConstants.transformPermissions(filePermissions);
    }

    @Override
    public void openFile(String fileName) throws IOException {

        super.openFile(fileName);
        RollingPolicyUtils.tryChangeFilePermissions(fileName, logFilePermissions, this);
    }
}
