import java.util.Arrays;

public class App {
    static int pivotCount = 0; // pivot 선택 횟수

    public static void quickSort(int[] arr, int left, int right) {
        // 정렬 범위가 유효할 때만 정렬 수행
        if (left < right) {
            // 피벗을 선택하고 분할 (pivot은 arr[right])
            int pivotIndex = partition(arr, left, right);

            // 피벗보다 작은 그룹 정렬 (왼쪽 부분)
            quickSort(arr, left, pivotIndex - 1);

            // 피벗보다 큰 그룹 정렬 (오른쪽 부분)
            quickSort(arr, pivotIndex + 1, right);
        }
    }

    // 피벗 선택 및 분할 작업
    public static int partition(int[] arr, int left, int right) {
        int pivot = arr[right]; // 배열 A[right]를 피벗으로 선택
        pivotCount++;

        System.out.println("\n피봇: " + pivot);
        System.out.println("배열: " + Arrays.toString(Arrays.copyOfRange(arr, left, right + 1)));

        int i = left - 1;

        // 배열 A[left+1] ~ A[right-1] 까지 피벗보다 작은 값은 앞으로 이동
        for (int j = left; j < right; j++) {
            if (arr[j] < pivot) {
                i++;
                // swap arr[i] <-> arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        //A[left]~A[i]: 작은 그룹, A[i+2]~A[right]: 큰 그룹
        int temp = arr[i + 1];
        arr[i + 1] = arr[right];
        arr[right] = temp;

        // 최종 피벗 위치 반환
        return i + 1;
    }

    public static void main(String[] args) throws Exception {
        int[] arr = {6, 3, 11, 9, 12, 2, 8, 15, 18, 10, 7, 14};
        System.out.println("기존 배열: " + Arrays.toString(arr));

        quickSort(arr, 0, arr.length - 1);

        System.out.println("\n정렬된 배열: " + Arrays.toString(arr));
        System.out.println("총 피봇 선택 횟수: " + pivotCount);
    }
}
