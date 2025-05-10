import java.util.*;

// 그래프 생성기 클래스: 랜덤 연결 단방향 가중치 그래프 생성
class GraphGenerator {
    static final int INF = Integer.MAX_VALUE;
    static Random rand = new Random();

    public static int[][] generateRandomConnectedGraph(int V, int maxWeight) {
        int[][] graph = new int[V][V];

        for (int i = 0; i < V; i++) {
            Arrays.fill(graph[i], INF);
            graph[i][i] = 0;
        }

        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < V; i++) vertices.add(i);
        Collections.shuffle(vertices);

        for (int i = 0; i < V - 1; i++) {
            int from = vertices.get(i);
            int to = vertices.get(i + 1);
            int weight = rand.nextInt(maxWeight) + 1;
            graph[from][to] = weight;
        }

        int extraEdges = rand.nextInt(V) + V;
        for (int i = 0; i < extraEdges; i++) {
            int from = rand.nextInt(V);
            int to = rand.nextInt(V);
            if (from != to && graph[from][to] == INF) {
                int weight = rand.nextInt(maxWeight) + 1;
                graph[from][to] = weight;
            }
        }
        return graph;
    }
}

// 다익스트라 알고리즘 실험 클래스
class DijkstraExperiment {
    static final int INF = Integer.MAX_VALUE;

    // 다익스트라 알고리즘 실행 후 거리 배열 반환
    public static int[] dijkstraWithResult(int[][] graph, int start, int V) {
        int[] dist = new int[V];
        boolean[] visited = new boolean[V];
        Arrays.fill(dist, INF);
        dist[start] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{start, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            if (visited[u]) continue;
            visited[u] = true;

            for (int v = 0; v < V; v++) {
                if (graph[u][v] != INF && !visited[v]) {
                    if (dist[v] > dist[u] + graph[u][v]) {
                        dist[v] = dist[u] + graph[u][v];
                        pq.offer(new int[]{v, dist[v]});
                    }
                }
            }
        }
        return dist;
    }

    // 거리 배열 출력 함수
    public static void printDijkstraDistances(int[] dist) {
        for (int d : dist) {
            if (d == INF) System.out.print(" INF");
            else System.out.printf("%4d", d);
        }
        System.out.println();
    }

    // 다익스트라 시간 측정 루프
    public static void runDijkstraTiming() {
        System.out.println("=== Dijkstra 실행 시간 측정 ===");

        for (int V = 10; V <= 20; V++) {
            int[][] graph = GraphGenerator.generateRandomConnectedGraph(V, 20);

            System.out.println("Dijkstra 거리 출력 (V = " + V + "):");
            for (int i = 0; i < V; i++) {
                int[] dist = dijkstraWithResult(graph, i, V);
                //printDijkstraDistances(dist);
            }

            long start = System.nanoTime();
            for (int i = 0; i < V; i++) {
                dijkstraWithResult(graph, i, V);
            }
            long end = System.nanoTime();
            double timeAllSource = (end - start) / 1_000_000.0;

            System.out.printf("V = %2d | 전체 정점 다익스트라: %.3f ms\n", V, timeAllSource);
        }
    }
}

// 플로이드-워셜 알고리즘 실험 클래스
class FloydExperiment {
    static final int INF = Integer.MAX_VALUE;

    // 플로이드-워셜 알고리즘 실행 후 거리 행렬 반환
    public static int[][] floydWarshallWithResult(int[][] graph, int V) {
        int[][] dist = new int[V][V];
        for (int i = 0; i < V; i++)
            for (int j = 0; j < V; j++)
                dist[i][j] = graph[i][j];

        for (int k = 0; k < V; k++)
            for (int i = 0; i < V; i++)
                for (int j = 0; j < V; j++)
                    if (dist[i][k] != INF && dist[k][j] != INF)
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);

        return dist;
    }

    // 거리 행렬 출력 함수
    public static void printMatrix(int[][] dist) {
        for (int[] row : dist) {
            for (int d : row) {
                if (d == INF) System.out.print(" INF");
                else System.out.printf("%4d", d);
            }
            System.out.println();
        }
    }

    // 플로이드-워셜 시간 측정 루프
    public static void runFloydTiming() {
        System.out.println("=== Floyd-Warshall 실행 시간 측정 ===");

        for (int V = 10; V <= 20; V++) {
            int[][] graph = GraphGenerator.generateRandomConnectedGraph(V, 20);

            int[][] result = floydWarshallWithResult(graph, V);
            System.out.println("Floyd-Warshall 거리 행렬 (V = " + V + "):");
            //printMatrix(result);

            long start = System.nanoTime();
            floydWarshallWithResult(graph, V);
            long end = System.nanoTime();
            double timeMs = (end - start) / 1_000_000.0;

            System.out.printf("V = %2d | Floyd-Warshall: %.3f ms\n", V, timeMs);
        }
    }
}

// 메인 클래스
public class App {
    public static void main(String[] args) {
        DijkstraExperiment.runDijkstraTiming();
        System.out.println();
        FloydExperiment.runFloydTiming();
    }
}
