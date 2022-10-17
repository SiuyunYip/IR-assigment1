package ie.tcd.zhye;

import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

/**
 * @author Zeren YE
 * @version 1.0
 * @date 2022/10/16 16:08
 */
public class MyAnalyzer extends StopwordAnalyzerBase {
    public MyAnalyzer(CharArraySet stopWordSet) {
        super(stopWordSet);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer tokenizer = new StandardTokenizer();
        TokenStream tks = new StandardFilter(tokenizer);
        tks = new LowerCaseFilter(tks);
        tks = new StopFilter(tks, stopwords);
        tks = new PorterStemFilter(tks);

        return new TokenStreamComponents(tokenizer, tks);
    }
}
