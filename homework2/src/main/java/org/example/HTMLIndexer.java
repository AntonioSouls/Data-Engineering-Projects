package org.example;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class HTMLIndexer {

    /* Funzione che crea effettivamente l'indice sui documenti */
    public void indexDocs(Directory directory) throws Exception {

        // Istanzio il nostro analyzer personalizzato da passare all'indice
        Analyzer analyzer = getPerFieldAnalyzer();

        // Creo l'indice
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setCodec(new SimpleTextCodec());

        IndexWriter writer = new IndexWriter(directory, config);
        writer.deleteAll();

        // Inizio la procedura per aggiungere i vari file all'indice
        File htmlDirectory = new File("target/all_htmls");
        File[] htmlFiles = htmlDirectory.listFiles();

        if (htmlFiles != null) {
            int totalFiles = htmlFiles.length;
            int processedFiles = 0;

            for (File htmlFile : htmlFiles) {
                Document documentoJSOUP = Jsoup.parse(htmlFile, "UTF-8");

                String title = documentoJSOUP.title();                 // Seleziono il titolo del documento
                if (title.isEmpty()) {
                    title = documentoJSOUP.select("h1.ltx_title").text();
                }
                String content = documentoJSOUP.body().text();         // Seleziono il contenuto del documento
                String abstractContent = documentoJSOUP.select("div.ltx_abstract").text();  // Seleziono l'abstract del documento

                // Seleziono tutti gli autori del documento
                List<String> authors = new ArrayList<>();
                Elements authorsSpan = documentoJSOUP.select("div.ltx_authors span.ltx_personname");
                for (Element author : authorsSpan) {
                    authors.add(author.text());
                }
                String oneStringAuthors = String.join(", ", authors);

                // Creo il documento per l'indice e aggiungo i vari campi
                org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
                doc.add(new TextField("title", title, Field.Store.YES));
                doc.add(new TextField("content", content, Field.Store.YES));
                doc.add(new TextField("authors", oneStringAuthors, Field.Store.YES));
                doc.add(new TextField("abstract", abstractContent, Field.Store.YES));
                writer.addDocument(doc);

                // Aggiorna la barra di avanzamento
                processedFiles++;
                printProgress(processedFiles, totalFiles);
            }
        }

        writer.commit();
        writer.close();
    }

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

    public static Analyzer getPerFieldAnalyzer () throws Exception {
        Analyzer defaultAnalyzer = new StandardAnalyzer();

        Map<String, Analyzer> perFieldAnalyzers = new HashMap<>();
        perFieldAnalyzers.put("title", PersonalAnalyzer.getTitleAnalyzer());
        perFieldAnalyzers.put("content", PersonalAnalyzer.getContentAnalyzer());
        perFieldAnalyzers.put("authors", PersonalAnalyzer.getAuthorsAnalyzer());
        perFieldAnalyzers.put("abstract", PersonalAnalyzer.getAbstractAnalyzer());
        return new PerFieldAnalyzerWrapper(defaultAnalyzer, perFieldAnalyzers);
    }

    /* Funzione principale che consente l'avvio della creazione di un indice per i documenti */
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("target/indexDocuments");
        try (Directory directory = FSDirectory.open(path)) {
            HTMLIndexer indexer = new HTMLIndexer();
            indexer.indexDocs(directory);
            directory.close();
            System.out.println("\nSUCCESS: Indicizzazione Terminata!");
        }

    }
}