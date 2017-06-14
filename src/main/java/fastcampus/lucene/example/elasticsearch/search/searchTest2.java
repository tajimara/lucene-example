package fastcampus.lucene.example.elasticsearch.search;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 https://www.elastic.co/guide/en/elasticsearch/client/java-rest/master/java-rest-high.html
 */
public class searchTest2 {
    public static void main(String[] args) throws IOException {

        RestClient restClient = RestClient.builder(
                new HttpHost("52.79.171.65", 9200, "http"),
                new HttpHost("52.79.171.65", 9201, "http")).build();

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("q", "title:대통령");
        paramMap.put("pretty", "true");


//        HttpEntity entity1 = new NStringEntity(
//                "{\n" +
//                        "    \"query\" : {\n" +
//                        "    \"match\": { \"company\":\"qbox\"} \n" +
//                        "} \n"+
//                        "}", ContentType.APPLICATION_JSON);
//

        Response response = restClient.performRequest("GET", "/news/_search",paramMap);


        System.out.println(EntityUtils.toString(response.getEntity()));
        System.out.println("Host -" + response.getHost() );
        System.out.println("RequestLine -"+ response.getRequestLine() );

    }
}


//    @Override
//    public State getClusterState(Cluster cluster) throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        // initialization
//        RestClient restClient = RestClient.builder(createHttpHosts(cluster.getNode())).build();
//        State state = null;
//
//        try {
//            Response response = restClient.performRequest("GET", "/_cluster/state");
//            state = objectMapper.readValue(EntityUtils.toString(response.getEntity()), State.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            restClient.close();
//        }
//
//        return state;
//
//    }