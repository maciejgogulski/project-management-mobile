package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.domain.Note;

public class NoteClient extends HttpClient{

    public NoteClient(Context context) throws IOException {
        super(context);
    }

    public void addNote(Note note, final ApiResponseListener<Note> listener) {
        String noteJsonObject = gson.toJson(note);

        httpService.sendRequest("/api/notes", HttpMethod.POST.name(), noteJsonObject, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) { // TODO Gdy serwer zwraca 500, nie wywoływana jest metoda onFaliure, tylko kod przechodzi tu.
                    final String responseData = response.body().string();
                    listener.onSuccess(
                            gson.fromJson(
                                    responseData,
                                    new TypeToken<Note>() {
                                    }.getType()
                            )
                    );
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }

    public void editNote(Note note, final ApiResponseListener<Note> listener) {
        String noteJsonObject = gson.toJson(note);

        httpService.sendRequest("/api/notes/" + note.getId(), HttpMethod.PUT.name(), noteJsonObject, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) { // TODO Gdy serwer zwraca 500, nie wywoływana jest metoda onFaliure, tylko kod przechodzi tu.
                    final String responseData = response.body().string();
                    listener.onSuccess(
                            gson.fromJson(
                                    responseData,
                                    new TypeToken<Note>() {
                                    }.getType()
                            )
                    );
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }

    public void getOneNote(Long noteId, final ApiResponseListener<Note> listener) {
        httpService.sendRequest("/api/notes/" + noteId, HttpMethod.GET.name(), null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    listener.onSuccess(
                            gson.fromJson(
                                    responseData,
                                    new TypeToken<Note>() {
                                    }.getType()
                            )
                    );
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }

    public void deleteNote(Long noteId, final ApiResponseListener<String> listener) throws IOException {
        httpService.sendRequest("/api/notes/" + noteId, HttpMethod.DELETE.name(), null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response){
                if (response.isSuccessful()) {
                    Log.d("DEBUG", "deleteNote: " + noteId);
                    listener.onSuccess("Usunięto notatkę " + noteId);
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }
}
