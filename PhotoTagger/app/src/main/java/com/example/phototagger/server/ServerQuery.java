package com.example.phototagger.server;

import retrofit2.Call;
import retrofit2.Callback;

public class ServerQuery {
    public static void queryToES(String query, Callback<EsQueryResponse> callback) {
        Call<EsQueryResponse> call = ServiceGenerator.createService(ServerAPI.class).queryPhotos(query);
        call.enqueue(callback);
    }
}
