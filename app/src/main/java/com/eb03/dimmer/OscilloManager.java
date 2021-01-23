package com.eb03.dimmer;

public class OscilloManager {
    private static OscilloManager mOscilloManager=null;
    private Transceiver mTransceiver;

    public static OscilloManager getInstance(){
        if (mOscilloManager==null){
            mOscilloManager=new OscilloManager();
        }
        return mOscilloManager;
    }

    public void attachTransceiver(Transceiver transceiver) {
        mTransceiver=transceiver;
    }

    public void connectTrasceiver(String id){
        mTransceiver.connect(id);
    }

    public void setCallibrationDutyCycle(byte b){
        byte[] msg={0x0A, b};
        mTransceiver.send(msg);
    }

}
