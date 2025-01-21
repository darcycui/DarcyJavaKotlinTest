package function;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class TestFunction {
    public static void main(String[] args) {
        System.out.println("Think in Function Programming");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("numbers=" + numbers);
        // 判断是否是偶数
        Predicate<Integer> isEven = n -> n %2 == 0;
        // java8 流式api
        // 函数式编程 filter/toList
        List<Integer> evenNumbers = numbers.stream().filter(isEven).toList();
        System.out.println("evenNumbers=" + evenNumbers);
    }
}
