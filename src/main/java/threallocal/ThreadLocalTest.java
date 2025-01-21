package threallocal;

public class ThreadLocalTest {
    public static void main(String[] args) throws InterruptedException {
        try {
            String name = Thread.currentThread().getName();
            ThreadLocal<String> threadLocal = new ThreadLocal<>();
            System.out.println(name + " set: ");
            threadLocal.set("Hello, ThreadLocal!"); // 设置当前线程的ThreadLocal变量值
            String readStr = threadLocal.get();
            System.out.println(name + " get: " + readStr); // 输出当前线程的ThreadLocal变量值: Hello, ThreadLocal!


            // 创建新线程，观察是否继承了main线程的ThreadLocal变量值
            new Thread(() -> {
                String nameOther = Thread.currentThread().getName();
                String readStrOtherThread = threadLocal.get();
                System.out.println(nameOther + " getFromOtherThread: " + readStrOtherThread); // 输出当前线程的ThreadLocal变量值:null
            }).start();

            // 移除当前线程的ThreadLocal变量值
            threadLocal.remove();
            String readStrAfterRemove = threadLocal.get();
            System.out.println(name + " getAfterRemove: " + readStrAfterRemove); // 输出当前线程的ThreadLocal变量值
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
