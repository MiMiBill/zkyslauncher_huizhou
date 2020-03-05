package com.muju.note.launcher.app.Cabinet.bleLock;

public class BleLockInfo {
    private static BleLockInfo app = new BleLockInfo();

    private BluetoothLeService bluetoothLeService;

    public static BleLockInfo getInstance(){
        return app;
    }
    public BluetoothLeService getBluetoothLeService(){
        return bluetoothLeService;
    }

    public void setBluetoothLeService(BluetoothLeService bluetoothLeService){
        this.bluetoothLeService = bluetoothLeService;
    }

}
