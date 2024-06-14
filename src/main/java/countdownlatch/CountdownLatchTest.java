package countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountdownLatchTest {
    //线程数
    private static int N = 10;

    public static void main(String[] args) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + ":start");
        // 
        CountDownLatch countDownLatch = new CountDownLatch(N);
        for (int i = 0; i < N; i++) {
            Thread myThread = new MyThread(countDownLatch, i + 1);
            myThread.start();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            // 主线程等待 countDownLatch 减到0
            countDownLatch.await();
            // 主线程等待 countDownLatch 减到0 或者 达到最大等待时间
//            countDownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(threadName + ":end");
    }
}
