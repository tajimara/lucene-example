package fastcampus.lucene.example.search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.icu.ICUNormalizer2Filter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.util.PagedBytes;

import java.io.StringReader;

//public class ICUAnalyzer extends Analyzer {
//
//    @Override
//    protected TokenStreamComponents createComponents(String field,String reader) {
//        AttributeFactory factory = AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY;
//        StandardTokenizer tokenizer = new StandardTokenizer(factory);
//        tokenizer.setReader(new StringReader(reader));
//        TokenStream filter = new ICUNormalizer2Filter()
//        return new TokenStreamComponents(tokenizer, filter);
//    }
//}
