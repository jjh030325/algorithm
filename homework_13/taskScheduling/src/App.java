import java.util.Arrays;

public class App {
    // 처리 시간이 긴 순서대로 정렬 후 순서대로 가장 빨리 비는 곳에 넣는 분배 방식
    public static int lpt(int n, int m, int[] time) {
        Integer[] idx = new Integer[time.length];
        int [] machine = new int[m];   // 전체 작업 시간 측정용
        int [] check = new int[n];     // 몇 번 작업이 몇번 머신에 들어갔는지 확인용
        for (int i = 0; i < time.length; i++) idx[i] = i;

        // time 값 기준으로 인덱스를 내림차순 정렬
        Arrays.sort(idx, (i1, i2) -> Integer.compare(time[i2], time[i1]));

        int min = 0;
        for (int i : idx) {
            // 가장 빠르게 끝나는 머신이 몇 번인지 확인
            for(int j = 0; j < m; j++)
            {
                // 초기값 인덱스 갱신
                if(j == 0)
                {
                    min = 0;
                }
                // 최소값 인덱스 갱신
                else if(machine[min] > machine[j])
                {
                    min = j;
                }
            }
            // 가장 빨리 끝나는 머신에 일 추가
            machine[min] += time[i];
            check[i] = min;
        }

        // 머신에 할당된 Task 출력
        for(int i = 0; i < m; i++)
        {
            int c = 0;
            System.out.print("머신" + (i + 1) + ":");
            for(int j : idx) 
            {
                if(check[j]==i)
                {
                    if(c == 0)
                    {
                        System.out.print((j+1));
                        c = 1;
                    }else
                    {
                        System.out.print(", " + (j+1));
                    }
                }
            }
            System.out.println();
        }

        // 종료 시간 표시
        int max = 0;
        for(int j : machine)
        {
            if(max < j)
            {
                max = j;
            }
        }
        System.out.println("종료시간 : " + max);
        return max;
    }

    // 주어진 순서대로 가장 빨리 가능한 곳에 넣는 분배 방식
    public static int listScheduling(int n, int m, int[] time) {
        int min = 0;
        int minidx = 0;
        int [] machine = new int[m];   // 전체 작업 시간 측정용
        int [] check = new int[n];     // 몇 번 작업이 몇번 머신에 들어갔는지 확인용

        for(int i = 0; i < n; i++)
        {
            // 가장 빠르게 끝나는 머신이 몇 번인지 확인
            for(int j = 0; j < m; j++)
            {
                if(j==0)
                {
                    min = machine[j];
                    minidx = j;
                }else if(min > machine[j])
                {
                    min = machine[j];
                    minidx = j;
                }
            }
            machine[minidx] += time[i];
            check[i] = minidx;
        }

        // 머신에 할당된 Task 출력
        for(int i = 0; i < m; i++)
        {
            int c = 0;
            System.out.print("머신" + (i + 1) + ":");
            for(int j = 0; j < n; j++) 
            {
                if(check[j]==i)
                {
                    if(c == 0)
                    {
                        System.out.print((j+1));
                        c = 1;
                    }else
                    {
                        System.out.print(", " + (j+1));
                    }
                }
            }
            System.out.println();
        }

        // 종료 시간 표시
        int max = 0;
        for(int j : machine)
        {
            if(max < j)
            {
                max = j;
            }
        }
        System.out.println("종료시간 : " + max);
        return max;
    }

    // 두 방식의 오차율 구하기
    public static float approximationRatio(int m, int[] time, int result) {
        int total = 0;
        int longest = 0;

        for (int t : time) {
            total += t;
            if (t > longest) longest = t;
        }

        // 평균 작업 시간
        double avg = (double) total / m;
        // upper bound는 평균과 최장 작업시간 중 큰 값을 반올림한 값
        int upperBound = (int) Math.ceil(Math.max(avg, longest));

        return result / (float) upperBound;
    }

    public static void main(String[] args) throws Exception {
        int n = 6;   // 작업 수
        int m = 3;   // 머신 수
        int [] time = {2, 5, 1, 7, 3, 4};   // 각 작업에 걸리는 시간
        int lptMax = 0;
        int listSchedulingMax = 0;

        lptMax = lpt(n, m, time);
        listSchedulingMax = listScheduling(n, m, time);

        System.out.println("LPT 방식의 오차율 : " + approximationRatio(m, time, lptMax));
        System.out.println("List Scheduling 방식의 오차율 : " + approximationRatio(m, time, listSchedulingMax));
    }
}

/*
1. 다음과 같은 작업과 머신이 주어졌을 때, *각 작업을 분배*하여 최종 종료시간을 출력하시오.
- 작업수 n = 6 (작업시간: 2, 5, 1, 7, 3, 4)
- 머신 수 m = 3

출력예시
머신1: 7
머신2: 5,1
머신3: 4,3,2
종료시간, 9

2. 각 작업을 분해할 때, 아래 두가지 방식을 사용하시오.
a) LPT(Longest Processing Time first)
b) List Scheduling (순서대로)

3. 두 방식 모두의 오차율(approximation ratio)을 구하시오.
 */