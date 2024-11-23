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
                String tableColumns = String.join(", ", extractColumnKeywords(tableContent));
                String tableRows = String.join(", ", extractRowKeywords(tableContent));

                // Limita la lunghezza a 100 caratteri
                tableColumns = tableColumns.length() > 100 ? tableColumns.substring(0, 100) : tableColumns;
                tableRows = tableRows.length() > 100 ? tableRows.substring(0, 100) : tableRows;

                Map<String, String> table = new HashMap<>();
                table.put("table_id", tableId);
                table.put("table_content", tableContent);
                table.put("table_columns", tableColumns);
                table.put("table_rows", tableRows);
                table.put("table_caption", tableCaption);
                tables.add(table);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return tables;
    }




    public static List<String> extractColumnKeywords(String htmlTable) {
        List<String> columnKeywords = new ArrayList<>();
        if (htmlTable == null || htmlTable.trim().isEmpty()){
            return columnKeywords;
        }
        org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(htmlTable);
        org.jsoup.nodes.Element firstRow = doc.select("tr").first();
        if (firstRow != null) {
            for (org.jsoup.nodes.Element cell : firstRow.select("th")) {
                columnKeywords.add(cell.text());
            }
        }
        return columnKeywords;
    }

    public static List<String> extractRowKeywords(String htmlTable) {
        List<String> rowKeywords = new ArrayList<>();
        if (htmlTable == null || htmlTable.trim().isEmpty()){
            return rowKeywords;
        }
        org.jsoup.nodes.Document doc = org.jsoup.Jsoup.parse(htmlTable);
        for (org.jsoup.nodes.Element row : doc.select("tr")) {
            org.jsoup.nodes.Element firstCell = row.select("th").first();
            if (firstCell != null) {
                rowKeywords.add(firstCell.text());
            }
        }
        return rowKeywords;
    }

}
