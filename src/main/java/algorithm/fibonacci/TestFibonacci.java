package algorithm.fibonacci;

public class TestFibonacci {
    public static void main(String[] args) {
        int N = 10;
        System.out.println("fibonacci:" + N + "==" + fibonacci(N));
        System.out.println("fibonacci2:" + N + "==" + fibonacci2(N));
    }

    private static int fibonacci(int n) {
        int a = 0;
        int b = 1;
        int sum = 0;
        for (int i = 1; i < n; i++) {
            sum = a +b;
            a = b;
            b = sum;
        }
        return sum;
    }

    private static int fibonacci2(int n) {
        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }
        return fibonacci2(n - 1) + fibonacci2(n - 2);
    }
}
