import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class App {
    public static void main(String[] args) {
        int[] weights = {5, 4, 6, 3};
        int[] values = {10, 40, 30, 50};
        int value = 80;
        int n = weights.length;

        int MAX_VALUE = Arrays.stream(values).sum(); // 10+40+30+50 = 130
        int INF = 1000000;
        int[] dp = new int[MAX_VALUE + 1];

        Arrays.fill(dp, INF);
        dp[0] = 0;

        // DP 테이블 채우기
        for (int i = 0; i < n; i++) {
            for (int v = MAX_VALUE; v >= values[i]; v--) {
                if (dp[v - values[i]] + weights[i] < dp[v]) {
                    dp[v] = dp[v - values[i]] + weights[i];
                }
            }
        }

        // 가치가 80만원이 넘는 것 중 최소 무게
        int minWeight = INF;
        int targetValue = -1;

        for (int v = value; v <= MAX_VALUE; v++) {
            if (dp[v] < minWeight) {
                minWeight = dp[v];
                targetValue = v;
            }
        }

        if (targetValue == -1) {
            System.out.println("조건을 만족하는 조합이 없습니다.");
            return;
        }

        System.out.println("최소 무게: " + minWeight + "kg");
        System.out.println("총 가치: " + targetValue + "만원");

        // 선택된 아이템 역추적
        List<Integer> selectedItems = new ArrayList<>();
        int v = targetValue;

        for (int i = n - 1; i >= 0; i--) {
            while (v >= values[i] && dp[v] == dp[v - values[i]] + weights[i]) {
                selectedItems.add(i + 1); // 아이템 번호 추가 
                v -= values[i];
            }
        }

        Collections.reverse(selectedItems);
        System.out.println("선택된 아이템 번호: " + selectedItems);
    }
}

/*
2. 응용문제
총 가치가 80만원을 넘지 않으면서, 총 무게를 최소로 하는 경우를 구하시오.
즉, 예산 안에서 가장 가벼운 아이템 조합을 찾으시오.
 */