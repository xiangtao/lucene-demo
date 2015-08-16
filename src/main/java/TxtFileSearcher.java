import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;

import java.io.File;

/**
 * Created by taox on 15-8-16.
 */
public class TxtFileSearcher {

    public static void main(String[] args) throws Exception{
        String queryStr = "INFO";
        //This is the directory that hosts the Lucene index
        File indexDir = new File("./data/luceneIndex");
        FSDirectory directory = FSDirectory.open(indexDir);
        IndexReader reader = IndexReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        if(!indexDir.exists()){
            System.out.println("The Lucene index is not exist");
            return;
        }
        Term term = new Term("contents",queryStr.toLowerCase());
        TermQuery luceneQuery = new TermQuery(term);
        TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
        searcher.search(luceneQuery, collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;

        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d);
            System.out.println((i + 1) + ". " + d.get("contents") + "\t" + d.get("path"));
        }
    }


}
