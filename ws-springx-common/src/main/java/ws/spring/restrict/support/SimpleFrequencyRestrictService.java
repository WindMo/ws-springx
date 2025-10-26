/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.restrict.support;

import ws.spring.restrict.FrequencyRestrictor;
import ws.spring.restrict.FrequencyRestrictorDefinition;
import ws.spring.restrict.RestrictorDeclarationException;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 频控服务一种简单实现
 *
 * @author WindShadow
 * @version 2024-01-26.
 */

public class SimpleFrequencyRestrictService extends GenericFrequencyRestrictService {

    @Override
    protected FrequencyRestrictor createRestrictor(FrequencyRestrictorDefinition definition) throws RestrictorDeclarationException {
        return new BlockingFrequencyRestrictor(definition);
    }

    /**
     * 在内存中基于阻塞队列实现的频控器
     */
    protected static class BlockingFrequencyRestrictor extends GenericFrequencyRestrictor {

        /**
         * key为refer，队列元素为过期时间戳
         */
        private final Map<String, BlockingQueue<Long>> referQueues;

        protected BlockingFrequencyRestrictor(FrequencyRestrictorDefinition definition) {
            super(definition);
            this.referQueues = new ConcurrentHashMap<>();
        }

        @Override
        protected boolean doTryRestrict(String refer) {

            BlockingQueue<Long> queue = referQueues.computeIfAbsent(refer, r -> new ArrayBlockingQueue<>(frequency));
            long currentTimeMillis = System.currentTimeMillis();
            long nextExpirationTimeMillis = currentTimeMillis + durationMillis;
            // 如果入队成功，则频控失败
            if (queue.offer(nextExpirationTimeMillis)) return false;
            // 如果队列已满，则尝试移除旧时间戳
            synchronized (queue) {
                while (!queue.isEmpty() && (queue.peek() <= currentTimeMillis)) queue.remove();
            }
            return !queue.offer(nextExpirationTimeMillis);
        }

        @Override
        protected void doResetRestrict(String refer) {
            referQueues.remove(refer);
        }

        @Override
        public void resetRestrictor() {
            referQueues.clear();
        }
    }
}
