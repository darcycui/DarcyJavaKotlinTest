package proxy;

import proxy.bean.Child;
import proxy.bean.Parent;

public class AManager {
    private static IService iService = new Parent(1, 2, new Child("张三"));

    public static void startService(String param) {
        if (iService == null) {
            System.out.println("Error: iService 为空！");
            return;
        }
        iService.doSomething(param);
    }
}
