import java.util.Random;

// 클래스를 통해 정점과 가중치 체크
class Node {
    private int v1;
    private int v2;
    private int e;

    public Node(int v1, int v2, int e) {
        this.v1 = v1;
        this.v2 = v2;
        this.e = e;
    }

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public int getE() {
        return e;
    }
}

public class App {
    static Random rand = new Random();
    static int vLength = 10;                     // 정점 개수
    static int[] check = new int[vLength + 1];   // 0번 안씀
    static int edgeCount = 0;                    // 간선 개수
    static Node[] node = new Node[201];          // node[1] ~ node[200] 사용
    static int[][] array = new int[11][11];      // 그래프 표현용 배열
    static int[][] ansArray = new int[11][11];   // MST 표현용 배열

    // 사이클 체크 함수
    public static int cycleCheck(int[] check, int n) {
        if (check[n] == n) {
            return n;
        } else {
            return cycleCheck(check, check[n]);
        }
    }

    // 초기화
    public static void setCheck(int[] check) {
        for (int i = 1; i < check.length; i++) {
            check[i] = i;
        }
    }

    // kruskal 알고리즘 처음 값은 무조건 포함하도록 함.
    public static int kruskal(int e, Node[] node, int[] check) {
        int ans = 0;
        int rootA = cycleCheck(check, node[1].getV1());
        int rootB = cycleCheck(check, node[1].getV2());
        if (rootA != rootB) {
            ans += node[1].getE();
            check[rootB] = rootA;
            ansArray[node[1].getV1()][node[1].getV2()] = node[1].getE();
            ansArray[node[1].getV2()][node[1].getV1()] = node[1].getE();
        }

        for (int i = 2; i <= e; i++) {
            rootA = cycleCheck(check, node[i].getV1());
            rootB = cycleCheck(check, node[i].getV2());
            if (rootA != rootB) {
                ans += node[i].getE();
                check[rootB] = rootA;
                ansArray[node[i].getV1()][node[i].getV2()] = node[i].getE();
                ansArray[node[i].getV2()][node[i].getV1()] = node[i].getE();
            } else {
                System.out.println("edge (" + node[i].getV1() + "," + node[i].getV2() + ") is excluded because it creates a cycle.");
            }
        }
        return ans;
    }

    // 연결 그래프 생성
    public static void randMatrixGeneration() {
        setCheck(check);
        edgeCount = 1;

        int connectedEdges = 0;
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

            edgeCount++;
            node[edgeCount] = new Node(v1, v2, weight);

            int rootA = cycleCheck(check, v1);
            int rootB = cycleCheck(check, v2);
            if (rootA != rootB) {
                check[rootB] = rootA;
                connectedEdges++;
            }
            System.out.println(edgeCount + "." + "random edge : " + "(" + v1 + "," + v2 + "), " + "(" + v2 + "," + v1 + "), " + "weight: " + weight);
            array[v1][v2] = weight;
            array[v2][v1] = weight;
        }

        System.out.println("✔ 연결 그래프 생성 완료 (간선 수: " + edgeCount + ")");
    }

    // 연결 그래프 출력 함수 
    public static void printGraph()
    {
        for(int i=1;i<=10;i++)
        {
            for(int j=1;j<=10;j++)
            {
                System.out.print(String.format("%4s", array[i][j]));
            }
            System.out.println();
        }
    }

    // 연결 그래프 첫 노드 생성 함수
    public static void firstMatrixGeneration()
    {
        System.out.println("Random Matrix Generation!!");
        int v1;
        int v2;
        while(true)
        {
            v1 = rand.nextInt(vLength) + 1;
            v2 = rand.nextInt(vLength) + 1;
            if (v1 != v2) break;
        }
        int weight = rand.nextInt(20) + 1;

        node[1] = new Node(v1, v2, weight);

        System.out.println(1 + "." + "random edge : " + "(" + v1 + "," + v2 + "), " + "(" + v2 + "," + v1 + "), " + "weight: " + weight + " 무조건 포함!");
        array[v1][v2] = weight;
        array[v2][v1] = weight;
    }

    // 최종 MST 그래프 출력 함수 
    public static void printAnsGraph()
    {
        for(int i=1;i<=10;i++)
        {
            for(int j=1;j<=10;j++)
            {
                System.out.print(String.format("%4s", ansArray[i][j]));
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        firstMatrixGeneration();
        randMatrixGeneration();
        setCheck(check);

        printGraph();

        java.util.Arrays.sort(node, 1, edgeCount + 1, (a, b) -> Integer.compare(a.getE(), b.getE()));

        int result = kruskal(edgeCount, node, check);

        printAnsGraph();
        System.out.println("총 가중치 합: " + result);
    }
}


/*
1. vertex가 10개일 때, 
연결그래프(connected graph)가 생성될 때 까지
edge를 추가하시오. 
(그래프는 weighted undirected graph) 

2. Kruskal 알고리즘을 구현하고, 
1에서 생성된 랜덤 그래프의 MST를 출력하시오. 

3. 사이클이 발생하여 간선이 선택되지 않는 경우,
이를 출력하시오. 
(예, edge (3,7) is excluded because 
it creates a cycle.)

+a) MST를 구할 때, 첫번째 생성한 edge가 
반드시 포함된 MST가 되도록 알고리즘을 
수정하여 구현하시오.
 */