package database.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<String[]> readCSV(String fileName) {

        List<String[]> records = new ArrayList<>();

        try {

            InputStream inputStream =
                    CSVReader.class.getClassLoader()
                            .getResourceAsStream("csv/" + fileName);

            if (inputStream == null) {

                throw new RuntimeException("Cannot find " + fileName);

            }

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream));

            String line;

            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {

                if (firstLine) {

                    firstLine = false;
                    continue;

                }

                records.add(line.split(","));

            }

            reader.close();

        }

        catch (IOException e) {

            e.printStackTrace();

        }

        return records;

    }

}