import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// 간선 정보를 담는 클래스 (무방향, 무가중치)
class Node {
    private int v1;
    private int v2;

    public Node(int v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }
}

public class App {
    static Random rand = new Random();                // 랜덤 객체
    static int vLength = 10;                          // 정점 개수
    static Node[] node = new Node[201];               // 간선 배열 (1번부터 사용)
    static int[][] array = new int[21][21];           // 인접 행렬 (1~20번 정점)

    // 인접 행렬 출력 함수
    public static void printAdjacencyMatrix() {
        System.out.println("\n[Adjacency Matrix]");
        System.out.print("    ");
        for (int i = 1; i <= vLength; i++) {
            System.out.printf("%2d ", i);
        }
        System.out.println();
        for (int i = 1; i <= vLength; i++) {
            System.out.printf("%2d |", i);
            for (int j = 1; j <= vLength; j++) {
                System.out.printf("%2d ", array[i][j]);
            }
            System.out.println();
        }
    }

    // 무가중치 무방향 그래프 생성 함수
    public static void generateUnweightedGraph() {
        vLength = rand.nextInt(11) + 10;             // 정점 수: 10 ~ 20
        int targetEdgeCount = rand.nextInt(11) + 20; // 간선 수: 20 ~ 30

        array = new int[21][21];                     // 인접 행렬 초기화
        node = new Node[201];                        // 간선 배열 초기화

        int edgeCount = 0;
        while (edgeCount < targetEdgeCount) {
            int v1 = rand.nextInt(vLength) + 1;
            int v2 = rand.nextInt(vLength) + 1;

            // 자기 자신 or 중복 간선 방지
            if (v1 == v2 || array[v1][v2] == 1) continue;

            edgeCount++;
            node[edgeCount] = new Node(v1, v2);
            array[v1][v2] = 1;
            array[v2][v1] = 1;

            System.out.printf("%2d. Edge: (%d, %d)\n", edgeCount, v1, v2);
        }

        System.out.println("\n그래프 생성 완료: 정점 수 = " + vLength + ", 간선 수 = " + targetEdgeCount);
    }

    public static int vertexPrioritySelection(int[][] graph, int vCount) {
        boolean[] selected = new boolean[vCount + 1]; // 선택된 정점 여부
        int[][] tempGraph = new int[vCount + 1][vCount + 1]; // 그래프 복사
        for (int i = 1; i <= vCount; i++) {
            for (int j = 1; j <= vCount; j++) {
                tempGraph[i][j] = graph[i][j];
            }
        }

        int result = 0;

        while (true) {
            int maxDegree = -1;
            int maxVertex = -1;

            // 1. 가장 간선 많은 정점 찾기
            for (int i = 1; i <= vCount; i++) {
                if (selected[i]) continue;

                int degree = 0;
                for (int j = 1; j <= vCount; j++) {
                    if (tempGraph[i][j] == 1) degree++;
                }

                if (degree > maxDegree) {
                    maxDegree = degree;
                    maxVertex = i;
                }
            }

            // 더 이상 간선이 없으면 종료
            if (maxDegree == 0 || maxVertex == -1) break;

            // 2. 해당 정점 선택
            selected[maxVertex] = true;
            result++;

            // 3. 연결된 간선 제거
            for (int j = 1; j <= vCount; j++) {
                tempGraph[maxVertex][j] = 0;
                tempGraph[j][maxVertex] = 0;
            }
        }

        return result;
    }

    public static int maximalMatching(int[][] graph, int vCount) {
        boolean[] matched = new boolean[vCount + 1]; // 정점 매칭 여부
        List<int[]> edges = new ArrayList<>();

        // 1. 모든 간선 수집 (무방향 -> 중복 제거: i < j 조건)
        for (int i = 1; i <= vCount; i++) {
            for (int j = i + 1; j <= vCount; j++) {
                if (graph[i][j] == 1) {
                    edges.add(new int[]{i, j});
                }
            }
        }

        // 2. 간선을 순회하면서 disjoint matching 선택
        int matchingCount = 0;
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];

            // 두 정점 모두 아직 매칭되지 않았다면 선택
            if (!matched[u] && !matched[v]) {
                matched[u] = true;
                matched[v] = true;
                matchingCount++;
            }
        }

        return matchingCount;
    }

    public static void runExperiment(int repeatCount) {
        int totalVPS = 0; // Vertex Priority Selection 선택 정점 합계
        int totalMM = 0;  // Maximal Matching 선택 정점 합계

        for (int i = 1; i <= repeatCount; i++) {
            System.out.println("\n========================");
            System.out.println("실험 " + i + "회차");

            generateUnweightedGraph();
            int[][] copiedGraph = new int[21][21];
            for (int x = 1; x <= vLength; x++) {
                for (int y = 1; y <= vLength; y++) {
                    copiedGraph[x][y] = array[x][y];
                }
            }

            int vpsResult = vertexPrioritySelection(copiedGraph, vLength);

            // Matching은 array 그대로 사용 가능 (복사 필요 없음)
            int mmEdgeCount = maximalMatching(array, vLength);
            int mmResult = mmEdgeCount * 2;

            totalVPS += vpsResult;
            totalMM += mmResult;

            System.out.println("Vertex Priority 선택된 정점 수 : " + vpsResult);
            System.out.println("Maximal Matching 커버된 정점 수 : " + mmResult);
        }

        System.out.println("\n========================");
        System.out.println("평균 결과");
        System.out.printf("Vertex Priority 평균 정점 수 : %.2f\n", totalVPS / (double) repeatCount);
        System.out.printf("Maximal Matching 평균 정점 수 : %.2f\n", totalMM / (double) repeatCount);

        if (totalVPS < totalMM) {
            System.out.println("Vertex Priority 방식이 평균적으로 더 우수합니다.");
        } else if (totalVPS > totalMM) {
            System.out.println("Maximal Matching 방식이 평균적으로 더 우수합니다.");
        } else {
            System.out.println("두 방식의 평균 성능이 동일합니다.");
        }
    }

    public static void main(String[] args) {
        runExperiment(10);  // 10회 반복 실험 실행
    }
}
