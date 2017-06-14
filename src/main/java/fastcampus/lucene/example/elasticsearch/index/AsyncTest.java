package fastcampus.lucene.example.elasticsearch.index;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;


public class AsyncTest {
    public static void main(String[] args) throws IOException, ParseException {
        RestClient restClient = RestClient.builder(
                new HttpHost("13.124.87.104", 9200, "http"),
                new HttpHost("13.124.87.104", 9201, "http")).build();
        int numRequests = 10;
        final CountDownLatch latch = new CountDownLatch(numRequests);

        for (int i = 0; i < numRequests; i++) {
            HttpEntity entity = new NStringEntity(
                    "{\n" +
                            "    \"user\" : \"kimchy\",\n" +
                            "    \"post_date\" : \"2009-11-15T14:12:12\",\n" +
                            "    \"message\" : \"trying out Elasticsearch\"\n" +
                            "}", ContentType.APPLICATION_JSON);

            restClient.performRequestAsync(
                    "PUT",
                    "/async/tweet/" + i,
                    Collections.<String, String>emptyMap(),
                    entity,
                    new ResponseListener() {
                        @Override
                        public void onSuccess(Response response) {
                            System.out.println(response);
                            latch.countDown();
//                            try {
//                                //restClient.close();
//                            } catch (IOException iex) {
//                                //some work
//                            }
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            System.out.println(exception.toString());
                            latch.countDown();
//                            try {
//                                //restClient.close();
//                            } catch (IOException iex) {
//                                //some work
//                            }
                        }
                    }
            );
        }


    }

}