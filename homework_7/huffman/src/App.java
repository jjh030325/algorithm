import java.io.*;
import java.util.*;
import java.nio.file.Files;

// Huffman Tree의 노드를 정의하는 클래스
class HuffmanNode implements Comparable<HuffmanNode> {
    char ch;
    int freq;
    HuffmanNode left, right;

    HuffmanNode(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }

    public int compareTo(HuffmanNode other) {
        return this.freq - other.freq;
    }
}

public class App {
    private static Map<Character, String> huffmanCodeMap = new HashMap<>(); // 문자 -> 허프만 코드
    private static Map<String, Character> reverseHuffmanCodeMap = new HashMap<>(); // 허프만 코드 -> 문자

    public static void main(String[] args) throws IOException {
        String inputFile = "sample.txt";      // 입력 파일
        String encodedFile = "sample.enc"; // 인코딩된 파일
        String decodedFile = "sample.dec"; // 디코딩된 파일

        String text = readFile(inputFile); // 입력 파일 읽기

        // 1. sample.txt 파일에서 사용된 문자와 각 문자의 출현빈도(frequency)를 출력하시오. (예, a: 30, b: 15, ...)
        Map<Character, Integer> freqMap = buildFrequencyMap(text);
        System.out.println("출현빈도:");
        freqMap.forEach((k, v) -> System.out.println(k + ": " + v));

        // 2. 1에서 추출한 값을 사용하여 huffman code를 완성하시오.
        HuffmanNode root = buildHuffmanTree(freqMap);
        buildCode(root, "");

        // 3. 완성된 huffman code를 사용하여 sample.txt 파일을 encode 하시오. (sample.enc)
        String encodedText = encode(text);
        writeBinaryFile(encodedFile, encodedText);

        // 4. 3번에서 enc된 파일을 원래 sample 파일로 decode 하시오. (sample.dec)
        String decodedText = decode(encodedFile);
        writeFile(decodedFile, decodedText);

        // 5. 디코드된 sample.dec 파일과 원본인 sample.txt 파일이 일치하는지 확인하시오.
        boolean isSame = text.equals(decodedText);
        System.out.println("원본과 압축된 파일 일치 : " + isSame);

        // 6. 압축전과 후의 비트수를 계산하여 압축률(%)을 계산하시오.
        int originalBits = text.length() * 8;
        int compressedBits = encodedText.length();
        double compressionRate = 100.0 * (originalBits - compressedBits) / originalBits;
        System.out.printf("기존 비트 : %d, 압축한 비트 : %d\n", originalBits, compressedBits);
        System.out.printf("압축률 : %.2f%%\n", compressionRate);
    }

    // 파일을 읽어 String으로 반환
    private static String readFile(String filename) throws IOException {
        return new String(Files.readAllBytes(new File(filename).toPath()));
    }

    // String 내용을 파일로 저장
    private static void writeFile(String filename, String content) throws IOException {
        Files.write(new File(filename).toPath(), content.getBytes());
    }

    // 이진 문자열(binaryString)을 파일로 저장
    private static void writeBinaryFile(String filename, String binaryString) throws IOException {
        BitSet bitSet = new BitSet(binaryString.length());
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1') {
                bitSet.set(i);
            }
        }
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(bitSet.toByteArray());
        }
    }

    // 파일로부터 이진 문자열(binaryString) 읽기
    private static String readBinaryFile(String filename) throws IOException {
        byte[] bytes = Files.readAllBytes(new File(filename).toPath());
        BitSet bitSet = BitSet.valueOf(bytes);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bitSet.length(); i++) {
            sb.append(bitSet.get(i) ? '1' : '0');
        }
        return sb.toString();
    }

    // 문자열에서 문자별 빈도수를 계산
    private static Map<Character, Integer> buildFrequencyMap(String text) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }
        return freqMap;
    }

    // 문자 빈도수로 Huffman Tree를 생성
    private static HuffmanNode buildHuffmanTree(Map<Character, Integer> freqMap) {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();

        for (var entry : freqMap.entrySet()) {
            pq.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();
            HuffmanNode merged = new HuffmanNode('\0', left.freq + right.freq);
            merged.left = left;
            merged.right = right;
            pq.offer(merged);
        }

        return pq.poll();
    }

    // Huffman Tree를 순회하며 코드 생성
    private static void buildCode(HuffmanNode node, String code) {
        if (node == null) return;

        if (node.left == null && node.right == null) { // 리프 노드
            huffmanCodeMap.put(node.ch, code);
            reverseHuffmanCodeMap.put(code, node.ch);
        }
        buildCode(node.left, code + '0');
        buildCode(node.right, code + '1');
    }

    // 문자열을 Huffman 인코딩
    private static String encode(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            sb.append(huffmanCodeMap.get(c));
        }
        return sb.toString();
    }

    // Huffman 인코딩된 파일을 디코딩
    private static String decode(String encodedFile) throws IOException {
        String binaryString = readBinaryFile(encodedFile);
        StringBuilder sb = new StringBuilder();

        String temp = "";
        for (char bit : binaryString.toCharArray()) {
            temp += bit;
            if (reverseHuffmanCodeMap.containsKey(temp)) {
                sb.append(reverseHuffmanCodeMap.get(temp));
                temp = "";
            }
        }
        return sb.toString();
    }
}
