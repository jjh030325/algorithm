import java.util.Scanner;

public class App {
    // 세개의 정수중 가장 작은 값을 리턴하는 함수
    public static int returnMin(int a, int b, int c) {
        int ans = a;
        if(a>=b && c>=b) {
            ans = b;
        }else if(a>=c && b>=c) {
            ans = c;
        }
        return ans;
    }

    // 초기 dp 세팅 함수
    public static int[][] dpSet(String str1, String str2, int[][] dp) {
        for(int i=0;i<=str1.length();i++)
        {
            dp[0][i] = i;
        }

        for(int i=0;i<=str2.length();i++)
        {
            dp[i][0] = i;
        }
        return dp;
    }

    // 편집 거리 계산 알고리즘 함수 
    public static int[][] EditDistance(String str1, String str2, int[][] dp) {
        for(int i=1;i<=str2.length();i++)
        {
            for(int j=1;j<=str1.length();j++)
            {
                if(str1.charAt(j-1) == str2.charAt(i-1))
                {
                    dp[i][j] = dp[i-1][j-1];
                }else
                {
                    dp[i][j] = returnMin(dp[i-1][j-1] + 1, dp[i-1][j] + 1, dp[i][j-1] + 1);
                }
            }
        }
        return dp;
    } 

    // dp 테이블 출력 함수 
    public static void printDpTable(String str1, String str2, int[][] dp) {
        System.out.print("    ");
        for(int i=1;i<=str1.length();i++)
        {
            System.out.print(str1.charAt(i-1)+" ");
        }
        System.out.println();
        
        for(int i=0;i<=str2.length();i++)
        {
            if(i==0) {
                System.out.print("  ");
            }else{
                System.out.print(str2.charAt(i-1)+" ");
            }
            for(int j=0;j<=str1.length();j++)
            {
                System.out.print(dp[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.print("str1 입력 : ");
        String str1 = sc.next();
        System.out.print("str2 입력 : ");
        String str2 = sc.next();

        sc.close();

        int[][] dp = new int[str2.length()+1][str1.length()+1];

        dp = dpSet(str1, str2, dp);

        dp = EditDistance(str1, str2, dp);

        printDpTable(str1, str2, dp);
    }
}
