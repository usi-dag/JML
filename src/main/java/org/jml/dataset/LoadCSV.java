package org.jml.dataset;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class LoadCSV {

    public static void main(String[] args) {
        LoadCSV loader = new LoadCSV("weatherHistory.csv");
        Map<String, List<String>> records = loader.getRecords();



        long size = 100_000_000;



        double[] dxs = records.get("Temperature (C)").stream()
                .mapToDouble(Double::parseDouble)
                .toArray();

        double[] xxs = Collections.nCopies(500, dxs)
                .stream()
                .flatMapToDouble(Arrays::stream)
                .limit(size)
                .toArray();





        System.out.println(xxs.length);

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

    public Map<String, List<String>> getRecords() {
        return records;
    }

    public Map<Integer, String> getIndex() {
        return index;
    }
}
