package org.insbaixcamp.todolist.sharedpreference;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder>  {

    private List<TodoItem> todoList;
    private OnItemLongClickListener longClickListener;
    private OnTaskCheckedChangeListener taskCheckedChangeListener;

    // Interfaz para el evento de long click
    public interface OnItemLongClickListener {
        void onItemLongClicked(int position);
    }

    // Interfaz para el cambio de estado del CheckBox
    public interface OnTaskCheckedChangeListener {
        void onTaskCheckedChanged(int position, boolean isChecked);
    }

    public TodoAdapter(List<TodoItem> todoList, OnItemLongClickListener longClickListener, OnTaskCheckedChangeListener taskCheckedChangeListener) {
        this.todoList = todoList;
        this.longClickListener = longClickListener;
        this.taskCheckedChangeListener = taskCheckedChangeListener;
    }


    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        TodoItem item = todoList.get(position);
        holder.taskTextView.setText(item.getTask());
        holder.checkBox.setChecked(item.isCompleted());

        // Manejo del cambio de estado del CheckBox
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (taskCheckedChangeListener != null) {
                taskCheckedChangeListener.onTaskCheckedChanged(position, isChecked);
            }
        });

        // Manejo del long click para eliminar
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClicked(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView taskTextView;
        CheckBox checkBox;

        public TodoViewHolder( View itemView) {
            super(itemView);
            taskTextView = itemView.findViewById(R.id.text_view_task);
            checkBox = itemView.findViewById(R.id.checkbox_completed);
        }
    }
}