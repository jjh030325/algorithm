import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App {
    public static void main(String[] args) throws Exception {
        int []objWeight = {5, 4, 6, 3};
        int []objValue = {10, 40, 30, 50};
        int backPackSize = 10;
        int n = objWeight.length;

        // dp 테이블 생성 : n개의 물건을 backPackSize 크기의 가방에 넣을 때 가장 가치가 높은 것.
        int [][]dp = new int[n + 1][backPackSize + 1];

        // 테이블 순서대로 채우면서 계산, 새 물건을 넣는게 더 좋은 경우 갱신, 아니면 이전 값을 가져옴.
        for(int i=1;i<=n;i++)
        {
            for(int j=1;j<=backPackSize;j++)
            {
                if(objWeight[i-1]<=j) {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i-1][j-objWeight[i-1]]+objValue[i-1]);
                } else {
                    dp[i][j] = dp[i-1][j];
                }
            }
        }

        // 최대 가치 출력
        System.out.println("최대 가치: " + dp[n][backPackSize] + "만원");

        // 어떤 아이템이 선택되었는지 역추적
        int w = backPackSize;
        List<Integer> selectedItems = new ArrayList<>();
        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selectedItems.add(i);
                w -= objWeight[i - 1];
            }
        }

        // 아이템 출력
        Collections.reverse(selectedItems);
        System.out.println("선택된 아이템 번호: " + selectedItems);
    }
}

/*
1. 기본문제

아래와 같이 4개의 물건에 대한 무게와 가치가 주었다.
10kg까지 담을 수 있는 가방이 있을 때, 
1-1. 가장 가치가 높게 만들 수 있는 물건의 조합(최대 가치)를 구하시오.
1-2. 어떤 아이템이 선택되었는지 출력하시오.
Item 1: 5kg, 10만원
Item 2: 4kg, 40만원
Item 3: 6kg, 30만원
Item 4: 3kg, 50만원
총 배낭 용량: 10kg
 */