import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.nio.file.Paths;

/**
 * 간단한 검색 예제
 * testXXX 메소드를 일괄적으로 실행함
 */
public class BasicSearchingTest extends TestCase{
    public void testTerm() throws Exception {
        String index = "./index";

        //프로세스 순서 Directory => Reader => Searcher
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
        IndexSearcher searcher = new IndexSearcher(reader);

        Term t = new Term("name","ant");
        Query query = new TermQuery(t);
        TopDocs docs = searcher.search(query,10);
        assertEquals("Ant in Action",0,docs.totalHits);

        t = new Term("name","junit");
        docs = searcher.search(new TermQuery(t),10);
        assertEquals(2,docs.totalHits);

        //searcher.close(); searcher에 대한 close는 deprecated됨
        reader.close();
    }
}
