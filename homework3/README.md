# <div align="center"> HOMEWORK 3: DATA DISCOVERY VIA JSON FILE INDEXING </div>


The aim of this homework is again to help us understand how to speed up the search for information within a large data 
set. 
The same request made for the previous homework, with the only difference that, in this project, the challenge is to 
index tables contained within JSON files.  
This represents a much more difficult obstacle to overcome since, unlike the HTML indexed in the previous project, the 
indexing of tables within JSON files 
has many more fields to take into account which, moreover, must also be analysed and parsed following much more complex 
logic.
Clearly, this challenge is very interesting and extremely useful as it allows us to develop a not inconsiderable 
capacity for reasoning and problem-solving and, moreover, gives us the opportunity to broaden the range of solutions to 
be proposed in all those areas of Data Science where it is necessary to carry out a process of Data Discovery


## - Homework Requirements
The project assignment was based on two requests:
- To index all 10k JSON files contained in a directory provided to us in the project specification;
- Create a Java programme that would behave like a search engine, i.e. read console queries, query the index and print 
the tabular result;
- Test the program's performances with a set of 10 queries;
- Analysing the programme using specific metrics that can validate how well the programme performs on a large scale


## - Description of Our Solution
Our solution starts with the creation of the index. This, first of all, takes care of extracting tables from the corpus of
JSON documents provided and stores the information useful for searching. In particular, the index stores the TABLE_ID,
the TABLE_CAPTION and the TABLE_CONTENT. The latter was obtained by extracting only the row and column header fields,
which are the only fields that are really relevant for a table search.  
Having done this, the index is created and stored in an appropriate directory.  
The search programme, on the other hand, takes care of reading the console queries, querying the index and printing the results
sorted in the order of relevance that the index prioritises.  
Finally, the programme was tested through metrics such as MRR and NDCG, which allow us to assess the effectiveness and accuracy
of our solution in terms of search and result ranking.