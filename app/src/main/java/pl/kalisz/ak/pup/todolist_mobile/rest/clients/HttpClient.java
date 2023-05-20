package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import com.google.gson.Gson;

import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;

public abstract class HttpClient {

    final Context context;

    final HttpService httpService;

    final Gson gson = new Gson();

    public HttpClient(Context context) {
        this.context = context;
        httpService = new HttpService(context);
    }

    public interface ApiResponseListener<T> {
        void onSuccess(T data);
        void onFailure(String errorMessage);
    }
}
