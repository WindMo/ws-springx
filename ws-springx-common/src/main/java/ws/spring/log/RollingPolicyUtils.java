/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.log;

import ch.qos.logback.core.spi.ContextAwareBase;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

/**
 * @author WindShadow
 * @version 2022-11-16.
 */

class RollingPolicyUtils {

    public static void tryChangeFilePermissions(String filePath, Set<PosixFilePermission> filePermissions, ContextAwareBase contextAware) {

        File logFile = new File(filePath);
        try {
            if (logFile.exists()) {

                Assert.isTrue(logFile.isFile(), String.format("The log file path [%s] not is a rel file", logFile));
                Files.setPosixFilePermissions(logFile.toPath(), filePermissions);
            }
        } catch (IOException e) {
            contextAware.addError("Change file [" + filePath + "] mode [" + filePermissions + "] failed", e);
        } catch (UnsupportedOperationException e) {

            // windows下可能不支持更改文件权限
            contextAware.addWarn("Change file [" + filePath + "] mode [" + filePermissions + "] failed. Maybe your current operating system supports changing file permissions", e);
        }
    }
}
