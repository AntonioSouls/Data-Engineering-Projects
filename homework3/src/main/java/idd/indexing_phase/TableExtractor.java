package idd.indexing_phase;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CLASSE CHE GESTISCE LA RESPONSABILITA' DI ESTRARRE TUTTE LE TABELLE DI UN FILE JSON
 * Descrizione: Elenca tutte le tabelle in formato JSON, dopodiché le esprime sotto forma di Mappa chiave-valore, ed
 * infine le inserisce in una lista di Mappe in modo da restituire l'elenco delle tabelle del file espresse in formato Mappa
 */
public class TableExtractor {
    /** Metodo che implementa il comportamento descritto nel commento soprastante */
    public static List<Map<String, String>> extractor(File jsonFile) throws IOException {
        List<Map<String, String>> tables = new ArrayList<>();    // Istanzio la lista da restituire

        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObj = new JSONObject(new JSONTokener(reader));    //Rendo l'intero file un oggetto JSON

            for (String tableId : jsonObj.keySet()) {
                if (!(jsonObj.get(tableId) instanceof JSONObject)){
                    continue;                                                                 //Escludo le chiavi a cui non corrisponde una tabella JSON
                }
                JSONObject tableObj = jsonObj.getJSONObject(tableId);                        // Estraggo tutte le tabelle JSON
                String tableContent = tableObj.optString("table", "");         // Delle tabelle mi memorizzo solo il contenuto, l'id e le caption
                String tableCaption = tableObj.optString("caption", "");
                Map<String, String> table = new HashMap<>();
                table.put("table_id", tableId);
                table.put("table_content", tableContent);
                table.put("table_caption", tableCaption);
                tables.add(table);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return tables;
    }

}
