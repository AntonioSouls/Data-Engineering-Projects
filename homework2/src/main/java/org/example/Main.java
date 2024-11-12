package org.example;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

// Classe principale che modella il comportamento di un motore di ricerca
public class Main {

    // Funzione principale che modella una query e la sfrutta per interrogare l'indice
    public static void main(String[] args) throws Exception {
        Path path = Paths.get("target/indexDocuments");

        String[] fields = {"title","content","authors","abstract"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, HTMLIndexer.getPerFieldAnalyzer());

        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println("Benvenuto in Vulturii Web Searcher!\n");
        while (true) {
            System.out.println("Cosa vuoi cercare?\n");
            input = scanner.nextLine();
            if (input.equals("exit") || input.equals("quit") || input.equals("EXIT") || input.equals("QUIT")) {
                break;
            }
            Query query = parser.parse(input);
            try (Directory directory = FSDirectory.open(path)) {
                try (IndexReader reader = DirectoryReader.open(directory)) {
                    IndexSearcher searcher = new IndexSearcher(reader);
                    runQuery(searcher,query);
                }
            }
            System.out.println("\n\n");
        }



    }

    private static void runQuery(IndexSearcher searcher, Query query) throws Exception {
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(query, 10);
        StoredFields storedFields = searcher.storedFields();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.print("Found Elements: [" + hits.scoreDocs.length + "] in "+ elapsedTime +" ms\n");
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = storedFields.document(scoreDoc.doc);
            System.out.println("[Document "+scoreDoc.doc + "]:"+ doc.get("title") + " (" + scoreDoc.score +")");
        }
    }
}


