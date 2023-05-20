package pl.kalisz.ak.pup.todolist_mobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.adapters.ProjectListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.HttpClient;
import pl.kalisz.ak.pup.todolist_mobile.rest.clients.ProjectClient;

public class ProjectListFragment extends Fragment {
    private ProjectClient projectClient;
    RecyclerView projectListRecyclerView;
    ProjectListAdapter projectListAdapter;

    public ProjectListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectListFragment.
     */
    public static ProjectListFragment newInstance() {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_list_fragment_layout, container, false);
        projectListRecyclerView = view.findViewById(R.id.project_list_recycler_view);

        projectClient = new ProjectClient(getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        projectListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        loadProjectListFromApi();
    }

    public void loadProjectListFromApi() {
        try {
            projectClient.getProjectsWithTasks(new HttpClient.ApiResponseListener<>() {
                @Override
                public void onSuccess(List<Project> data) {
                    requireActivity().runOnUiThread(() -> {
                        projectListAdapter = new ProjectListAdapter(data);
                        projectListRecyclerView.setAdapter(projectListAdapter);
                    });
                }

                @Override
                public void onFailure(String errorMessage) {

                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
