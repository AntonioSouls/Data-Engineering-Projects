package idd.indexing_phase;

import org.json.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JSON_Indexer {

    /* Funzione che crea effettivamente l'indice sui documenti */
    public void indexDocs(Directory directory) throws Exception {

        // Istanzio il nostro analyzer personalizzato da passare all'indice
        Analyzer analyzer = getPerFieldAnalyzer();

        // Creo l'indice
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setCodec(new SimpleTextCodec());

        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteAll();

        // Inizio la procedura per aggiungere le varie tabelle all'indice
        File jsonDirectory = new File("target/all_tables");
        File[] jsonFiles = jsonDirectory.listFiles();

        if (jsonFiles != null) {
            int totalFiles = jsonFiles.length;
            int processedFiles = 1;

            for (File jsonFile : jsonFiles) {
                System.out.println(processedFiles + "/" + totalFiles);

                // Estraggo le tabelle dal file
                List<Map<String, String>> tables = TableExtractor.extractor(jsonFile);

                // Per ogni tabella estratta, creo il documento per l'indice e aggiungo i vari campi
                for (Map<String, String> table : tables){
                    org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                    doc.add(new TextField("table_id", table.get("table_id"), Field.Store.YES));
                    doc.add(new TextField("table_content", table.get("table_content"), Field.Store.YES));
                    doc.add(new TextField("table_caption", table.get("table_caption"), Field.Store.YES));
                    writer.addDocument(doc);
                }

                // Aggiorna la barra di avanzamento
                processedFiles++;
            }
        }

        writer.commit();
        writer.close();
    }

    /*// Funzione che crea e restituisce un PerFieldAnalyzer
    public static Analyzer getPerFieldAnalyzer () throws Exception {
        Analyzer defaultAnalyzer = new StandardAnalyzer();

        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
        perFieldAnalyzers.put("title", PersonalAnalyzer.getTitleAnalyzer());
        perFieldAnalyzers.put("content", PersonalAnalyzer.getContentAnalyzer());
        perFieldAnalyzers.put("authors", PersonalAnalyzer.getAuthorsAnalyzer());
        perFieldAnalyzers.put("abstract", PersonalAnalyzer.getAbstractAnalyzer());
        return new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);
    }*/


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Metodo per stampare la barra di avanzamento
    private void printProgress(int processedFiles, int totalFiles) {
        int barLength = 50; // Lunghezza della barra
        int progress = (int) ((double) processedFiles / totalFiles * barLength);
        StringBuilder bar = new StringBuilder("[");

        for (int i = 0; i < barLength; i++) {
            if (i < progress) {
                bar.append("=");
            } else {
                bar.append(" ");
            }
        }
        bar.append("] ").append(processedFiles).append("/").append(totalFiles);

        // Stampa la barra di avanzamento e sovrascrive la linea precedente
        System.out.print("\r" + bar.toString());
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /* Funzione principale che consente l'avvio della creazione di un indice per i documenti */
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        Path path = Paths.get("target/indexDocuments");
        try (Directory directory = FSDirectory.open(path)) {
            JSON_Indexer indexer = new JSON_Indexer();
            indexer.indexDocs(directory);
            directory.close();
            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;
            totalTime /= 1000;
            System.out.println("\nSUCCESS: Indicizzazione Terminata in " + totalTime + "s!\n");
        }

    }
}