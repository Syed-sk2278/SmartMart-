package com.example.smartmartplus;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit;
    private static Context appContext;


    public static void initialize(Context context) {

        appContext =
                context.getApplicationContext();
    }


    public static Retrofit getRetrofitInstance() {

        if (retrofit == null) {

            if (appContext == null) {

                throw new IllegalStateException(
                        "RetrofitClient is not initialized"
                );
            }


            OkHttpClient client =
                    new OkHttpClient.Builder()

                            .addInterceptor(
                                    new Interceptor() {

                                        @Override
                                        public Response intercept(
                                                Chain chain)
                                                throws IOException {

                                            Request.Builder builder =
                                                    chain.request()
                                                            .newBuilder();

                                            // Supabase API key
                                            builder.addHeader(
                                                    "apikey",
                                                    Constants.SUPABASE_KEY
                                            );

                                            // Content type
                                            builder.addHeader(
                                                    "Content-Type",
                                                    "application/json"
                                            );


                                            // =================================
                                            // GET ACCESS TOKEN
                                            // =================================

                                            SharedPreferences prefs =
                                                    appContext
                                                            .getSharedPreferences(
                                                                    "SmartMartPrefs",
                                                                    Context.MODE_PRIVATE
                                                            );


                                            String accessToken =
                                                    prefs.getString(
                                                            "ACCESS_TOKEN",
                                                            null
                                                    );


                                            // =================================
                                            // SEND AUTHORIZATION
                                            // =================================

                                            if (accessToken != null
                                                    && !accessToken
                                                    .trim()
                                                    .isEmpty()) {

                                                builder.addHeader(
                                                        "Authorization",
                                                        "Bearer "
                                                                + accessToken
                                                );
                                            }


                                            return chain.proceed(
                                                    builder.build()
                                            );
                                        }
                                    }
                            )
                            .build();


            retrofit =
                    new Retrofit.Builder()

                            .baseUrl(
                                    Constants.SUPABASE_URL
                            )

                            .client(client)

                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )

                            .build();
        }


        return retrofit;
    }
}