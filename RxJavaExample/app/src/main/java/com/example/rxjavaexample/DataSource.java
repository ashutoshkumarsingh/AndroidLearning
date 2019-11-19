package com.example.rxjavaexample;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    public static List<Task> createTaskList() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Take out the Trash", 3, true));
        tasks.add(new Task("Walk the Dog", 2, false));
        tasks.add(new Task("Make my Bed", 1, true));
        tasks.add(new Task("Unload the Dishwasher", 0, false));
        tasks.add(new Task("Make dinner", 5, true));
        return tasks;
    }
}

