import java.util.PriorityQueue;
import java.util.Random;

// Node 클래스: 두 정점(v1, v2)과 간선의 가중치(e)를 저장하는 구조체
class Node {
    private int v1;
    private int v2;
    private int e;

    public Node(int v1, int v2, int e) {
        this.v1 = v1;
        this.v2 = v2;
        this.e = e;
    }

    public int getV1() { return v1; }
    public int getV2() { return v2; }
    public int getE() { return e; }
}

public class App {
    static Random rand = new Random();
    static int vLength = 10;                       // 정점 개수
    static int[] check = new int[vLength + 1];     // 사이클 체크 용 배열
    static int edgeCount = 0;                      // 간선 개수
    static Node[] node = new Node[201];            // 간선 정보 저장 배열
    static int[][] array = new int[11][11];        // 그래프 표현 용 배열

    // 사이클 여부 확인 함수
    public static int cycleCheck(int[] check, int n) {
        if (check[n] == n) return n;
        else return cycleCheck(check, check[n]);
    }

    // check 배열 초기화
    public static void setCheck(int[] check) {
        for (int i = 1; i < check.length; i++) {
            check[i] = i;
        }
    }

    // 연결 그래프 생성 함수
    public static void randMatrixGeneration() {
        setCheck(check);
        edgeCount = 1;

        int connectedEdges = 0;

        // 최소 vLength-1개의 간선을 연결해야 연결 그래프가 됨
        while (connectedEdges < vLength - 1 && edgeCount < 100) {
            int v1 = rand.nextInt(vLength) + 1;
            int v2 = rand.nextInt(vLength) + 1;
            if (v1 == v2) continue;

            int weight = rand.nextInt(20) + 1;

            // 중복 간선 방지
            boolean exists = false;
            for (int i = 1; i <= edgeCount; i++) {
                if ((node[i].getV1() == v1 && node[i].getV2() == v2) ||
                    (node[i].getV1() == v2 && node[i].getV2() == v1)) {
                    exists = true;
                    break;
                }
            }
            if (exists) continue;

            // 간선 추가
            edgeCount++;
            node[edgeCount] = new Node(v1, v2, weight);

            // 사이클이 발생하지 않으면 간선 연결
            int rootA = cycleCheck(check, v1);
            int rootB = cycleCheck(check, v2);
            if (rootA != rootB) {
                check[rootB] = rootA;
                connectedEdges++;
            }

            // 인접 행렬에 간선 정보 저장 (양방향 그래프)
            array[v1][v2] = weight;
            array[v2][v1] = weight;

            // 간선 정보 출력
            System.out.println(edgeCount + ".random edge : (" + v1 + "," + v2 + "), (" + v2 + "," + v1 + "), weight: " + weight);
        }

        System.out.println("연결 그래프 생성 완료 (간선 수: " + edgeCount + ")");
    }

    // 그래프 출력 함수
    public static void printGraph() {
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                System.out.print(String.format("%4s", array[i][j]));
            }
            System.out.println();
        }
    }

    // 연결 그래프의 최초 간선 생성 (최소 연결 보장 및 저번 코드에서 추가)
    public static void firstMatrixGeneration() {
        System.out.println("Random Matrix Generation!!");

        int v1, v2;
        while (true) {
            v1 = rand.nextInt(vLength) + 1;
            v2 = rand.nextInt(vLength) + 1;
            if (v1 != v2) break;
        }

        int weight = rand.nextInt(20) + 1;
        node[1] = new Node(v1, v2, weight);
        array[v1][v2] = weight;
        array[v2][v1] = weight;

        System.out.println(1 + ".random edge : (" + v1 + "," + v2 + "), (" + v2 + "," + v1 + "), weight: " + weight + " 무조건 포함!");
    }

    // 다익스트라
    public static void dijkstra(int start) {
        int[] dist = new int[vLength + 1];     // 최단 거리 저장 배열
        int[] parent = new int[vLength + 1];   // 최단 경로 추적용 부모 배열
        boolean[] visited = new boolean[vLength + 1]; // 방문 여부

        for (int i = 1; i <= vLength; i++) {
            dist[i] = Integer.MAX_VALUE; // 최대 값으로 초기화
            parent[i] = -1;              // 부모 미지정
        }
        dist[start] = 0; // 시작 정점은 거리 0

        // 최소 힙 기반 우선순위 큐 (거리 기준 정렬)
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.offer(new int[] {start, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];

            if (visited[u]) continue;
            visited[u] = true;

            // u 정점에서 연결된 모든 정점 확인
            for (int v = 1; v <= vLength; v++) {
                if (array[u][v] != 0 && !visited[v]) {
                    if (dist[v] > dist[u] + array[u][v]) {
                        dist[v] = dist[u] + array[u][v]; // 거리 갱신
                        parent[v] = u;                   // 경로 추적
                        pq.offer(new int[] {v, dist[v]});
                    }
                }
            }
        }

        // 결과 출력
        System.out.println("\nShortest paths from vertex " + start + ":");
        for (int i = 1; i <= vLength; i++) {
            if (i == start) continue;

            System.out.print(start);     // 시작점 출력
            printPath(parent, i);        // 경로 출력
            if (dist[i] == Integer.MAX_VALUE) {
                System.out.println(" (Cost: 갈 수 없음)");
            } else {
                System.out.println(" (Cost: " + dist[i] + ")");
            }
        }
    }

    // 재귀적으로 경로 출력 (시작 정점 제외)
    public static void printPath(int[] parent, int vertex) {
        if (parent[vertex] == -1) return; // 루트 도달 시 종료
        printPath(parent, parent[vertex]);
        System.out.print(" -> " + vertex);
    }

    public static void main(String[] args) throws Exception {
        firstMatrixGeneration();    // 최초 간선 1개 생성
        randMatrixGeneration();     // 나머지 간선 생성 (연결 그래프)
        setCheck(check);            // check 배열 초기화
        printGraph();               // 인접 행렬 출력
        dijkstra(1);          // 1번 정점에서 다익스트라 수행
    }
}