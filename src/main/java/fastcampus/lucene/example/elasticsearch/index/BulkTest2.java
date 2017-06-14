package fastcampus.lucene.example.elasticsearch.index;


import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Date;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;


/*
TransportClient 로 벌크 인서트함
 */
public class BulkTest2 {
    public static void main(String[] args) throws IOException, ParseException {
        BulkRequestBuilder bulkRequest;
        String ts;
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("13.124.87.104"), 9300));

        bulkRequest = client.prepareBulk();

        int i=0;



        for (i=0; i<100000; i++) {
            ts = String.valueOf(System.currentTimeMillis());
            bulkRequest.add(client.prepareIndex("news", "article")
                    //.setRouting(ts)
                    .setSource(jsonBuilder()
                        .startObject()
                        .field("title", "test test")
                        .field("modDate", new Date())
                        .field("description", "test test post")
                        .endObject()
                    ));

        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet(5000);
        System.out.println("DOCUMENT Sequence :"+i+"");
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
        }


    }
}