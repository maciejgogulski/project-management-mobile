package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;

public abstract class HttpClient {

    final Context context;

    final HttpService httpService;

    final Gson gson;

    public HttpClient(Context context) {
        this.context = context;
        httpService = new HttpService(context);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, cont) -> {
                    String dateString = json.getAsString();
                    try {
                        return dateFormat.parse(dateString);
                    } catch (ParseException e) {
                        // Handle parsing exception as needed
                        return null;
                    }
                })
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, typeOfSrc, cont) ->
                        new JsonPrimitive(dateFormat.format(date))
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
