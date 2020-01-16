package com.example.phototagger.server;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServerAPI {
    String ES_INDEX = "photo_tag";
    @GET("/" + ES_INDEX + "/_search")
    Call<EsQueryResponse> queryPhotos(@Query("q")String query);
}
