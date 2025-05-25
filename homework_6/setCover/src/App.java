import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// PowerSet 클래스로 묶어서 관리함
class PowerSet{
    private String name;
    private int[] array;
    private int cost;

    // 생성자
    public PowerSet(String name, int[] array, int cost) {
        this.name = name;
        this.array = array;
        this.cost = cost;
    }

    // 값 변경 용 함수
    public void SetPowerSet(String name, int[] array, int cost) {
        this.name = name;
        this.array = array;
        this.cost = cost;
    }

    // 출력 용 함수
    public void printArray() {
        System.out.print("{");
        for (int i=0;i<array.length;i++) {
            System.out.print(array[i]);
            if (i != array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("}");
    }

    // 변수 리턴 함수 
    public int[] getArray() { return array; }
    public int getCost() { return cost; }
    public int getLength() { return array.length; }
    public String getName() { return name; }
}

public class App {
    // PowerSet 입력 함수 
    public static PowerSet[] inputPowerSet(PowerSet[] F){
        int[] tmp1 = {1,2,3,8};
        F[1] = new PowerSet("S1", tmp1, 6);
        int[] tmp2 = {1,2,3,4,8};
        F[2] = new PowerSet("S2", tmp2, 10);
        int[] tmp3 = {1,2,3,4};
        F[3] = new PowerSet("S3", tmp3, 4);
        int[] tmp4 = {2,3,4,5,7,8};
        F[4] = new PowerSet("S4", tmp4, 12);
        int[] tmp5 = {4,5,6,7};
        F[5] = new PowerSet("S5", tmp5, 4);
        int[] tmp6 = {5,6,7,9,10};
        F[6] = new PowerSet("S6", tmp6, 8);
        int[] tmp7 = {4,5,6,7};
        F[7] = new PowerSet("S7", tmp7, 4);
        int[] tmp8 = {1,2,4,8};
        F[8] = new PowerSet("S8", tmp8, 4);
        int[] tmp9 = {6,9};
        F[9] = new PowerSet("S9", tmp9, 3);
        int[] tmp10 = {6,10};
        F[10] = new PowerSet("S10", tmp10, 4);
        return F;
    }

    // SetCovering 함수. 가장 적은 수의 집합으로 U가 되는 조합을 찾는 함수.
    public static void setCovering(PowerSet[] F) {
        boolean[] covered = new boolean[11]; // 인덱스 1~10 사용
        List<PowerSet> selected = new ArrayList<>();
        int coveredCount = 0;
    
        // 원소 수 기준으로 내림차순 정렬
        Arrays.sort(F, 1, 11, (a, b) -> Integer.compare(b.getLength(), a.getLength()));
    
        for (int i = 1; i <= 10; i++) {
            PowerSet ps = F[i];
            int[] elements = ps.getArray();
    
            // 이 집합이 커버 안 된 원소를 포함하는지 확인
            boolean isUseful = false;
            for (int j = 0; j < elements.length; j++) {
                int e = elements[j];
                if (!covered[e]) {
                    isUseful = true;
                    break;
                }
            }
    
            // 포함한다면 선택하고, 해당 원소들을 커버로 표시
            if (isUseful) {
                selected.add(ps);
                for (int j = 0; j < elements.length; j++) {
                    int e = elements[j];
                    if (!covered[e]) {
                        covered[e] = true;
                        coveredCount++;
                    }
                }
            }
    
            // 모든 원소 커버되었으면 종료
            if (coveredCount == 10) break;
        }
    
        // 결과 출력
        System.out.println("\n[선택된 집합들 (비용 고려 X)]");
        for (int i = 0; i < selected.size(); i++) {
            PowerSet ps = selected.get(i);
            System.out.print(ps.getName() + " = ");
            ps.printArray();
            System.out.println();
        }        
        System.out.println("총 선택된 집합 수: " + selected.size());
    }

    // 출력 함수 
    public static void printSetCovering(PowerSet[] F) {
        for(int i=1;i<=10;i++)
        {
            System.out.print(F[i].getName() + " = ");
            F[i].printArray();
            System.out.println(" => Cost " + F[i].getCost());
        }
    }
    public static void main(String[] args) throws Exception {
        PowerSet[] F = new PowerSet[11];
        F = inputPowerSet(F);
        printSetCovering(F);
        setCovering(F);
    }
}
