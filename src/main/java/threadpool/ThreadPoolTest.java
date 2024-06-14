package threadpool;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");
//        ThreadPoolTest.testThreadPoolShutdown();
//        ThreadPoolTest.testThreadPoolShutdownNow();
//        ThreadPoolTest.testThreadPoolJVMHook();

        testThreadPoolOrder();
    }

    /**
     * 提交顺序 aaa bbb(入队列) ccc(队列已满 创建非核心线程执行)
     * 执行顺序 aaa ccc bbb
     * @throws InterruptedException
     */
    public static void testThreadPoolOrder() throws InterruptedException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 2, 60L,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(1));
        pool.execute(new Thread("aaa") {
            @Override
            public void run() {
                System.out.println("start:" + getName());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("end:" + getName());
            }
        });
        Thread.sleep(5);
        pool.execute(new Thread("bbb") {
            @Override
            public void run() {
                System.out.println("start:" + getName());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("end:" + getName());
            }
        });
        Thread.sleep(5);
        pool.execute(new Thread("ccc") {
            @Override
            public void run() {
                System.out.println("start:" + getName());
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("end:" + getName());
            }
        });
        // 结束线程池
        pool.shutdown();
        // 等待线程池的任务执行完成
        boolean result = pool.awaitTermination(5, TimeUnit.SECONDS);
        if (result) {
            System.out.println("awaitTermination 成功");
        } else {
            System.out.println("awaitTermination 不成功");
        }
    }

    public static void testThreadPoolShutdown() throws InterruptedException {

        // corePoolSize 是 2，maximumPoolSize 是 2
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2, 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        pool.prestartAllCoreThreads(); // 启动所有 worker
        pool.execute(new Task()); // Task是一个访问某网站的 HTTP 请求，跑的慢，后面会贴出来完整代码，这里把他当做一个跑的慢的异步任务就行
        Thread.sleep(100);
        pool.shutdown();
        pool.execute(new Task()); // 在线程池 shutdown() 后 继续添加任务，这里预期是抛出异常
    }

    public static void testThreadPoolShutdownNow() throws InterruptedException {

        // corePoolSize 是 1，maximumPoolSize 是 1，无限容量
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());
        pool.prestartAllCoreThreads(); // 启动所有 worker
        pool.execute(new Task()); // Task是一个访问某网站的 HTTP 请求，跑的慢，后面会贴出来完整代码，这里把他当做一个跑的慢的异步任务就行
        pool.execute(new Task());
        Thread.sleep(100);
        List<Runnable> result = pool.shutdownNow();
        System.out.println(result);
        pool.execute(new Task()); // 在线程池 shutdownNow() 后 继续添加任务，这里预期是抛出异常
    }

    /**
     * JVM会等待当前线程池任务执行完毕再退出
     */
    public static void testThreadPoolJVMHook() throws InterruptedException {

        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        pool.execute(new Task());
        pool.execute(new Task());
        pool.execute(new Task());


        Thread shutdownHook = new Thread(() -> {
            pool.shutdown();
            try {
                pool.awaitTermination(3, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("等待超时，直接关闭");
            }
        });
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}
