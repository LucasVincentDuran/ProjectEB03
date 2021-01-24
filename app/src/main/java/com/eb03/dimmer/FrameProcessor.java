package com.eb03.dimmer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Permet la gestion des Frames pour le protocole bluetooth
 */
public class FrameProcessor {
    private byte[] txFrame;
    private byte[] rxDate;

    public void fromFrame(){

    }

    /**
     * Permet la création des trames bluetooth
     * @param payload
     *              Tableau de bytes à inserer dans la trame
     * @return
     *          Trame  suivant le protocole et contenant les bytes à envoyer
     */
    public byte[] toFrame(byte[] payload){

        //HEADER AND LENGTH
        ArrayList<Byte> msg = new ArrayList<Byte>();
        msg.add((byte) 0x05);
        byte temp= (byte) (payload.length>>8);
        if (temp== 0x05 ||  temp== 0x04){
            msg.add((byte) 0x06);
            msg.add((byte) (temp+0x06));

        }else{msg.add(temp);}

        temp= (byte) (payload.length);
        if (temp== 0x05 ||  temp== 0x04){
            msg.add((byte) 0x06);
            msg.add((byte) (temp+0x06));

        }else{msg.add(temp);}

        //PAYLOAD
        for (int i=0; i<payload.length; i++){
            msg.add(payload[i]);
            if (payload[i]==0x06){
                msg.add((byte)0x0C);
            }
            if (payload[i]== 0x05 ||  payload[i]== 0x04){
                msg.add((byte) 0x06);
                msg.add((byte) (payload[i]+0x06));
            }
        }

        //CTRL AND TAIL
        byte ctrl=0;
        for(int i=0; i<payload.length;i++){
            ctrl+=payload[i];
        }
        ctrl+=(byte) (payload.length);
        ctrl+=(byte) (payload.length>>8);
        ctrl=(byte) (ctrl^0xff);
        ctrl+=1;
        ctrl= (byte)( ctrl%256);

        msg.add( ctrl);
        msg.add((byte)0x04);

        byte[] frame = new byte[msg.size()];
        for (int i=0; i<msg.size(); i++){
            frame[i] = (byte) msg.get(i);
        }
        return frame;

    }
}
