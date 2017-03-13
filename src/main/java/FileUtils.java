import java.io.*;

/**
 * Created by Karina on 26.02.2017.
 */

class FileUtils {
    private final static File logFile = new File("log.txt");

    public static void writeLog(String log) {
        if (!logFile.exists())
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try (FileWriter fileWriter = new FileWriter(logFile, true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(log);
            bufferedWriter.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
