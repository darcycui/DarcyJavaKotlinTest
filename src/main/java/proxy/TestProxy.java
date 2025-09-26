package proxy;

import proxy.bean.Child;
import proxy.bean.Parent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class TestProxy {

    public static void main(String[] args) {
        Child child = new Child("李四");
        Parent parent = new Parent(10, 20, child);

        class MyInvocationHandler implements InvocationHandler {
            private final Object target;

            public MyInvocationHandler(Object target) {
                this.target = target;
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("方法 " + method.getName() + " 开始执行" + " 参数：" + Arrays.toString(args));
                Object result = method.invoke(target, args);
                System.out.println("方法 " + method.getName() + " 执行结束" + " 参数：" + Arrays.toString(args));
                return result;
            }
        }
        // 动态代理 parent
        IService parentProxy = (IService) Proxy.newProxyInstance(
                Parent.class.getClassLoader(),
                Parent.class.getInterfaces(), new MyInvocationHandler(parent));
        parentProxy.doSomething("AAA");

        AManager aManager = new AManager();
        AManager.startService("AAA"); // 原来的逻辑
        // 反射 用 parentProxy 替换 AManager 中的 iService
        // 修复2：反射替换静态字段
        try {
            Field iServiceField = AManager.class.getDeclaredField("iService");
            iServiceField.setAccessible(true);
            iServiceField.set(null, parentProxy);  // 静态字段设置
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // 验证替换效果
        AManager.startService("AAA");  // 此时应触发代理逻辑
    }
}
