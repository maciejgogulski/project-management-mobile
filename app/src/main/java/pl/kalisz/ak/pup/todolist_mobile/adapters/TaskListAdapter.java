package pl.kalisz.ak.pup.todolist_mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.activities.ProjectShowActivity;
import pl.kalisz.ak.pup.todolist_mobile.activities.TaskShowActivity;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private final List<Task> taskList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View listItem;

        public ViewHolder(View listItem) {
            super(listItem);
            this.listItem = listItem;
        }
    }

    public TaskListAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        View listItem = holder.listItem;
        TextView taskName = listItem.findViewById(R.id.list_item_task_name);
        taskName.setText(task.getName());

        TextView taskFinished = listItem.findViewById(R.id.list_item_finished);
        taskFinished.setText(task.isCompleted() ? "Tak" : "Nie");

        listItem.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, TaskShowActivity.class);
            intent.putExtra(TaskShowActivity.EXTRA_TASK_ID, task.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

}
