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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get("target/indexDocuments");

        String[] fields1 = {"table_id"};
        String[] fields2 = {"table_columns", "table_rows", "table_caption"};
        MultiFieldQueryParser table_id_parser = new MultiFieldQueryParser(fields1, JSON_Indexer.getPerFieldAnalyzer());
        MultiFieldQueryParser table_content_caption_parser = new MultiFieldQueryParser(fields2, JSON_Indexer.getPerFieldAnalyzer());

        System.out.println("BENVENUTO IN VULTURII WEB SEARCHER!\n");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();

                String input = scanner.nextLine();
                if (isExitCommand(input)) {
                    System.out.println("Grazie per aver usato VULTURII WEB SEARCHER. Arrivederci!");
                    break;
                }

                switch (input) {
                    case "1":
                        handleTableIdSearch(scanner, table_id_parser, path);
                        break;
                    case "2":
                        handleContentSearch(scanner, table_content_caption_parser, path);
                        break;
                    default:
                        System.out.println("Comando non valido. Riprova.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("Cosa vuoi cercare?\n");
        System.out.println("-- Digita 1 se vuoi cercare l'id di una tabella");
        System.out.println("-- Digita 2 se vuoi effettuare una ricerca più approfondita");
        System.out.println("-- Digita {'exit'/'quit'/'EXIT'/'QUIT'} se vuoi terminare la sessione");
    }

    private static boolean isExitCommand(String input) {
        return input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit");
    }

    private static void handleTableIdSearch(Scanner scanner, MultiFieldQueryParser parser, Path path) throws Exception {
        System.out.println("Inserisci l'ID della tabella cercata:");
        String queryStr = scanner.nextLine();
        Query query = parser.parse(queryStr);
        executeSearch(path, query, queryStr);
    }

    private static void handleContentSearch(Scanner scanner, MultiFieldQueryParser parser, Path path) throws Exception {
        System.out.println("Inserisci la query:");
        String queryStr = scanner.nextLine();
        Query query = parser.parse(queryStr);
        executeSearch(path, query, queryStr);
    }

    private static void executeSearch(Path path, Query query, String queryStr) throws Exception {
        try (Directory directory = FSDirectory.open(path);
             IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            List<Integer> rankedResults = runQuery(searcher, query);

            // Ottieni i documenti rilevanti per la query corrente
            List<Integer> relevantDocuments = getRelevantDocuments(queryStr, searcher);

            // Stampa di debug
            System.out.println("Ranked Results: " + rankedResults);
            System.out.println("Relevant Documents: " + relevantDocuments);

            // Calcola le metriche
            double mrr = EvaluationMetrics.calculateMRR(Arrays.asList(rankedResults), relevantDocuments);
            double ndcg = EvaluationMetrics.calculateNDCG(rankedResults, relevantDocuments, 10);

            System.out.println("MRR: " + mrr);
            System.out.println("NDCG: " + ndcg);
        }
    }

    private static List<Integer> getRelevantDocuments(String queryStr, IndexSearcher searcher) throws Exception {
        Query query = new MultiFieldQueryParser(new String[]{"table_content", "table_caption"}, JSON_Indexer.getPerFieldAnalyzer()).parse(queryStr);
        TopDocs hits = searcher.search(query, 10);
        List<Integer> relevantDocuments = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            relevantDocuments.add(scoreDoc.doc);
        }
        return relevantDocuments;
    }

    private static List<Integer> runQuery(IndexSearcher searcher, Query query) throws Exception {
        long startTime = System.currentTimeMillis();
        TopDocs hits = searcher.search(query, 10);
        StoredFields storedFields = searcher.storedFields();
        long elapsedTime = System.currentTimeMillis() - startTime;

        System.out.println("Found Elements: [" + hits.scoreDocs.length + "] in " + elapsedTime + " ms");

        List<Integer> rankedResults = new ArrayList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = storedFields.document(scoreDoc.doc);
            rankedResults.add(scoreDoc.doc);
            System.out.println("ID TABELLA: [" + doc.get("table_id") + "] PERTINENZA: (" + scoreDoc.score + ")");
            System.out.println(doc.get("table_content"));
            System.out.println(doc.get("table_caption") + "\n\n\n\n");
        }
        return rankedResults;
    }
}
