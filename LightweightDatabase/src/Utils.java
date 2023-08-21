import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static ArrayList<LinkedHashMap<String, String>> runWhereClause(ArrayList<LinkedHashMap<String, String>> rows,
            String where) {
        return runWhereClause(rows, where, false);
    }

    public static ArrayList<LinkedHashMap<String, String>> runWhereClause(ArrayList<LinkedHashMap<String, String>> rows,
            String where, boolean isExcept) {
        // only good for single where

        ArrayList<LinkedHashMap<String, String>> selectedRows = new ArrayList<>();
        String operator = new String();
        String columnName = new String();
        String valueToCompare = new String();
        // Regular expression to match assignment operators
        // String operatorRegex = "\\s*(=|\\+=|-=|\\*=|/=|%=)\\s*";
        String operatorRegex = "\\s*((=)|(\\<>)|(\\>=)|(\\<=)|(\\>)|(\\<)|(\\!=))\\s*";

        // Pattern object to compile the regular expression
        Pattern pattern = Pattern.compile(operatorRegex);

        // Matcher object to find matches in the input string
        Matcher matcher = pattern.matcher(where);

        // Split the input string based on matches for assignment operators
        String[] parts = pattern.split(where);

        // Iterate over the resulting array and print the operator for each element
        for (int i = 0; i < parts.length; i++) {
            // System.out.println(parts[i].trim());
            // -- section to be changed in case of multiple where
            if (i == 0) {
                columnName = parts[i].trim();
            } else if (i == parts.length - 1) {
                valueToCompare = parts[i].trim();
            }
            // -- end section
            if (matcher.find()) {
                // System.out.println("Operator: " + matcher.group(1).trim());
                operator = matcher.group(1).trim();
            }
        }
        for (var row : rows) {
            switch (operator) {
                case "=":
                    if (isExcept) {
                        if (!row.get(columnName).equals(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    } else {
                        if (row.get(columnName).equals(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    }
                    break;
                case "!=":
                case "<>":
                    if (isExcept) {
                        if (row.get(columnName).equals(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    } else {

                        if (!row.get(columnName).equals(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    }
                    break;
                case "<=":
                    if (isExcept) {
                        if (Integer.parseInt(row.get(columnName)) > Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    } else {

                        if (Integer.parseInt(row.get(columnName)) <= Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    }
                    break;
                case ">=":
                    if (isExcept) {
                        if (Integer.parseInt(row.get(columnName)) < Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    } else {
                        if (Integer.parseInt(row.get(columnName)) >= Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    }
                    break;
                case "<":
                    if (isExcept) {
                        if (Integer.parseInt(row.get(columnName)) >= Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    } else {
                        if (Integer.parseInt(row.get(columnName)) < Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }

                    }
                    break;
                case ">":
                    if (isExcept) {
                        if (Integer.parseInt(row.get(columnName)) <= Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    } else {
                        if (Integer.parseInt(row.get(columnName)) > Integer.parseInt(valueToCompare)) {
                            selectedRows.add(row);
                        }
                    }
                    break;

                default:
                    System.out.println("Operator in where clause not yet supported");
            }
        }
        return selectedRows;
    }
}
