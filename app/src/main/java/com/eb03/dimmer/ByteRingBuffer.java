package com.eb03.dimmer;

import java.nio.BufferOverflowException;


public class ByteRingBuffer {
    public byte[] buffer = null;

    private int capacity  = 0;
    private int writePos  = 0;
    private int available = 0;

    public ByteRingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new byte[capacity];
        this.writePos = 0;
        this.available = 0;
    }

    public void put(byte b){

        if(available < capacity){
            if(writePos >= capacity){
                writePos = 0;
            }
            buffer[writePos] = b;
            writePos++;
            available++;
            return;
        }
        throw new BufferOverflowException();
    }

    public void put(byte b[]){
        for (int i=0; i<b.length; i++){
            put(b[i]);
        }
    }


    public byte get() {
        if(available == 0){
            return (byte) -1;
        }
        int nextSlot = writePos - available;
        if(nextSlot < 0){
            nextSlot += capacity;
        }
        byte nextObj = buffer[nextSlot];
        available--;
        return nextObj;
    }

    public byte[] getAll(){
        if(available == 0){
            return null;
        }
        byte[] result = new byte[available];
        int index=0;
        while (available>0){
            result[index]=get();
            index++;
        }
        return result;
    }

    public int bytesToRead(){
        return available;
    }

    public String toString(){
        return "Ring buffer of size " + String.valueOf(capacity) + " with " + String.valueOf(available) + " values";
    }
}
