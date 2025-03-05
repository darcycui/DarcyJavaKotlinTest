package algorithm.sort;

public class QuickSort2 {
    public static void quickSort(int[] arr, int low, int high) {
        if (low > high) {
            return;
        }
        int i = low;
        int j = high;
        int target = arr[low];
        while (i < j) {
            while (arr[j] >= target && i < j) {
                j--;
            }
            arr[i] = arr[j];
            while (arr[i] <= target && i < j) {
                i++;
            }
            arr[j] = arr[i];
        }
        arr[i] = target;
        quickSort(arr, low, i - 1);
        quickSort(arr, i + 1, high);

    }

    public static void main(String[] args) {
        int[] arr = {10, 7, 2, 4, 62, 3, 1, 9, 19};
        quickSort(arr, 0, arr.length - 1);
        for (int item : arr) {
            System.out.print(item + " ");
        }
    }
}
