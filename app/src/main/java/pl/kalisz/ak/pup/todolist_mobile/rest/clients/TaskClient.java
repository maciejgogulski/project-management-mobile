package pl.kalisz.ak.pup.todolist_mobile.rest.clients;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class TaskClient extends HttpClient{

    public TaskClient(Context context) throws IOException {
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
                } else {
                    listener.onFailure(response.message());
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
                } else {
                    listener.onFailure(response.message());
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
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }

    public void deleteTask(Long taskId, final ApiResponseListener<String> listener) throws IOException {
        httpService.sendRequest("/api/tasks/" + taskId, HttpMethod.DELETE.name(), null, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response){
                if (response.isSuccessful()) {
                    Log.d("DEBUG", "deleteTask: " + taskId);
                    listener.onSuccess("Usunięto zadanie " + taskId);
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }

    public void getTasksAfterTerm(ApiResponseListener<List<Task>> listener) throws IOException {
        httpService.sendRequest("/api/tasks/after-deadline", HttpMethod.GET.name(), null, new Callback() {
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
                                    new TypeToken<List<Task>>() {
                                    }.getType()
                            )
                    );
                    Log.d("DEBUG", "getTasksAfterTerm: " + responseData);
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }

    public void getTasksBeforeDeadline(ApiResponseListener<List<Task>> listener) throws IOException {
        httpService.sendRequest("/api/tasks/before-deadline", HttpMethod.GET.name(), null, new Callback() {
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
                                    new TypeToken<List<Task>>() {
                                    }.getType()
                            )
                    );
                    Log.d("DEBUG", "getTasksAfterTerm: " + responseData);
                } else {
                    listener.onFailure(response.message());
                }
            }
        });
    }
}
