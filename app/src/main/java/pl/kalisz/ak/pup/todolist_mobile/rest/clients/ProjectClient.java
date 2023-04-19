package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import java.io.IOException;

import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;

/**
 * Klasa do wysyłania requestów związanych z projektami do api.
 */
public class ProjectClient extends HttpClient {

    public ProjectClient(Context context) {
        super(context);
    }

//    public String getProjects() throws IOException {
//        return httpService.sendRequest("/api/projects");
//    }
}
