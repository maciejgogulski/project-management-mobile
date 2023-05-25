package pl.kalisz.ak.pup.todolist_mobile.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.adapters.TaskListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.TaskClient;

public class TaskListFragment extends Fragment {

    private TaskClient taskClient;
    RecyclerView taskListRecyclerView;
    TaskListAdapter taskListAdapter;

    boolean afterTerm;

    public TaskListFragment(boolean afterTerm) {
        this.afterTerm = afterTerm;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        taskListRecyclerView = view.findViewById(R.id.task_list_recycler_view);

        taskClient = new TaskClient(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadTasksFromApi();
    }

    public void loadTasksFromApi() {
        try {
            if (afterTerm) {
                taskClient.getTasksAfterTerm(new HttpClient.ApiResponseListener<>() {
                    @Override
                    public void onSuccess(List<Task> data) {
                        requireActivity().runOnUiThread(() -> {
                            taskListAdapter = new TaskListAdapter(data);
                            taskListRecyclerView.setAdapter(taskListAdapter);
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            } else {
                taskClient.getTasksBeforeDeadline(new HttpClient.ApiResponseListener<>() {
                    @Override
                    public void onSuccess(List<Task> data) {
                        requireActivity().runOnUiThread(() -> {
                            taskListAdapter = new TaskListAdapter(data);
                            taskListRecyclerView.setAdapter(taskListAdapter);
                        });
                    }

                    @Override
                    public void onFailure(String errorMessage) {

                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}