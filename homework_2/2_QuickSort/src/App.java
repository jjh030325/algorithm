import java.io.*;
import java.util.*;

// 날짜와 온도를 저장하는 클래스
class WeatherData {
    String date;
    int temperature;

    public WeatherData(String date, int temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return date + " " + temperature;
    }
}

class QuickSortRandom {
    static int pivotCount = 0;
    static Random rand = new Random();

    public static void quickSort(List<WeatherData> list, int left, int right) {
        if (left < right) {
            pivotCount++;
            int pivotIdx = left + rand.nextInt(right - left + 1);
            Collections.swap(list, left, pivotIdx);  // 피벗을 A[left]로 이동

            WeatherData pivot = list.get(left); // 피벗 값(온도)

            int i = left + 1;
            int j = right;

            while (i <= j) {
                // 내림차순 정렬 기준으로 변경, 중복 무시
                while (i <= j && list.get(i).temperature > pivot.temperature) i++;
                while (i <= j && list.get(j).temperature < pivot.temperature) j--;

                if (i <= j) {
                    Collections.swap(list, i, j);
                    i++;
                    j--;
                }
            }

            Collections.swap(list, left, j);  // 피벗을 제자리로

            quickSort(list, left, j - 1);
            quickSort(list, j + 1, right);
        }
    }
}

class QuickSortMedian {
    static int pivotCount = 0;

    public static void quickSort(List<WeatherData> list, int left, int right) {
        if (left < right) {
            // median-of-three 피봇 선택
            int mid = (left + right) / 2;
            int a = list.get(left).temperature;
            int b = list.get(mid).temperature;
            int c = list.get(right).temperature;

            int medianIdx;
            if ((a <= b && b <= c) || (c <= b && b <= a)) {
                medianIdx = mid;
            } else if ((b <= a && a <= c) || (c <= a && a <= b)) {
                medianIdx = left;
            } else {
                medianIdx = right;
            }

            Collections.swap(list, left, medianIdx);  // 피벗을 A[left]로 이동
            WeatherData pivot = list.get(left);       // 피벗 값 저장
            pivotCount++;

            int i = left + 1;
            int j = right;

            while (i <= j) {
                // 내림차순 정렬, 중복 무시
                while (i <= j && list.get(i).temperature > pivot.temperature) i++;
                while (i <= j && list.get(j).temperature < pivot.temperature) j--;

                if (i <= j) {
                    Collections.swap(list, i, j);
                    i++;
                    j--;
                }
            }

            Collections.swap(list, left, j);  // 피벗 제자리로

            quickSort(list, left, j - 1);
            quickSort(list, j + 1, right);
        }
    }
}


public class App {
    public static void main(String[] args) throws Exception {
        String inputFile = "daegu_weather_2024.txt";
        String outputFile1 = "randomList.txt";
        String outputFile2 = "medianList.txt";

        List<WeatherData> weatherList = new ArrayList<>();

        // 파일 읽기
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                String date = parts[0];
                int temp = Integer.parseInt(parts[1]);
                weatherList.add(new WeatherData(date, temp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 랜덤 피봇 정렬
        List<WeatherData> randomList = new ArrayList<>(weatherList);
        QuickSortRandom.pivotCount = 0;
        QuickSortRandom.quickSort(randomList, 0, randomList.size() - 1);
        System.out.println("랜덤 pivot 선택 횟수: " + QuickSortRandom.pivotCount);

        // Median-of-three 피봇 정렬
        List<WeatherData> medianList = new ArrayList<>(weatherList);
        QuickSortMedian.pivotCount = 0;
        QuickSortMedian.quickSort(medianList, 0, medianList.size() - 1);
        System.out.println("Median-of-three pivot 선택 횟수: " + QuickSortMedian.pivotCount);

        // 정렬된 결과 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile1))) {
            for (WeatherData data : randomList) {
                writer.write(data.toString());
                writer.newLine();
            }
        }

        // 정렬된 결과 저장
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile2))) {
            for (WeatherData data : medianList) {
                writer.write(data.toString());
                writer.newLine();
            }
        }
    }
}
