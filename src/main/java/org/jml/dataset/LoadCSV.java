package org.jml.dataset;


import java.io.*;
import java.nio.Buffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class LoadCSV {

    public static void main(String[] args) throws IOException {
        LoadCSV loader = new LoadCSV("sample.csv");
        Map<String, List<String>> records = loader.getRecords();
        ArrayList<String> C = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20)
                .map(Object::toString)
                .collect(Collectors.toCollection(ArrayList::new));
        loader.addRecord("C", C);


    }

    private static final String COMMA_DELIMITER = ",";
    private final Map<String, List<String>> records = new HashMap<>();
    private final Map<Integer, String> index = new HashMap<>();

    public LoadCSV(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            String line;
            boolean isColumnName = true;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                for (int i = 0; i < values.length; i++) {
                    if (isColumnName) {
                        index.put(i, values[i]);
                        records.put(values[i], new ArrayList<>());
                    } else {
                        records.get(index.get(i)).add(values[i]);
                    }
                }

                isColumnName = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveCSV(String filename) throws IOException {

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (int i = 0; i < index.size(); i++) {

                bw.write(index.get(i));
                if (i != index.size() -1) bw.write(COMMA_DELIMITER);
            }
            bw.newLine();

            int size = records.get(index.get(0)).size();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < records.size(); j++) {
                    bw.write(records.get(index.get(j)).get(i));
                    if (j != records.size() - 1)  bw.write(COMMA_DELIMITER);
                }
                bw.newLine();
            }

        }
    }

    public void addRecord(String indexName, List<String> record) {
        index.put(index.size(), indexName);
        records.put(indexName, record);
    }


    public Map<String, List<String>> getRecords() {
        return records;
    }

    public Map<Integer, String> getIndex() {
        return index;
    }
}
