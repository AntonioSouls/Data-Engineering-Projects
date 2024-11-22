package idd.indexing_phase;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class JSONIndexerTest {

    Path path = Paths.get("target/indexDocuments");

    //Test per verificare che l'indice abbia incluso ed indicizzato tutte le tabelle
    @Test
    public void allDocsTest() throws Exception {
        Query query = new MatchAllDocsQuery();
        try (Directory directory = FSDirectory.open(path)) {
            try (IndexReader reader = DirectoryReader.open(directory)) {
                IndexSearcher searcher = new IndexSearcher(reader);
                int IndexedTables = runAllDocsQuery(searcher,query);
                assertEquals(IndexedTables, 44470);                                                              // Ho messo 44470 perchè in TableExtractorTest scopro che il totale di tabelle è 49538 ma di queste 5068 vengono scartate perchè non sono tabelle reali
                                                                                                                        // Con questo test mi assicuro che tutte e 44470 tabelle che devono essere indicizzate, vengano effettivamente indicizzate
            }
        }
    }

    private int runAllDocsQuery(IndexSearcher searcher, Query query) throws Exception {
        TopDocs hits = searcher.search(query, 100000);
        return hits.scoreDocs.length;
    }

}



