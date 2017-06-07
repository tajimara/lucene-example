package fastcampus.lucene.example.index;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.*;


public class IndexingExample {

    private IndexingExample() {
    }

    /**
     * 루씬 색인 예제 파일
     */
    public static void main(String[] args) {

        if(args.length != 3) {
            throw new IllegalArgumentException("사용법 : java "+ IndexingExample.class.getName() + " <index dir> <data dir> <create mode bool>");
        }

        String indexPath = args[0]; //기본 index 패스
        String docsPath =  args[1]; //기본 data 패스
        boolean create = Boolean.parseBoolean(args[2]);      //생성모드인지 추가 모드인지   true면 create 모드

        final Path docDir = Paths.get(docsPath);
        if (!Files.isReadable(docDir)) {
            System.out.println("Document directory '" + docDir.toAbsolutePath() + "' 파일이 존재하지 않거나 해당 경로를 읽을수 없습니다.");
            System.exit(1);
        }
        Date start = new Date();  //수행 시간 측정용

        try {
            System.out.println("인덱스 디렉토리 문서를 색인 합니다. '" + indexPath + "'...");

            Directory dir = SimpleFSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new StandardAnalyzer();                             //기본 스탠다드분석기를 사용함
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);  //인덱스 Writer의 설정을 지정하는 클래스
//            if (create) {
//                // 새로운 인덱스를 생성하고 기존의 문서를 삭제함
//                indexWriterConfig.setOpenMode(OpenMode.CREATE);
//            } else {
//                // 기존 인덱스에 새도큐먼트를 추가함
//                indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
//            }
            indexWriterConfig.setOpenMode(OpenMode.APPEND);
            indexWriterConfig.setUseCompoundFile(false); //다중 파일 색인 생성시  !!!!!!!

            // 생인성능을 위해 램버퍼를 지정할수 있음
            // 많은 수의 문서를 색인 할 경우 램 버퍼값을 추가해 주면 좋음
            // 단 힙사이즈는 넘기면 안됨
            // indexWriterConfig.setRAMBufferSizeMB(256.0);
            //indexWriterConfig.setMergePolicy()

            IndexWriter writer = new IndexWriter(dir, indexWriterConfig);           //lucene in action에 있는 IndexWriter 생성 방법은 deprecated됨 이제는  Config를 받는 방식으로 변경
            indexDocs(writer, docDir); //문서를 색인함

            //writer.commit();

            //indexDatabase(writer);  //데이터 베이스에서 색인
//            final Path docDir2 = Paths.get("data/csv");
//            indexDocs(writer, docDir2); //문서를 색인함
//            writer.commit();
            //색인 성능을 위해서 색인 종료후
            // 강제 병합을 할 수 있음
            // writer.forceMerge(1);

            writer.close();

//            IndexWriterConfig indexWriterConfig2 = new IndexWriterConfig(analyzer);  //인덱스 Writer의 설정을 지정하는 클래스
//            IndexWriter writer2 = new IndexWriter(dir, indexWriterConfig2);           //lucene in action에 있는 IndexWriter 생성 방법은 deprecated됨 이제는  Config를 받는 방식으로 변경
//            writer2.close();

            Date end = new Date();
            System.out.println(end.getTime() - start.getTime() + " total milliseconds");

        } catch (IOException e) {
            System.out.println(" caught a " + e.getClass() +
                    "\n with message: " + e.getMessage());
        }
    }


    //data 경로에서 파일을 찾아내어 색인 함
    static void indexDocs(final IndexWriter writer, Path path) throws IOException {
        if (Files.isDirectory(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        //indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
                        indexDocForCsv(writer, file, attrs.lastModifiedTime().toMillis());
                    } catch (IOException ignore) {
                        // don't index files that can't be read.
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
        }
    }

    /**
     * 문서하나를 하나의 Doc으로 보면서 색인
     */
    static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
        try (InputStream stream = Files.newInputStream(file)) {
            // 빈 새로운 문서 생성
            Document doc = new Document();

            FieldType koreanType = new FieldType();
            koreanType.setStored(true);
            koreanType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
            koreanType.setStoreTermVectorOffsets(true);

            // 파일 경로 저장
            Field pathField = new StringField("path", file.toString(), Field.Store.YES);
            doc.add(pathField);

            //파일 최종 수정일자 저장
            doc.add(new LongPoint("modified", lastModified));


            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            // 파일 내용 저장
            String result = null;
            String data = null;
            while((result=br.readLine())!=null){

                // 읽은 데이터(한줄)을 BufferedWriter에 쓴다.
                // 한줄씩 읽으므로, newLine() 메소드로 줄바꿈을 해준다.
                data += result;
            }
            doc.add(new TextField("contents",data,Field.Store.YES));

            if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                // 새로운 색인의 경우 새로 색인을 생성함
                System.out.println("adding " + file);
                writer.addDocument(doc);

            } else {
                //이미 색인이 존재하고 업데이트인 경우 색인을 추가함
                System.out.println("updating " + file);
                writer.updateDocument(new Term("path", file.toString()), doc);
            }
        }
    }

    /**
     * 하나의 문서안에 1줄을 1개 Doc으로 생각하고 생인
     * CSV파일 구조
     * *---------------------------------------------------------
     *| id |cat|name|price|inStock|author|series|sequence| genre|
     *-----------------------------------------------------------
    */
    static void indexDocForCsv(IndexWriter writer, Path file, long lastModified) throws IOException {
        String csvFile ="./data/csv/books.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                Document doc = new Document();
                // use comma as separator

                String[] data = line.split(cvsSplitBy);

                FieldType koreanType = new FieldType();
                koreanType.setStored(true);
                koreanType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
                koreanType.setStoreTermVectorOffsets(true);
                final FieldType ft = new FieldType(TextField.TYPE_STORED);

//                ft.setStoreTermVectors(true);
//                ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
//                ft.setOmitNorms(true);
//                ft.setStored(true);
//                doc.add(new Field("title", "나는 자랑스러운", ft));
//
////
//                Map<String, Analyzer> analyzerMap = new HashMap<String, Analyzer>();
//                analyzerMap.put("id", new StandardAnalyzer();
//                analyzerMap.put("title", new WhitespaceAnalyzer();
//                PerFieldAnalyzerWrapper wrapper = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerMap);
//                IndexWriterConfig iwConfig = new IndexWriterConfig(wrapper);

//                class 	LegacyDoubleField	Deprecated. 
//                Please use DoublePoint instead
//                class 	LegacyFloatField	Deprecated. 
//                Please use FloatPoint instead
//                class 	LegacyIntField	Deprecated. 
//                Please usinsteade IntPoint
//                class 	LegacyLongField	Deprecated. 
//                Please use LongPoint instead

                //필드별 부스트 예제
                Field field = new StringField("id", data[0], Field.Store.YES);
                field.setBoost(10);
                //문서 부스트는 DoubleDocValuesField로 잊ㄴ

                doc.add(new StringField("id", data[0], Field.Store.YES));
                doc.add(new StringField("cat", data[1], Field.Store.YES));
                doc.add(new StringField("name", data[2], Field.Store.YES));
                doc.add(new FloatDocValuesField("price",Float.parseFloat(data[3])));
                doc.add(new StringField("instock", data[4], Field.Store.YES));
                doc.add(new StringField("author", data[5], Field.Store.YES));
                doc.add(new StringField("series", data[6], Field.Store.YES));
                doc.add(new StringField("genre", data[8], Field.Store.YES));
                doc.add(new DoubleDocValuesField("boost", 10));



                if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
                    // 새로운 색인의 경우 새로 색인을 생성함
                    System.out.println("adding " + file);
                    writer.addDocument(doc);
                } else {
                    //이미 색인이 존재하고 업데이트인 경우 색인을 추가함
                    System.out.println("updating " + file);
                    writer.updateDocument(new Term("path", file.toString()), doc);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
