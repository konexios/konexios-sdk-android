package com.kronossdk.api.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;
import com.kronossdk.api.Constants;
import com.kronossdk.api.ServerEndpoint;
import com.kronossdk.api.mqtt.common.NoSSLv3SocketFactory;
import com.kronossdk.api.rest.IotConnectAPIService;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by osminin on 3/15/2016.
 */
public abstract class RetrofitHolder {
    private static ApiRequestSigner requestSigner = ApiRequestSigner.getInstance();

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS'Z'");
                    df.setTimeZone(tz);
                    String nowAsISO = df.format(new Date()).replace(" ", "T");

                    String body = bodyToString(chain.request().body());
                    if (requestSigner.getSecretKey() == null || requestSigner.getSecretKey().isEmpty()) {
                        requestSigner.setSecretKey(Constants.DEFAULT_API_SECRET);
                    }
                    String apiKey = requestSigner.getApiKey()== null ? Constants.DEFAULT_API_KEY : requestSigner.getApiKey();
                    String signature = requestSigner.method(chain.request().method())
                            .canonicalUri(chain.request().url().uri().getPath())
                            .apiKey(apiKey).timestamp(nowAsISO).payload(body).signV1();
                    Request request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json")
                            .addHeader(Constants.Api.X_ARROW_APIKEY, requestSigner.getApiKey())
                            .addHeader(Constants.Api.X_ARROW_DATE, nowAsISO)
                            .addHeader(Constants.Api.X_ARROW_SIGNATURE, signature)
                            .addHeader(Constants.Api.X_ARROW_VERSION, Constants.Api.X_ARROW_VERSION_1)
                            .build();

                    okhttp3.Response response = chain.proceed(request);
                    return response;
                }
            })
            .sslSocketFactory(new NoSSLv3SocketFactory())
            .build();

    private static Retrofit retrofit;

    public static IotConnectAPIService getIotConnectAPIService(ServerEndpoint endpoint) {

        String baseUrl = endpoint == ServerEndpoint.DEMO ? Constants.BASE_IOT_CONNECT_URL_DEMO :
                Constants.BASE_IOT_CONNECT_URL_DEV;
        if (retrofit == null || !retrofit.baseUrl().toString().equals(baseUrl)) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(IotConnectAPIService.class);
    }

    private static String bodyToString(final RequestBody request){
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if(copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        }
        catch (final IOException e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "bodyToString");
            FirebaseCrash.report(e);
            return "did not work";
        }
    }
}
