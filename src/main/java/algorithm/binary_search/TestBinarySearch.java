package algorithm.binary_search;

public class TestBinarySearch {
    public static void main(String[] args) {
        int[] arr = {1, 3, 5, 6, 7, 8, 10, 14, 23, 29, 46, 59, 60};
        int target = 46;
        boolean exist = binarySearch(arr, target);
        System.out.println("exist " + target + "==" + exist);
    }

    private static boolean binarySearch(int[] arr, int target) {
        int mid = arr.length / 2;
        if (arr.length == 0) {
            return false;
        }
        if (target == arr[mid]) {
            return true;
        } else if (target > arr[mid]) {
            int length = arr.length - mid;
            int[] rightArr = new int[length - 1];
            System.arraycopy(arr, mid + 1, rightArr, 0, length - 1);
            return binarySearch(rightArr, target);
        } else {
            int length = mid - 0;
            int[] leftArr = new int[length];
            System.arraycopy(arr, 0, leftArr, 0, length);
            return binarySearch(leftArr, target);
        }
    }
}
