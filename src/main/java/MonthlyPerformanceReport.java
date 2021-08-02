import classes.Employee;
import classes.ReportDefinition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MonthlyPerformanceReport {
    private static final String CSV_SEPARATOR = ",";
    private static final String FILE_PATH = "output\\report.csv";
    private static final String ARG_DATA_PATH="-pathDataFile";

    public static void main(String[] args) throws FileNotFoundException {
        if(args.length != 2) {
            System.out.print("Please run the app using following arguments: MonthlyPerformanceReport -pathDataFile='<path_to_data_file>' -pathDefinitionFile='<path_to_definition_file>'");
            System.exit(1);
        }

        String argDataPath;
        String argDefinitionPath;
        if(args[0].startsWith(ARG_DATA_PATH)) {
            argDataPath = args[0];
            argDefinitionPath = args[1];
        } else {
            argDataPath = args[1];
            argDefinitionPath = args[0];
        }

        String dataPath = argDataPath.substring(argDataPath.indexOf("=") + 1);
        String formattingPath = argDefinitionPath.substring(argDefinitionPath.indexOf("=") + 1);

        Gson gson = new GsonBuilder().create();
        JsonReader dataReader = new JsonReader(new FileReader(dataPath));
        Type type = new TypeToken<List<Employee>>() {
        }.getType();
        List<Employee> employees = gson.fromJson(dataReader, type);
        JsonReader definitionReader = new JsonReader(new FileReader(formattingPath));
        ReportDefinition reportDefinition = gson.fromJson(definitionReader, ReportDefinition.class);
        List<Employee> employeeResult = new ArrayList<>();

        employees.forEach(em -> {
            if(em.getSalesPeriod() > reportDefinition.getPeriodLimit()) {
                // skip employee with bigger sales period
                return;
            }

            float score = (float)em.getTotalSales() / em.getSalesPeriod();
            if(reportDefinition.isUseExperienceMultiplier()) {
                score *= em.getExperienceMultiplier();
            }

            em.setScore(score);
            employeeResult.add(em);
        });

        employeeResult.sort((e1, e2) -> Double.compare(e2.getScore(), e1.getScore()));  // Reversed order
        int numberOfFirstXPercentOfEmployees = (int)Math.ceil(employeeResult.size()*(reportDefinition.getTopPerformersThreshold()/100.0));

        List<Employee> employeeFinalResult = new ArrayList<>();
        for (int i = 0; i < employeeResult.size(); i++) {
            if (i < numberOfFirstXPercentOfEmployees) {
                employeeFinalResult.add(employeeResult.get(i));
            } else if (i == numberOfFirstXPercentOfEmployees) {
                // when we have many employees with same scores
                double score = employeeResult.get(i).getScore();
                int j = i;
                while (j < employeeResult.size() && employeeResult.get(j).getScore() == score) {
                    employeeFinalResult.add(employeeResult.get(j));
                    j++;
                }
                break;
            }
        }

        writeToCSV(employeeFinalResult);
    }

    private static void writeToCSV(List<Employee> employees) {
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH)));
            bw.write("name" + CSV_SEPARATOR + "score");
            bw.newLine();
            for (Employee employee : employees) {
                StringBuilder row = new StringBuilder();
                row.append(employee.getName());
                row.append(CSV_SEPARATOR);
                row.append(employee.getScore());
                bw.write(row.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
