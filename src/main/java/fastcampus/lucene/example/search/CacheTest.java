package fastcampus.lucene.example.search;

/**
루씬 스코어 예제
*/

import junit.framework.TestCase;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Set;

// 스코어 테스트
public class CacheTest extends TestCase {
  public void testSanity() throws IOException {
//    final int maxNumberOfCachedQueries = 256;
//    final long maxRamBytesUsed = 50 * 1024L * 1024L; // 50MB
//    // these cache and policy instances can be shared across several queries and readers
//    // it is fine to eg. store them into static variables
//    final QueryCache queryCache = new LRUQueryCache(maxNumberOfCachedQueries, maxRamBytesUsed);
//    final QueryCachingPolicy defaultCachingPolicy = new UsageTrackingQueryCachingPolicy();
//
//    // ...
//
//    // Then at search time
//    Query query = new TermQuery(new Term("text", "aaa"));
//    Weight weight =
//    Query myCacheQuery = queryCache.doCache(query, defaultCachingPolicy);
//    // myCacheQuery is now a wrapper around the original query that will interact with the cache
//    IndexSearcher searcher = "";
//    TopDocs topDocs = searcher.search(new ConstantScoreQuery(myCacheQuery), 10);
//  }
  }
}
