package org.example;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HTMLIndexerTest {
    Path path = Paths.get("target/indexDocuments");

    //Test per verificare che l'indice abbia incluso ed indicizzato tutti i documenti
    @Test
    public void allDocsTest() throws Exception {
        Query query = new MatchAllDocsQuery();
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }

    // Test per verificare il funzionamento della ricerca di un termine sul campo titolo
    @Test
    public void TermQueryTestOnTitleField1() throws Exception {
        String[] fields = {"title"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getTitleAnalyzer());
        String queryString = "biodenoising";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }

    // Secondo test per verificare il funzionamento della ricerca di un termine sul campo titolo.
    // In questo test abbiamo complicato la logica ricercando una parola più complessa nella struttura
    @Test
    public void TermQueryTestOnTitleField2() throws Exception {
        String[] fields = {"title"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getTitleAnalyzer());
        String queryString = "BiodenoisIng";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }


    // Test per verificare il funzionamento della ricerca di un termine sul campo content
    @Test
    public void TermQueryTestOnContentField1() throws Exception {
        String[] fields = {"content"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getContentAnalyzer());
        String queryString = "denoising";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }

    // Secondo test per verificare il funzionamento della ricerca di un termine sul campo content
    // Anche in questo caso abbiamo complicato la ricerca inserendo un temrine più complesso nella struttura
    @Test
    public void TermQueryTestOnContentField2() throws Exception {
        String[] fields = {"content"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getContentAnalyzer());
        String queryString = "DenoiSing";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }


    // Test per verificare il funzionamento della ricerca di un termine sul campo authors
    @Test
    public void TermQueryTestOnAuthorsField() throws Exception {
        String[] fields = {"authors"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getAuthorsAnalyzer());
        String queryString = "Hoffman";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }

    // Test per verificare il funzionamento della ricerca di un termine sul campo abstract
    @Test
    public void TermQueryTestOnAbstractField() throws Exception {
        String[] fields = {"abstract"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getAbstractAnalyzer());
        String queryString = "denoising";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }


    // Test per verificare il funzionamento della ricerca di una frase sul campo titolo
    @Test
    public void PhraseQueryTestOnTitleField() throws Exception {
        String[] fields = {"title"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getTitleAnalyzer());
        String queryString = "denoising vocalization";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }


    // Test per verificare il funzionamento della ricerca di una frase sul campo content
    @Test
    public void PhraseQueryTestOnContentField() throws Exception {
        String[] fields = {"content"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getContentAnalyzer());
        String queryString = "pairing Vocalization";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }


    // Test per verificare il funzionamento della ricerca di una frase sul campo authors
    @Test
    public void PhraseQueryTestOnAuthorsField() throws Exception {
        String[] fields = {"authors"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getAuthorsAnalyzer());
        String queryString = "benjamin Hoffman";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }


    // Test per verificare il funzionamento della ricerca di una frase sul campo abstract
    @Test
    public void PhraseQueryTestOnAbstractField() throws Exception {
        String[] fields = {"abstract"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, PersonalAnalyzer.getAbstractAnalyzer());
        String queryString = "Vocalization denoising";
        Query query = parser.parse(queryString);
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                runQuery(searcher,query);
            }
        }
    }

    private void runQuery(IndexSearcher searcher, Query query) throws Exception {
        TopDocs hits = searcher.search(query, 10000);
        StoredFields storedFields = searcher.storedFields();
        System.out.print(hits.scoreDocs.length + "\n");
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            ScoreDoc scoreDoc = hits.scoreDocs[i];
            Document doc = storedFields.document(scoreDoc.doc);
            System.out.println("[Document "+scoreDoc.doc + "]:"+ doc.get("title") + " (" + scoreDoc.score +")");
        }
    }


}



