package time;

import java.util.concurrent.TimeUnit;

public class TestTimePeriod {
    public static void main(String[] args) {
        long age =  TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - 0);
        System.out.println("age=" + age + "天"); //age=20405天
    }
}
