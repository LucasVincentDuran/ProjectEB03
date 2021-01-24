package com.eb03.dimmer;

/**
 * Singleton permettant de faire l'interface entre la MainActivity qui est l'interface utilisateur
 * et la la partie modèle qui gère la communication en bluetooth
 */
public class OscilloManager {
    private static OscilloManager mOscilloManager=null;
    private Transceiver mTransceiver;

    /**
     * Constructeur pour le singleton
     * @return
     *          Un singleton
     */
    public static OscilloManager getInstance(){
        if (mOscilloManager==null){
            mOscilloManager=new OscilloManager();
        }
        return mOscilloManager;
    }

    /**
     * Permet d'attacher un transceiver
     * @param transceiver
     *                  Transceiver que l'on veut attacher
     */
    public void attachTransceiver(Transceiver transceiver) {
        mTransceiver=transceiver;
    }

    /**
     * Connection du transceiver avec avec un device (l'oscilloscope)
     * @param id
     *          id du device auquel on veut se connecter
     */
    public void connectTrasceiver(String id){
        mTransceiver.connect(id);
    }

    /**
     * Permet de regler le rapport cyclique de l'oscilloscope
     * @param b
     *          Valeur du rapport cyclique
     */
    public void setCallibrationDutyCycle(byte b){
        byte[] msg={0x0A, b};
        mTransceiver.send(msg);
    }

}
