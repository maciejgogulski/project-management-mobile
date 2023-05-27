package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.domain.User;
import pl.kalisz.ak.pup.todolist_mobile.dto.UserAuthDto;

/**
 * Klasa do wysyłania requestów związanych z projektami do api.
 */
public class UserClient extends HttpClient {

    public UserClient(Context context) throws IOException {
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

    public void login(UserAuthDto userAuthDto, final ApiResponseListener<String> listener) throws IOException {
        Gson gson = new Gson();
        String userAuthJsonObject = gson.toJson(userAuthDto);
        httpService.sendRequest("/api/login", HttpMethod.POST.name(), userAuthJsonObject, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                    JsonElement jsonElement = jsonObject.get("token");
                    String token = jsonElement.getAsString();
                    listener.onSuccess(token);
                }
            }
        });
    }

    public void logout(final ApiResponseListener<String> listener) throws IOException {
        httpService.sendRequest("/api/logout", HttpMethod.POST.name(), "", new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    listener.onSuccess(responseData);
                }
            }
        });
    }

    public void checkAuth(final ApiResponseListener<Boolean> listener) {
        httpService.sendRequest("/api/check-auth", HttpMethod.GET.name(), null, new Callback() {
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
                                    new TypeToken<Boolean>() {
                                    }.getType()
                            )
                    );
                } else {
                    listener.onSuccess(false);
                }
            }
        });
    }
}
