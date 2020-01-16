package com.example.phototagger.server;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator extends NetDefine {
    private static HttpLoggingInterceptor sLogging;

    private static Retrofit.Builder sBuilder =
            new Retrofit.Builder()
                    .baseUrl(getBasicPath())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("mm/dd/yyyy HH:mm").create()));

    public static <S> S createService(Class<S> serviceClass) {
        return sBuilder.client(getHttpClient()).build().create(serviceClass);
    }

    protected static OkHttpClient getHttpClient() {
        if (sLogging == null) {
            sLogging = new HttpLoggingInterceptor();
            sLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }

        return new OkHttpClient.Builder()
                .addInterceptor(sLogging)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

}