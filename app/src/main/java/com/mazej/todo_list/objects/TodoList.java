package com.mazej.todo_list.objects;

import java.util.List;

public class TodoList {

    private String id;
    private String name;
    private List<Task> tasks;

    public TodoList(String id, String name, List<Task> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
