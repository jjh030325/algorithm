import java.util.Random;

public class App {
    static int arraySize[];
    static int n;
    static int dp[][];
    static int s[][];
    static int ans = 0;

    public static int[] generateRandomMatrixDimensions(int matrixCount) {
        Random rand = new Random();
        int[] arr = new int[matrixCount + 1];
        arr[0] = rand.nextInt(10) + 5;

        for (int i = 1; i <= matrixCount; i++) {
            arr[i] = rand.nextInt(10) + 5;
        }

        return arr;
    }

    public static void startSetDp() {
        for (int i = 1; i <= n; i++) {
            dp[i][i] = 0;
        }
    }

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

    public static String getOptimalParens(int i, int j) {
        if (i == j) {
            return "A" + i;
        } else {
            return "(" + getOptimalParens(i, s[i][j]) + " x " + getOptimalParens(s[i][j] + 1, j) + ")";
        }
    }

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
                System.out.printf("%8d", dp[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int initialMatrixCount = 5;
        int maxMatrixCount = 10;

        Random rand = new Random();
        int[] baseDimensions = new int[maxMatrixCount + 1]; // 최대 길이 확보

        baseDimensions[0] = rand.nextInt(10) + 5;
        for (int i = 1; i <= maxMatrixCount; i++) {
            baseDimensions[i] = rand.nextInt(10) + 5;
        }

        for (int matrixCount = initialMatrixCount; matrixCount <= maxMatrixCount; matrixCount++) {
            System.out.println("\n===========================");
            System.out.println("Matrix Count: " + matrixCount);
            System.out.println("===========================\n");

            arraySize = new int[matrixCount + 1];
            for (int i = 0; i <= matrixCount; i++) {
                arraySize[i] = baseDimensions[i]; // 안전하게 복사
            }

            n = matrixCount;
            dp = new int[n + 1][n + 1];
            s = new int[n + 1][n + 1];

            System.out.print("Generated Dimensions: ");
            for (int dim : arraySize) {
                System.out.print(dim + " ");
            }
            System.out.println("\n");

            startSetDp();
            ChainedMatrixMultiplication();

            printDpTable();
            System.out.println();
            System.out.println("Final Solution : " + ans + "\n");
            System.out.println("Implicit Order for Matrix Multiplication: " + getOptimalParens(1, n));
            System.out.println("\n\n");
        }
    }
}
