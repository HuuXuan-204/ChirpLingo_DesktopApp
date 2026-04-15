package com.chirplingo.client.impls;

import com.chirplingo.client.interfaces.FetchDataClient;
import com.chirplingo.client.base.BaseSupabaseClient;
import com.chirplingo.client.base.SupabaseContext;
import com.chirplingo.domain.newspaper.Newspaper;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Collections;
import com.fasterxml.jackson.core.type.TypeReference;

public class NewspaperRemoteData extends BaseSupabaseClient implements FetchDataClient<Newspaper> {
    private String tableName = "newspapers";

    public NewspaperRemoteData(SupabaseContext context) {
        super(context);
    }

    @Override
    public Newspaper fetchLatest() {
        try {
            String endpoint = "/rest/v1/" + tableName + "?order=published_at.desc&limit=1";

            HttpRequest request = createRequest(endpoint).GET().build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if(status == 200 || status == 201){
                List<Newspaper> newspapers = context.getMapper().readValue(
                    response.body(), 
                    new TypeReference<List<Newspaper>>() {}
                );

                if(newspapers != null && !newspapers.isEmpty()){
                    return newspapers.get(0);
                }
            } else {
                System.err.println("Lỗi FetchLatest Newspaper [" + status + "]: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Newspaper> fetchAll() {
        try {
            String endpoint = "/rest/v1/" + tableName + "?order=published_at.desc&limit=20";

            HttpRequest request = createRequest(endpoint).GET().build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if(status == 200 || status == 201){
                List<Newspaper> newspapers = context.getMapper().readValue(
                    response.body(), 
                    new TypeReference<List<Newspaper>>() {}
                );

                if(newspapers != null && !newspapers.isEmpty()){
                    return newspapers;
                }
            } else {
                System.err.println("Lỗi FetchAll Newspaper [" + status + "]: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }


}
