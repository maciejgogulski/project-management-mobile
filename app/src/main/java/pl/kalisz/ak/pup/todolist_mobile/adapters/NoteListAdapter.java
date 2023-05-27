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
import pl.kalisz.ak.pup.todolist_mobile.activities.TaskShowActivity;
import pl.kalisz.ak.pup.todolist_mobile.domain.Note;
import pl.kalisz.ak.pup.todolist_mobile.domain.Task;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private final List<Note> noteList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View listItem;

        public ViewHolder(View listItem) {
            super(listItem);
            this.listItem = listItem;
        }
    }

    public NoteListAdapter(List<Note> noteList) {
        this.noteList = noteList;
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
//            Context context = v.getContext();
//            Intent intent = new Intent(context, TaskShowActivity.class);
//            intent.putExtra(TaskShowActivity.EXTRA_TASK_ID, task.getId());
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
