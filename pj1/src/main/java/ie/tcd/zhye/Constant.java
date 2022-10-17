package ie.tcd.zhye;

/**
 * @author Zeren YE
 * @version 1.0
 * @date 2022/10/16 16:42
 */
public interface Constant {
    String INDEX_DIRECTORY = "./dir";

    String DOCUMENT_DIRECTORY = "./cran/cran.all.1400";

    String QUERY_FILE = "./cran/cran.qry";

    Integer MAX_CLAUSE = 50;

    String VPM_RESULT = "./result/vpm_result.txt";

    String BM25_RESULT = "./result/bm25_result.txt";

    String[] searchFields = {"title", "author", "content", "issue"};
}
