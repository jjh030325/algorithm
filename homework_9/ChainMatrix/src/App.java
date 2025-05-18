public class App {
    static int arraySize[] = {10, 20, 5, 15, 30};  // 행렬 차원
    static int n = arraySize.length - 1;           // 행렬 개수
    static int dp[][] = new int[n + 1][n + 1];     // 최소 곱셈 수 저장
    static int s[][] = new int[n + 1][n + 1];      // 최적 분할 위치 저장
    static int ans = 0;                            // 최종 최소 곱셈 수

    // DP 대각선 초기화
    public static void startSetDp() {
        for (int i = 1; i <= n; i++) {
            dp[i][i] = 0;
        }
    }

    // 연속 행렬 곱셈 DP 테이블 채우기
    public static void ChainedMatrixMultiplication() {
        for (int length = 2; length <= n; length++) {
            for (int i = 1; i <= n - length + 1; i++) {
                int j = i + length - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + arraySize[i - 1] * arraySize[k] * arraySize[j];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        s[i][j] = k;
                    }
                }
            }
        }
        ans = dp[1][n];
    }

    // 최적 괄호 순서를 문자열로 반환
    public static String getOptimalParens(int i, int j) {
        if (i == j) {
            return "A" + i;
        } else {
            return "(" + getOptimalParens(i, s[i][j]) + " x " + getOptimalParens(s[i][j] + 1, j) + ")";
        }
    }

    // DP 테이블 출력
    public static void printDpTable() {
        System.out.println("Chained Matrix Multiplication");
        System.out.println("===================================");
        System.out.printf("M");
        for (int i = 1; i <= n; i++) {
            System.out.printf("%8d", i);
        }
        System.out.println();
        for (int i = 1; i <= n; i++) {
            System.out.print(i);
            for (int j = 1; j <= n; j++) {
                System.out.printf("%8d", dp[i][j]);  // 대각선 아래도 0으로 출력
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        startSetDp();
        ChainedMatrixMultiplication();
        printDpTable();

        System.out.println();
        System.out.println("Final Solution : " + ans + "\n");
        System.out.println("Implicit Order for Matrix Multiplication: " + getOptimalParens(1, n));
    }
}
