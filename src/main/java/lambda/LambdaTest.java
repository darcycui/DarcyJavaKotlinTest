package lambda;

public class LambdaTest {
    public static void main(String[] args) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("内部类");
//            }
//        }).start();
        new Thread(() -> {
            System.out.println("lambda");
        }).start();
    }
}
