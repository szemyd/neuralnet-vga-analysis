/**
 * Copyright (c) 2014-2016, Data Geekery GmbH, contact@datageekery.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jooq.lambda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import org.jooq.lambda.fi.util.function.CheckedBiConsumer;
import org.jooq.lambda.fi.util.function.CheckedObjDoubleConsumer;
import org.jooq.lambda.fi.util.function.CheckedObjIntConsumer;
import org.jooq.lambda.fi.util.function.CheckedObjLongConsumer;
import org.junit.Test;

/**
 * @author Lukas Eder
 */
public class CheckedBiConsumerTest {

    @Test
    public void testCheckedBiConsumer() {

        final CheckedBiConsumer<Object, Object> biConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };

        BiConsumer<Object, Object> test = Unchecked.biConsumer(biConsumer);
        BiConsumer<Object, Object> alias = CheckedBiConsumer.unchecked(biConsumer);

        assertBiConsumer(test, UncheckedException.class);
        assertBiConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedBiConsumerWithCustomHandler() {

        final CheckedBiConsumer<Object, Object> biConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };

        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        BiConsumer<Object, Object> test = Unchecked.biConsumer(biConsumer, handler);
        BiConsumer<Object, Object> alias = CheckedBiConsumer.unchecked(biConsumer, handler);

        assertBiConsumer(test, IllegalStateException.class);
        assertBiConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedObjIntConsumer() {

        final CheckedObjIntConsumer<Object> objIntConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };

        ObjIntConsumer<Object> test = Unchecked.objIntConsumer(objIntConsumer);
        ObjIntConsumer<Object> alias = CheckedObjIntConsumer.unchecked(objIntConsumer);

        assertObjIntConsumer(test, UncheckedException.class);
        assertObjIntConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedObjIntConsumerWithCustomHandler() {

        final CheckedObjIntConsumer<Object> objIntConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ObjIntConsumer<Object> test = Unchecked.objIntConsumer(objIntConsumer, handler);
        ObjIntConsumer<Object> alias = CheckedObjIntConsumer.unchecked(objIntConsumer, handler);

        assertObjIntConsumer(test, IllegalStateException.class);
        assertObjIntConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedObjLongConsumer() {

        final CheckedObjLongConsumer<Object> objLongConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };

        ObjLongConsumer<Object> test = Unchecked.objLongConsumer(objLongConsumer);
        ObjLongConsumer<Object> alias = CheckedObjLongConsumer.unchecked(objLongConsumer);

        assertObjLongConsumer(test, UncheckedException.class);
        assertObjLongConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedObjLongConsumerWithCustomHandler() {

        final CheckedObjLongConsumer<Object> objLongConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ObjLongConsumer<Object> test = Unchecked.objLongConsumer(objLongConsumer, handler);
        ObjLongConsumer<Object> alias = CheckedObjLongConsumer.unchecked(objLongConsumer, handler);

        assertObjLongConsumer(test, IllegalStateException.class);
        assertObjLongConsumer(alias, IllegalStateException.class);
    }

    @Test
    public void testCheckedObjDoubleConsumer() {

        final CheckedObjDoubleConsumer<Object> objDoubleConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };

        ObjDoubleConsumer<Object> test = Unchecked.objDoubleConsumer(objDoubleConsumer);
        ObjDoubleConsumer<Object> alias = CheckedObjDoubleConsumer.unchecked(objDoubleConsumer);

        assertObjDoubleConsumer(test, UncheckedException.class);
        assertObjDoubleConsumer(alias, UncheckedException.class);
    }

    @Test
    public void testCheckedObjDoubleConsumerWithCustomHandler() {

        final CheckedObjDoubleConsumer<Object> objDoubleConsumer = (o1, o2) -> {
            throw new Exception(o1 + ":" + o2);
        };
        final Consumer<Throwable> handler = e -> {
            throw new IllegalStateException(e);
        };

        ObjDoubleConsumer<Object> test = Unchecked.objDoubleConsumer(objDoubleConsumer, handler);
        ObjDoubleConsumer<Object> alias = CheckedObjDoubleConsumer.unchecked(objDoubleConsumer, handler);

        assertObjDoubleConsumer(test, IllegalStateException.class);
        assertObjDoubleConsumer(alias, IllegalStateException.class);
    }

    private <E extends RuntimeException> void assertBiConsumer(BiConsumer<Object, Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, null);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:null");
        }

        try {
            Map<String, Integer> map = new LinkedHashMap<>();
            map.put("a", 1);
            map.put("b", 2);
            map.put("c", 3);
            map.forEach(test);
        } catch (RuntimeException e) {
            assertException(type, e, "a:1");
        }
    }

    private <E extends RuntimeException> void assertObjIntConsumer(ObjIntConsumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, 0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:0");
        }
    }

    private <E extends RuntimeException> void assertObjLongConsumer(ObjLongConsumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, 0L);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:0");
        }
    }

    private <E extends RuntimeException> void assertObjDoubleConsumer(ObjDoubleConsumer<Object> test, Class<E> type) {
        assertNotNull(test);
        try {
            test.accept(null, 0.0);
            fail();
        } catch (RuntimeException e) {
            assertException(type, e, "null:0.0");
        }
    }

    private <E extends RuntimeException> void assertException(Class<E> type, RuntimeException e, String message) {
        assertEquals(type, e.getClass());
        assertEquals(Exception.class, e.getCause().getClass());
        assertEquals(message, e.getCause().getMessage());
    }
}
