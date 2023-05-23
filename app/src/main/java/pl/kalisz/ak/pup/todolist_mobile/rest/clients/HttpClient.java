package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

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
                .create();

    }

    public interface ApiResponseListener<T> {
        void onSuccess(T data);
        void onFailure(String errorMessage);
    }
}
