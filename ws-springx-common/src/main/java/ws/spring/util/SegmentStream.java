package ws.spring.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 分段流
 *
 * @author WindShadow
 * @version 2025-05-28.
 * @see SegmentStreams
 */
public interface SegmentStream<E> {

    /**
     * 重置此分段流
     */
    void reset();

    /**
     * 转换成另外的分段流，其中偏移量和peek过的元素总数记录保持不变
     *
     * @param interval
     * @param mapping
     * @return
     * @param <T>
     */
    <T> SegmentStream<T> map(int interval, Function<? super E, ? extends T> mapping);

    /**
     * {@link #map(int, Function)}的重载方法，其中分段间隔保持不变
     *
     * @param mapping
     * @return
     * @param <T>
     */
    default <T> SegmentStream<T> map(Function<? super E, ? extends T> mapping) {
        return map(getInterval(), mapping);
    }

    /**
     * 获取当前peek过的元素总数
     *
     * @return
     */
    int getOffset();

    /**
     * 获取分段间隔
     *
     * @return
     */
    int getInterval();

    /**
     * 获取下一分段
     *
     * @return
     */
    List<E> nextSegment();

    /**
     * 从当前偏移量开始迭代每个元素
     *
     * @param consumer
     */
    default void forEach(Consumer<E> consumer) {

        Objects.requireNonNull(consumer);
        forEachSegment((Consumer<List<E>>) es -> es.forEach(consumer));
    }

    /**
     * 从当前偏移量开始迭代每个元素，当 predicate 结果为false时停止
     *
     * @param predicate
     */
    void forEach(Predicate<E> predicate);

    /**
     * 从当前偏移量开始迭代每个分段，当 predicate 结果为false时停止
     *
     * @param predicate
     */
    void forEachSegment(Predicate<List<E>> predicate);

    /**
     * 从当前偏移量开始迭代每个分段
     *
     * @param consumer
     */
    default void forEachSegment(Consumer<List<E>> consumer) {

        Objects.requireNonNull(consumer);
        forEachSegment(es -> {
            consumer.accept(es);
            return true;
        });
    }

    @FunctionalInterface
    interface SegmentVernier<E> {

        /**
         * @param offset offset 从0开始
         * @param interval interval > 0
         * @return elements Not be null
         */
        List<E> peek(int offset, int interval);
    }
}
