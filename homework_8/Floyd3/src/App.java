import java.util.*;

// 그래프 생성기 클래스
class GraphGenerator {
    static final int INF = Integer.MAX_VALUE;
    static Random rand = new Random();

    // 연결된 랜덤 단방향 가중치 그래프 생성
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

    // 인접 행렬 형식으로 그래프 출력
    public static void printGraph(int[][] graph) {
        System.out.println("\n[인접 행렬 형식 그래프 출력]");
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] == INF) System.out.print(" INF");
                else System.out.printf("%4d", graph[i][j]);
            }
            System.out.println();
        }
    }
}

// 다익스트라 실험 클래스
class DijkstraExperiment {
    static final int INF = Integer.MAX_VALUE;

    // 시작 정점에서 각 정점까지 거리 계산
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

    // 경로 추적용 parent 배열 포함 다익스트라
    public static int[] dijkstraWithPath(int[][] graph, int start, int[] parent, int V) {
        int[] dist = new int[V];
        boolean[] visited = new boolean[V];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
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
                        parent[v] = u;
                        pq.offer(new int[]{v, dist[v]});
                    }
                }
            }
        }
        return dist;
    }

    // 거리 배열 출력
    public static void printDijkstraDistances(int[] dist) {
        for (int d : dist) {
            if (d == INF) System.out.print(" INF");
            else System.out.printf("%4d", d);
        }
        System.out.println();
    }

    // 경로 출력 (재귀 방식)
    public static void printPath(int[] parent, int end) {
        if (parent[end] == -1) {
            System.out.print((end + 1));
            return;
        }
        printPath(parent, parent[end]);
        System.out.print(" -> " + (end + 1));
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

// 플로이드 워셜 실험 클래스
class FloydExperiment {
    static final int INF = Integer.MAX_VALUE;

    // Floyd-Warshall 수행 및 경로 추적 배열 반환
    public static int[][] floydWarshallWithPath(int[][] graph, int V, int[][] next) {
        int[][] dist = new int[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
                if (graph[i][j] != INF && i != j) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
        }

        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    if (dist[i][k] != INF && dist[k][j] != INF && dist[i][j] > dist[i][k] + dist[k][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        return dist;
    }

    // Floyd-Warshall 경로 출력
    public static void printFloydPath(int[][] next, int u, int v) {
        if (next[u][v] == -1) {
            System.out.println("경로 없음");
            return;
        }
        System.out.print((u + 1));
        while (u != v) {
            u = next[u][v];
            System.out.print(" -> " + (u + 1));
        }
    }

    // Floyd-Warshall 거리 행렬 출력
    public static void printMatrix(int[][] dist) {
        for (int[] row : dist) {
            for (int d : row) {
                if (d == INF) System.out.print(" INF");
                else System.out.printf("%4d", d);
            }
            System.out.println();
        }
    }

    // Floyd-Warshall 시간 측정 
    public static void runFloydTiming() {
        System.out.println("=== Floyd-Warshall 실행 시간 측정 ===");

        for (int V = 10; V <= 20; V++) {
            int[][] graph = GraphGenerator.generateRandomConnectedGraph(V, 20);
            int[][] next = new int[V][V];

            int[][] result = floydWarshallWithPath(graph, V, next);
            System.out.println("Floyd-Warshall 거리 행렬 (V = " + V + "):");
            //printMatrix(result);

            long start = System.nanoTime();
            floydWarshallWithPath(graph, V, next);
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

        // 경로 추적 기능 실행
        Scanner sc = new Scanner(System.in);
        int V = 10;
        int[][] graph = GraphGenerator.generateRandomConnectedGraph(V, 20);
        GraphGenerator.printGraph(graph);  // 그래프 출력

        System.out.print("경로를 확인할 시작점과 도착점 입력 (예: 1,5): ");
        String[] input = sc.nextLine().split(",");
        int start = Integer.parseInt(input[0].trim()) - 1;
        int end = Integer.parseInt(input[1].trim()) - 1;

        if (start < 0 || start >= V || end < 0 || end >= V) {
            System.out.println("정점 번호는 1부터 " + V + "까지 입력해야 합니다.");
            return;
        }

        int[] parent = new int[V];
        int[] distD = DijkstraExperiment.dijkstraWithPath(graph, start, parent, V);

        System.out.println("[Dijkstra 경로]");
        if (distD[end] == Integer.MAX_VALUE) {
            System.out.println("도착 불가능한 경로입니다.");
        } else {
            System.out.print("경로: ");
            DijkstraExperiment.printPath(parent, end);
            System.out.println(" (총 비용: " + distD[end] + ")");
        }

        int[][] next = new int[V][V];
        int[][] distF = FloydExperiment.floydWarshallWithPath(graph, V, next);

        System.out.println("[Floyd-Warshall 경로]");
        if (distF[start][end] == Integer.MAX_VALUE) {
            System.out.println("도착 불가능한 경로입니다.");
        } else {
            System.out.print("경로: ");
            FloydExperiment.printFloydPath(next, start, end);
            System.out.println(" (총 비용: " + distF[start][end] + ")");
        }
    }
}
