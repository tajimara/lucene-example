package fastcampus.lucene.example.elasticsearch.index;


import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * @author OJS
 */
class BulkInsertWithDB
{

    public static void main(String[] args) throws UnknownHostException {

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("52.79.172.251"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("52.79.172.251"), 9300));

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        try {
            // either use client#prepare, or use Requests# to directly build index/delete requests
            bulkRequest.add(client.prepareIndex("twitter", "tweet", "1")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("user", "kimchy")
                            .field("postDate", new Date())
                            .field("message", "trying out Elasticsearch")
                            .endObject()
                    )
            );

            bulkRequest.add(client.prepareIndex("twitter", "tweet", "2")
                    .setSource(jsonBuilder()
                            .startObject()
                            .field("user", "kimchy")
                            .field("postDate", new Date())
                            .field("message", "another post")
                            .endObject()
                    )
            );
        } catch (IOException iex) {

        }
        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
        }
    }
}


/*
자바에서 색인용 JSON 문서 만드는 법

1. 직접만들기
String json = "{" +
        "\"user\":\"kimchy\"," +
        "\"postDate\":\"2013-01-30\"," +
        "\"message\":\"trying out Elasticsearch\"" +
    "}";



2.Map 사용하기
Map<String, Object> json = new HashMap<String, Object>();
json.put("user","kimchy");
json.put("postDate",new Date());
json.put("message","trying out Elasticsearch");


3. 객체 직렬화하기 (jackson 이용
import com.fasterxml.jackson.databind.*;
// instance a json mapper
ObjectMapper mapper = new ObjectMapper(); // create once, reuse
// generate json
byte[] json = mapper.writeValueAsBytes(yourbeaninstance);

4. Elasticsearch의 helper 이용하기
import static org.elasticsearch.common.xcontent.XContentFactory.*;
XContentBuilder builder = jsonBuilder()
    .startObject()
        .field("user", "kimchy")
        .field("postDate", new Date())
        .field("message", "trying out Elasticsearch")
    .endObject()


문서를 색인 하기
import static org.elasticsearch.common.xcontent.XContentFactory.*;

IndexResponse response = client.prepareIndex("twitter", "tweet", "1")
        .setSource(jsonBuilder()
                    .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                    .endObject()
                  )
        .get();

response 객체에 결과 저장
// Index name
String _index = response.getIndex();
// Type name
String _type = response.getType();
// Document ID (generated or not)
String _id = response.getId();
// Version (if it's the first time you index this document, you will get: 1)
long _version = response.getVersion();
// isCreated() is true if the document is a new one, false if it has been updated
boolean created = response.isCreated();

 */

