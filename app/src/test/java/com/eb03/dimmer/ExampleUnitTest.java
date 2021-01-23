package com.eb03.dimmer;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testBuffer() {
        FrameProcessor f= new FrameProcessor();
        byte b[]={0x07, 0x06};
        byte[] r= f.toFrame(b);
        for (int i =0; i<r.length;i++){
            System.out.println(Integer.toHexString(r[i]));
        }

        ByteRingBuffer buf= new ByteRingBuffer(2048);
        buf.put(b);
        System.out.println(buf.bytesToRead());
    }
}