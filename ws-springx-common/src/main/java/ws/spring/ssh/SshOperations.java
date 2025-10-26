/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.ssh;

import org.springframework.util.StreamUtils;
import ws.spring.constant.NetworkConstants;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @author WindShadow
 * @version 2024-02-26.
 */
public interface SshOperations {

    // ~exec
    // ==================================

    default String exec(String command) {
        return exec(command, StandardCharsets.UTF_8);
    }

    default String exec(String command, Charset charset) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        exec(command, out);
        return StreamUtils.copyToString(out, charset);
    }

    void exec(String command, OutputStream out);

    // ~shell
    // ==================================

    /**
     * 创建一个shell信道连接输入输出流，在信道关闭前阻塞，通常输入exit退出shell
     *
     * @param in
     * @param out
     */
    void shell(InputStream in, OutputStream out);

    /**
     * 同 {@link #shell(InputStream, OutputStream)}，命令输入可通过输出流控制，通常在其它线程使用该输出流；
     * shell的输出结果可通过输入流读取制，通常在其它线程使用该输入流。
     *
     * @param inputConsumer
     * @param outputConsumer
     */
    void shell(Consumer<OutputStream> inputConsumer, Consumer<InputStream> outputConsumer);

    void shell(Consumer<OutputStream> inputConsumer, OutputStream out);

    void shell(InputStream in, Consumer<InputStream> outputConsumer);

    // ~forward
    // ==================================

    default PortForwardingTracker forward(String remoteHost, int remotePort) {
        return forward(NetworkConstants.RANDOM_PORT, remoteHost, remotePort);
    }

    default PortForwardingTracker forward(String localHost, String remoteHost, int remotePort) {
        return forward(localHost, NetworkConstants.RANDOM_PORT, remoteHost, remotePort);
    }

    default PortForwardingTracker forward(int localPort, String remoteHost, int remotePort) {
        return forward(NetworkConstants.IP_V4_ANY_ADDR, localPort, remoteHost, remotePort);
    }

    PortForwardingTracker forward(String localHost, int localPort, String remoteHost, int remotePort);
}
