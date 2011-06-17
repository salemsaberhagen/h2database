/*
 * Copyright 2004-2008 H2 Group. Multiple-Licensed under the H2 License, 
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.h2.test.unit;

import java.util.BitSet;
import java.util.Random;

import org.h2.test.TestBase;
import org.h2.util.BitField;

/**
 * A unit test for bit fields.
 */
public class TestBitField extends TestBase {

    public void test() {
        testRandom();
        testGetSet();
    }

    private void testRandom() {
        BitField bits = new BitField();
        BitSet set = new BitSet();
        int max = 300;
        int count = 100000;
        Random random = new Random(1);
        for (int i = 0; i < count; i++) {
            int idx = random.nextInt(max);
            if (random.nextBoolean()) {
                if (random.nextBoolean()) {
                    bits.set(idx);
                    set.set(idx);
                } else {
                    bits.clear(idx);
                    set.clear(idx);
                }
            } else {
                assertEquals(bits.get(idx), set.get(idx));
                assertEquals(bits.nextClearBit(idx), set.nextClearBit(idx));
                assertEquals(bits.nextSetBit(idx), set.nextSetBit(idx));
            }
        }
    }

    private void testGetSet() {
        BitField bits = new BitField();
        for (int i = 0; i < 10000; i++) {
            bits.set(i);
            if (!bits.get(i)) {
                fail("not set: " + i);
            }
            if (bits.get(i + 1)) {
                fail("set: " + i);
            }
        }
        for (int i = 0; i < 10000; i++) {
            if (!bits.get(i)) {
                fail("not set: " + i);
            }
        }
        for (int i = 0; i < 1000; i++) {
            int k = bits.nextClearBit(0);
            if (k != 10000) {
                fail("" + k);
            }
        }
    }
}