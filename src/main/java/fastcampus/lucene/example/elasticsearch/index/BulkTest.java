package fastcampus.lucene.example.elasticsearch.index;

import fastcampus.lucene.example.database.MysqlConnect;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.lucene.queryparser.classic.ParseException;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/*
REST Client로 벌크 인서트함
 */
public class BulkTest {
    public static void main(String[] args) throws IOException, ParseException {
        RestClient restClient = RestClient.builder(
                new HttpHost("13.124.87.104", 9200, "http"),
                new HttpHost("13.124.87.104", 9201, "http")).build();
        String index = "news";
        String type = "article";
        HttpEntity entity = makeBulkData(index,type);
        try {
            Response response = restClient.performRequest("POST", "/"+index+"/"+type+"/_bulk", Collections.emptyMap(), entity);
            System.out.println(response.toString());
        } catch (Exception e) {
            // do something
        }
        restClient.close();
    }


    public static HttpEntity makeBulkData(String index, String type) {

        MysqlConnect mysqlConnect = new MysqlConnect();
        ResultSet resultSet = null;
        List<String> bulkData = new ArrayList<>();

        String sql = "SELECT * FROM  NewsData LIMIT 1000";
        try {
            PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);

            resultSet = statement.executeQuery();
            if (resultSet != null) {
                while (resultSet.next()) {
                    try {
                        bulkData.add("{ \"title\" : \""+ resultSet.getString("title") +"\",\"author\" : \""+resultSet.getString("author")+"\",\"date\" : \""+resultSet.getString("modDate")+"\",\"description\": \""+resultSet.getString("description")+"\" }\n");
                    } catch (IllegalArgumentException ex) {
                        //ex.printStackTrace();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            mysqlConnect.disconnect();
        }



        String actionMetaData = String.format("{ \"index\" : { \"_index\" : \"%s\", \"_type\" : \"%s\" } }%n", index, type);
        StringBuilder bulkRequestBody = new StringBuilder();
        for (String bulkItem : bulkData) {
            bulkRequestBody.append(actionMetaData);
            bulkRequestBody.append(bulkItem);
            bulkRequestBody.append("\n");
        }
        HttpEntity entity = new NStringEntity(bulkRequestBody.toString(), ContentType.APPLICATION_JSON);
        return entity;
    }
}