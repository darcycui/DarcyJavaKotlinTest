package design.mode.factory.simple;

import design.mode.factory.bean.netmanager.INetManager;
import design.mode.factory.bean.netmanager.impl.*;

/**
 * 简单工厂
 */
public class SimpleFactory {
    public static INetManager create(NetType netType) {
        switch (netType){
            case HTTP:
                return new HttpImpl();
            case HTTPS:
                return new HttpsImpl();
            case WEBSOCKET:
                return new WebsocketImpl();
            case TCP:
                return new TCPImpl();
            default:
                return new EmptyNetImpl();
        }
    }

    public enum NetType {
        HTTP, HTTPS, WEBSOCKET, TCP
    }
}
