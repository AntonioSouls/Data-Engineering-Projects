# <div align="center"> HOMEWORK 2: CREATING A SEARCH ENGINE VIA A Lucene INDEX </div>


The aim of this homework is to help us understand how to speed up the search for information within a large data set. Through the challenges presented here, it was possible to understand how the Lucene library works and
how it can be exploited to index a large dataset. This is a widely used methodology in Data Science to speed up the search for information within a large dataset.
## - Homework Requirements
The project assignment was based on two requests:
- To index all 10k HTML files contained in a directory provided to us in the project specification;
- Create a Java programme that would behave like a search engine, i.e. read console queries, query the index and print the result;
- Test the program's performances with a set of 10 queries
## - Description of Our Solution
The solution proposed by me and my colleague [@danielluca00](https://github.com/danielluca00) consists of two main Java components:
- **An HTMLIndexer class for indexing**.
    - The HTMLIndexer class is responsible for reading HTML files in a specific directory (target/all_htmls), extracting the main relevant contents, and indexing them in a structured format. To improve the quality of the index, HTMLIndexer uses a custom class called PersonalAnalyzer. This analyzer, extending Lucene's Analyzer class, creates a term tokenization and filtering process specific to each field in the document
      ;
- **A Main class for searching:**
    - The Main class enables searches on the index created by HTMLIndexer, returning and displaying documents relevant to the user's query.


In summary, this HTML indexing and search programme offers an efficient solution for managing and querying a large number of HTML documents. With targeted indexing via HTMLIndexer and the ability to perform customised searches via Main, this application enables quick and precise content analysis of HTML documents, providing users with fast answers to their search queries.