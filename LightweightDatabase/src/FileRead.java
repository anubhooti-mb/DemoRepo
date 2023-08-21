import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileRead {
    public static String createTableToText(Map<String, String[]> createTableObj, String separator) {
        String text = "";
        for (String s : createTableObj.get("columnNames")) {
            text += s + separator;
        }
        // remove extra separator in end
        text = text.substring(0, text.length() - separator.length());
        text += "\n";
        for (int i = 0; i < createTableObj.get("columnTypes").length; i++) {
            text += createTableObj.get("columnTypes")[i];
            if (createTableObj.get("columnSizes")[i] != null) {
                text += "(" + createTableObj.get("columnSizes")[i] + ")";
            }
            text += separator;
        }
        // remove extra separator in end
        text = text.substring(0, text.length() - separator.length());

        return text;
    }

    public static ArrayList<LinkedHashMap<String, String>> selectTable(String tableName, String separator) {
        ArrayList<LinkedHashMap<String, String>> rows = new ArrayList<>();
        String[] header = new String[1];
        String[] currentRow = new String[1];
        String line = "";
        // Read all rows from the file/table
        try (BufferedReader br = new BufferedReader(new FileReader(tableName + ".txt"))) {

            for (int counter = 0; (line = br.readLine()) != null; counter++) {
                if (counter == 1) {
                    continue;
                }
                if (counter == 0) {
                    header = line.split(separator);
                    continue;
                }
                LinkedHashMap<String, String> row = new LinkedHashMap<>();
                currentRow = line.split(separator);
                for (int i = 0; i < header.length; i++) {
                    row.put(header[i], currentRow[i]);
                }
                rows.add(row);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return rows;

    }

    public static String insertTableToText(Map<String, String[]> insertParsedQuery, String separator) throws Exception {
        var text = "";
        var tableName = insertParsedQuery.get("tableName")[0];
        var columnNames = insertParsedQuery.get("columnNames");
        var columnValues = insertParsedQuery.get("columnValues");
        LinkedHashMap<String, String> colValMap = new LinkedHashMap<>();
        String[] columnsFromFile = new String[0];
        // pick columns from file
        try (BufferedReader br = new BufferedReader(new FileReader(tableName + ".txt"))) {
            columnsFromFile = br.readLine().split(separator);
        } catch (Exception e) {
            // TODO: handle exception
        }
        // for each column in file add value to respective column
        for (int i = 0; i < columnsFromFile.length; i++) {
            var indexOfColumn = Arrays.asList(columnNames).indexOf(columnsFromFile[i]);
            if (indexOfColumn >= 0) {
                colValMap.put(columnsFromFile[i], columnValues[indexOfColumn]);
            } else {
                colValMap.put(columnsFromFile[i], null);
            }
        }

        for (String s : colValMap.values()) {
            text += s + separator;
        }
        // remove extra separator in end
        text = text.substring(0, text.length() - separator.length());
        return text;
    }

    public static String getFirstTwoLinesFromFile(String tableName) {
        try (BufferedReader br = new BufferedReader(new FileReader(tableName + ".txt"))) {
            String line = "";
            String text = "";
            int lineNumber = 0;
            while ((line = br.readLine()) != null && lineNumber < 2) {
                // System.out.println(line);
                text += line + "\n";
                lineNumber++;
            }
            return text;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return "";
    }

    public static String convertRowsToText(ArrayList<LinkedHashMap<String, String>> rows, String separator) {
        String text = "";
        // get header from keys of first row
        String[] header = rows.get(0).keySet().toArray(new String[0]);

        for (int i = 0; i < rows.size(); i++) {
            LinkedHashMap<String, String> map = rows.get(i);
            ArrayList<String> values = new ArrayList<>();
            for (String key : header) {
                values.add(map.get(key));
            }
            text += String.join(separator, values);
            text += "\n";
        }
        return text;
    }

    public static void writeTextFile(String fileName, String text) {
        try {
            FileWriter writer = new FileWriter(fileName + ".txt");
            writer.write(text + "\n");
            writer.close();
            System.out.println("Text saved to file.");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void appendTextFile(String fileName, String text) {
        try {
            // create a FileWriter object with append flag set to true
            FileWriter fw = new FileWriter(fileName + ".txt", true);
            BufferedWriter bw = new BufferedWriter(fw);

            // append the text to the file
            bw.write(text);
            bw.newLine();
            System.out.println("Values Inserted into Text file.");
            // close the writer
            bw.close();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static ArrayList<LinkedHashMap<String, String>> readTable(String fileName, String separator) {
        String line = "";
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(separator);
                if (columnNames.isEmpty()) {
                    columnNames = new ArrayList<>(Arrays.asList(data));
                } else {
                    LinkedHashMap<String, String> map1 = new LinkedHashMap<>();
                    for (int i = 0; i < columnNames.size(); i++) {
                        map1.put(columnNames.get(i), data[i]);
                    }
                    list.add(map1);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        return list;
    }
}
