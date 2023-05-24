package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class TaskClient extends HttpClient{

    public TaskClient(Context context) {
        super(context);
    }

    public void addTask(Task task, final ApiResponseListener<Task> listener) throws IOException {
        String taskJsonObject = gson.toJson(task);

        httpService.sendRequest("/api/tasks", HttpMethod.POST.name(), taskJsonObject, new Callback() {
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
                                    new TypeToken<Task>() {
                                    }.getType()
                            )
                    );
                }
            }
        });
    }

    public void editTask(Task task, final ApiResponseListener<Task> listener) throws IOException {
        String taskJsonObject = gson.toJson(task);

        httpService.sendRequest("/api/tasks/" + task.getId(), HttpMethod.PUT.name(), taskJsonObject, new Callback() {
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
                                    new TypeToken<Task>() {
                                    }.getType()
                            )
                    );
                }
            }
        });
    }

    public void getOneTask(Long taskId, final ApiResponseListener<Task> listener) throws IOException {
        httpService.sendRequest("/api/tasks/" + taskId, HttpMethod.GET.name(), null, new Callback() {
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
                                    new TypeToken<Task>() {
                                    }.getType()
                            )
                    );
                }
            }
        });
    }
}
