package sort;

public class QuickSort3 {
    private static void quickSort(int[] array, int low, int high) {
        // todo 递归结束条件
        if(low > high){
            return;
        }
        int i = low;
        int j = high;
        int target = array[i];
        int t = 0;
        while (i < j) {
            // 右哨兵 j找比target小的
            while (array[j] >= target && i < j) {
                j--;
            }
            // todo 左哨兵 i找比target大的
            while (array[i] <= target && i < j) {
                i++;
            }
            // 交换
            if (i < j) {
                t = array[i];
                array[i] = array[j];
                array[j] = t;
            }
        }
        // 交换
        array[low] = array[i];
        array[i] = target;
        // todo 递归
        quickSort(array, low, i - 1);
        quickSort(array, i + 1, high);
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 2, 4, 62, 3, 1, 9, 19};
        quickSort(arr, 0, arr.length - 1);
        for (int item : arr) {
            System.out.println(item);
        }
    }
}
