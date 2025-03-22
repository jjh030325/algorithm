import java.io.*;
import java.util.*;

// 날씨 데이터 클래스
class WeatherData {
    String date;
    int temperature;

    // 생성자
    public WeatherData(String date, int temperature) {
        this.date = date;
        this.temperature = temperature;
    }

    // 스트링 값으로 출력하는 경우
    @Override
    public String toString() {
        return date + " " + temperature;
    }
}

// 합병 정렬 클래스
class MergeSort {
    public static void sort(List<WeatherData> dataList) {
        // 사이즈가 1 이하인 경우는 정렬이 필요 없음.
        if (dataList.size() <= 1) return;

        // 절반으로 리스트 나누기
        int mid = dataList.size() / 2;
        List<WeatherData> left = new ArrayList<>(dataList.subList(0, mid));
        List<WeatherData> right = new ArrayList<>(dataList.subList(mid, dataList.size()));

        // 절반으로 나누어진 리스트 정렬시키기 -> 사이즈 1까지 계속 나누기.
        sort(left);
        sort(right);

        // 병합
        merge(dataList, left, right);
    }

    private static void merge(List<WeatherData> merged, List<WeatherData> left, List<WeatherData> right) {
        int i = 0, j = 0, k = 0;
        // 온도 순으로 정렬
        while (i < left.size() && j < right.size()) {
            if (left.get(i).temperature >= right.get(j).temperature) {
                merged.set(k++, left.get(i++));
            } else {
                merged.set(k++, right.get(j++));
            }
        }
        // 한쪽이 다 정렬된 경우 남은 요소 이어붙이기.
        while (i < left.size()) {
            merged.set(k++, left.get(i++));
        }
        while (j < right.size()) {
            merged.set(k++, right.get(j++));
        }
    }
}

public class App {
    public static void main(String[] args) throws Exception {
        // 입출력 파일 경로 설정정
        String inputFile = "daegu_weather_2024.txt";
        String outputFile = "sorted_weather.txt";

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

        // 합병 정렬
        MergeSort.sort(weatherList);

        // 정렬된 파일로 출력
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (WeatherData data : weatherList) {
                writer.write(data.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            // 오류 예외처리
            e.printStackTrace();
        }

        // 가장 더웠던 날 3개 출력 : 내림차순 정렬을 했으니 맨 위에서부터 3개 출력
        System.out.println("2024년도 가장 더웠던 날 Top 3:");
        for (int i = 0; i < 3; i++) {
            System.out.println(weatherList.get(i));
        }
    }
}
