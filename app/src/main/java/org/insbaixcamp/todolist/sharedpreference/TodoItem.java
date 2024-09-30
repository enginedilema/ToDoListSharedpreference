package org.insbaixcamp.todolist.sharedpreference;

public class TodoItem {
    private String task;
    private boolean isCompleted;

    public TodoItem() {
    }
    public TodoItem(String task, boolean isCompleted) {
        this.task = task;
        this.isCompleted = isCompleted;
    }

    // Getters y setters
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean getCompleted() {
        return isCompleted;
    }
    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "task='" + task + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}