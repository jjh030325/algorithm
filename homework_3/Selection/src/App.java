import java.util.Random;
import java.util.Scanner;

public class App {
    static Random rand = new Random();
    static int start = 1, end = 100000;

    // 2차원 배열에 랜덤한 숫자 넣는 함수.
    public static void randomSet(int[][] arr, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                arr[i][j] = start + rand.nextInt(end);
            }
        }
    }

    // 1차원 배열로 변환 함수. 계산 간단하게 만들기 위함.
    public static int[] flatten(int[][] arr, int n) {
        int[] flat = new int[n * n];
        int idx = 0;
        for (int[] row : arr) {
            for (int val : row) {
                flat[idx++] = val;
            }
        }
        return flat;
    }

    // Select 시작
    public static int Select(int[] arr, int k) {
        return SelectHelper(arr, 0, arr.length - 1, k - 1);
    }

    // Select 재귀 함수
    private static int SelectHelper(int[] arr, int left, int right, int k) {
        if (left == right) return arr[left];  // 원소가 하나만 남는 경우
        int pivotIndex = partition(arr, left, right);  // pivot 기준으로 분할
        if (k == pivotIndex) return arr[k];  // pivot이 찾는 위치
        else if (k < pivotIndex) return SelectHelper(arr, left, pivotIndex - 1, k);  // 왼쪽 탐색 
        else return SelectHelper(arr, pivotIndex + 1, right, k);  // 오른쪽 탐색
    }

    // 분할 함수 (pivot과 같거나 작으면 왼쪽, 크면 오른쪽)
    private static int partition(int[] arr, int left, int right) {
        int pivot = arr[right];  // 마지막 원소를 pivot으로 선택
        int i = left;
        for (int j = left; j < right; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);  // pivot보다 작거나 같으면 왼쪽으로
                i++;
            }
        }
        swap(arr, i, right);  // pivot을 해당하는 자리로 이동.
        return i;
    }

    // 위치 변경 함수
    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // 반복적으로 min부터 세며 k번째 값 찾기
    public static int Counting(int[] arr, int k) {
        int min = 1;
        int max = 100000;

        for (int i = min; i <= max; i++) {
            int count = 0;
            for (int num : arr) {
                if (num == i) count++;
            }
            if (count > 0) {
                if (k <= count) return i;
                k -= count;
            }
        }
        return -1; // 오류 발생
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 500~1,000 사이의 입력값 n을 받으시오.
        System.out.print("n 입력 (500 ~ 1000): ");
        int n = sc.nextInt();
        while (n < 500 || n > 1000) {
            System.out.print("범위를 밖. 다시 입력하세요 (500 ~ 1000): ");
            n = sc.nextInt();
        }

        // n * n 크기의 이차원 배열을 만들고 1~100,000 사이의 랜덤값으로 채우시오.
        int[][] arr2D = new int[n][n];
        randomSet(arr2D, n);

        // 계산 간소화를 위한 2차원 배열 1차원화.
        int[] arr = flatten(arr2D, n);

        // 1~100,000 사이의 랜덤값 k
        int k = start + rand.nextInt(end);
        System.out.println("k = " + k);

        // 방법 1: Select 알고리즘으로 정렬 후 k번째 작은 값 찾는 방법
        int[] arr1 = arr.clone();
        long startTime1 = System.nanoTime();
        int kth1 = Select(arr1, k);
        long endTime1 = System.nanoTime();

        // 방법 2: MIN부터 차례대로 카운팅
        int[] arr2 = arr.clone();
        long startTime2 = System.nanoTime();
        int kth2 = Counting(arr2, k);
        long endTime2 = System.nanoTime();

        // 시간 비교
        System.out.printf("Selection 결과: %d (%.4f초)\n", kth1, (endTime1 - startTime1) / 1000000000.0);
        System.out.printf("Counting 결과: %d (%.4f초)\n", kth2, (endTime2 - startTime2) / 1000000000.0);

        sc.close();
    }
}


/*
500~1,000 사이의 입력값 n을 받으시오.

n * n 크기의 이차원 배열을 만들고 1~100,000 사이의 랜덤값으로 채우시오.

1~100,000 사이의 랜덤값 k를 선택하고, k번째 작은 수를 찾으시오.

위 과정을 “정렬 후 k번째 작은 값 찾는 방법”, 
“MIN값부터 차례로 k번째까지 반복하여 찾는 방법”과 
비교하여 어떤 방식이 더 효율적인지 실험적으로 보이시오.
 */