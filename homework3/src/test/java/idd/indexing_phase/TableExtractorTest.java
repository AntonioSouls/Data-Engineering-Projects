package idd.indexing_phase;

import idd.application.AppendOperationResults;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe di test volta a verificare per ogni file JSON nella directory "all_tables" quali tabelle non estrae e controllare
 * che sia corretta la scelta di non estrarle
 */
public class TableExtractorTest {

    // Classe che testa il reale comportamento di extractor semplicemente contando quante tabelle ho in totale dentro un file JSON
    // e quante tabelle non estraggo
    public static List<Integer> extractorTest (File jsonFile) throws IOException{
        List<Integer> TotalTableVSunextractedTables = new ArrayList<>();
        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObj = new JSONObject(new JSONTokener(reader));
            int TabelleMancanti = 0;
            int TabelleTotaliNelFile = jsonObj.keySet().size();
            for (String tableId : jsonObj.keySet()) {
                if (!(jsonObj.get(tableId) instanceof JSONObject)){
                    System.out.println("Non ho estratto la seguente tabella: " + tableId);
                    TabelleMancanti++;
                }
            }
            TotalTableVSunextractedTables.add(TabelleTotaliNelFile);
            TotalTableVSunextractedTables.add(TabelleMancanti);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    return TotalTableVSunextractedTables;
    }


    // Classe principale che passa alla funzione precedente tutti i file JSON fa un conteggio di quante tabelle totali ci
    // sono in tutta la directory e quante sono quelle non estratte
    public static void main (String[] args) throws Exception{
        File jsonDirectory = new File("target/all_tables");
        File[] jsonFiles = jsonDirectory.listFiles();
        int TotaleTabelleNonEstratte = 0;
        int TotaleTabelle = 0;
        if (jsonFiles != null) {
            for (File jsonFile : jsonFiles) {
                List<Integer> TotalTableVSunextractedTables = TableExtractorTest.extractorTest(jsonFile);
                TotaleTabelle = TotaleTabelle + TotalTableVSunextractedTables.get(0);
                int TabelleMancanti = TotalTableVSunextractedTables.get(1);
                if (TabelleMancanti > 0) {
                    System.out.println(jsonFile.getName() +  ": Da questo file NON sono state estratte " + TabelleMancanti + " tabelle\n");
                    TotaleTabelleNonEstratte = TotaleTabelleNonEstratte + TabelleMancanti;
                }
            }
            System.out.println("Totale Tabelle Non Estratte: " + TotaleTabelleNonEstratte + "/" + TotaleTabelle);
            AppendOperationResults.appendToFile("Totale Tabelle Non Estratte: " + TotaleTabelleNonEstratte + "/" + TotaleTabelle);
        }
    }


}
