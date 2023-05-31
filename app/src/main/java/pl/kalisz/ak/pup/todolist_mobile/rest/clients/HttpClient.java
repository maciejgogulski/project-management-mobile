package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;

public abstract class HttpClient {

    final Context context;

    final HttpService httpService;

    final Gson gson;

    public HttpClient(Context context) throws IOException {
        this.context = context;
        httpService = new HttpService(context);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, cont) -> {
                    String dateString = json.getAsString();
                    try {
                        return dateFormat1.parse(dateString);
                    } catch (ParseException e1) {
                        try {
                            return dateFormat2.parse(dateString);
                        } catch (ParseException e2) {
                            // Handle parsing exception as needed
                            return null;
                        }
                    }
                })
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, typeOfSrc, cont) ->
                        new JsonPrimitive(dateFormat1.format(date))
                )
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    public interface ApiResponseListener<T> {
        void onSuccess(T data);

        void onFailure(String errorMessage);
    }

    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }
}
