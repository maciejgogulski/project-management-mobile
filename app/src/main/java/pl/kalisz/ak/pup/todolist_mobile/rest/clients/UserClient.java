package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.domain.User;

/**
 * Klasa do wysyłania requestów związanych z projektami do api.
 */
public class UserClient extends HttpClient {

    public UserClient(Context context) {
        super(context);
    }

    public void getUsers(final ApiResponseListener<List<User>> listener) throws IOException {
        httpService.sendRequest("/api/users", HttpMethod.GET.name(), null, new Callback() {
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
                                    new TypeToken<List<User>>() {
                                    }.getType()
                            )
                    );
                }
            }
        });
    }
}
