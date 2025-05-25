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
            for(int j=0;j<=str2.length();j++)
            {
                dp[j][i] = 0;
            }
        }

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

    // 가장 긴 단어의 글자수 리턴 
    public static int mostLongWord(String [] dic) {
        int ans = 0;
        for(int i=0;i<dic.length;i++)
        {
            if(dic[i].length()>=ans)
            {
                ans = dic[i].length();
            }
        }
        return ans;
    }

    // 가장 유사한 단어의 인덱스 리턴 함수 
    public static int findBestWordIndex(String [] dic, int mostLength, int[][] dp, String str1) {
        int ans = mostLength;
        int index=0;

        for(int i=0;i<dic.length;i++)
        {
            dp = dpSet(str1, dic[i], dp);
            dp = EditDistance(str1, dic[i], dp);
            if(dp[dic[i].length()+1][str1.length()+1] < ans)
            {
                ans = dp[dic[i].length()+1][str1.length()+1];
                index = i;
            }
        }
        return index;
    }

    // 사전 단어 출력 함수
    public static void printDic(String [] dic)
    {
        System.out.print("사전 단어: ");
        System.out.print("[");
        for(int i=0;i<dic.length;i++)
        {
            System.out.print("\"" + dic[i] + "\"");
            if(i!=dic.length - 1){
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    // 추천 출력 함수
    public static void printSuggest(int ans, String[] dic, int [][] dp, String str1) {
        System.out.print("(출력) 추천: ");
        System.out.print("\"" + dic[ans] + "\" ");
        System.out.print("(edit distance ");
        
        dp = dpSet(str1, dic[ans], dp);
        dp = EditDistance(str1, dic[ans], dp);
        System.out.print(dp[dic[ans].length()][str1.length()]);
        System.out.println(")");
    }

    public static void main(String[] args) throws Exception {
        String [] dic = {"definitely", "defiantly", "define"};
        Scanner sc = new Scanner(System.in);

        System.out.print("사용자 입력 : ");
        String str1 = sc.next();

        sc.close();

        int[][] dp = new int[mostLongWord(dic)+2][str1.length()+2];

        int ans = findBestWordIndex(dic, mostLongWord(dic) + 1, dp, str1);

        printDic(dic);
        printSuggest(ans, dic, dp, str1);
    }
}

/*
사용자 입력: "definately"

사전 단어: ["definitely", "defiantly", "define"]

(출력) 추천: "definitely" (edit distance 3)
 */