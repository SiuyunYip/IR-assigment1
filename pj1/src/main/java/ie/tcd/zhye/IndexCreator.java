package ie.tcd.zhye;

import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeren YE
 * @version 1.0
 * @date 2022/10/16 14:17
 */
public class IndexCreator {

    public static void main(String[] args) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(Constant.INDEX_DIRECTORY));

        MyAnalyzer analyzer = new MyAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        /**
         * ClassicSimilarity: VSM
         * BM25Similarity: BM25
         */
//        config.setSimilarity(new ClassicSimilarity());
        config.setSimilarity(new BM25Similarity());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter indexWriter = new IndexWriter(dir, config);

        List<Document> docs = getDocument();
        for (Document doc : docs) {
            indexWriter.addDocument(doc);
        }
        indexWriter.close();
        dir.close();
    }

    private static List<Document> getDocument() throws IOException {
        List<Document> docList = new ArrayList();

        FileReader fileReader = new FileReader(Constant.DOCUMENT_DIRECTORY);
        BufferedReader bfr = new BufferedReader(fileReader);

        String line = "";
        int id = 1;
        while (line != null) {
            while (!(line = bfr.readLine()).equals(".T"));
            String title = "";
            while (!(line = bfr.readLine()).equals(".A")) {
                title += line + " ";
            }

            String author = "";
            while (!(line = bfr.readLine()).equals(".B")) {
                author += line + " ";
            }

            String issue = "";
            while (!(line = bfr.readLine()).equals(".W")) {
                issue += line + " ";
            }

            String content = "";
            while ((line = bfr.readLine()) != null && !line.startsWith(".I")) {
                content += line + " ";
            }
            docList.add(getDocument(id, title, author, issue, content));
            ++ id;
        }

        return docList;
    }

    /**
     * @param id:     .I
     * @param title   .T
     * @param author  .A
     * @param issue   .B
     * @param content .W
     * @return
     */
    private static Document getDocument(int id, String title, String author, String issue, String content) {
        Document doc = new Document();
        doc.add(new TextField("id", "" + id, Field.Store.YES));
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new TextField("author", author, Field.Store.YES));
        doc.add(new TextField("issue", issue, Field.Store.YES));
        doc.add(new TextField("content", content, Field.Store.YES));

        return doc;
    }
}
