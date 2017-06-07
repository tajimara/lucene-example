package fastcampus.lucene.example.index;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/** Simple command-line based search demo. */
public class SearchingExample {

  private SearchingExample() {
  }

  /**
   * 검색 예제
   */
  public static void main(String[] args) throws Exception {
    String usage =
            "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] ";
    if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
      System.out.println(usage);
      System.exit(0);
    }


    String index = "index/txt"; //기본 인덱스 위치
    String field = "contents";
    String queryString = "굉장히 비싼"; //검색어
    int repeat = 0;
    boolean raw = false;
    int hitsPerPage = 10;

    //인덱스를 읽어들임
    IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
    //서처를 생성함
    IndexSearcher searcher = new IndexSearcher(reader);

    //기본분석기를 사용함
    Analyzer analyzer = new StandardAnalyzer();

    Query q = new QueryParser(field, analyzer).parse(queryString);

    System.out.println("Searching for: " + queryString);
    TopDocs matches = searcher.search(q, 10);
    System.out.println("검색 결과 Found " + matches.totalHits + " hits.");

    for (int i = 0 ; i < matches.totalHits;i ++) {
      System.out.println(i + ": " + matches.scoreDocs[i].toString());
      Document d = searcher.doc(matches.scoreDocs[i].doc);
      System.out.println(matches.scoreDocs[i].doc +":점수:" + matches.scoreDocs[i].score + ":" + d.get("contents") + "\t" + d.get("path"));
    }

    reader.close();
  }
}



























































