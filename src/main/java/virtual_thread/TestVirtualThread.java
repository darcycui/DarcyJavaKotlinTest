package virtual_thread;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class TestVirtualThread {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello from " + Thread.currentThread());
        Runnable runnable = () -> {
            System.out.println("Hello from " + Thread.currentThread());
        };
        testVirtualThread(runnable);
        testVirtualThreadPool(runnable);
        Thread.sleep(2_000);
    }

    private static void testVirtualThread(Runnable runnable) {
        // TODO 启动一个虚拟线程 并执行任务runnable
//        Thread virtualThread =  Thread.startVirtualThread(runnable);

//        for (int i = 0; i < 10; i++) {
//            // TODO 启动一个虚拟线程 并执行任务runnable
//            Thread virtualThread = Thread.ofVirtual().start(runnable);
//            try {
//                virtualThread.join();
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    private static void testVirtualThreadPool(Runnable runnable) throws InterruptedException {
        // TODO 使用try-with-resource 自动关闭ExecutorService
//        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
//            // TODO 这里使用函数式编程 生成0-10的任务
//            IntStream.range(0, 10).forEach(i -> {
//                        // runnable 任务 使用execute
//                        executor.execute(runnable);
//                    }
//            );
//
//            // future 任务 使用submit
//            Future<Integer> futureInt = executor.submit(() -> {
//                // 线程睡眠
//                Thread.sleep(1000);
//                return 4;
//            });
//            executor.submit(() -> {
//                Thread.sleep(500);
//                return "This is a message from Tom.";
//            });
//            int resultInt = futureInt.get();
//            System.out.println("resultInt==" + resultInt);
//            executor.shutdown();
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }
}
