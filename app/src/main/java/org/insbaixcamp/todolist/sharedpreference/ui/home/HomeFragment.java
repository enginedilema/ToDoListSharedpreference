package org.insbaixcamp.todolist.sharedpreference.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.insbaixcamp.todolist.sharedpreference.TodoAdapter;
import org.insbaixcamp.todolist.sharedpreference.TodoItem;
import org.insbaixcamp.todolist.sharedpreference.databinding.FragmentHomeBinding;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private static final String PREFS_NAME = "todo_prefs";
    private static final String TODO_LIST_KEY = "todo_list";
    private List<TodoItem> todoList;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private EditText editTextTask;
    private Button buttonAddTask;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        // Inicializar lista de tareas
        todoList = getTodoListFromPrefs();

        // Configurar RecyclerView
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        todoAdapter = new TodoAdapter(todoList,this::removeTask, this::updateTaskStatus);
        recyclerView.setAdapter(todoAdapter);

        // Inicializar EditText y Button
        editTextTask = binding.editTextTask;
        buttonAddTask = binding.buttonAddTask;

        // Configurar evento de clic en el botón
        buttonAddTask.setOnClickListener(v -> addNewTask());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    // Método para obtener la lista desde SharedPreferences
    private List<TodoItem> getTodoListFromPrefs() {
        String json = sharedPreferences.getString(TODO_LIST_KEY, null);
        if (json != null) {
            Type type = new TypeToken<List<TodoItem>>(){}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>(); // Retornar lista vacía si no hay datos
    }

    // Método para guardar la lista en SharedPreferences
    private void saveTodoListToPrefs() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(todoList);
        editor.putString(TODO_LIST_KEY, json);
        editor.apply();
    }

    // Método para agregar una nueva tarea
    private void addNewTask() {
        String taskText = editTextTask.getText().toString().trim();

        // Verificar si el EditText no está vacío
        if (!taskText.isEmpty()) {
            TodoItem newItem = new TodoItem(taskText, false);
            todoList.add(newItem);
            todoAdapter.notifyDataSetChanged();
            saveTodoListToPrefs(); // Guardar lista actualizada

            // Limpiar el EditText después de agregar la tarea
            editTextTask.setText("");
        } else {
            Toast.makeText(getContext(), "Introduce una tarea", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para eliminar una tarea
    private void removeTask(int position) {
        todoList.remove(position);
        todoAdapter.notifyItemRemoved(position);
        saveTodoListToPrefs(); // Guardar lista actualizada después de eliminar
        Toast.makeText(getContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show();
    }

    // Método para actualizar el estado de la tarea (CheckBox)
    private void updateTaskStatus(int position, boolean isChecked) {
        todoList.get(position).setCompleted(isChecked);
        saveTodoListToPrefs(); // Guardar lista actualizada después de cambiar el estado
        Toast.makeText(getContext(), isChecked ? "Tarea completada" : "Tarea no completada", Toast.LENGTH_SHORT).show();
    }
}