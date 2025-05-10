import java.util.PriorityQueue;

public class App {
    static final int INF = Integer.MAX_VALUE;
    static int vLength = 10;  // 정점 개수
    static int[][] array = {
        {  0, 15, 12, INF, INF, INF, INF, INF, INF, INF }, // 서울
        { 15,  0, INF, INF, INF, 21, INF,  7, INF, INF }, // 원주
        { 12, INF,  0,  4, 10, INF, INF, INF, INF, INF }, // 천안
        { INF, INF, 4,  0,  3, INF, 13, INF, INF, INF }, // 논산
        { INF, INF, 10, 3,  0, INF, INF, 10, INF, INF }, // 대전
        { INF, 21, INF, INF, INF,  0, INF, INF, 25, INF }, // 강릉
        { INF, INF, INF, 13, INF, INF,  0, INF, INF, 15 }, // 광주
        { INF,  7, INF, INF, 10, INF, INF,  0, 19,  9 }, // 대구
        { INF, INF, INF, INF, INF, 25, INF, 19,  0,  5 }, // 포항
        { INF, INF, INF, INF, INF, INF, 15,  9,  5,  0 }  // 부산
    };

    // dijkstra 알고리즘 
    public static int[][] dijkstra() {
        int[][] result = new int[vLength][vLength];

        for (int start = 0; start < vLength; start++) {
            int[] dist = new int[vLength];
            boolean[] visited = new boolean[vLength];

            for (int i = 0; i < vLength; i++) dist[i] = INF;
            dist[start] = 0;

            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
            pq.offer(new int[]{start, 0});

            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int u = current[0];
                if (visited[u]) continue;
                visited[u] = true;

                for (int v = 0; v < vLength; v++) {
                    if (array[u][v] != INF && !visited[v]) {
                        if (dist[v] > dist[u] + array[u][v]) {
                            dist[v] = dist[u] + array[u][v];
                            pq.offer(new int[]{v, dist[v]});
                        }
                    }
                }
            }
            result[start] = dist;
        }

        return result;
    }

    // 배열 형태로 다익스트라 결과 표시 함수.
    public static void printDistanceMatrix(int[][] dist, String title) {
        System.out.println(title);
        for (int i = 0; i < vLength; i++) {
            for (int j = 0; j < vLength; j++) {
                if (dist[i][j] == INF)
                    System.out.print(" INF");
                else
                    System.out.printf("%4d", dist[i][j]);
            }
            System.out.println();
        }
    }

    // floyd Warshall 알고리즘 
    public static void floydWarshall(int[][] graph) {
        int[][] dist = new int[vLength][vLength];

        // 초기화
        for (int i = 0; i < vLength; i++) {
            for (int j = 0; j < vLength; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        // 핵심 로직
        for (int k = 0; k < vLength; k++) {
            for (int i = 0; i < vLength; i++) {
                for (int j = 0; j < vLength; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        if (dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }

        // 결과 출력
        System.out.println("Floyd-Warshall distance matrix:");
        for (int i = 0; i < vLength; i++) {
            for (int j = 0; j < vLength; j++) {
                if (dist[i][j] == INF)
                    System.out.print(" INF");
                else
                    System.out.printf("%4d", dist[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Dijkstra 측정
        long dStart = System.nanoTime();
        int[][] dijkstraResult = dijkstra();
        long dEnd = System.nanoTime();
        printDistanceMatrix(dijkstraResult, "Dijkstra distance matrix:");
        long dTime = dEnd - dStart;
        System.out.println("Dijkstra (모든 정점 시작) 수행 시간: " + (dTime / 1_000_000_000.0) + " 초");

        // Floyd-Warshall 측정
        long fStart = System.nanoTime();
        floydWarshall(array);
        long fEnd = System.nanoTime();
        long fTime = fEnd - fStart;
        System.out.println("Floyd-Warshall 수행 시간: " + (fTime / 1_000_000_000.0) + " 초");
    }
}
