package idd.application;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AppendOperationResults {

    public static void appendToFile(String informazioneDaMemorizzare) {
        File file = new File("src/main/resources/operation_results.txt");
        BufferedWriter writer = null;

        try {
            // Apri il file in modalità append
            writer = new BufferedWriter(new FileWriter(file, true));

            // Scrivi il risultato e vai a capo
            writer.write(informazioneDaMemorizzare);
            writer.newLine();

        } catch (IOException e) {
            System.err.println("Errore durante la scrittura nel file: " + e.getMessage());
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (IOException e) {
                System.err.println("Errore durante la chiusura del file: " + e.getMessage());
            }
        }
    }
}
