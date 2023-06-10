package pl.kalisz.ak.pup.todolist_mobile.rest;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Klasa służąca do obsługi requestów HTTP z zewnętrznej aplikacji webowej todolist.
 *
 * @author Maciej Gogulski
 */
public class HttpService {

    /**
     * Pole przechowujące adres url aplikacji webowej todolist.
     */
    private final String webAppUrl = "http://mtodolist.stronazen.pl/";

    private final String apiToken;

    private final Context context;

    private OkHttpClient client = new OkHttpClient();

    public HttpService(Context context) {
        this.context = context;
        this.apiToken = getApiTokenFromSharedPreferences();
    }

    public void sendRequest(String endpoint, String method, String body, Callback callback) {
        String url = webAppUrl + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .method(method, (body != null) ? RequestBody.create(body, MediaType.get("application/json")) : null)
                .header("Authorization", "Bearer " + apiToken)
                .header("Accept", "application/json")
                .build();

        Call call = client.newCall(request);

        call.enqueue(callback);
    }

    private String getApiTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        return sharedPreferences.getString("API_TOKEN", null);
    }
}