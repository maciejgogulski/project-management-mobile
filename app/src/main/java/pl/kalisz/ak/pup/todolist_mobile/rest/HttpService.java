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
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;

/**
 * Klasa służąca do obsługi requestów HTTP z zewnętrznej aplikacji webowej todolist.
 *
 * @author Maciej Gogulski
 */
public class HttpService {

    /**
     * Pole przechowujące adres url aplikacji webowej todolist.
     */
    private final String webAppUrl = "http://10.0.2.2:8000";

    private final String apiToken = "492Mg2JawqdwqZdR5PTppGGTtW1PGdzofwUQDZsF";

    private final Context context;

    private OkHttpClient client = new OkHttpClient();

    public HttpService(Context context) {
        this.context = context;
    }

    public void sendRequest(String endpoint, Callback callback) throws IOException {
        String url = webAppUrl + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiToken)
                .header("X-CSRF-TOKEN", getCsrfTokenFromSharedPreferences())
                .build();

        Call call = client.newCall(request);

        call.enqueue(callback);
    }

    public void sendCSRFRequest() throws IOException {

        String url = webAppUrl + "/sanctum/csrf-cookie";

        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + apiToken)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            public void onResponse(Call call, Response response)
                    throws IOException {
                putCsrfTokenInSharedPreferences(extractCSRF(response));
                Log.d(TAG, "onResponse: " + getCsrfTokenFromSharedPreferences());
            }

            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
        });
    }

    private String extractCSRF(Response response) {
        Headers headers = response.headers();

        List<String> cookies = headers.values("Set-Cookie");

        String csrfToken = "";

        for (String cookie : cookies) {
            if (cookie.startsWith("XSRF-TOKEN=")) {
                csrfToken = cookie.substring("XSRF-TOKEN=".length(), cookie.indexOf(";"));
            }
        }

        return csrfToken;
    }

    private void putCsrfTokenInSharedPreferences(String csrfToken) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putString("XCSRF", csrfToken).apply();
    }

    private String getCsrfTokenFromSharedPreferences() throws IOException {
        SharedPreferences sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("XCSRF", null);

        if (token == null) {
            sendCSRFRequest();
        }

        return token;
    }
}