package fastcampus.lucene.example.elasticsearch.search;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryBuilders.*;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by jihoon on 2017. 6. 12..
 */
public class searchTest1 {
    public static void main(String[] args) throws UnknownHostException {

//        RestClient client = RestClient.builder(
//                new HttpHost("52.79.172.251", 9200, "http"),
//                new HttpHost("52.79.172.251", 9201, "http")).build();

        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("13.124.87.104"), 9400))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("13.124.87.104"), 9400));

        SearchResponse response = client.prepareSearch("news", "test")
                .setTypes("store", "article")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("title", "기자"))                 // Query
                //.setPostFilter(QueryBuilders.rangeQuery("age").from(12).to(18))     // Filter
                .setFrom(0).setSize(60).setExplain(true)
                .get();

        System.out.println(response.toString());

    }
}
