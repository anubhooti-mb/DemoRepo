import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class ExecuteQueries {

    public static String runQueries() {

        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        String tableName;
        File file;
        String SEPARATOR = "!!";

        switch (choice.split(" ")[0].toLowerCase().trim()) {
            case "select":
                // Perform select/read case.
                // parse query
                var selectParsedQuery = Query.select(choice);
                // check table exists
                tableName = selectParsedQuery.get("tableName")[0];
                file = new File(tableName + ".txt");
                if (file.exists()) {
                    var columnNames = selectParsedQuery.get("columnNames");
                    // if table exists read file and fetch data
                    var selectedRows = FileRead.selectTable(tableName, SEPARATOR);
                    if (selectParsedQuery.containsKey("where")) {
                        // use where clause to filter rows
                        selectedRows = Utils.runWhereClause(selectedRows, selectParsedQuery.get("where")[0]);
                    }
                    if (columnNames[0].equals("*")) {
                        System.out.println(selectedRows);
                    } else {
                        // filtering columnname keys from selected rows
                        selectedRows.forEach(map -> {
                            map.entrySet().stream().filter(e -> Arrays.asList(columnNames).contains(e.getKey()))
                                    .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
                        });
                    }
                } else {
                    // else if table doesn't exists throw error
                    System.out.println("INVALID TABLE: Table Doesn't exists!!");
                }
                break;
            case "insert":
                // Perform insert into table case.
                // Parse query
                var insertParsedQuery = Query.insert(choice);
                tableName = insertParsedQuery.get("tableName")[0];
                // Check table exists
                file = new File(tableName + ".txt");
                if (!file.exists()) {
                    // if not throw error
                    System.out.println("INVALID TABLE NAME: Table doesn't exists");
                } else {
                    // if exist append in table file
                    try {
                        var text = FileRead.insertTableToText(insertParsedQuery, SEPARATOR);
                        FileRead.appendTextFile(tableName, text);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
            case "create":
                // Perform create table case.
                // Parse create query
                var createTableObj = Query.create(choice);
                tableName = createTableObj.get("tableName")[0];
                file = new File(tableName + ".txt");
                if (file.exists()) {
                    // if file/table exists throw error
                    System.out.println("INVALID TABLE NAME: Table already exists");
                } else {
                    // if doesn't exist create file with header
                    var text = FileRead.createTableToText(createTableObj, SEPARATOR);
                    FileRead.writeTextFile(createTableObj.get("tableName")[0], text);
                }
                break;
            case "delete":
                // Perform delete case.
                var deleteParsedQuery = Query.delete(choice);
                tableName = deleteParsedQuery.get("tableName");
                file = new File(tableName + ".txt");
                if (file.exists()) {
                    var selectedRows = FileRead.selectTable(tableName, SEPARATOR);
                    var remainingRows = Utils.runWhereClause(selectedRows, deleteParsedQuery.get("where"), true);
                    var fileText = FileRead.getFirstTwoLinesFromFile(tableName);
                    fileText += FileRead.convertRowsToText(remainingRows, SEPARATOR);
                    FileRead.writeTextFile(tableName, fileText);
                } else {
                    System.out.println("Table doesn't exist");
                }
                break;
            case "update":
                // Perform update case.
                Query.update(choice);
                break;
            case "q":
                scanner.close();
                break;
            default:
                // The user input an unexpected choice.
                System.out.println("INVALID QUERY: Please enter a valid query");
        }

        return choice;
    }
}
