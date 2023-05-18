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
import pl.kalisz.ak.pup.todolist_mobile.activities.TaskShowActivity;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private final List<Task> taskList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView cardView;

        public ViewHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
        }
    }

    public TaskListAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_cardview_layout, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);

        CardView cardView = holder.cardView;
        TextView taskName = (TextView) cardView.findViewById(R.id.card_task_name);
        taskName.setText(task.getName());

        TextView taskFinished = (TextView) cardView.findViewById(R.id.card_finished);
        taskFinished.setText(task.isCompleted() ? "Tak" : "Nie");

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ProjectShowActivity.class);
                intent.putExtra(TaskShowActivity.EXTRA_TASK, (Task) task);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }


}
