package idd.indexing_phase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.core.KeywordTokenizerFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilterFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import java.io.IOException;

public class PersonalAnalyzer {


    public static Analyzer getTableIdAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                    .withTokenizer(KeywordTokenizerFactory.class)
                    .addCharFilter(PatternReplaceCharFilterFactory.class,"pattern", "_", "replacement", "")
                    .addTokenFilter(LowerCaseFilterFactory.class)
                    .build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    /*public static Analyzer getTableContentAnalyzer() {
        try {
            return CustomAnalyzer.builder().build();
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
    }*/


    public static Analyzer getTableCaptionAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                    .withTokenizer(WhitespaceTokenizerFactory.class)              // Tokenizzazione su spazi bianchi
                    .addTokenFilter(LowerCaseFilterFactory.class)                 // Converte tutto in minuscolo
                    .addTokenFilter(WordDelimiterGraphFilterFactory.class)        // Gestisce i delimitatori come `-`, `_`, e camel case
                    .addTokenFilter(EnglishPossessiveFilterFactory.class)         // Gestisce la possessiva inglese (es. "John's" → "John")
                    .addTokenFilter(PorterStemFilterFactory.class)                // Applica stemming per normalizzare i suffissi (es. "running" → "run")
                    .build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}