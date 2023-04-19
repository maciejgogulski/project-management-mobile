package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;

public abstract class HttpClient {

    final Context context;

    final HttpService httpService;

    public HttpClient(Context context) {
        this.context = context;
        httpService = new HttpService(context);
    }
}
