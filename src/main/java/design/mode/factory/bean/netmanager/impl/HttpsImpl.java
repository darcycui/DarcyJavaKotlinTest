package design.mode.factory.bean.netmanager.impl;

import design.mode.factory.bean.netmanager.INetManager;

public class HttpsImpl implements INetManager {
    private final String TAG = this.getClass().getSimpleName() + ":";

    @Override
    public void send(String message) {
        System.out.println(TAG + "send " + message);
    }
}
