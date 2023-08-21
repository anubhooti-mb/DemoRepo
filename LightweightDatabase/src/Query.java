import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Query {

    public static Map<String, String[]> select(String str) {
        // Define regular expressions for each component of the SELECT query
        String selectRegex = "^SELECT (.+) FROM (.+)$";
        String whereRegex = "WHERE (.+)$";
        Map<String, String[]> map = new HashMap<>();

        // Compile the regular expressions
        Pattern selectPattern = Pattern.compile(selectRegex, Pattern.CASE_INSENSITIVE);
        Pattern wherePattern = Pattern.compile(whereRegex, Pattern.CASE_INSENSITIVE);

        // Extract the components of the SELECT query
        Matcher selectMatcher = selectPattern.matcher(str);
        if (selectMatcher.find()) {
            String columns = selectMatcher.group(1);
            String tableName = selectMatcher.group(2).split(" ")[0];
            map.putAll(Map.of(
                    "tableName", new String[] { tableName },
                    "columnNames", Arrays.stream(columns.split(",")).map(String::trim).toArray(String[]::new)));
            // System.out.println("Columns: " + columns);
            // System.out.println("Table name: " + tableName);
        }

        Matcher whereMatcher = wherePattern.matcher(str);
        if (whereMatcher.find()) {
            String whereClause = whereMatcher.group(1);

            map.put("where", new String[] { whereClause });
            // System.out.println("Where clause: " + whereClause);
        }

        return map;
    }

    public static Map<String, String[]> insert(String insertQuery) {
        // Define regular expressions for each component of the INSERT query
        String insertRegex = "^INSERT INTO (.+) \\((.+)\\) VALUES \\((.+)\\)$";
        Map<String, String[]> map = new HashMap<>();

        // Compile the regular expression
        Pattern insertPattern = Pattern.compile(insertRegex, Pattern.CASE_INSENSITIVE);

        // Extract the components of the INSERT query
        Matcher insertMatcher = insertPattern.matcher(insertQuery);
        if (insertMatcher.find()) {
            String tableName = insertMatcher.group(1);
            String columns = insertMatcher.group(2);
            String values = insertMatcher.group(3);
            String[] arrTableName = { tableName };
            map = Map.of(
                    "tableName", arrTableName,
                    "columnNames", Arrays.stream(columns.split(","))
                            .map(String::trim).toArray(String[]::new),
                    "columnValues", Arrays.stream(values.split(","))
                            .map(String::trim).toArray(String[]::new));

            // System.out.println("Table name: " + tableName);
            // System.out.println("Columns: " + columns);
            // System.out.println("Values: " + values);
        }
        return map;
    }

    public static Map<String, String[]> create(String createQuery) {
        // Define regular expressions for each component of the CREATE TABLE query
        String createTableRegex = "^CREATE TABLE (.+) \\((.+)\\)$";
        String columnRegex = "(\\w+)\\s+(\\w+)(?:\\((\\d+)\\))?";
        Map<String, String[]> map = new HashMap<>();
        // Compile the regular expressions
        Pattern createTablePattern = Pattern.compile(createTableRegex, Pattern.CASE_INSENSITIVE);
        Pattern columnPattern = Pattern.compile(columnRegex, Pattern.CASE_INSENSITIVE);

        // Extract the components of the CREATE TABLE query
        Matcher createTableMatcher = createTablePattern.matcher(createQuery);
        if (createTableMatcher.find()) {
            String tableName = createTableMatcher.group(1);
            String columns = createTableMatcher.group(2);
            map.put("tableName", new String[] { tableName });
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> columnTypes = new ArrayList<>();
            ArrayList<String> columnSizes = new ArrayList<>();
            // System.out.println("Table name: " + tableName);
            // System.out.println("Columns:");
            Matcher columnMatcher = columnPattern.matcher(columns);
            while (columnMatcher.find()) {
                String columnName = columnMatcher.group(1);
                String columnType = columnMatcher.group(2);
                String columnSize = columnMatcher.group(3);
                columnNames.add(columnName);
                columnTypes.add(columnType);
                columnSizes.add(columnSize);
                // System.out.println(columnName + " " + columnType + (columnSize != null ? "("
                // + columnSize + ")" : ""));
            }
            map.putAll(Map.of(
                    "columnNames", columnNames.toArray(String[]::new),
                    "columnTypes", columnTypes.toArray(String[]::new),
                    "columnSizes", columnSizes.toArray(String[]::new)));
        }
        return map;
    }

    public static void update(String updateQuery) {
        // Define regular expressions for each component of the UPDATE query
        String updateRegex = "^UPDATE (.+) SET (.+) WHERE (.+)$";
        String setRegex = "(\\w+)\\s*=\\s*'(.+?)'";

        // Compile the regular expressions
        Pattern updatePattern = Pattern.compile(updateRegex, Pattern.CASE_INSENSITIVE);
        Pattern setPattern = Pattern.compile(setRegex, Pattern.CASE_INSENSITIVE);

        // Extract the components of the UPDATE query
        Matcher updateMatcher = updatePattern.matcher(updateQuery);
        if (updateMatcher.find()) {
            String tableName = updateMatcher.group(1);
            String setClause = updateMatcher.group(2);
            String whereClause = updateMatcher.group(3);
            System.out.println("Table name: " + tableName);
            System.out.println("Set clause:");
            Matcher setMatcher = setPattern.matcher(setClause);
            while (setMatcher.find()) {
                String columnName = setMatcher.group(1);
                String columnValue = setMatcher.group(2);
                System.out.println(columnName + " = " + columnValue);
            }
            System.out.println("Where clause: " + whereClause);
        }
    }

    public static Map<String, String> delete(String deleteQuery) {
        // Define regular expressions for each component of the DELETE query
        String deleteRegex = "^DELETE FROM (.+) WHERE (.+)$";
        Map<String, String> map = new HashMap<>();
        // Compile the regular expressions
        Pattern deletePattern = Pattern.compile(deleteRegex, Pattern.CASE_INSENSITIVE);

        // Extract the components of the DELETE query
        Matcher deleteMatcher = deletePattern.matcher(deleteQuery);
        if (deleteMatcher.find()) {
            String tableName = deleteMatcher.group(1);
            String whereClause = deleteMatcher.group(2);
            map.putAll(Map.of(
                    "tableName", tableName,
                    "where", whereClause));
            System.out.println("Table name: " + tableName);
            System.out.println("Where clause: " + whereClause);
        }
        return map;
    }
}
