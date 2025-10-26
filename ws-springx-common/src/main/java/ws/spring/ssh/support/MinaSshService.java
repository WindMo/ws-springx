/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh.support;

import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.client.session.ClientSessionCreator;
import org.apache.sshd.common.session.SessionHeartbeatController;
import ws.spring.ssh.*;

import java.io.IOException;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-03-03.
 */
public class MinaSshService extends AbstractSshService {

    private final ClientSessionCreator client;

    public MinaSshService(ClientSessionCreator client, SshDuration sshDuration) {
        super(sshDuration);
        this.client = Objects.requireNonNull(client);
    }

    @Override
    protected SshOperator doBuildSshOperator(SshSource sshSource) {
        return new MinaSshOperator(sshSource, openSession(sshSource), sshDuration.getChannelTimeout());
    }

    private ClientSession openSession(SshSource sshSource) {

        try {

            ClientSession session = client.connect(sshSource.getUsername(), sshSource.getHost(), sshSource.getPort())
                    .verify(sshDuration.getConnectTimeout())
                    .getSession();
            // 设置心跳，避免服务端无流量断开连接
            session.setSessionHeartbeat(SessionHeartbeatController.HeartbeatType.IGNORE, sshDuration.getHeartbeatTimeout());
            if (sshSource instanceof SshAccount) {

                SshAccount account = (SshAccount) sshSource;
                session.addPasswordIdentity(account.getPassword());
            } else if (sshSource instanceof SshKeyPair) {

                SshKeyPair kp = (SshKeyPair) sshSource;
                session.addPublicKeyIdentity(kp.getKeyPair());
            } else {
                throw new UnsupportedOperationException("Unmatched SshSource");
            }
            session.auth().verify(sshDuration.getConnectTimeout());
            return session;
        } catch (IOException e) {
            throw new SshIOException(sshSource, "Open ssh connect failed", e);
        }
    }
}