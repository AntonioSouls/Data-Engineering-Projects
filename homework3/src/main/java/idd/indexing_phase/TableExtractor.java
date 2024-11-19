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

public class TableExtractor {

    public static List<Map<String, String>> extractor(File jsonFile) throws IOException {
        List<Map<String, String>> tables = new ArrayList<>();

        try (FileReader reader = new FileReader(jsonFile)) {
            JSONObject jsonObj = new JSONObject(new JSONTokener(reader));

            for (String tableId : jsonObj.keySet()) {
                JSONObject tableObj = jsonObj.getJSONObject(tableId);
                String tableContent = tableObj.optString("table", "");
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
