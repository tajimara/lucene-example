package fastcampus.lucene.example.restclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BulkTest {
    public static void main(String[] args) throws IOException, ParseException {
        RestClient restClient = RestClient.builder(
                new HttpHost("52.79.172.251", 9200, "http"),
                new HttpHost("52.79.172.251", 9201, "http")).build();
        String index = "test_index";
        String type = "test_type";
        String actionMetaData = String.format("{ \"index\" : { \"_index\" : \"%s\", \"_type\" : \"%s\" } }%n", index, type);

        List<String> bulkData = new ArrayList<>();
        bulkData.add("{ \"title\" : \"부산 뇌전증 환자 교통사고 대응조치…장애등급 판정자도 포함\",\"author\" : \"정대기 기자\",\"date\" : \"2016-08-02 13:50:00\",\"content\": \"최근 부산에서 뇌전증 환자가 차를 몰다 도심에서 17명의 사상자를 낸 사고와 관련, 경찰이 운전면허 수시적성검사 대상인 뇌전증 환자의 범위를 확대하는 내용으로 관련 법령을 손보기로 했다.\" }\n");

        StringBuilder bulkRequestBody = new StringBuilder();
        for (String bulkItem : bulkData) {
            bulkRequestBody.append(actionMetaData);
            bulkRequestBody.append(bulkItem);
            bulkRequestBody.append("\n");
        }
        HttpEntity entity = new NStringEntity(bulkRequestBody.toString(), ContentType.APPLICATION_JSON);
        try {
            Response response = restClient.performRequest("POST", "/your_index/your_index/_bulk", Collections.emptyMap(), entity);
            System.out.println(response.toString());
        } catch (Exception e) {
            // do something
        }
        restClient.close();
    }

}