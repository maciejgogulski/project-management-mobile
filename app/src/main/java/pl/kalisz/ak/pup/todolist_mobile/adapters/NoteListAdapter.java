package pl.kalisz.ak.pup.todolist_mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.kalisz.ak.pup.todolist_mobile.R;
import pl.kalisz.ak.pup.todolist_mobile.activities.NoteActivity;
import pl.kalisz.ak.pup.todolist_mobile.activities.TaskShowActivity;
import pl.kalisz.ak.pup.todolist_mobile.domain.Note;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private final List<Note> noteList;
    Long taskId;
    String taskName;
    Long projectId;
    String projectName;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View listItem;

        public ViewHolder(View listItem) {
            super(listItem);
            this.listItem = listItem;
        }
    }

    public NoteListAdapter(List<Note> noteList,
                           @Nullable Long taskId,
                           @Nullable Long projectId,
                           @Nullable String taskName,
                           @Nullable String projectName)
    {
        this.noteList = noteList;
        this.taskId = taskId;
        this.projectId = projectId;
        this.taskName = taskName;
        this.projectName = projectName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);

        View listItem = holder.listItem;
        TextView noteUpdatedAt = listItem.findViewById(R.id.list_item_note_updated_at);
        noteUpdatedAt.setText(note.getUpdatedAtString());

        TextView noteContent = listItem.findViewById(R.id.list_item_note_content);
        noteContent.setText(note.getContent());

        listItem.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra(NoteActivity.EXTRA_NOTE_ID, note.getId());

            if (taskId != null) {
                intent.putExtra(NoteActivity.EXTRA_TASK_ID, taskId);
                intent.putExtra(NoteActivity.EXTRA_TASK_NAME, taskName);
            }
            if (projectId != null) {
                intent.putExtra(NoteActivity.EXTRA_PROJECT_ID, projectId);
                intent.putExtra(NoteActivity.EXTRA_PROJECT_NAME, projectName);
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
