package design.mode.factory.simple;

import design.mode.factory.bean.netmanager.INetManager;

public class Client {
    public static void main(String[] args) {
        INetManager iNetManager = SimpleFactory.create(SimpleFactory.NetType.WEBSOCKET);
        iNetManager.send("Hello");
        iNetManager = SimpleFactory.create(SimpleFactory.NetType.TCP);
        iNetManager.send("Hello");
    }
}
