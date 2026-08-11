package com.example.smartmartplus;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {

    private static SupabaseApi api;


    public static SupabaseApi getApi() {

        if (api == null) {

            OkHttpClient client =
                    new OkHttpClient.Builder()
                            .addInterceptor(chain -> {

                                Request request =
                                        chain.request()
                                                .newBuilder()

                                                .addHeader(
                                                        "apikey",
                                                        Constants.SUPABASE_KEY
                                                )

                                                .addHeader(
                                                        "Authorization",
                                                        "Bearer " +
                                                                Constants.SUPABASE_KEY
                                                )

                                                .addHeader(
                                                        "Content-Type",
                                                        "application/json"
                                                )

                                                .build();

                                return chain.proceed(request);
                            })
                            .build();


            Retrofit retrofit =
                    new Retrofit.Builder()

                            .baseUrl(
                                    Constants.SUPABASE_URL
                            )

                            .client(client)

                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )

                            .build();


            api =
                    retrofit.create(
                            SupabaseApi.class
                    );
        }


        return api;
    }
}