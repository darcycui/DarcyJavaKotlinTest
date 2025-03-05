package algorithm.sort;

/**
 * 快速排序
 */
public class QuickSort {
    private static void quickSort(int[] array, int low, int high) {
        if (low > high) {
            return;
        }
        int i = low;
        int j = high;
        int target = array[i];
        int t = 0;
        while (i < j) {
            // 右边哨兵 找比target小的
            while (array[j] >= target && i < j) {
                j--;
            }
            // 左边哨兵 找比target大的
            while (array[i] <= target && i < j) {
                i++;
            }
            // 交换 哨兵停下的位置的两元素
            if (i < j) {
                t = array[i];
                array[i] = array[j];
                array[j] = t;
            }

        }
        // 交换 low位置和左哨兵(右哨兵)位置的两元素
        array[low] = array[i];
        array[i] = target;
        // 分治 递归左半边
        quickSort(array, low, i - 1);
        // 分治 递归右半边
        quickSort(array, i + 1, high);
    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 2, 4, 62, 3, 1, 9, 19};
        quickSort(arr, 0, arr.length - 1);
        for (int item : arr) {
            System.out.print(item + " ");
        }

    }

}
