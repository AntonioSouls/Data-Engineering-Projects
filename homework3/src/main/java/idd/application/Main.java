package idd.application;

import idd.indexing_phase.JSON_Indexer;
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

public class Main {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("target/indexDocuments");

        String[] fields1 = {"table_id"};
        String[] fields2 = {"table_content","title_caption"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields1, JSON_Indexer.getPerFieldAnalyzer());

        Scanner scanner = new Scanner(System.in);
        String input = "";

        System.out.println("Benvenuto in Vulturii Web Searcher!\n");
        while (true) {
            System.out.println("Cosa vuoi cercare?\n");
            System.out.println("-- Digita 1 se vuoi cercare l'id di una tabella");
            System.out.println("-- Digita 2 se vuoi effettuare una ricerca più approfondita");
            System.out.println("-- Digita {'exit'/'quit'/'EXIT'/'QUIT'} se vuoi terminare la sessione");
            input = scanner.nextLine();
            if (input.equals("exit") || input.equals("quit") || input.equals("EXIT") || input.equals("QUIT")) {
                break;
            }
            if(input.equals("1")){
                System.out.println("Inserisci l'ID della tabella cercata:");
                input = scanner.nextLine();
            }
            if(input.equals("2")){
                System.out.println("Inserisci la query: ");
                input = scanner.nextLine();
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
        scanner.close();



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