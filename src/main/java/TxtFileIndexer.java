import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Date;

/**
 * Created by taox on 15-8-16.
 */
/**
 * This class demonstrate the process of creating index with Lucene
 * for text files
 */
public class TxtFileIndexer {
    public static void main(String[] args) throws Exception{
        //indexDir is the directory that hosts Lucene's index files
        File   indexDir = new File("./data/luceneIndex");
        //dataDir is the directory that hosts the text files that to be indexed
        File   dataDir  = new File("./data/luceneData");
        Analyzer luceneAnalyzer = new StandardAnalyzer(Version.LUCENE_40);
        File[] dataFiles  = dataDir.listFiles();

        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, luceneAnalyzer);
        Directory index = FSDirectory.open(indexDir);
        IndexWriter indexWriter = new IndexWriter(index,config);
        long startTime = new Date().getTime();
        for(int i = 0; i < dataFiles.length; i++) {
            if (dataFiles[i].isFile() && dataFiles[i].getName().endsWith(".txt")) {
                System.out.println("Indexing file " + dataFiles[i].getCanonicalPath());
                Document document = new Document();
                Reader txtReader = new FileReader(dataFiles[i]);
                document.add(new TextField("path", dataFiles[i].getCanonicalPath(),Field.Store.YES));
                document.add(new TextField("contents", txtReader));
                indexWriter.addDocument(document);
            }
        }
        indexWriter.close();
        long endTime = new Date().getTime();

        System.out.println("It takes " + (endTime - startTime)
                + " milliseconds to create index for the files in directory "
                + dataDir.getPath());
    }
}
