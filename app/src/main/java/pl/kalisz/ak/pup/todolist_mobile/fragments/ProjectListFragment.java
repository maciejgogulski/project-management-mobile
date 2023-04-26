package pl.kalisz.ak.pup.todolist_mobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.adapters.ProjectListAdapter;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.rest.HttpService;

public class ProjectListFragment extends Fragment {
    private HttpService httpService;
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

        httpService = new HttpService(getContext());

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
            httpService.sendRequest("/api/projects/with-tasks", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        final String responseData = response.body().string();
                         requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();

                                projectListAdapter = new ProjectListAdapter(
                                        gson.fromJson(responseData, new TypeToken<List<Project>>() {
                                        }.getType())
                                );

                                projectListRecyclerView.setAdapter(projectListAdapter);
                            }
                        });
                    }
                }
            });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
