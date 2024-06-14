package countdownlatch;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MyThread extends Thread {
    CountDownLatch countDownLatch;
    int count = 0;

    public MyThread(CountDownLatch countDownLatch, int count) {
        super("mythread:" + System.currentTimeMillis());
        this.countDownLatch = countDownLatch;
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println(getName() + " start");
        try {
            Thread.sleep(1000L * count);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            countDownLatch.countDown();
            System.out.println(getName() + " end");
        }
    }
}
