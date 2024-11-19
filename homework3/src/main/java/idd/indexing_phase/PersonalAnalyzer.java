package idd.indexing_phase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilterFactory;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;
import org.apache.lucene.analysis.miscellaneous.WordDelimiterGraphFilterFactory;
import org.apache.lucene.analysis.pattern.PatternReplaceCharFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;

import java.io.IOException;

public class PersonalAnalyzer {

    //Metodo per personalizzare l'Analyzer da usare per il titolo
    public static Analyzer getTitleAnalyzer() {
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

    //Metodo per personalizzare l'Analyzer da usare per il contenuto
    public static Analyzer getContentAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                    .withTokenizer(StandardTokenizerFactory.class)                 // Tokenizzazione avanzata su punteggiatura e spazi
                    .addCharFilter(HTMLStripCharFilterFactory.class)              // Rimuove i tag HTML
                    .addTokenFilter(LowerCaseFilterFactory.class)                 // Converte tutto in minuscolo
                    .addTokenFilter(WordDelimiterGraphFilterFactory.class)        // Gestisce i delimitatori come `-`, `_`, camel case
                    .addTokenFilter(EnglishPossessiveFilterFactory.class)         // Gestisce possessivi inglesi (es. "John's" → "John")
                    .addTokenFilter(PorterStemFilterFactory.class)                // Stemming per normalizzare suffissi
                    .build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Metodo per personalizzare l'Analyzer da usare per gli autori
    public static Analyzer getAuthorsAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                    .withTokenizer(WhitespaceTokenizerFactory.class)             // Tokenizza su spazi bianchi
                    .addCharFilter(PatternReplaceCharFilterFactory.class,"pattern", ",", "replacement", " ")                           // Sostituisce le virgole con spazi per uniformare la separazione dei nomi
                    .addTokenFilter(LowerCaseFilterFactory.class)                // Converte i token in minuscolo per uniformità
                    .build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Metodo per personalizzare l'Analyzer da usare per l'abstract
    public static Analyzer getAbstractAnalyzer() {
        try {
            return CustomAnalyzer.builder()
                    .withTokenizer(StandardTokenizerFactory.class)                 // Tokenizzazione avanzata su punteggiatura e spazi
                    .addCharFilter(HTMLStripCharFilterFactory.class)              // Rimuove i tag HTML
                    .addTokenFilter(LowerCaseFilterFactory.class)                 // Converte tutto in minuscolo
                    .addTokenFilter(WordDelimiterGraphFilterFactory.class)        // Gestisce i delimitatori come `-`, `_`, camel case
                    .addTokenFilter(EnglishPossessiveFilterFactory.class)         // Gestisce possessivi inglesi (es. "John's" → "John")
                    .addTokenFilter(PorterStemFilterFactory.class)                // Stemming per normalizzare suffissi
                    .build();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}