package com.eb03.dimmer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

/**
 * Clasee heritée de Transciever et qui permet la gestion de la communication Bluetooth
 */
public class BTManager extends Transceiver {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mAdapter;

    private BluetoothSocket mSocket = null;

    private ConnectThread mConnectThread = null;
    private WritingThread mWritingThread = null;

    public BTManager(BluetoothAdapter adapter){
        mAdapter=adapter;
    }

    /**
     * Permet la connexion avec un device
     * @param id
     *          L'id du device auquel on veut se connecter
     */
    @Override
    public void connect(String id) {
        attachFrameProcessor(new FrameProcessor());
        BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(id);
        disconnect();
        mConnectThread = new ConnectThread(device);
        setState(STATE_CONNECTING);
        mConnectThread.start();

    }


    @Override
    public void disconnect() {

    }

    /**
     * Permet de formater les trames de données grace au FrameProcessor et de les envoyer à traves le WritingThread
     * @param b
     *          Bytes de données à envoyer
     */
    @Override
    public void send(byte[] b) {
        mFrameProcessor= new FrameProcessor();
        byte[] f=mFrameProcessor.toFrame(b);
        System.out.println(Arrays.toString(f));
        mWritingThread.write(f);
    }

    /**
     * Classe du Thread de connexion au device
     */
    private class ConnectThread extends Thread{


        public ConnectThread(BluetoothDevice device) {
            //BluetoothSocket socket = null;

            try {
                mSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            mAdapter.cancelDiscovery();

            try {
                mSocket.connect();
            } catch (IOException e) {
                disconnect();
            }
            mConnectThread = null;
            startReadWriteThreads();

        }
    }

    /**
     * Fonction qui permet l'instanciation des threads de lecture/ecriture
     */
    private void startReadWriteThreads(){
        // instanciation d'un thread de lecture

        mWritingThread = new WritingThread(mSocket);
        Log.i("ConnectThread","Thread WritingThread lancé");
        mWritingThread.start();
        setState(STATE_CONNECTED);
    }

    /**
     * Classe du Thread d'écriture vers le device
     */
    private class WritingThread extends Thread{
        private OutputStream mOutStream;
        private ByteRingBuffer buffer;

        public WritingThread(BluetoothSocket mSocket) {
            try {
                mOutStream = mSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            buffer=new ByteRingBuffer(2048);
        }

        @Override
        public void run() {
            while(mSocket != null){
                if (buffer.bytesToRead()>0){
                    try {
                        mOutStream.write(buffer.get());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // déclarer une ref vers un buffer circulaire

        public void write(byte b[]){
            buffer.put(b);
        }

    }




}
