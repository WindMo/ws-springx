/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh;

/**
 * @author WindShadow
 * @version 2024-02-26.
 */
public class SshAccount extends AbstractSshSource {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
