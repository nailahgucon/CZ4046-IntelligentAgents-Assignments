package output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
public class createFile {
    /**
     writeToFile(String fileName, List<String> headers, List<List<Double>> data):
     Writes the headers and data to a CSV file located in a "outputFiles" directory in the current working directory.
     */
    public static void writeToFile(String fileName, List<String> headers, List<List<Double>> data) {
        FileWriter fileWriter;
        try {
            String filePath = new File("").getAbsolutePath();
            fileWriter = new FileWriter(filePath.concat("/outputFiles/" + fileName + ".csv"));

            // iterates over the headers and writes them to the CSV file, separated by commas
            for (String header : headers) {
                fileWriter.append(header);
                fileWriter.append(",");
            }
            fileWriter.append("\n");

            // iterates over each list of Double values in the data list
            for (List<Double> iteration : data) {
                for (Double value : iteration) {
                    fileWriter.append(Double.toString(value));
                    fileWriter.append(",");
                }
                fileWriter.append("\n");
            }

            // flushes and closes the FileWriter object
            fileWriter.flush();
            fileWriter.close();

            System.out.println();
            System.out.println("Please see " + fileName + ".csv for the results :)");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
