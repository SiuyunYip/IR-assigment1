package ie.tcd.zhye;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zeren YE
 * @version 1.0
 * @date 2022/10/16 16:44
 */
public class QueryResolver {

    public static void main(String[] args) throws Exception {
        Directory dir = FSDirectory.open(Paths.get(Constant.INDEX_DIRECTORY));

        Analyzer analyzer = new MyAnalyzer(StopAnalyzer.ENGLISH_STOP_WORDS_SET);
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Constant.searchFields, analyzer, getBoostMap());

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
//        searcher.setSimilarity(new ClassicSimilarity());
        searcher.setSimilarity(new BM25Similarity());
        List<String> queries = getQueries();
        List<String> results = new ArrayList<>();
        for (int i = 0; i < queries.size(); i ++ ) {
            String qy = QueryParser.escape(queries.get(i).trim());
            Query query = queryParser.parse(qy);
            ScoreDoc[] scoreDocs = searcher.search(query, Constant.MAX_CLAUSE).scoreDocs;
            for (int j = 0; j < scoreDocs.length; j ++ ) {
                Document doc = searcher.doc(scoreDocs[j].doc);
                // query-id Q0 document-id rank score STANDARD
                int rank = j + 1;
                results.add(i + 1 + " 0 " + doc.get("id") + " " + rank + " " + scoreDocs[j].score + " Standard");
            }
        }

        dir.close();
        reader.close();
        writeRank2File(results);
    }

    private static void writeRank2File(List<String> results) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(Constant.BM25_RESULT));
        for (String res : results) {
            bw.write(res);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    private static Map<String, Float> getBoostMap() {
        Map<String, Float> map = new HashMap<>();
        map.put("title", 0.3f);
        map.put("author", 0.05f);
        map.put("content", 0.7f);
        map.put("issue", 0.01f);

        return map;
    }

    private static List<String> getQueries() throws IOException {
        List<String> queryList = new ArrayList<>();

        FileReader fileReader = new FileReader(Constant.QUERY_FILE);
        BufferedReader bfr = new BufferedReader(fileReader);
        String line = "";
        while (line != null) {
            String query = "";
            while ((line = bfr.readLine()).startsWith(".I") || line.startsWith(".W"));
            while (line != null && !line.startsWith(".I")) {
                query += line + " ";
                line = bfr.readLine();
            }
            queryList.add(query);
        }

        return queryList;
    }
}
