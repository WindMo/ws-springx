package ws.spring.log;

import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.rolling.helper.Compressor;
import org.springframework.util.ReflectionUtils;
import ws.spring.constant.LinuxConstants;

import java.lang.reflect.Field;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Objects;
import java.util.Set;

/**
 * 支持设置日志文件权限的滚动策略
 *
 * @author WindShadow
 * @version 2022-11-06.
 */

public class AccessControlBasedRollingPolicy<E> extends SizeAndTimeBasedRollingPolicy<E> {

    public static final int DEFAULT_LOG_FILE_MODE = 600;
    private static final Set<PosixFilePermission> DEFAULT_FILE_PERMISSIONS = LinuxConstants.transformPermissions(DEFAULT_LOG_FILE_MODE);

    protected Set<PosixFilePermission> logFilePermissions = DEFAULT_FILE_PERMISSIONS;

    public void setFilePermissions(String filePermissions) {

        try {
            this.logFilePermissions = LinuxConstants.transformPermissions(filePermissions);
        } catch (IllegalArgumentException e) {
            addError("Set File Permissions failed", e);
        }
    }

    @Override
    public void start() {
        super.start();
        replaceCompressor();
    }

    private void replaceCompressor() {

        AgileCompressor agileCompressor = new AgileCompressor(getCompressionMode(),
                (nameOfFile2Compress, nameOfCompressedFile, innerEntryName, compressedFileName) ->
                        RollingPolicyUtils.tryChangeFilePermissions(compressedFileName, logFilePermissions, this));
        agileCompressor.setContext(context);
        setCompressor(agileCompressor);
    }

    protected final void setCompressor(Compressor compressor) {

        Field compressorField = ReflectionUtils.findField(AccessControlBasedRollingPolicy.class, "compressor", Compressor.class);
        Objects.requireNonNull(compressorField).setAccessible(true);
        ReflectionUtils.setField(compressorField, this, compressor);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "@" + this.hashCode();
    }
}
