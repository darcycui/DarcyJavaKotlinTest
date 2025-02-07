package future;

import java.util.concurrent.*;

public class TestFuture {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        testFuture();
        testCompletableFuture();
    }

    private static void testCompletableFuture() throws InterruptedException, ExecutionException {
        // TODO 使用supplyAsync 创建一个异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try {
                // 测试异常
//                int a = 1/0;
                Thread.sleep(2000);
                // TODO 这里是Callable的返回值
                return "Hello I'm a string from CompletableFuture.";
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).whenComplete((result, throwable) -> { //TODO 获取结果以及异常处理
            System.out.println("resultString=" + result);
            System.err.println("throwable=" + throwable);
        });
        Thread.sleep(3000);
    }

    private static void testFuture() {
        // 创建线程池
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
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
            // Callable接口有返回值
            // TODO submit 执行 返回一个Future
            Future<String> stringFuture = executor.submit(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    Thread.sleep(2000);
                    System.out.println("Future finish---");
                    // TODO 这里是Callable的返回值
                    return "Hello I'm a string from future.";
                }
            });
            try {
                // TODO 通过Future的get方法获取返回值
                String str = stringFuture.get();
                System.out.println("str=" + str);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
