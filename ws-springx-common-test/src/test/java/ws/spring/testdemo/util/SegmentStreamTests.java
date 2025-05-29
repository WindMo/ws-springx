package ws.spring.testdemo.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ws.spring.util.SegmentStream;
import ws.spring.util.SegmentStreams;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author WindShadow
 * @version 2025-05-29.
 * @see SegmentStream
 * @see SegmentStreams
 */
public class SegmentStreamTests {

    protected final Random random = new Random();

    private List<Object> data;
    private SegmentStream<Object> stream;

    @BeforeEach
    void before() {

        data = new ArrayList<>();
        int length = random.nextInt(100) + 1;
        for (int i = 1; i <= length; i++) {
            data.add(i);
        }
        int interval = (int) Math.ceil(length / 20.0);
        stream = SegmentStreams.ofArray(interval, data.toArray());
    }

    @Test
    void forEachTest() {

        final int interval = stream.getInterval();

        List<Object> list = new ArrayList<>(data.size());
        int count = 0;

        while (count < 2) {

            count++;
            Assertions.assertSame(interval, stream.getInterval());
            Assertions.assertSame(0, stream.getOffset());

            stream.forEach((Consumer<Object>) list::add);
            Assertions.assertEquals(data, list);
            Assertions.assertSame(list.size(), stream.getOffset());

            stream.reset();
            list.clear();
        }

        while (count < 4) {

            count++;
            stream.forEachSegment((Consumer<List<Object>>) list::addAll);
            Assertions.assertEquals(data, list);
            Assertions.assertSame(list.size(), stream.getOffset());

            stream.reset();
            list.clear();
        }

        int maxSize = data.size() / 2;
        stream.forEach(v -> {

            list.add(v);
            return list.size() < maxSize;
        });
        Assertions.assertSame(maxSize, list.size());
        Assertions.assertTrue(list.size() <= stream.getOffset());
        Assertions.assertEquals(data.stream().limit(maxSize).collect(Collectors.toList()), list);

        stream.reset();
        list.clear();

        int maxSegment = Math.max(data.size() / interval - 1, 1);
        stream.forEachSegment(vs -> {

            list.addAll(vs);
            return Math.ceil(list.size() / (interval + 0.0)) < maxSegment;
        });
        Assertions.assertSame(Math.min(maxSegment * interval, data.size()), list.size());
        Assertions.assertEquals(data.stream().limit(list.size()).collect(Collectors.toList()), list);
    }

    @Test
    void mapTest() {

        data = data.stream().map(Object::toString).collect(Collectors.toList());
        stream = stream.map(Object::toString);
        forEachTest();
    }

    @Test
    void emptyTest() {

        int interval = random.nextInt(10) + 1;
        SegmentStream<Object> emptyStream = SegmentStreams.emptySegmentStream(interval);
        int count = 0;
        while (count < 3) {

            count++;
            Assertions.assertSame(interval, emptyStream.getInterval());
            Assertions.assertSame(0, emptyStream.getOffset());

            Consumer<Object> consumer = Mockito.mock(Consumer.class);
            emptyStream.forEach(consumer);
            Mockito.verify(consumer, Mockito.times(0)).accept(Mockito.anyString());

            Consumer<List<Object>> listConsumer = Mockito.mock(Consumer.class);
            emptyStream.forEachSegment(listConsumer);
            Mockito.verify(listConsumer, Mockito.times(0)).accept(Mockito.any(List.class));

            emptyStream.reset();
            if (count == 2) {
                emptyStream = emptyStream.map(Object::toString);
            }
        }
    }
}
