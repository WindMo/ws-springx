/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.constant;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.file.attribute.PosixFilePermission;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * @author WindShadow
 * @version 2022-09-28.
 */

public interface LinuxConstants {

    Pattern MODE_PATTERN = Pattern.compile(RegexpConstants.REGEXP_LINUX_MODE_EXPRESSION);
    int MODE_READ = 4;
    int MODE_WRITE = 2;
    int MODE_EXECUTABLE = 1;

    int MODE_NO = 0;

    int MODE_ALL = MODE_READ + MODE_WRITE + MODE_EXECUTABLE;

    /**
     *
     * @param singleMode 满足 0 <= singleMode <= 7
     * @return
     */
    static boolean isValidSingleMode(final int singleMode) {

        return MODE_NO <= singleMode && singleMode <= MODE_ALL;
    }

    /**
     *
     * @param fullMode 满足 0 <= fullMode <= 777
     * @return
     */
    static boolean isValidFullMode(final int fullMode) {

        return MODE_NO <= fullMode && fullMode <= (MODE_ALL * 111);
    }

    /**
     *
     * @param modeExpression 满足从 "000" 至 "777"
     * @return
     */
    static boolean isValidModeExpression(@Nullable final String modeExpression) {

        return StringUtils.hasText(modeExpression) && MODE_PATTERN.matcher(modeExpression).matches();
    }

    /**
     *
     * @param fullMode fullMode
     * @return PosixFilePermissions, read only
     */
    static Set<PosixFilePermission> transformPermissions(final int fullMode) {

        Assert.isTrue(isValidFullMode(fullMode), "Incorrect permission full mode value: [" + fullMode + "]");
        return transformPermissions(String.format("%03d",fullMode));
    }

    /**
     *
     * @param modeExpression modeExpression
     * @return PosixFilePermissions, read only
     */
    static Set<PosixFilePermission> transformPermissions(final String modeExpression) {

        Assert.isTrue(isValidModeExpression(modeExpression), "Incorrect permission mode expression: [" + modeExpression + "]");
        char[] modes = modeExpression.toCharArray();
        Set<PosixFilePermission> perms = new HashSet<>();
        if (modeExpression.length() == 3) {

            int ownerMode = modes[0] - 48;
            int groupMode = modes[1] - 48;
            int othersMode = modes[2] - 48;
            perms.addAll(PermissionCategory.Owner.transformPermissions(ownerMode));
            perms.addAll(PermissionCategory.Group.transformPermissions(groupMode));
            perms.addAll(PermissionCategory.Others.transformPermissions(othersMode));
        } else {

            for (int i = 0; i < 9; i++) {

                PermissionCategory pc = (i < 3) ? PermissionCategory.Owner :
                        ((5 < i) ? PermissionCategory.Others : PermissionCategory.Group);
                pc.acceptPermission(modes[i], perms::add);
            }
        }
        return Collections.unmodifiableSet(perms);
    }

    enum PermissionCategory {

        Owner(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE, PosixFilePermission.OWNER_EXECUTE),
        Group(PosixFilePermission.GROUP_READ, PosixFilePermission.GROUP_WRITE, PosixFilePermission.GROUP_EXECUTE),
        Others(PosixFilePermission.OTHERS_READ, PosixFilePermission.OTHERS_WRITE, PosixFilePermission.OTHERS_EXECUTE);

        private final PosixFilePermission readPerm;
        private final PosixFilePermission writePerm;
        private final PosixFilePermission execPerm;

        PermissionCategory(PosixFilePermission readPerm, PosixFilePermission writePerm, PosixFilePermission execPerm) {
            this.readPerm = readPerm;
            this.writePerm = writePerm;
            this.execPerm = execPerm;
        }

        public PosixFilePermission getReadPerm() {
            return readPerm;
        }

        public PosixFilePermission getWritePerm() {
            return writePerm;
        }

        public PosixFilePermission getExecPerm() {
            return execPerm;
        }

        /**
         * 根据权限数值获取其表示的权限集
         *
         * @param singleMode 权限数值，满足 0 <= singleMode <= 7
         * @return 权限集，无权时为空集合
         */
        public final Set<PosixFilePermission> transformPermissions(final int singleMode) {

            Assert.isTrue(isValidSingleMode(singleMode), () -> "Incorrect permission singleMode: " + singleMode);
            Set<PosixFilePermission> perms = new HashSet<>();
            if ((singleMode & MODE_READ) == MODE_READ) {
                perms.add(readPerm);
            }
            if ((singleMode & MODE_WRITE) == MODE_WRITE) {
                perms.add(writePerm);
            }
            if ((singleMode & MODE_EXECUTABLE) == MODE_EXECUTABLE) {
                perms.add(execPerm);
            }
            return Collections.unmodifiableSet(perms);
        }

        /**
         * 根据权限字符获取其表示的权限，'-'代表无权限
         *
         * @param singleMode 权限字符，如：r、w、x、-
         * @return null代表无权限
         */
        @Nullable
        public final PosixFilePermission transformPermission(final char singleMode) {

            switch (singleMode) {

                case 'r':
                case 'R':
                    return readPerm;
                case 'w':
                case 'W':
                    return writePerm;
                case 'x':
                case 'X':
                    return execPerm;
                case '-':
                    return null;
                default:
                    throw new IllegalArgumentException("Incorrect permission singleMode: " + singleMode);
            }
        }

        /**
         * 根据权限字符消费其表示的权限，'-'代表无权限
         *
         * @param singleMode 权限字符，如：r、w、x、-
         * @param predicate 消费条件，PosixFilePermission为null代表无权限
         * @param consumer 消费者
         */
        public final void acceptPermission(final char singleMode, Predicate<PosixFilePermission> predicate, Consumer<PosixFilePermission> consumer) {

            PosixFilePermission permission = transformPermission(singleMode);
            if (predicate.test(permission)) {
                consumer.accept(permission);
            }
        }

        /**
         * 根据权限字符消费其表示的权限，'-'代表无权限
         *
         * @param singleMode 权限字符，如：r、w、x、-
         * @param consumer 消费者，消费的权限枚举不为null
         */
        public final void acceptPermission(final char singleMode, Consumer<PosixFilePermission> consumer) {

            acceptPermission(singleMode, Objects::nonNull, consumer);
        }
    }
}
