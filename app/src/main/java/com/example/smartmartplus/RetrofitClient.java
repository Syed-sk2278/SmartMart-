package com.example.smartmartplus;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {

            // Add headers automatically to every request
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain)
                                throws IOException {

                            Request request = chain.request()
                                    .newBuilder()
                                    .addHeader(
                                            "apikey",
                                            Constants.SUPABASE_KEY
                                    )
                                    .addHeader(
                                            "Content-Type",
                                            "application/json"
                                    )
                                    .build();

                            return chain.proceed(request);
                        }
                    })
                    .build();


            // Retrofit Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.SUPABASE_URL)
                    .client(client)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();
        }

        return retrofit;
    }
}