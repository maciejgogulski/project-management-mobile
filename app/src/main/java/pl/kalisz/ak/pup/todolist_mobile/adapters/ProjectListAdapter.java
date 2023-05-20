package pl.kalisz.ak.pup.todolist_mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.activities.ProjectShowActivity;
import pl.kalisz.ak.pup.todolist_mobile.domain.Project;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {

    private final List<Project> projectList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    public ProjectListAdapter(List<Project> projectList) {
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.project_cardview_layout, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Project project = projectList.get(position);

        CardView cardView = holder.cardView;
        TextView projectName = (TextView) cardView.findViewById(R.id.card_project_name);
        projectName.setText(project.getName());

        long finishedTasksCount;
        long unfinishedTasksCount;

        if (project.getTasks() != null) {
            finishedTasksCount = project.getTasks()
                    .stream().filter(Task::isCompleted)
                    .count();

            unfinishedTasksCount = project.getTasks()
                    .stream().filter(task -> !task.isCompleted())
                    .count();
        } else {
            finishedTasksCount = 0;
            unfinishedTasksCount = 0;
        }


        TextView tasksFinished = (TextView) cardView.findViewById(R.id.card_finished_tasks);
        tasksFinished.setText("Ukończone zadania: " + finishedTasksCount);

        TextView tasksUnfinished = (TextView) cardView.findViewById(R.id.card_unfinished_tasks);
        tasksUnfinished.setText("Nieukończone zadania: " + unfinishedTasksCount);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ProjectShowActivity.class);
                intent.putExtra(ProjectShowActivity.EXTRA_PROJECT, (Project) project);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }


}
