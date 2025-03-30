import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

// 날짜와 온도를 저장하는 클래스
class WeatherData {
    String date;
    int temperature;

    // 생성자: 날짜와 온도를 초기화
    public WeatherData(String date, int temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    // 객체를 문자열로 출력할 때의 형식 지정
    @Override
    public String toString() {
        return date + " " + temperature;
    }
}

class QuickSort3Way {
    static int pivotCount = 0;

    public static void quickSort(List<WeatherData> list, int low, int high) {
        if (low >= high) return;

        pivotCount++;

        int lt = low, i = low + 1, gt = high;
        int pivot = list.get(low).temperature;

        while (i <= gt) {
            int cmp = Integer.compare(list.get(i).temperature, pivot);

            if (cmp < 0) {
                Collections.swap(list, lt++, i++);
            } else if (cmp > 0) {
                Collections.swap(list, i, gt--);
            } else {
                i++;
            }
        }

        quickSort(list, low, lt - 1);
        quickSort(list, gt + 1, high);
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        String inputFile = "daegu_weather_2024.txt";
        String outputFile = "threeWayList.txt";
        
        // 날씨 정보 저장용 리스트
        List<WeatherData> weatherList = new ArrayList<>();

        // 파일 읽기
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");          // 공백 기준 분리
                String date = parts[0];                          // 앞이 날짜
                int temp = Integer.parseInt(parts[1]);           // 뒤가 온도
                weatherList.add(new WeatherData(date, temp));    // 객체를 리스트에 추가
            }
        } catch (IOException e) {
            // 읽기 오류 OR 파일이 없는 경우 에러 처리.
            e.printStackTrace();
        }
        
        // 3-way partitioning 정렬
        List<WeatherData> threeWayList = new ArrayList<>(weatherList);
        QuickSort3Way.pivotCount = 0;
        QuickSort3Way.quickSort(threeWayList, 0, threeWayList.size() - 1);
        System.out.println("3-way partitioning pivot 선택 횟수: " + QuickSort3Way.pivotCount);

        // 정렬된 파일로 출력
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (WeatherData data : threeWayList) {
                writer.write(data.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            // 오류 예외처리
            e.printStackTrace();
        }
    }
}
