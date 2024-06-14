package future;

import java.util.concurrent.*;

public class Test {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        System.out.println("Runnable start+++");
        // Runnable接口没有返回值
        // execute 执行 没有返回值
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    System.out.println("Runnable finish---");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        System.out.println("Future start+++");
        // Callable借口有返回值
        // submit 执行 返回一个Future
        Future<String> stringFuture = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(2000);
                System.out.println("Future finish---");
                return "Hello I'm a string from future.";
            }
        });
        try {
            // 通过Future的get方法获取返回值
            String str = stringFuture.get();
            System.out.println("str=" + str);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }
}
