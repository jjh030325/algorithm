public class App {
    private static int[] sorted; // 저장용 배열
	
	// 합병 정렬 시작 함수
	public static void merge_sort(int[] a) {
		sorted = new int[a.length];
		merge_sort(a, 0, a.length - 1);
		sorted = null;
	}
	
	// 합병 정렬 중간 나누기 과정 함수
	private static void merge_sort(int[] a, int left, int right) {
		if(left == right) return; // 리프 노드인 경우
		
		int mid = (left + right) / 2;  // 중간지점 파악
		
		merge_sort(a, left, mid);      // 왼쪽으로 나누어 합치기
		merge_sort(a, mid + 1, right); // 오른쪽으로 나누어 합치기
		
		merge(a, left, mid, right);    // 정렬된 배열 합치며 정렬렬
	}
	
	// 합병 정렬 합치기 과정 함수
	private static void merge(int[] a, int left, int mid, int right) {
		int l = left;
		int r = mid + 1;
		int idx = left;
		
		// 합병되는 배열은 정렬이 진행된 상태, 정렬된 배열끼리 비교하면서 순서대로 이어붙임.
		while(l <= mid && r <= right) {
			if(a[l] <= a[r]) {
				sorted[idx] = a[l];
				idx++;
				l++;
			}
			else {
				sorted[idx] = a[r];
				idx++;
				r++;
			}
		}
		
		// 한 쪽 배열이 먼저 다 들어간 경우 다른 쪽 전체 붙이기.
		if(l > mid) {
			while(r <= right) {
				sorted[idx] = a[r];
				idx++;
				r++;
			}
		}
		else {
			while(l <= mid) {
				sorted[idx] = a[l];
				idx++;
				l++;
			}
		}
		
		// 정렬된 배열에 맞게 처음 입력받은 배열에 정리.
		for(int i = left; i <= right; i++) {
			a[i] = sorted[i];
		}
	}

    // 전체 배열 출력 함수
    private static void printAll(int[] a) {
        for(int i=0;i<a.length;i++)
        {
            System.out.println(a[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        int[] array = {37, 10, 22, 30, 35, 13, 25, 24};
        printAll(array);

        merge_sort(array);
        printAll(array);
    }
}