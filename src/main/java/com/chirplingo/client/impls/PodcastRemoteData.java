package com.chirplingo.client.impls;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.chirplingo.client.base.BaseSupabaseClient;
import com.chirplingo.client.base.SupabaseContext;
import com.chirplingo.client.interfaces.FetchDataClient;
import java.util.Collections;
import com.chirplingo.domain.Podcast;
import com.fasterxml.jackson.core.type.TypeReference;

public class PodcastRemoteData extends BaseSupabaseClient implements FetchDataClient<Podcast>{
    private String tableName = "podcasts";

    public PodcastRemoteData(SupabaseContext context) {
        super(context);
    }

    @Override
    public Podcast fetchLatest() {
        try {
            String endpoint = "/rest/v1/" + tableName + "?order=published_at.desc&limit=1";

            HttpRequest request = createRequest(endpoint).GET().build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if(status == 200 || status == 201) {
                List<Podcast> podcasts = context.getMapper().readValue(response.body(), new TypeReference<List<Podcast>>() {});


                if(podcasts != null && !podcasts.isEmpty()){
                    return podcasts.get(0);
                }
            } else {
                System.err.println("Lỗi FetchLatest Podcast [" + status + "]: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Podcast> fetchAll() {
        try {
            String endpoint = "/rest/v1/" + tableName + "?order=published_at.desc&limit=20";

            HttpRequest request = createRequest(endpoint).GET().build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if(status == 200 || status == 201){
                List<Podcast> podcasts = context.getMapper().readValue(
                    response.body(), 
                    new TypeReference<List<Podcast>>() {}
                );

                if(podcasts != null && !podcasts.isEmpty()){
                    return podcasts;
                }
            } else {
                System.err.println("Lỗi FetchAll Podcast [" + status + "]: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
