/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh;

import java.security.KeyPair;

/**
 * @author WindShadow
 * @version 2024-02-26.
 */
public class SshKeyPair extends AbstractSshSource {

   private KeyPair keyPair;

    public KeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.keyPair = keyPair;
    }
}
