package sort;

public class QuickSort2 {
    public static void quickSort(int[] array, int low, int high) {
        if (low > high) {
            return;
        }
        int i = low;
        int j = high;
        int target = array[low];
        int t = 0;
        while (i < j) {
            // 右哨兵
            while (array[j] >= target && i < j) {
                j--;
            }
            // 左哨兵
            while (array[i] <= target && i < j) {
                i++;
            }
            // 交换 i j
            if (i < j) {
                t = array[i];
                array[i] = array[j];
                array[j] = t;
            }
        }
        // 交换 low i(=j)
        array[low] = array[i];
        array[i] = target;

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
